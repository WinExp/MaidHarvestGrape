package com.winexp.maid.brew.barrel;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.ysbbbbbb.kaleidoscopetavern.api.blockentity.IBarrel;
import com.github.ysbbbbbb.kaleidoscopetavern.crafting.recipe.BarrelRecipe;
import com.google.common.collect.ImmutableMap;
import com.winexp.entity.MaidTavernEntities;
import com.winexp.maid.IBrewTask;
import com.winexp.maid.brew.BrewingSession;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class MaidBrewAddIngredientTask extends Behavior<EntityMaid> {
    private final IBrewTask task;
    private final int stepCooldown;
    private int cooldown;

    public MaidBrewAddIngredientTask(IBrewTask task, int stepCooldown) {
        super(ImmutableMap.of(
                InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_PRESENT,
                MaidTavernEntities.BREWING_SESSION.get(), MemoryStatus.VALUE_PRESENT
        ));
        this.task = task;
        this.stepCooldown = stepCooldown;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, EntityMaid maid) {
        Brain<EntityMaid> brain = maid.getBrain();
        PositionTracker targetPos = brain.getMemory(InitEntities.TARGET_POS.get()).get();
        Vec3 targetV3d = targetPos.currentPosition();
        if (maid.distanceToSqr(targetV3d) > Math.pow(task.getCloseEnoughDist(), 2)) {
            Optional<WalkTarget> walkTarget = brain.getMemory(MemoryModuleType.WALK_TARGET);
            if (walkTarget.isEmpty() || !walkTarget.get().getTarget().currentPosition().equals(targetV3d)) {
                brain.eraseMemory(InitEntities.TARGET_POS.get());
                clearSession(maid);
            }
            return false;
        }
        BlockPos pos = targetPos.currentBlockPosition();
        IBarrel barrel = task.getBarrel(level, pos);
        if (!task.isBarrelAvailable(maid, barrel)) {
            clearSession(maid);
            return false;
        }
        return true;
    }

    @Override
    protected boolean canStillUse(ServerLevel level, EntityMaid maid, long gameTime) {
        if (!maid.getBrain().hasMemoryValue(MaidTavernEntities.BREWING_SESSION.get())) return false;
        PositionTracker targetPos = maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).get();
        BlockPos pos = targetPos.currentBlockPosition();
        IBarrel barrel = task.getBarrel(level, pos);
        return task.isBarrelAvailable(maid, barrel);
    }

    @Override
    protected void tick(ServerLevel level, EntityMaid maid, long gameTime) {
        Brain<EntityMaid> brain = maid.getBrain();
        PositionTracker targetPos = maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).get();
        BlockPos pos = targetPos.currentBlockPosition();
        IBarrel barrel = task.getBarrel(level, pos);
        BrewingSession session = brain.getMemory(MaidTavernEntities.BREWING_SESSION.get()).get();

        if (--cooldown > 0) return;
        BarrelRecipe recipe = session.getRecipe(maid.level().getRecipeManager());
        if (recipe == null) {
            clearSession(maid);
            return;
        }
        if (!barrel.isOpen()) {
            barrel.openLid(maid);
        } else if (!session.fluidPlaced().booleanValue()) {
            for (int i = 0; i < 4; i++) {
                barrel.addFluid(maid, recipe.fluid().getBucket().getDefaultInstance());
            }
            session.fluidPlaced().setTrue();
        } else if (!session.ingredientsPlaced().booleanValue()) {
            for (Ingredient ingredient : recipe.ingredients()) {
                if (ingredient.isEmpty()) continue;
                for (ItemStack stack : ingredient.getItems()) {
                    if (stack.isEmpty()) continue;
                    barrel.addIngredient(maid, stack.copyWithCount(16));
                    break;
                }
            }
            session.ingredientsPlaced().setTrue();
        } else {
            barrel.closeLid(maid);
            clearSession(maid);
        }
        cooldown = stepCooldown;
    }

    private void clearSession(EntityMaid maid) {
        maid.getBrain().eraseMemory(MaidTavernEntities.BREWING_SESSION.get());
    }

    @Override
    protected void stop(ServerLevel level, EntityMaid maid, long gameTime) {
        Brain<EntityMaid> brain = maid.getBrain();
        brain.eraseMemory(InitEntities.TARGET_POS.get());
        brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        var session = brain.getMemory(MaidTavernEntities.BREWING_SESSION.get());
        session.ifPresent(brewingSession -> clearSession(maid));
    }
}
