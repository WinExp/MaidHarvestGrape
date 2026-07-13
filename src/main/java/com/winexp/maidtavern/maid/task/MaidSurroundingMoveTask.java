package com.winexp.maidtavern.maid.task;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidMoveToBlockTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public abstract class MaidSurroundingMoveTask extends MaidMoveToBlockTask {
    protected BoundingBox moveRange = new BoundingBox(-1, -1, -1, 1, 1, 1);

    public MaidSurroundingMoveTask(float movementSpeed, int verticalSearchRange) {
        super(movementSpeed, verticalSearchRange);
    }

    @Override
    protected boolean checkPathReach(EntityMaid maid, MaidPathFindingBFS pathFinding, BlockPos pos) {
        for (int x = moveRange.minX(); x <= moveRange.maxX(); x++) {
            for (int y = moveRange.minY(); y <= moveRange.maxY(); y++) {
                for (int z = moveRange.minZ(); z <= moveRange.maxZ(); z++) {
                    if (super.checkPathReach(maid, pathFinding, pos.offset(x, y, z))) return true;
                }
            }
        }
        return false;
    }
}
