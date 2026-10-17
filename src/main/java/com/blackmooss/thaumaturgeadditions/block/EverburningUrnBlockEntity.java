package com.blackmooss.thaumaturgeadditions.block;

import com.blackmooss.thaumaturgeadditions.registry.TABlockEntities;
import com.leclowndu93150.thaumaturge.api.aura.AuraHelper;
import com.leclowndu93150.thaumaturge.content.effect.EffectDispatch;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class EverburningUrnBlockEntity extends BlockEntity {
    public static final int CAPACITY = 1000;
    private static final int WORK_INTERVAL = 5;
    private static final int PUSH_AMOUNT = 25;
    private static final int CAULDRON_COST = 333;
    private static final float MAX_REFILL_VIS = 0.1F;
    private static final int ZONE_XZ = 5;
    private static final int ZONE_Y = 3;
    private static final int TRAIL_COLOR = 0xFF4500;
    private static final double TRAIL_SOURCE_Y = 0.66;
    private static final float TRAIL_SCALE = 0.1F;
    private static final int TRAIL_EXTEND = 0;
    private static final double TRAIL_UPWARD = 0.2;

    private final FluidTank tank = new FluidTank(CAPACITY) {
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid() == Fluids.LAVA;
        }

        @Override
        protected void onContentsChanged() {
            setChanged();
        }
    };

    private final List<Integer> handlers = new ArrayList<>();
    private int zone;
    private int counter;

    public EverburningUrnBlockEntity(BlockPos pos, BlockState state) {
        super(TABlockEntities.EVERBURNING_URN.get(), pos, state);
    }

    public static void serverTick(
            Level level,
            BlockPos pos,
            BlockState state,
            EverburningUrnBlockEntity entity) {
        entity.counter++;
        if (entity.counter % WORK_INTERVAL != 0 || !(level instanceof ServerLevel server)) {
            return;
        }
        entity.zone++;
        BlockPos probe = entity.zonePos(entity.zone);
        if (!entity.handlers.contains(entity.zone % 75) && entity.acceptsLava(server, probe)) {
            entity.handlers.add(entity.zone % 75);
            entity.setChanged();
        }
        int i = 0;
        while (i < entity.handlers.size() && entity.lavaAmount() >= PUSH_AMOUNT) {
            int zz = entity.handlers.get(i);
            BlockPos target = entity.zonePos(zz);
            BlockState targetState = server.getBlockState(target);
            IFluidHandler handler = server.getCapability(Capabilities.FluidHandler.BLOCK, target, Direction.UP);
            if (handler == null) {
                if (!targetState.is(Blocks.CAULDRON)
                        && !targetState.is(Blocks.LAVA_CAULDRON)
                        || entity.lavaAmount() < CAULDRON_COST) {
                    entity.handlers.remove(i);
                    entity.setChanged();
                    continue;
                }
                if (targetState.is(Blocks.CAULDRON)) {
                    server.setBlock(target, Blocks.LAVA_CAULDRON.defaultBlockState(), Block.UPDATE_CLIENTS);
                    entity.drainLava(CAULDRON_COST);
                    entity.pourInto(server, target);
                }
            } else {
                int moved = handler.fill(new FluidStack(Fluids.LAVA, PUSH_AMOUNT), FluidAction.EXECUTE);
                if (moved > 0) {
                    entity.drainLava(moved);
                    entity.pourInto(server, target);
                    break;
                }
            }
            i++;
        }
        if (entity.lavaAmount() < CAPACITY) {
            float demand = Math.min(MAX_REFILL_VIS, (CAPACITY - entity.lavaAmount()) / (float) CAPACITY);
            if (demand > 0.0F) {
                float drawn = AuraHelper.drainVis(server, pos, demand, false);
                int lava = (int) (CAPACITY * drawn);
                if (lava > 0) {
                    entity.fillLava(lava);
                }
            }
        }
    }

    public FluidTank getTank() {
        return this.tank;
    }

    public int lavaAmount() {
        return this.tank.getFluidAmount();
    }

    public void drainLava(int amount) {
        this.tank.drain(amount, FluidAction.EXECUTE);
    }

    private void fillLava(int amount) {
        this.tank.fill(new FluidStack(Fluids.LAVA, amount), FluidAction.EXECUTE);
    }

    private BlockPos zonePos(int zone) {
        int x = zone / ZONE_XZ % ZONE_XZ;
        int y = zone / ZONE_XZ / ZONE_XZ % ZONE_Y;
        int z = zone % ZONE_XZ;
        return getBlockPos().offset(x - 2, y - 1, z - 2);
    }

    private boolean acceptsLava(ServerLevel server, BlockPos pos) {
        if (server.getCapability(Capabilities.FluidHandler.BLOCK, pos, Direction.UP) != null) {
            return true;
        }
        BlockState state = server.getBlockState(pos);
        return state.is(Blocks.CAULDRON) || state.is(Blocks.LAVA_CAULDRON);
    }

    private void pourInto(ServerLevel server, BlockPos target) {
        Vec3 from = new Vec3(getBlockPos().getX() + 0.5, getBlockPos().getY() + TRAIL_SOURCE_Y, getBlockPos().getZ() + 0.5);
        EffectDispatch.spawnEssentiaStream(server, from, Vec3.atCenterOf(target), TRAIL_COLOR, 0, this.counter, TRAIL_SCALE, TRAIL_EXTEND, TRAIL_UPWARD);
    }

    @Override
    protected void saveAdditional(@NonNull CompoundTag tag, HolderLookup.@NonNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Tank", this.tank.writeToNBT(registries, new CompoundTag()));
        int[] zones = new int[this.handlers.size()];
        for (int i = 0; i < zones.length; i++) {
            zones[i] = this.handlers.get(i);
        }
        tag.putIntArray("Handlers", zones);
    }

    @Override
    protected void loadAdditional(@NonNull CompoundTag tag, HolderLookup.@NonNull Provider registries) {
        super.loadAdditional(tag, registries);
        this.tank.readFromNBT(registries, tag.getCompound("Tank"));
        this.handlers.clear();
        for (int zone : tag.getIntArray("Handlers")) {
            this.handlers.add(zone);
        }
    }

    @Override
    public @NonNull CompoundTag getUpdateTag(HolderLookup.@NonNull Provider registries) {
        CompoundTag nbt = super.getUpdateTag(registries);
        saveAdditional(nbt, registries);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}