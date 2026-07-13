package com.winexp.maidtavern.maid.brew.storage;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.winexp.maidtavern.entity.MaidTavernEntities;
import com.winexp.maidtavern.maid.brew.IBrewTask;
import com.winexp.maidtavern.util.ItemHandlerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

import java.util.Optional;

public class MaidBrewTakeAndStoreTask extends Behavior<EntityMaid> {
    private final IBrewTask task;
    private final double closeEnoughDist;

    public MaidBrewTakeAndStoreTask(IBrewTask task, double closeEnoughDist) {
        super(ImmutableMap.of(
                InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_PRESENT,
                MaidTavernEntities.BREWING_LIST.get(), MemoryStatus.VALUE_PRESENT,
                MaidTavernEntities.BREWING_SESSION.get(), MemoryStatus.VALUE_ABSENT
        ));
        this.task = task;
        this.closeEnoughDist = closeEnoughDist;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, EntityMaid maid) {
        Brain<EntityMaid> brain = maid.getBrain();
        PositionTracker targetPos = brain.getMemory(InitEntities.TARGET_POS.get()).get();

        BlockPos pos = targetPos.currentBlockPosition();
        if (!task.isStorageValid(level, pos)) return false;

        Vec3 targetV3d = targetPos.currentPosition();
        if (maid.distanceToSqr(targetV3d) > Math.pow(closeEnoughDist, 2)) {
            Optional<WalkTarget> walkTarget = brain.getMemory(MemoryModuleType.WALK_TARGET);
            if (walkTarget.isEmpty() || !walkTarget.get().getTarget().currentPosition().equals(targetV3d)) {
                brain.eraseMemory(InitEntities.TARGET_POS.get());
            }
            return false;
        }
        return true;
    }

    @Override
    protected void start(ServerLevel level, EntityMaid maid, long gameTime) {
        Brain<EntityMaid> brain = maid.getBrain();
        BlockPos pos = brain.getMemory(InitEntities.TARGET_POS.get()).get().currentBlockPosition();
        BaseContainerBlockEntity container = (BaseContainerBlockEntity) level.getBlockEntity(pos);
        IItemHandlerModifiable storage = new InvWrapper(container);
        IItemHandlerModifiable maidInv = maid.getAvailableInv(true);
        for (Pair<ItemStack, Integer> pair : task.getNeedToTakeStacks(maid, storage)) {
            ItemStack stack = pair.getFirst();
            int count = pair.getSecond();
            if (!ItemHandlerUtil.canInsert(maidInv, stack.copyWithCount(count))) continue;
            ItemHandlerHelper.insertItemStacked(maidInv, stack.copyWithCount(count), false);
            stack.shrink(count);
        }

        for (ItemStack stack : task.getNeedToStoreStacks(maid)) {
            if (!ItemHandlerUtil.canInsert(storage, stack)) continue;
            ItemHandlerUtil.replaceStack(maidInv, stack,
                    ItemHandlerHelper.insertItemStacked(storage, stack, false));
        }
        brain.eraseMemory(InitEntities.TARGET_POS.get());
        maid.playSound(SoundEvents.ITEM_FRAME_REMOVE_ITEM, 1.0f, 1.0f);
    }
}
