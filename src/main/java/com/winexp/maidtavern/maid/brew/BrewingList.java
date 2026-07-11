package com.winexp.maidtavern.maid.brew;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BrewingList {
    public static final Codec<BrewingList> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            ResourceLocation.CODEC.listOf().fieldOf("recipes").validate(list -> {
                if (!list.isEmpty()) return DataResult.success(list);
                else return DataResult.error(() -> "recipes cannot be empty");
            }).forGetter(BrewingList::getRecipes)
    ).apply(instance, BrewingList::new));
    private final List<ResourceLocation> recipeIds;

    public BrewingList() {
        this(List.of());
    }

    public BrewingList(List<ResourceLocation> recipes) {
        recipeIds = new ArrayList<>(recipes);
    }

    public int size() {
        return recipeIds.size();
    }

    public boolean isEmpty() {
        return recipeIds.isEmpty();
    }

    public void shuffle() {
        if (isEmpty()) return;
        Collections.shuffle(recipeIds);
    }

    public @Nullable ResourceLocation pop() {
        if (isEmpty()) return null;
        ResourceLocation recipeId = recipeIds.getFirst();
        select(recipeId);
        return recipeId;
    }

    public @Nullable ResourceLocation get() {
        if (isEmpty()) return null;
        return recipeIds.getFirst();
    }

    public boolean select(ResourceLocation recipeId) {
        if (isEmpty()) return false;
        if (!recipeIds.remove(recipeId)) return false;
        recipeIds.addFirst(recipeId);
        return true;
    }

    public boolean add(ResourceLocation recipeId) {
        if (recipeIds.contains(recipeId)) return false;
        recipeIds.add(recipeId);
        return true;
    }

    public boolean remove(ResourceLocation recipeId) {
        return recipeIds.remove(recipeId);
    }

    public List<ResourceLocation> getRecipes() {
        return new ArrayList<>(recipeIds);
    }
}
