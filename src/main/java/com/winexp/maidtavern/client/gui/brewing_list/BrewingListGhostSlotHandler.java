package com.winexp.maidtavern.client.gui.brewing_list;

import com.github.ysbbbbbb.kaleidoscopetavern.init.ModRecipes;
import com.winexp.maidtavern.menu.BrewingListMenu;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public class BrewingListGhostSlotHandler implements IGhostIngredientHandler<BrewingListScreen> {
    @Override
    public <I> List<Target<I>> getTargetsTyped(BrewingListScreen gui, ITypedIngredient<I> ingredient, boolean doStart) {
        ItemStack stack = ingredient.getItemStack().orElse(null);
        if (stack == null) return List.of();
        BrewingListMenu menu = gui.getMenu();
        ResourceLocation recipeId = menu.player.level().getRecipeManager().getAllRecipesFor(ModRecipes.BARREL_RECIPE)
                .stream()
                .filter(recipeHolder ->
                        ItemStack.isSameItem(stack, recipeHolder.value().getResultItem(menu.player.registryAccess())))
                .findFirst()
                .map(RecipeHolder::id)
                .orElse(null);
        if (recipeId == null) return List.of();
        List<Rect2i> areas = gui.getListEntryAreas();
        Rect2i firstArea = areas.getFirst();
        Rect2i lastArea = areas.getLast();
        Rect2i area = new Rect2i(firstArea.getX(), firstArea.getY(),
                lastArea.getX() + lastArea.getWidth() - firstArea.getX(),
                lastArea.getY() + lastArea.getHeight() - firstArea.getY());
        return List.of(
                new Target<>() {
                    @Override
                    public Rect2i getArea() {
                        return area;
                    }

                    @Override
                    public void accept(I ingredient) {
                        menu.brewingList.add(recipeId);
                        gui.getMenu().updateSlots();
                    }
                }
        );
    }

    @Override
    public void onComplete() {

    }
}
