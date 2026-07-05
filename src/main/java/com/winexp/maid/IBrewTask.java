package com.winexp.maid;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.ysbbbbbb.kaleidoscopetavern.api.blockentity.IBarrel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public interface IBrewTask extends IMaidTask {
    double getCloseEnoughDist();

    @Contract("null -> false")
    boolean isBarrelAvailable(@Nullable IBarrel barrel);

    @Contract("_, null -> false")
    boolean isBarrelAvailable(EntityMaid maid, @Nullable IBarrel barrel);

    @Nullable IBarrel getBarrel(ServerLevel level, BlockPos pos);

    @Nullable ItemStack getToStoreStack(EntityMaid maid);

    boolean isStorageValid(EntityMaid maid, BlockPos pos);
}
