package mods.betterwithpatches.block.tile;

import mods.betterwithpatches.block.Campfire;
import mods.betterwithpatches.craft.CampFireCraftingManager;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;


public class TileEntityCampfire extends TileEntity {

    private ItemStack spitStack = null;
    private ItemStack cookStack = null;

    private int getCurrentFireLevel()
    {
        Campfire block = (Campfire)worldObj.getBlock( xCoord, yCoord, zCoord );
        return block.fireLevel;
    }
    public boolean getIsFoodBurning()
    {
        if (cookStack != null && getCurrentFireLevel() >= 3 )
        {
            return true;
        }

        return false;
    }


    public boolean getIsCooking()
    {
        if (cookStack != null && getCurrentFireLevel() >= 2 )
        {
            ItemStack cookResult = CampFireCraftingManager.instance.getRecipeResult(cookStack.getItem());

            if ( cookResult != null )
            {
                return true;
            }
        }

        return false;
    }

}
