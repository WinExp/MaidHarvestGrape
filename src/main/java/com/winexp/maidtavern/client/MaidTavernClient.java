package com.winexp.maidtavern.client;

import com.winexp.maidtavern.MaidTavern;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = MaidTavern.MOD_ID, dist = Dist.CLIENT)
public class MaidTavernClient {
    public MaidTavernClient(IEventBus modEventBus, ModContainer modContainer) {

    }
}
