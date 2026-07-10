package com.winexp.maidtavern;

import com.mojang.logging.LogUtils;
import com.winexp.maidtavern.entity.MaidTavernEntities;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(MaidTavernMod.MOD_ID)
public class MaidTavernMod {
    public static final String MOD_ID = "maidtavern";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MaidTavernMod(IEventBus modEventBus, ModContainer modContainer) {
        MaidTavernEntities.register(modEventBus);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
