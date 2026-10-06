package com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.client;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.compat.travelersbackpack.upgrades.TravelersBackpackArcaneWorkbenchUpgrade;
import com.leclowndu93150.thaumaturge.TCIds;
import com.leclowndu93150.thaumaturge.content.recipe.ThaumaturgeCraftingManager;
import com.leclowndu93150.thaumaturge.content.recipe.workbench.ArcaneCraftingInput;
import com.leclowndu93150.thaumaturge.content.recipe.workbench.ArcaneCraftingRecipe;
import com.leclowndu93150.thaumaturge.content.workbench.WorkbenchPayment;
import com.tiviacz.travelersbackpack.client.screens.BackpackScreen;
import com.tiviacz.travelersbackpack.client.screens.widgets.UpgradeWidgetBase;
import com.tiviacz.travelersbackpack.client.screens.widgets.WidgetElement;
import com.tiviacz.travelersbackpack.inventory.upgrades.Point;
import com.tiviacz.travelersbackpack.inventory.upgrades.ResultArrowElement;
import com.tiviacz.travelersbackpack.util.StacksHandlerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class TravelersBackpackArcaneWorkbenchWidget extends UpgradeWidgetBase<TravelersBackpackArcaneWorkbenchUpgrade> {
    private static final Identifier PANEL_TEXTURE = ThaumaturgeAdditions.identifier("textures/gui/travelersbackpack_arcane_workbench_upgrade.png");
    private static final Identifier WAND_SLOT_TEXTURE = TCIds.rl("textures/gui/workbench_wand_slot.png");
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

    public TravelersBackpackArcaneWorkbenchWidget(BackpackScreen screen, TravelersBackpackArcaneWorkbenchUpgrade upgrade, Point pos) {
        super(screen, upgrade, pos, new Point(0, 0), "screen.thaumaturgeadditions.travelersbackpack_arcane_workbench_upgrade");
        WidgetElement arrowElement = new WidgetElement(ARROW_POSITION, ARROW_SIZE);
        this.resultArrowElement = new ResultArrowElement(screen, this, arrowElement);
    }

    @Override
    public void renderBg(GuiGraphicsExtractor graphics, int x, int y, int mouseX, int mouseY) {
        if (!this.isTabOpened()) {
            return;
        }
        int left = this.pos.x();
        int top = this.pos.y();
        graphics.blit(RenderPipelines.GUI_TEXTURED, PANEL_TEXTURE, left, top, 0.0F, 0.0F,
                TravelersBackpackArcaneWorkbenchUpgrade.TAB_WIDTH, TravelersBackpackArcaneWorkbenchUpgrade.TAB_HEIGHT, PANEL_TEXTURE_SIZE, PANEL_TEXTURE_SIZE);
        drawAvailableVis(graphics, left, top);
        graphics.item(StacksHandlerUtils.getStackInSlot(this.screen.getWrapper().getUpgrades(), this.dataHolderSlot), left + 4, top + 4);
        this.resultArrowElement.renderBg(graphics, x, y, mouseX, mouseY);
    }

    @Override
    public void renderAboveBg(GuiGraphicsExtractor graphics, int xPos, int yPos, int mouseX, int mouseY, float partialTicks) {
        if (!this.isTabOpened()) {
            return;
        }
        Point wand = TravelersBackpackArcaneWorkbenchUpgrade.WAND_SLOT_POSITION;
        graphics.blit(RenderPipelines.GUI_TEXTURED, WAND_SLOT_TEXTURE,
                this.pos.x() + wand.x() - WAND_FRAME_OFFSET_X, this.pos.y() + wand.y() - WAND_FRAME_OFFSET_Y,
                0.0F, 0.0F, WAND_FRAME_WIDTH, WAND_FRAME_HEIGHT, WAND_FRAME_WIDTH, WAND_FRAME_HEIGHT);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.isBackpackOwner()) {
            renderRemoveButton(graphics, mouseX, mouseY);
        }
    }

    @Override
    public boolean enableButtonMouseClicked(double mouseX, double mouseY) {
        return false;
    }

    @Override
    public void renderEnableButtonTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
    }

    @Override
    public void renderTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.renderTooltip(graphics, mouseX, mouseY);
        this.resultArrowElement.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (this.resultArrowElement.mouseClicked(event)) {
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    private void drawAvailableVis(GuiGraphicsExtractor graphics, int left, int top) {
        int available = this.upgrade.getStoredAura();
        int required = requiredVis();
        Component text = Component.translatable(VIS_AVAILABLE_KEY, available);
        Font font = this.screen.getScreenFont();
        int halfWidth = font.width(text) / 2;
        graphics.pose().pushMatrix();
        graphics.pose().translate(left + VIS_LINE_CENTER_X, top + VIS_LINE_Y);
        graphics.pose().scale(VIS_TEXT_SCALE);
        graphics.text(font, text, -halfWidth, 0, required > available ? VIS_COLOR_INSUFFICIENT : VIS_COLOR_AVAILABLE, false);
        graphics.pose().popMatrix();
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
