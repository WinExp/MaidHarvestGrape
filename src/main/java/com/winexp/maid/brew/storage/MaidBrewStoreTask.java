package com.winexp.maid.brew.storage;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.ImmutableMap;
import com.winexp.entity.MaidTavernEntities;
import com.winexp.maid.IBrewTask;
import com.winexp.util.ItemHandlerUtil;
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
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MaidBrewStoreTask extends Behavior<EntityMaid> {
    private final IBrewTask task;
    private @Nullable ItemStack toStoreStackCached;

    public MaidBrewStoreTask(IBrewTask task) {
        super(ImmutableMap.of(
                InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_PRESENT
        ));
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
        Brain<EntityMaid> brain = maid.getBrain();
        PositionTracker targetPos = brain.getMemory(InitEntities.TARGET_POS.get()).get();
        Vec3 targetV3d = targetPos.currentPosition();
        if (maid.distanceToSqr(targetV3d) > Math.pow(task.getCloseEnoughDist(), 2)) {
            Optional<WalkTarget> walkTarget = brain.getMemory(MemoryModuleType.WALK_TARGET);
            if (walkTarget.isEmpty() || !walkTarget.get().getTarget().currentPosition().equals(targetV3d)) {
                brain.eraseMemory(InitEntities.TARGET_POS.get());
            }
            return false;
        }
        if (getToStoreStack(maid) == null || brain.hasMemoryValue(MaidTavernEntities.BREWING_SESSION.get())) return false;
        BlockPos pos = targetPos.currentBlockPosition();
        return task.isStorageValid(maid, pos);
    }

    @Override
    protected void start(ServerLevel level, EntityMaid maid, long gameTime) {
        Brain<EntityMaid> brain = maid.getBrain();
        BlockPos pos = brain.getMemory(InitEntities.TARGET_POS.get()).get().currentBlockPosition();
        BaseContainerBlockEntity container = (BaseContainerBlockEntity) level.getBlockEntity(pos);
        IItemHandler storage = new InvWrapper(container);
        while (true) {
            ItemStack toStoreStack = getToStoreStack(maid);
            if (toStoreStack == null) {
                brain.eraseMemory(InitEntities.TARGET_POS.get());
                break;
            }
            ItemHandlerUtil.replaceStack(maid.getAvailableInv(true), toStoreStack,
                    ItemHandlerHelper.insertItemStacked(storage, toStoreStack, false));
            toStoreStackCached = null;
        }
        maid.playSound(SoundEvents.ITEM_FRAME_ADD_ITEM, 1.0f, 1.0f);
    }
}
