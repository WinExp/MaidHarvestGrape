package com.winexp.maidtavern.maid.brew.storage;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.winexp.maidtavern.entity.MaidTavernEntities;
import com.winexp.maidtavern.maid.brew.IBrewTask;
import com.winexp.maidtavern.maid.task.MaidSurroundingMoveToBlockTask;
import com.winexp.maidtavern.util.ItemHandlerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

public class MaidBrewMoveToStorageTask extends MaidSurroundingMoveToBlockTask {
    private final IBrewTask task;

    public MaidBrewMoveToStorageTask(IBrewTask task, float movementSpeed, int verticalSearchRange) {
        super(movementSpeed, verticalSearchRange);
        this.task = task;
        setMaxCheckRate(40);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, EntityMaid maid) {
        Brain<EntityMaid> brain = maid.getBrain();
        if (!super.checkExtraStartConditions(level, maid)
                || brain.hasMemoryValue(InitEntities.TARGET_POS.get())
                || brain.hasMemoryValue(MaidTavernEntities.BREWING_SESSION.get())
                || !brain.hasMemoryValue(MaidTavernEntities.BREWING_LIST.get())) return false;
        return task.shouldTake(maid) || !task.getNeedToStoreStacks(maid).isEmpty();
    }

    @Override
    protected void start(ServerLevel level, EntityMaid maid, long gameTimeIn) {
        searchForDestination(level, maid);
    }

    @Override
    protected boolean shouldMoveTo(ServerLevel level, EntityMaid maid, BlockPos pos) {
        if (!(level.getBlockEntity(pos) instanceof Container container)) return false;
        if (!task.isStorageValid(level, pos)) return false;
        IItemHandler containerInv = new InvWrapper(container);
        if (!task.getNeedToTakeStacks(maid, containerInv).isEmpty()) return true;
        return ItemHandlerUtil.canInsertAny(containerInv, task.getNeedToStoreStacks(maid));
    }
}
