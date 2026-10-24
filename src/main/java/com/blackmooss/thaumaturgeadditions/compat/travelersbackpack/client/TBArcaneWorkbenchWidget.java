package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.client;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.upgrades.TBArcaneWorkbenchUpgrade;
import com.leclowndu93150.thaumaturge.TTIds;
import com.leclowndu93150.thaumaturge.content.recipe.ThaumaturgeCraftingManager;
import com.leclowndu93150.thaumaturge.content.recipe.workbench.ArcaneCraftingInput;
import com.leclowndu93150.thaumaturge.content.recipe.workbench.ArcaneCraftingRecipe;
import com.leclowndu93150.thaumaturge.content.workbench.WorkbenchPayment;
import com.tiviacz.travelersbackpack.client.screens.BackpackScreen;
import com.tiviacz.travelersbackpack.client.screens.widgets.UpgradeWidgetBase;
import com.tiviacz.travelersbackpack.client.screens.widgets.WidgetElement;
import com.tiviacz.travelersbackpack.inventory.upgrades.Point;
import com.tiviacz.travelersbackpack.inventory.upgrades.ResultArrowElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TBArcaneWorkbenchWidget extends UpgradeWidgetBase<TBArcaneWorkbenchUpgrade> {
    private static final ResourceLocation PANEL_TEXTURE = ThaumaturgeAdditions.identifier("textures/gui/arcane_workbench_upgrade.png");
    private static final ResourceLocation WAND_SLOT_TEXTURE = TTIds.rl("textures/gui/workbench_wand_slot.png");
    private static final int PANEL_TEXTURE_SIZE = 256;
    private static final int WAND_FRAME_WIDTH = 38;
    private static final int WAND_FRAME_HEIGHT = 34;
    private static final int WAND_FRAME_OFFSET_X = 10;
    private static final int WAND_FRAME_OFFSET_Y = 6;
    private static final String VIS_AVAILABLE_KEY = "gui.thaumaturge.arcane_workbench.vis_available";
    private static final int VIS_LINE_CENTER_X = 83;
    private static final int VIS_LINE_Y = 115;
    private static final float VIS_TEXT_SCALE = 0.5F;
    private static final int VIS_COLOR_AVAILABLE = 0xFF6E6EEE;
    private static final int VIS_COLOR_INSUFFICIENT = 0xFFEE6E6E;
    private static final Point ARROW_POSITION = new Point(11, 90);
    private static final Point ARROW_SIZE = new Point(12, 12);

    private final ResultArrowElement resultArrowElement;

    public TBArcaneWorkbenchWidget(BackpackScreen screen, TBArcaneWorkbenchUpgrade upgrade, Point pos) {
        super(screen, upgrade, pos, new Point(0, 0), "screen.thaumaturgeadditions.arcane_workbench_upgrade");
        this.resultArrowElement = new ResultArrowElement(screen, this, new WidgetElement(ARROW_POSITION, ARROW_SIZE));
    }

    @Override
    public void renderBg(GuiGraphics guiGraphics, int x, int y, int mouseX, int mouseY) {
        if (this.isTabOpened()) {
            guiGraphics.blit(PANEL_TEXTURE, this.pos.x(), this.pos.y(), 0.0F, 0.0F,
                    TBArcaneWorkbenchUpgrade.TAB_WIDTH, TBArcaneWorkbenchUpgrade.TAB_HEIGHT,
                    PANEL_TEXTURE_SIZE, PANEL_TEXTURE_SIZE);
            drawAvailableVis(guiGraphics);
            guiGraphics.renderItem(this.screen.getWrapper().getUpgrades().getStackInSlot(this.dataHolderSlot), this.pos.x() + 4, this.pos.y() + 4);
        }
        this.resultArrowElement.renderBg(guiGraphics, x, y, mouseX, mouseY);
    }

    @Override
    public void renderAboveBg(GuiGraphics guiGraphics, int xPos, int yPos, int mouseX, int mouseY, float partialTicks) {
        if (this.isTabOpened()) {
            Point wand = TBArcaneWorkbenchUpgrade.WAND_SLOT_POSITION;
            guiGraphics.blit(WAND_SLOT_TEXTURE,
                    this.pos.x() + wand.x() - WAND_FRAME_OFFSET_X, this.pos.y() + wand.y() - WAND_FRAME_OFFSET_Y,
                    0.0F, 0.0F, WAND_FRAME_WIDTH, WAND_FRAME_HEIGHT, WAND_FRAME_WIDTH, WAND_FRAME_HEIGHT);
        }
    }

    @Override
    public void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);
        this.resultArrowElement.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.resultArrowElement.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void drawAvailableVis(GuiGraphics guiGraphics) {
        int available = this.upgrade.getStoredAura();
        int required = requiredVis();
        Component text = Component.translatable(VIS_AVAILABLE_KEY, available);
        Font font = this.screen.getFont();
        int halfWidth = font.width(text) / 2;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.pos.x() + VIS_LINE_CENTER_X, this.pos.y() + VIS_LINE_Y, 0.0F);
        guiGraphics.pose().scale(VIS_TEXT_SCALE, VIS_TEXT_SCALE, VIS_TEXT_SCALE);
        guiGraphics.drawString(font, text, -halfWidth, 0, required > available ? VIS_COLOR_INSUFFICIENT : VIS_COLOR_AVAILABLE, false);
        guiGraphics.pose().popPose();
    }

    private int requiredVis() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) {
            return 0;
        }
        ArcaneCraftingInput input = this.upgrade.asArcaneCraftInput();
        if (input.width() <= 0 || input.height() <= 0) {
            return 0;
        }
        ArcaneCraftingRecipe recipe = ThaumaturgeCraftingManager.findMatchingArcaneRecipe(minecraft.level, input, minecraft.player);
        if (recipe == null) {
            return 0;
        }
        return WorkbenchPayment.plan(recipe, input, minecraft.player).auraVis();
    }
}
