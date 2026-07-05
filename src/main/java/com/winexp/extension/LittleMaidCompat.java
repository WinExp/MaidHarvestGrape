package com.winexp.extension;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.ExtraMaidBrainManager;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.winexp.maid.TaskGrape;
import com.winexp.maid.TaskBrew;

@LittleMaidExtension
public class LittleMaidCompat implements ILittleMaid {
    @Override
    public void addMaidTask(TaskManager manager) {
        manager.add(new TaskGrape());
        manager.add(new TaskBrew());
    }

    @Override
    public void addExtraMaidBrain(ExtraMaidBrainManager manager) {
        manager.addExtraMaidBrain(new TavernExtraMaidBrain());
    }
}
