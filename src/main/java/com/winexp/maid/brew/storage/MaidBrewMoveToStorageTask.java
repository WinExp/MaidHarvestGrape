package com.winexp.maid.brew.storage;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidMoveToBlockTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.winexp.entity.MaidTavernEntities;
import com.winexp.maid.brew.BrewingList;
import com.winexp.maid.brew.IBrewTask;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class MaidBrewMoveToStorageTask extends MaidMoveToBlockTask {
    private final IBrewTask task;
    private List<ItemStack> toStoreStacksCached;

    public MaidBrewMoveToStorageTask(IBrewTask task, float movementSpeed, int verticalSearchRange) {
        super(movementSpeed, verticalSearchRange);
        this.task = task;
    }

    private List<ItemStack> getToStoreStacks(EntityMaid maid) {
        if (toStoreStacksCached == null) {
            toStoreStacksCached = task.getToStoreStacks(maid);
        }
        return toStoreStacksCached;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, EntityMaid maid) {
        toStoreStacksCached = null;
        Brain<EntityMaid> brain = maid.getBrain();
        BrewingList brewingList = brain.getMemory(MaidTavernEntities.BREWING_LIST.get()).orElse(null);
        boolean takeFlag = false;
        if (brewingList != null) {
            for (ResourceLocation recipeId : brewingList.getRecipes()) {
                if (!takeFlag && !task.hasRequiredMaterials(maid, recipeId)) {
                    takeFlag = true;
                }
            }
        } else return false;

        if (!super.checkExtraStartConditions(level, maid)
                || brain.hasMemoryValue(InitEntities.TARGET_POS.get())
                || brain.hasMemoryValue(MaidTavernEntities.BREWING_SESSION.get())
                || !brain.hasMemoryValue(MaidTavernEntities.BREWING_LIST.get())) return false;
        return takeFlag || !getToStoreStacks(maid).isEmpty();
    }

    @Override
    protected void start(ServerLevel level, EntityMaid maid, long gameTimeIn) {
        searchForDestination(level, maid);
    }

    @Override
    protected boolean shouldMoveTo(ServerLevel level, EntityMaid maid, BlockPos pos) {
        return task.isStorageValid(maid, pos);
    }
}
