package com.winexp.maidtavern.compat;

import com.winexp.maidtavern.MaidTavern;
import com.winexp.maidtavern.client.gui.brewing_list.BrewingListGhostSlotHandler;
import com.winexp.maidtavern.client.gui.brewing_list.BrewingListScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JeiCompat implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return MaidTavern.asResource("jei_plugin");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGhostIngredientHandler(BrewingListScreen.class, new BrewingListGhostSlotHandler());
    }
}
