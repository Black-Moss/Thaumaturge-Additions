package com.blackmooss.thaumaturgeadditions.block;

import java.util.ArrayList;
import java.util.List;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
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
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.NonNull;

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

    private final FluidStacksResourceHandler tank = new FluidStacksResourceHandler(1, CAPACITY) {
        @Override
        public boolean isValid(int index, FluidResource resource) {
            return resource.is(Fluids.LAVA);
        }

        @Override
        protected void onContentsChanged(int index, net.neoforged.neoforge.fluids.@NonNull FluidStack previousContents) {
            super.onContentsChanged(index, previousContents);
            setChanged();
        }
    };

    private final List<Integer> handlers = new ArrayList<>();
    private int zone;
    private int counter;

    public EverburningUrnBlockEntity(BlockPos pos, BlockState state) {
        super(TABlockEntities.EVERBURNING_URN.get(), pos, state);
    }

    public FluidStacksResourceHandler getTank() {
        return tank;
    }

    public int lavaAmount() {
        return tank.getAmountAsInt(0);
    }

    public void drainLava(int amount) {
        try (Transaction ctx = Transaction.openRoot()) {
            tank.extract(FluidResource.of(Fluids.LAVA), amount, ctx);
            ctx.commit();
        }
    }

    private void fillLava(int amount) {
        try (Transaction ctx = Transaction.openRoot()) {
            tank.insert(FluidResource.of(Fluids.LAVA), amount, ctx);
            ctx.commit();
        }
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
            ResourceHandler<FluidResource> handler = server.getCapability(Capabilities.Fluid.BLOCK, target, Direction.UP);
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
                int moved;
                try (Transaction ctx = Transaction.openRoot()) {
                    moved = handler.insert(FluidResource.of(Fluids.LAVA), PUSH_AMOUNT, ctx);
                    if (moved > 0) {
                        ctx.commit();
                    }
                }
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

    private BlockPos zonePos(int zone) {
        int x = zone / ZONE_XZ % ZONE_XZ;
        int y = zone / ZONE_XZ / ZONE_XZ % ZONE_Y;
        int z = zone % ZONE_XZ;
        return getBlockPos().offset(x - 2, y - 1, z - 2);
    }

    private boolean acceptsLava(ServerLevel server, BlockPos pos) {
        if (server.getCapability(Capabilities.Fluid.BLOCK, pos, Direction.UP) != null) {
            return true;
        }
        BlockState state = server.getBlockState(pos);
        return state.is(Blocks.CAULDRON) || state.is(Blocks.LAVA_CAULDRON);
    }

    private void pourInto(ServerLevel server, BlockPos target) {
        Vec3 from = new Vec3(getBlockPos().getX() + 0.5, getBlockPos().getY() + TRAIL_SOURCE_Y, getBlockPos().getZ() + 0.5);
        EffectDispatch.spawnEssentiaStream(server, from, Vec3.atCenterOf(target), TRAIL_COLOR, 0, counter, TRAIL_SCALE, TRAIL_EXTEND, TRAIL_UPWARD);
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);
        tank.serialize(output.child("Tank"));
        int[] zones = new int[handlers.size()];
        for (int i = 0; i < zones.length; i++) {
            zones[i] = handlers.get(i);
        }
        output.putIntArray("Handlers", zones);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        input.child("Tank").ifPresent(tank::deserialize);
        handlers.clear();
        int[] zones = input.getIntArray("Handlers").orElse(new int[0]);
        for (int zone : zones) {
            handlers.add(zone);
        }
    }

    @Override
    public @NonNull CompoundTag getUpdateTag(HolderLookup.@NonNull Provider registries) {
        CompoundTag nbt = super.getUpdateTag(registries);
        try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(this.problemPath(), ThaumaturgeAdditions.LOGGER)) {
            TagValueOutput output = TagValueOutput.createWithContext(reporter, registries);
            saveAdditional(output);
            nbt.merge(output.buildResult());
        }
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
