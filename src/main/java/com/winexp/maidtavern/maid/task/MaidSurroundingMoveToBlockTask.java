package com.winexp.maidtavern.maid.task;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidMoveToBlockTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public abstract class MaidSurroundingMoveToBlockTask extends MaidMoveToBlockTask {
    protected BoundingBox pathRange = new BoundingBox(-1, -1, -1, 1, 1, 1);

    public MaidSurroundingMoveToBlockTask(float movementSpeed, int verticalSearchRange) {
        super(movementSpeed, verticalSearchRange);
    }

    @Override
    protected boolean checkPathReach(EntityMaid maid, MaidPathFindingBFS pathFinding, BlockPos pos) {
        for (int x = pathRange.minX(); x <= pathRange.maxX(); x++) {
            for (int y = pathRange.minY(); y <= pathRange.maxY(); y++) {
                for (int z = pathRange.minZ(); z <= pathRange.minZ(); z++) {
                    if (super.checkPathReach(maid, pathFinding, pos.offset(x, y, z))) return true;
                }
            }
        }
        return false;
    }
}
