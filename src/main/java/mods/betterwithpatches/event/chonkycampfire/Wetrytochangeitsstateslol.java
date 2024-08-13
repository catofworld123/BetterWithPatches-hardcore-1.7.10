package mods.betterwithpatches.event.chonkycampfire;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mods.betterwithpatches.block.tile.TileEntityCampfire;
import mods.betterwithpatches.item.ItemShaft;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class Wetrytochangeitsstateslol {
    @SubscribeEvent
    public void rightClickBlockThingy(PlayerInteractEvent event){


        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK ) {
            TileEntity tile = event.world.getTileEntity(event.x, event.y, event.z);
            ItemStack item = event.entityPlayer.getCurrentEquippedItem();
            if (event.world.getBlockMetadata(event.x, event.y, event.z) == 0){
                if (!event.entityPlayer.isSneaking()){
                    if(tile instanceof TileEntityCampfire) {
                        if (item != null) {
                            if (item.getItem() instanceof ItemShaft) {

                                    event.world.setBlockMetadataWithNotify(event.x, event.y, event.z, 2, 2);


                            }

                        }
                    }}}
        }}
}
