package com.winexp.maidtavern.event;

import com.winexp.maidtavern.command.MaidTavernCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber
public class RegisterCommands {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        MaidTavernCommand.register(event.getDispatcher());
    }
}
