package mods.betterwithpatches.event.chonkycampfire;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mods.betterwithpatches.block.Campfire;
import mods.betterwithpatches.block.tile.TileEntityCampfire;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import static betterwithmods.BWRegistry.material;
import static mods.betterwithpatches.BWPRegistry.*;

import mods.betterwithpatches.block.Campfire.*;


public class campfirelightupEvent {

    @SubscribeEvent
    public void rightClickBlock(PlayerInteractEvent event){


        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK ) {
            TileEntity tile = event.world.getTileEntity(event.x, event.y, event.z);
            ItemStack item = event.entityPlayer.getCurrentEquippedItem();
                if (event.world.getBlockMetadata(event.x, event.y, event.z) == 0){
                    if (!event.entityPlayer.isSneaking()){
                        if(tile instanceof TileEntityCampfire) {
                         if (item != null) {
                            if (item.getItem() instanceof ItemFlintAndSteel) {
                                item.damageItem(2, event.entityPlayer);
                            double a = Math.random()*4;
                                 if (a >= 3.0) {
                                 event.world.setBlockMetadataWithNotify(event.x, event.y, event.z, 2, 2);
                                 event.world.playSoundEffect( event.x + 0.5D, event.y + 0.5D, event.z + 0.5D,
                                         "mob.ghast.fireball", 1F, event.world.rand.nextFloat() * 0.4F + 0.8F );


                        }

            }

        }
    }}}
}}}
