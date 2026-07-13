package com.winexp.maidtavern.client.gui.brewing_list;

import com.winexp.maidtavern.MaidTavern;
import com.winexp.maidtavern.menu.BrewingListMenu;
import com.winexp.maidtavern.network.ServerBoundSetBrewingListPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;

public class BrewingListScreen extends AbstractContainerScreen<BrewingListMenu> {
    public static final ResourceLocation LIST_LOCATION = MaidTavern.asResource("textures/gui/brewing_list/list.png");

    public BrewingListScreen(BrewingListMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(LIST_LOCATION, (width - 192) / 2, (height - 192) / 2, 0, 0, 192, 192);
    }

    @Override
    public void onClose() {
        super.onClose();
        int slot = menu.hand == InteractionHand.MAIN_HAND ? menu.player.getInventory().selected : 40;
        var payload = new ServerBoundSetBrewingListPayload(slot, menu.brewingList);
        Minecraft.getInstance().getConnection().send(payload);
    }

    public Rect2i getEntryArea() {
        int x = (width - 192) / 2 + 39;
        int y = (height - 192) / 2 + 16;
        return new Rect2i(x, y, 18 * menu.getColumns(), 18 * menu.getRows());
    }
}
