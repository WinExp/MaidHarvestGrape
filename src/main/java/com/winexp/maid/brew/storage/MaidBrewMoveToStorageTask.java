package com.winexp.maid.brew.storage;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidMoveToBlockTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.winexp.entity.MaidTavernEntities;
import com.winexp.maid.IBrewTask;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class MaidBrewMoveToStorageTask extends MaidMoveToBlockTask {
    private final IBrewTask task;
    private @Nullable ItemStack toStoreStackCached;

    public MaidBrewMoveToStorageTask(IBrewTask task, float movementSpeed) {
        super(movementSpeed);
        this.task = task;
    }

    private @Nullable ItemStack getToStoreStack(EntityMaid maid) {
        if (toStoreStackCached == null) {
            toStoreStackCached = task.getToStoreStack(maid);
        }
        return toStoreStackCached;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, EntityMaid maid) {
        toStoreStackCached = null;
        if (!super.checkExtraStartConditions(level, maid) || maid.getBrain().hasMemoryValue(InitEntities.TARGET_POS.get())) return false;
        return getToStoreStack(maid) != null
                && !maid.getBrain().hasMemoryValue(MaidTavernEntities.BREWING_SESSION.get());
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
