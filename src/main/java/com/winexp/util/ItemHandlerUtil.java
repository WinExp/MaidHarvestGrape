package com.winexp.util;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

public class ItemHandlerUtil {
    public static boolean isEmpty(IItemHandler itemHandler) {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) return false;
        }
        return true;
    }

    public static boolean canInsert(IItemHandler itemHandler, ItemStack stack) {
        return ItemHandlerHelper.insertItemStacked(itemHandler, stack.copyWithCount(1), true).isEmpty();
    }

    public static boolean hasItem(IItemHandler itemHandler, ItemPredicate predicate) {
        return findStack(itemHandler, predicate) != null;
    }

    public static @Nullable ItemStack findStack(IItemHandler itemHandler, ItemPredicate predicate) {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (predicate.test(stack)) return stack;
        }
        return null;
    }

    public static boolean replaceStack(IItemHandlerModifiable itemHandler, ItemStack from, ItemStack to) {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (stack == from) {
                itemHandler.setStackInSlot(i, to);
                return true;
            }
        }
        return false;
    }
}
