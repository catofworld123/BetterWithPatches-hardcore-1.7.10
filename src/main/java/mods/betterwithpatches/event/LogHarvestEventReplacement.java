package mods.betterwithpatches.event;

import betterwithmods.BWMod;
import betterwithmods.BWRegistry;
import betterwithmods.event.LogHarvestEvent;
import betterwithmods.event.TConHelper;
import betterwithmods.items.ItemBark;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mods.betterwithpatches.Config;
import mods.betterwithpatches.craft.HardcoreWoodInteractionExtensions;
import mods.betterwithpatches.util.BWPUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.world.BlockEvent;

import static mods.betterwithpatches.BWPRegistry.*;
import static mods.betterwithpatches.util.BWPUtils.presentInOD;
import static net.minecraft.init.Items.stick;

public class LogHarvestEventReplacement extends LogHarvestEvent {

    /**
     * This is a very fat event. Caution is advised.
     */
    @Override
    @SubscribeEvent
    public void harvestLog(BlockEvent.HarvestDropsEvent evt) {
        Block block = evt.block;
        int meta = evt.blockMetadata;
        ItemStack log = new ItemStack(block, 1, meta);
        if (evt.harvester != null && presentInOD(log, "logWood")) {
            int harvestMeta = block.damageDropped(meta);
            boolean harvest = true, fort = evt.fortuneLevel > 0, force = false, saw = false;
            if (evt.harvester.getCurrentEquippedItem() != null) {
                ItemStack item = evt.harvester.getCurrentEquippedItem();
                if (item.getItem() instanceof ItemTool) {
                    ItemTool tool = (ItemTool) item.getItem();
                    if (tool.getHarvestLevel(item, "axe") >= 0) {
                        saw = item.hasTagCompound() && item.stackTagCompound.hasKey("BWMHarvest");
                        if (!saw) {
                            harvest = false;
                        }
                    }
                } else if (Loader.isModLoaded("TConstruct") && TConHelper.isEquippedItemCorrectTool(evt.harvester, "axe", true) && TConHelper.isEquippedItemCorrectLevel(evt.harvester, "axe", block.getHarvestLevel(meta))) {
                    harvest = false;
                } else if (presentInOD(item, "craftingToolAxe")) {
                    harvest = false;
                } else if (presentInOD(item, "craftingToolKnife")) {
                    fort = force = true;
                } else if (presentInOD(item, "craftingToolSaw")) {
                    saw = true;
                }
            }

            if (harvest && !evt.isSilkTouching) {
                int fortune = force ? 2 : evt.fortuneLevel;
                for (ItemStack logStack : evt.drops) {
                    if (presentInOD(logStack, "logWood")) {
                        craft.setInventorySlotContents(0, new ItemStack(block, 1, harvestMeta));
                        IRecipe recipe = findMatchingRecipe(craft, evt.world);
                        if (recipe != null && recipe.getCraftingResult(craft) != null) {
                            ItemStack sticks = new ItemStack(BWRegistry.bark);
                                Block drop = BWPUtils.getBlock(BWRegistry.bark);

                                evt.drops.add(sticks);
                                evt.drops.remove(logStack);
                            evt.world.setBlock(evt.x, evt.y, evt.z, blocktreestage1);
                                break;
                        }
                    }
                }
            }
        }
    }
}
