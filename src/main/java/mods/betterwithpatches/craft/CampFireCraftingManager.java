package mods.betterwithpatches.craft;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;


public class CampFireCraftingManager
{
    public static CampFireCraftingManager instance = new CampFireCraftingManager();

    private Map recipeMap = new HashMap();

    private CampFireCraftingManager() {}
    public ItemStack getRecipeResult(Item InputItemID)
    {
        return (ItemStack) recipeMap.get(InputItemID);
    }

    public void addRecipe(int iInputItemID, ItemStack outputStack)
    {
        recipeMap.put(iInputItemID, outputStack);
    }
}


