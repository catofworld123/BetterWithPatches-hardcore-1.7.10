package mods.betterwithpatches.craft.NewCrafts;

import cpw.mods.fml.common.registry.GameRegistry;
import mods.betterwithpatches.item.tool.ItemPointyStick;
import net.minecraft.item.ItemStack;

import static mods.betterwithpatches.BWPRegistry.*;


public class CraftingTableStuff {

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

        GameRegistry.addShapelessRecipe(new ItemStack(itemPointyStick), new Object[]{new ItemStack(itemShaft)});

    }
}
