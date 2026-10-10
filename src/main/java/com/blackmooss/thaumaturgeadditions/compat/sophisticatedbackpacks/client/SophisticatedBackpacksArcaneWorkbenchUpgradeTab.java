package com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.client;

import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
import com.blackmooss.thaumaturgeadditions.compat.sophisticatedbackpacks.common.gui.SophisticatedBackpacksArcaneWorkbenchUpgradeContainer;
import com.leclowndu93150.thaumaturge.TTIds;
import com.leclowndu93150.thaumaturge.content.recipe.ThaumaturgeCraftingManager;
import com.leclowndu93150.thaumaturge.content.recipe.workbench.ArcaneCraftingInput;
import com.leclowndu93150.thaumaturge.content.recipe.workbench.ArcaneCraftingRecipe;
import com.leclowndu93150.thaumaturge.content.workbench.WorkbenchPayment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.Slot;
import net.p3pp3rf1y.sophisticatedbackpacks.client.gui.BackpackButtonDefinitions;
import net.p3pp3rf1y.sophisticatedcore.client.gui.IForegroundRenderable;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeSettingsTab;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ToggleButton;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Dimension;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class SophisticatedBackpacksArcaneWorkbenchUpgradeTab
        extends UpgradeSettingsTab<SophisticatedBackpacksArcaneWorkbenchUpgradeContainer>
        implements IForegroundRenderable {
    public static final int PANEL_WIDTH = 100;
    public static final int PANEL_HEIGHT = 131;
    private static final int PANEL_OFFSET_X = 3;
    private static final int PANEL_OFFSET_Y = 26;

    private static final Identifier PANEL_TEXTURE = ThaumaturgeAdditions.identifier("textures/gui/arcane_workbench_upgrade.png");
    private static final Identifier WAND_SLOT_TEXTURE = TTIds.rl("textures/gui/workbench_wand_slot.png");
    private static final int PANEL_TEXTURE_SIZE = 256;
    private static final int WAND_FRAME_WIDTH = 38;
    private static final int WAND_FRAME_HEIGHT = 34;
    private static final int WAND_FRAME_OFFSET_X = 10;
    private static final int WAND_FRAME_OFFSET_Y = 6;

    private static final List<Position> GRID_SLOT_POSITIONS = List.of(
            new Position(23, 23), new Position(42, 23), new Position(61, 23),
            new Position(23, 42), new Position(42, 42), new Position(61, 42),
            new Position(23, 61), new Position(42, 61), new Position(61, 61));
    private static final List<Position> CRYSTAL_SLOT_POSITIONS = List.of(
            new Position(80, 51), new Position(5, 32), new Position(80, 32),
            new Position(5, 51), new Position(33, 4), new Position(52, 4));
    private static final Position RESULT_SLOT_POSITION = new Position(42, 109);
    private static final Position WAND_SLOT_POSITION = new Position(9, 109);

    private static final Position SHIFT_CLICK_BUTTON_POSITION = new Position(3, 79);
    private static final Position REFILL_BUTTON_POSITION = new Position(79, 79);

    private static final String VIS_AVAILABLE_KEY = "gui.thaumaturge.arcane_workbench.vis_available";
    private static final int VIS_LINE_CENTER_X = 83;
    private static final int VIS_LINE_Y = 115;
    private static final float VIS_TEXT_SCALE = 0.5F;
    private static final int VIS_COLOR_AVAILABLE = 0xFF6E6EEE;
    private static final int VIS_COLOR_INSUFFICIENT = 0xFFEE6E6E;

    public SophisticatedBackpacksArcaneWorkbenchUpgradeTab(
            SophisticatedBackpacksArcaneWorkbenchUpgradeContainer upgradeContainer,
            Position position,
            StorageScreenBase<?> screen
    ) {
        super(upgradeContainer, position, screen,
                Component.translatable("screen.thaumaturgeadditions.arcane_workbench_upgrade"),
                Component.translatable("screen.thaumaturgeadditions.arcane_workbench_upgrade"));
        this.openTabDimension = new Dimension(PANEL_OFFSET_X + PANEL_WIDTH + 3, PANEL_OFFSET_Y + PANEL_HEIGHT + 4);
        this.addHideableChild(new ToggleButton<>(
                new Position(this.x + PANEL_OFFSET_X + SHIFT_CLICK_BUTTON_POSITION.x(), this.y + PANEL_OFFSET_Y + SHIFT_CLICK_BUTTON_POSITION.y()),
                BackpackButtonDefinitions.SHIFT_CLICK_TARGET,
                button -> this.getContainer().setShiftClickIntoStorage(!this.getContainer().shouldShiftClickIntoStorage()),
                this.getContainer()::shouldShiftClickIntoStorage));
        // 自动填充方格：合成后空掉的合成格自动从背包/物品栏补材料（复用 SB 自带按钮图标）
        this.addHideableChild(new ToggleButton<>(
                new Position(this.x + PANEL_OFFSET_X + REFILL_BUTTON_POSITION.x(), this.y + PANEL_OFFSET_Y + REFILL_BUTTON_POSITION.y()),
                BackpackButtonDefinitions.REFILL_CRAFTING_GRID,
                button -> this.getContainer().setRefillCraftingGrid(!this.getContainer().shouldRefillCraftingGrid()),
                this.getContainer()::shouldRefillCraftingGrid));
    }

    @Override
    protected void extractBg(@NonNull GuiGraphicsExtractor guiGraphics, @NonNull Minecraft minecraft, int mouseX, int mouseY) {
        super.extractBg(guiGraphics, minecraft, mouseX, mouseY);
        if (!this.isOpen) {
            return;
        }
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, PANEL_TEXTURE, this.x + PANEL_OFFSET_X, this.y + PANEL_OFFSET_Y,
                0.0F, 0.0F, PANEL_WIDTH, PANEL_HEIGHT, PANEL_TEXTURE_SIZE, PANEL_TEXTURE_SIZE);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, WAND_SLOT_TEXTURE,
                this.x + PANEL_OFFSET_X + WAND_SLOT_POSITION.x() - WAND_FRAME_OFFSET_X,
                this.y + PANEL_OFFSET_Y + WAND_SLOT_POSITION.y() - WAND_FRAME_OFFSET_Y,
                0.0F, 0.0F, WAND_FRAME_WIDTH, WAND_FRAME_HEIGHT, WAND_FRAME_WIDTH, WAND_FRAME_HEIGHT);
    }

    @Override
    public void extractForeground(@NonNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (!this.isOpen) {
            return;
        }
        int available = this.getContainer().getStoredAura();
        Component text = Component.translatable(VIS_AVAILABLE_KEY, available);
        Font font = this.font;
        int halfWidth = font.width(text) / 2;
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(this.x + PANEL_OFFSET_X + VIS_LINE_CENTER_X, this.y + PANEL_OFFSET_Y + VIS_LINE_Y);
        guiGraphics.pose().scale(VIS_TEXT_SCALE);
        guiGraphics.text(font, text, -halfWidth, 0, this.requiredVis() > available ? VIS_COLOR_INSUFFICIENT : VIS_COLOR_AVAILABLE, false);
        guiGraphics.pose().popMatrix();
    }

    private int requiredVis() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) {
            return 0;
        }
        ArcaneCraftingInput input = this.getContainer().asArcaneCraftInput();
        if (input.width() <= 0 || input.height() <= 0) {
            return 0;
        }
        ArcaneCraftingRecipe recipe = ThaumaturgeCraftingManager.findMatchingArcaneRecipe(minecraft.level, input, minecraft.player);
        if (recipe == null) {
            return 0;
        }
        return WorkbenchPayment.plan(recipe, input, minecraft.player).auraVis();
    }

    @Override
    protected void moveSlotsToTab() {
        List<Slot> slots = this.getContainer().getSlots();
        int index = 0;
        for (Position position : GRID_SLOT_POSITIONS) {
            this.moveSlotTo(slots.get(index++), position);
        }
        for (Position position : CRYSTAL_SLOT_POSITIONS) {
            this.moveSlotTo(slots.get(index++), position);
        }
        this.moveSlotTo(slots.get(index++), WAND_SLOT_POSITION);
        this.moveSlotTo(slots.get(index), RESULT_SLOT_POSITION);
    }

    private void moveSlotTo(Slot slot, Position position) {
        slot.x = this.x + PANEL_OFFSET_X + position.x() - this.screen.getGuiLeft();
        slot.y = this.y + PANEL_OFFSET_Y + position.y() - this.screen.getGuiTop();
    }
}
