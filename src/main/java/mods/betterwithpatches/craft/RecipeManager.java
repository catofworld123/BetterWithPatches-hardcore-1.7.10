package mods.betterwithpatches.craft;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipeManager {
    public static void addAllModRecipes() {
        SmeltingRecipeList.addRecipes();
    }
    
    public static void addCampfireRecipe(Item item, ItemStack outputStack) {
        CampFireCraftingManager.instance.addRecipe(item, outputStack);
    }
    
}

