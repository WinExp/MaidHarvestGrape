package com.winexp.maidtavern.menu;

import com.winexp.maidtavern.maid.brew.BrewingList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

public class BrewingListMenu extends AbstractContainerMenu {
    public final RecipeManager recipeManager;
    public final RegistryAccess registryAccess;
    public final BrewingList brewingList;

    public BrewingListMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
        this(containerId, playerInventory, new BrewingList());
    }

    public BrewingListMenu(int containerId, Inventory playerInventory, BrewingList brewingList) {
        super(MaidTavernMenuTypes.BREWING_LIST.get(), containerId);
        registryAccess = playerInventory.player.registryAccess();
        recipeManager = playerInventory.player.level().getRecipeManager();
        this.brewingList = brewingList;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public int getRows() {
        return 2;
    }

    public int getColumns() {
        return 6;
    }
}
