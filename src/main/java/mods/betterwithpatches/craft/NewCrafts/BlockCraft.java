package mods.betterwithpatches.craft.NewCrafts;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

import static mods.betterwithpatches.BWPRegistry.campfire;
import static mods.betterwithpatches.BWPRegistry.itemShaft;


public class BlockCraft {

    public static void mainRegistry(){
        addCraftingRec();


    }

    private static void addCraftingRec(){

        GameRegistry.addRecipe(new ItemStack(campfire), new Object[]{"SS ",
                                                                     "SS ",
                                                                     "   ", 'S', itemShaft});

        GameRegistry.addRecipe(new ItemStack(campfire), new Object[]{"   ",
                                                                     " SS",
                                                                      " SS", 'S', itemShaft});

    }
}
