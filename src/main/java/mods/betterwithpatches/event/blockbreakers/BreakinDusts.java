package mods.betterwithpatches.event.blockbreakers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.world.BlockEvent;
import static mods.betterwithpatches.BWPRegistry.*;

public class BreakinDusts {
    @SubscribeEvent
    public void BadBreak001(BlockEvent.HarvestDropsEvent event) {
        boolean removeflag = false;
        ItemStack toremove = null;


            for (ItemStack dirtblock : event.drops) {

                if (event.block == Blocks.dirt || event.block == Blocks.grass) {

                if (event.harvester.getCurrentEquippedItem() != null)  {
                    ItemStack item = event.harvester.getCurrentEquippedItem();
                    if (item.getItem() instanceof ItemTool) {
                        ItemTool tool = (ItemTool) item.getItem();
                        if (tool.getHarvestLevel(item, "shovel") > 2) {

                        }
                        else {
                            removeflag = true;
                             toremove = dirtblock;
                        }

                    }
                    else {
                        removeflag = true;
                         toremove = dirtblock;
                    }
                }
                else {
                    removeflag = true;
                     toremove = dirtblock;
                }
            }
        }
           if (removeflag == true){
               if (toremove != null) {
                   event.drops.remove(toremove);
               }
               for (int iTempCount = 0; iTempCount < 6; iTempCount++) {
                   if (event.world.rand.nextFloat() <= 0.75) {
                       ItemStack stack = new ItemStack(pileOfDirt, 1, 0);

                       event.drops.add(stack);
                   }
               }

           }
    }
}
