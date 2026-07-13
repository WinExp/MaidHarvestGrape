package com.winexp.maidtavern.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface MaidInteractionItem {
    boolean useOnMaid(Level level, Player player, EntityMaid maid, ItemStack stack);
}
