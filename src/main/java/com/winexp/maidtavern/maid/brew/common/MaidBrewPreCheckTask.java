package com.winexp.maidtavern.maid.brew.common;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import com.winexp.maidtavern.MaidTavernMod;
import com.winexp.maidtavern.entity.MaidTavernEntities;
import com.winexp.maidtavern.maid.brew.BrewingList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;

public class MaidBrewPreCheckTask extends Behavior<EntityMaid> {
    public MaidBrewPreCheckTask() {
        super(ImmutableMap.of());
    }

    @Override
    protected void start(ServerLevel level, EntityMaid maid, long gameTime) {
        Brain<EntityMaid> brain = maid.getBrain();
        BrewingList brewingList = brain.getMemory(MaidTavernEntities.BREWING_LIST.get()).orElse(null);
        if (brewingList != null) {
            for (ResourceLocation recipeId : brewingList.getRecipes()) {
                if (level.getRecipeManager().byKey(recipeId).isEmpty()) {
                    brewingList.remove(recipeId);
                    MaidTavernMod.LOGGER.warn("Brewing list has an invalid recipe: {}, removed", recipeId);
                }
            }
            if (brewingList.isEmpty()) {
                brain.eraseMemory(MaidTavernEntities.BREWING_LIST.get());
                MaidTavernMod.LOGGER.warn("Brewing list is empty, erased");
            }
        }
    }
}
