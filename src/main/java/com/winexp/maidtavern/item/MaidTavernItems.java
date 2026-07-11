package com.winexp.maidtavern.item;

import com.winexp.maidtavern.MaidTavern;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MaidTavernItems {
    private static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(MaidTavern.MOD_ID);

    public static final DeferredItem<BrewingListItem> BREWING_LIST = REGISTER.register("brewing_list", () ->
            new BrewingListItem(new Item.Properties()
                    .stacksTo(1)));

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
