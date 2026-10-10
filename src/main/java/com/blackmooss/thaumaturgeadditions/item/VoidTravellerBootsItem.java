package com.blackmooss.thaumaturgeadditions.item;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.leclowndu93150.thaumaturge.api.items.ChargeDisplay;
import com.leclowndu93150.thaumaturge.api.items.ChargeProfile;
import com.leclowndu93150.thaumaturge.api.items.IVisDiscountGear;
import com.leclowndu93150.thaumaturge.api.items.RechargeAccess;
import com.leclowndu93150.thaumaturge.content.equipment.VoidGearItem;
import com.leclowndu93150.thaumaturge.registry.TTDataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class VoidTravellerBootsItem extends VoidGearItem implements IVisDiscountGear {
    public static final float SAFE_FALL_DISTANCE_BONUS = 6.0F;
    public static final AttributeModifier SAFE_FALL_MODIFIER = new AttributeModifier(
            ThaumaturgeAdditions.identifier("void_traveller_safe_fall"), SAFE_FALL_DISTANCE_BONUS, AttributeModifier.Operation.ADD_VALUE);
    private static final AttributeModifier STEP_MODIFIER = new AttributeModifier(
            ThaumaturgeAdditions.identifier("void_traveller_step"), 0.8F, AttributeModifier.Operation.ADD_VALUE);
    private static final AttributeModifier JUMP_MODIFIER = new AttributeModifier(
            ThaumaturgeAdditions.identifier("void_traveller_jump"), 0.55F, AttributeModifier.Operation.ADD_VALUE);
    private static final Vec3 FORWARD = new Vec3(0.0, 0.0, 1.0);

    public VoidTravellerBootsItem(Item.Properties properties) {
        super(properties.component(TTDataComponents.RECHARGEABLE.get(), new ChargeProfile(350, ChargeDisplay.ON_CHANGE)));
    }

    public static void clientMovementTick(Player player, ItemStack stack) {
        if (RechargeAccess.getCharge(stack) <= 0 || player.getAbilities().flying || player.zza <= 0.0F) {
            return;
        }
        if (player.onGround()) {
            float bonus = 0.10F;
            if (player.isInWater()) {
                bonus /= 4.0F;
            }
            player.moveRelative(bonus, FORWARD);
        } else if (player.isInWater()) {
            player.moveRelative(0.05F, FORWARD);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, level, entity, slot);
        if (slot != EquipmentSlot.FEET || !(entity instanceof ServerPlayer player)) {
            return;
        }
        if (player.tickCount % 20 == 0) {
            int energy = stack.getOrDefault(TTDataComponents.ENERGY.get(), 0);
            if (energy > 0) {
                energy--;
            } else if (RechargeAccess.consumeCharge(stack, player, 1)) {
                energy = 120;
            }
            stack.set(TTDataComponents.ENERGY.get(), energy);
        }
        boolean active = RechargeAccess.getCharge(stack) > 0
                && !player.getAbilities().flying
                && (player.getLastClientInput().forward()
                || player.getLastClientInput().backward()
                || player.getLastClientInput().left()
                || player.getLastClientInput().right())
                && !player.isShiftKeyDown();
        AttributeInstance stepHeight = player.getAttribute(Attributes.STEP_HEIGHT);
        AttributeInstance jumpHeight = player.getAttribute(Attributes.JUMP_STRENGTH);
        if (stepHeight != null) {
            if (active && !stepHeight.hasModifier(STEP_MODIFIER.id())) {
                stepHeight.addTransientModifier(STEP_MODIFIER);
            } else if (!active && stepHeight.hasModifier(STEP_MODIFIER.id())) {
                stepHeight.removeModifier(STEP_MODIFIER.id());
            }
        }
        if (jumpHeight != null) {
            if (active && !jumpHeight.hasModifier(JUMP_MODIFIER.id())) {
                jumpHeight.addTransientModifier(JUMP_MODIFIER);
            } else if (!active && jumpHeight.hasModifier(JUMP_MODIFIER.id())) {
                jumpHeight.removeModifier(JUMP_MODIFIER.id());
            }
        }
    }

    @Override
    public int getVisDiscount(ItemStack stack) {
        return 5;
    }
}
