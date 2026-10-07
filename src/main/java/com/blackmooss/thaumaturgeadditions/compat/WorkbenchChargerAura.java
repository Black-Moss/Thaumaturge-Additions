package com.blackmooss.thaumaturgeadditions.compat;

import com.leclowndu93150.thaumaturge.api.aura.AuraHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.ArrayList;
import java.util.List;

public final class WorkbenchChargerAura {
    public static final int CHARGER_RADIUS = 1;

    private WorkbenchChargerAura() {
    }

    public static List<BlockPos> anchors(BlockPos anchor) {
        ChunkPos center = ChunkPos.containing(anchor);
        List<BlockPos> anchors = new ArrayList<>(9);
        for (int x = -CHARGER_RADIUS; x <= CHARGER_RADIUS; x++) {
            for (int z = -CHARGER_RADIUS; z <= CHARGER_RADIUS; z++) {
                anchors.add(new ChunkPos(center.x() + x, center.z() + z).getMiddleBlockPosition(anchor.getY()));
            }
        }
        return anchors;
    }

    // 读数用：把所有锚点的灵气加起来（对齐原版 BlockEntityArcaneWorkbench.refreshAura）
    public static int total(ServerLevel level, List<BlockPos> anchors) {
        int total = 0;
        for (BlockPos anchor : anchors) {
            total += (int) AuraHelper.getVis(level, anchor);
        }
        return total;
    }

    public static int drain(ServerLevel level, List<BlockPos> anchors, int need, TransactionContext transaction) {
        if (need <= 0 || anchors.isEmpty()) {
            return 0;
        }
        float remaining = need;
        int share = Math.max(1, need / anchors.size());
        while (remaining > 0.0F) {
            float drainedThisPass = 0.0F;
            for (BlockPos anchor : anchors) {
                float drained = AuraHelper.drainVis(level, anchor, Math.min(share, remaining), transaction);
                drainedThisPass += drained;
                remaining -= drained;
                if (remaining <= 0.0F) {
                    return need;
                }
            }
            if (drainedThisPass <= 0.0F) {
                break;
            }
        }
        return Math.max(0, (int) (need - remaining));
    }
}
