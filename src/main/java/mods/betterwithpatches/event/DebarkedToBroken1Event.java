package mods.betterwithpatches.event;


import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mods.betterwithpatches.block.BlockTreeStage1;
import mods.betterwithpatches.util.BWPUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.world.BlockEvent;

import static mods.betterwithpatches.BWPRegistry.*;
import static mods.betterwithpatches.util.BWPUtils.presentInOD;

public class DebarkedToBroken1Event {
    @SubscribeEvent
    public void eventIce(BlockEvent.HarvestDropsEvent event){
        Block block = event.block;
        int meta = event.blockMetadata;
        ItemStack log = new ItemStack(block, 1, meta);

        for (ItemStack logStack : event.drops) {
            if(event.block == blocktreestage1){
                event.drops.add(new ItemStack(itemShaft));
                event.drops.remove(logStack);
                event.world.setBlock(event.x, event.y, event.z, blocktreestage2);

            }
}}}