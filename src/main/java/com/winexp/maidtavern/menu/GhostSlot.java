package com.winexp.maidtavern.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GhostSlot extends Slot {
    private final List<SlotEmptiedListener> listeners = new ArrayList<>();

    public GhostSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    public void addListener(SlotEmptiedListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onQuickCraft(ItemStack oldStack, ItemStack newStack) {
    }

    @Override
    public Optional<ItemStack> tryRemove(int count, int decrement, Player player) {
        Optional<ItemStack> result = super.tryRemove(count, decrement, player);
        for (SlotEmptiedListener listener : this.listeners) {
            listener.onSlotEmptied(this, result.orElse(ItemStack.EMPTY));
        }
        return Optional.empty();
    }

    @Override
    public boolean isFake() {
        return true;
    }

    public interface SlotEmptiedListener {
        void onSlotEmptied(GhostSlot slot, ItemStack prevStack);
    }
}
