package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack;

import com.blackmooss.thaumaturgeadditions.compat.WorkbenchChargerAura;
import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.item.TravelersBackpackArcaneChargerUpgradeItem;
import com.leclowndu93150.thaumaturge.api.aura.AuraHelper;
import com.leclowndu93150.thaumaturge.api.recipe.ArcaneWorkbenchContext;
import com.leclowndu93150.thaumaturge.api.recipe.IArcaneWorkbench;
import com.leclowndu93150.thaumaturge.api.recipe.IWorkbenchAuraSource;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public final class TravelersBackpackArcaneWorkbenchAuraSource implements IWorkbenchAuraSource {
    @Override
    public int supply(ArcaneWorkbenchContext context, ServerPlayer player, IArcaneWorkbench workbench, int need, TransactionContext transaction) {
        if (need <= 0 || context.kind() != ArcaneWorkbenchContext.Kind.VIRTUAL) {
            return 0;
        }
        if (!TravelersBackpackCompat.WORKBENCH_IDENTITY.equals(context.hostIdentity())) {
            return 0;
        }
        ServerLevel serverLevel = player.level();
        BlockPos anchor = context.blockPosition().orElseGet(player::blockPosition);
        if (TravelersBackpackArcaneChargerUpgradeItem.hasCharger(player)) {
            return WorkbenchChargerAura.drain(serverLevel, WorkbenchChargerAura.anchors(anchor), need, transaction);
        }
        return (int) AuraHelper.drainVis(serverLevel, anchor, need, transaction);
    }
}
