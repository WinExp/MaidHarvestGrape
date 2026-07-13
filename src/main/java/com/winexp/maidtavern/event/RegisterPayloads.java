package com.winexp.maidtavern.event;

import com.winexp.maidtavern.network.ServerBoundSetBrewingListPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber
public class RegisterPayloads {
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(ServerBoundSetBrewingListPayload.TYPE, ServerBoundSetBrewingListPayload.STREAM_CODEC, ServerBoundSetBrewingListPayload::handle);
    }
}
