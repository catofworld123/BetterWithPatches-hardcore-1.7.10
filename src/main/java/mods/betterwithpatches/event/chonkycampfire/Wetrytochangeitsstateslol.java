package mods.betterwithpatches.event.chonkycampfire;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.block.tile.TileEntityCampfire;
import mods.betterwithpatches.item.ItemOakBark;
import mods.betterwithpatches.item.ItemShaft;
import mods.betterwithpatches.item.tool.ItemPointyStick;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class Wetrytochangeitsstateslol {
    int f;
    @SubscribeEvent
    public void rightClickBlockThingy(PlayerInteractEvent event){


        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK ) {
            TileEntity tile = event.world.getTileEntity(event.x, event.y, event.z);
            ItemStack item = event.entityPlayer.getCurrentEquippedItem();

            if (event.world.getBlockMetadata(event.x, event.y, event.z) >= 1 ){
                if (!event.entityPlayer.isSneaking()){
                    if(tile instanceof TileEntityCampfire) {
                        if (item != null) {
                            if (item.getItem() instanceof ItemShaft) {
                                    event.world.setBlockMetadataWithNotify(event.x, event.y, event.z, 3, 2);
                            }
                            if (item.getItem() instanceof ItemOakBark) {
                                f = event.world.getBlockMetadata(event.x, event.y, event.z) + 1;
                                event.world.setBlockMetadataWithNotify(event.x, event.y, event.z, f, 2);
                            }


                        }
                    }}}
            if (item != null){

                if (item.getItem() instanceof ItemPointyStick) {
                    if ((event.world.getBlockMetadata(event.x, event.y, event.z) == 0)){
                        item.stackSize = 0;
                        event.world.setBlockMetadataWithNotify(event.x,  event.y,  event.z, 6, 2);
                    }
                    if ((event.world.getBlockMetadata(event.x, event.y, event.z) == 1)){
                        item.stackSize = 0;
                        event.world.setBlockMetadataWithNotify(event.x,  event.y,  event.z, 11, 2);
                    }
                    if ((event.world.getBlockMetadata(event.x, event.y, event.z) >= 2 && event.world.getBlockMetadata(event.x, event.y, event.z) <=5)){
                        item.stackSize = 0;
                        f = event.world.getBlockMetadata(event.x, event.y, event.z) + 5;
                        event.world.setBlockMetadataWithNotify(event.x,  event.y,  event.z, f, 2);
                    }

                }

            }
            if (item == null){
                if ((event.world.getBlockMetadata(event.x, event.y, event.z) >= 6)){
                    if ((event.world.getBlockMetadata(event.x, event.y, event.z) == 6)){
                        event.entityPlayer.inventory.addItemStackToInventory(new ItemStack(BWPRegistry.itemPointyStick, 1));

                        event.world.setBlockMetadataWithNotify(event.x,  event.y,  event.z, 0, 2);
                    }
                    if ((event.world.getBlockMetadata(event.x, event.y, event.z) >= 6 && event.world.getBlockMetadata(event.x, event.y, event.z) <= 10)){
                        event.entityPlayer.inventory.addItemStackToInventory(new ItemStack(BWPRegistry.itemPointyStick, 1));
                        f = event.world.getBlockMetadata(event.x, event.y, event.z) - 5;
                        event.world.setBlockMetadataWithNotify(event.x,  event.y,  event.z, f, 2);
                    }
                    if ((event.world.getBlockMetadata(event.x, event.y, event.z) == 11)){
                        event.entityPlayer.inventory.addItemStackToInventory(new ItemStack(BWPRegistry.itemPointyStick, 1));

                        event.world.setBlockMetadataWithNotify(event.x,  event.y,  event.z, 1, 2);

                    }

                }
            }
        }}
}
