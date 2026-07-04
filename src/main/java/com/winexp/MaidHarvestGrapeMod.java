package com.winexp;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(MaidHarvestGrapeMod.MOD_ID)
public class MaidHarvestGrapeMod {
    public static final String MOD_ID = "maidharvestgrape";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MaidHarvestGrapeMod(IEventBus modEventBus, ModContainer modContainer) {
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
