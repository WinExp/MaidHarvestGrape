package com.winexp.maidtavern.client.event;

import com.winexp.maidtavern.client.gui.brewing_list.BrewingListScreen;
import com.winexp.maidtavern.menu.MaidTavernMenuTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber
public class RegisterMenuScreens {
    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(MaidTavernMenuTypes.BREWING_LIST.get(), BrewingListScreen::new);
    }
}
