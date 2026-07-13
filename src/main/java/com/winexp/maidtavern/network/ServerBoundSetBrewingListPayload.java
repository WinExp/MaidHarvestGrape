package com.winexp.maidtavern.network;

import com.winexp.maidtavern.MaidTavern;
import com.winexp.maidtavern.item.MaidTavernItems;
import com.winexp.maidtavern.maid.brew.BrewingList;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerBoundSetBrewingListPayload(int slot, BrewingList brewingList) implements CustomPacketPayload {
    public static final Type<ServerBoundSetBrewingListPayload> TYPE = new Type<>(MaidTavern.asResource("set_brewing_list"));
    public static final StreamCodec<ByteBuf, ServerBoundSetBrewingListPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ServerBoundSetBrewingListPayload::slot,
            BrewingList.STREAM_CODEC,
            ServerBoundSetBrewingListPayload::brewingList,
            ServerBoundSetBrewingListPayload::new
    );

    @Override
    public Type<ServerBoundSetBrewingListPayload> type() {
        return TYPE;
    }

    public static void handle(ServerBoundSetBrewingListPayload payload, IPayloadContext context) {
        Player player = context.player();
        int slot = payload.slot;
        if (!Inventory.isHotbarSlot(slot) && slot != Inventory.SLOT_OFFHAND) return;
        ItemStack stack = player.getSlot(slot).get();
        if (!stack.is(MaidTavernItems.BREWING_LIST)) return;
        stack.set(MaidTavernItems.BREWING_LIST_DATA, payload.brewingList);
    }
}
