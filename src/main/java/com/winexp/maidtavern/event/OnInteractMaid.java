package com.winexp.maidtavern.event;

import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.winexp.maidtavern.item.MaidTavernItems;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public class OnInteractMaid {
    @SubscribeEvent
    public static void onInteractMaid(InteractMaidEvent event) {
        if (event.getStack().is(MaidTavernItems.BREWING_LIST)) {
            MaidTavernItems.BREWING_LIST.get().useOnMaid(event.getWorld(), event.getPlayer(), event.getMaid(), event.getStack());
        }
    }
}
