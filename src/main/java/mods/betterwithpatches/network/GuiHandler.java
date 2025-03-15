package mods.betterwithpatches.network;
//Deprecated Method//
import cpw.mods.fml.common.network.IGuiHandler;
import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.BetterWithPatches;
import mods.betterwithpatches.block.tile.TileEntitySteelAnvil;
import mods.betterwithpatches.client.GUI.InfernalEnchanterGui;
import mods.betterwithpatches.client.menu.GuiSteelAnvil;
import mods.betterwithpatches.inventory.container.InfernalEnchanterContainer;
import mods.betterwithpatches.menu.MenuSteelAnvil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity entity = world.getTileEntity(x, y, z);

        if(entity != null) {

            if(ID == BWPRegistry.ENUM_IDS.InfernalEnchanter.ordinal()) {
                return new InfernalEnchanterContainer(player.inventory, world, x, y, z);
            }
            if (ID == 0) {
                TileEntity tile = world.getTileEntity(x, y, z);
                if (tile instanceof TileEntitySteelAnvil)
                    return new MenuSteelAnvil(player.inventory, (TileEntitySteelAnvil) world.getTileEntity(x, y, z));

            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity entity = world.getTileEntity(x, y, z);

        if(entity != null)
        {

            if(ID == BWPRegistry.ENUM_IDS.InfernalEnchanter.ordinal()) {
                return new InfernalEnchanterGui(player.inventory, world, x, y, z);
            }
            if (ID == 0) {
                TileEntity tile = world.getTileEntity(x, y, z);
                if (tile instanceof TileEntitySteelAnvil)
                    return new GuiSteelAnvil(player.inventory, (TileEntitySteelAnvil) tile);
            }

        }
        return null;
    }

}
