package mods.betterwithpatches.craft;

import betterwithmods.BWCrafting;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.block.Block;

public class SmeltingRecipeList extends BWCrafting {
    public static void addRecipes() {
        addCampfireRecipes();
    }



    private static void addCampfireRecipes() {
        RecipeManager.addCampfireRecipe(Items.porkchop, new ItemStack(Items.cooked_porkchop));
        RecipeManager.addCampfireRecipe(Items.beef, new ItemStack(Items.cooked_beef));
        RecipeManager.addCampfireRecipe(Items.chicken, new ItemStack(Items.cooked_chicken));
        RecipeManager.addCampfireRecipe(Items.fish, new ItemStack(Items.cooked_fished));
        RecipeManager.addCampfireRecipe(Items.potato, new ItemStack(Items.baked_potato));
    }
}

