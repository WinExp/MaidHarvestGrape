package com.winexp.maidtavern.menu;

import com.winexp.maidtavern.MaidTavern;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MaidTavernMenuTypes {
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, MaidTavern.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<BrewingListMenu>> BREWING_LIST = MENU_TYPES.register("brewing_list", () ->
            IMenuTypeExtension.create(BrewingListMenu::new));

    public static void register(IEventBus modEventBus) {
        MENU_TYPES.register(modEventBus);
    }
}
