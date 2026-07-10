package com.winexp.maidtavern.command;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.winexp.maidtavern.entity.MaidTavernEntities;
import com.winexp.maidtavern.maid.brew.BrewingList;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class MaidTavernCommand {
    public static int setBrewingList(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (EntityArgument.getEntity(context, "maid") instanceof EntityMaid maid) {
            String brewingList = StringArgumentType.getString(context, "list");
            List<ResourceLocation> recipes = Arrays.stream(brewingList.split(",")).map(ResourceLocation::parse).toList();
            maid.getBrain().setMemory(MaidTavernEntities.BREWING_LIST.get(), new BrewingList(recipes));
        } else {
            context.getSource().sendFailure(Component.literal("选择实体不是女仆"));
            return -1;
        }
        return 1;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("maidtavern").then(
                literal("set_brewing_list")
                        .then(
                                argument("maid", EntityArgument.entity()).then(
                                        argument("list", StringArgumentType.greedyString()).executes(MaidTavernCommand::setBrewingList)
                                )
                        )
                )
        );
    }
}
