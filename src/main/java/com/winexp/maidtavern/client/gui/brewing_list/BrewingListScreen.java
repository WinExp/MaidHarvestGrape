package com.winexp.maidtavern.client.gui.brewing_list;

import com.github.ysbbbbbb.kaleidoscopetavern.crafting.recipe.BarrelRecipe;
import com.winexp.maidtavern.MaidTavern;
import com.winexp.maidtavern.menu.BrewingListMenu;
import com.winexp.maidtavern.network.ServerBoundUpdateBrewingListPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

public class BrewingListScreen extends AbstractContainerScreen<BrewingListMenu> {
    public static final ResourceLocation LIST_LOCATION = MaidTavern.asResource("textures/gui/brewing_list/list.png");
    private final List<ItemStack> displayItems = new ArrayList<>();

    public BrewingListScreen(BrewingListMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        updateRenderItems();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        int idx = getAreaIdx((int) mouseX, (int) mouseY);
        if (idx < 0 || idx >= menu.brewingList.size()) return false;
        ResourceLocation recipeId = menu.brewingList.getRecipes().get(idx);
        if (button == 0) {
            menu.brewingList.remove(recipeId);
        }
        updateRenderItems();
        return true;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        int idx = 0;
        for (ItemStack stack : displayItems) {
            Rect2i area = getListEntryAreas().get(idx++);
            int x = area.getX();
            int y = area.getY();
            int seed = area.getX() + area.getY() * imageWidth;
            guiGraphics.renderFakeItem(stack, x, y, seed);
            if (area.contains(mouseX, mouseY)) {
                renderSlotHighlight(guiGraphics, x, y, 0);
            }
        }
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
        var payload = new ServerBoundUpdateBrewingListPayload(slot, menu.brewingList);
        Minecraft.getInstance().getConnection().send(payload);
    }

    public void updateRenderItems() {
        displayItems.clear();
        for (ResourceLocation recipeId : menu.brewingList.getRecipes()) {
            BarrelRecipe recipe = (BarrelRecipe) menu.player.level().getRecipeManager().byKey(recipeId).map(RecipeHolder::value).get();
            displayItems.add(recipe.getResultItem(menu.player.registryAccess()));
        }
    }

    public int getAreaIdx(int x, int y) {
        int idx = 0;
        for (Rect2i area : getListEntryAreas()) {
            if (area.contains(x, y)) return idx;
            idx++;
        }
        return -1;
    }

    public List<Rect2i> getListEntryAreas() {
        List<Rect2i> list = new ArrayList<>();
        for (int row = 0; row < menu.getRows(); row++) {
            for (int column = 0; column < menu.getColumns(); column++) {
                list.add(new Rect2i((width - 114) / 2 + 18 * column, (height - 160) / 2 + 18 * row, 17, 17));
            }
        }
        return list;
    }
}
