package mods.betterwithpatches.block.tile;

import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.block.Campfire;
import mods.betterwithpatches.craft.CampFireCraftingManager;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.Random;

import static codechicken.nei.NEIClientConfig.world;
import static codechicken.nei.NEIClientUtils.isRaining;



public class TileEntityCampfire extends TileEntity {

    private ItemStack spitStack = null;
    private ItemStack cookStack = null;
    private static final int CookTime = (400);
    private static final int BurnTimeMultiply = (8);
    private static final int BaseBurnTimeMultiply = (2);
    private static final int BurnTimeMax = (5 * 20 * 60);
    private static final int BurnTimeInitial = (50 * 4 * BurnTimeMultiply * BaseBurnTimeMultiply);
    private static final int WarmupTime = (10 * 20);
    private static final int RevertToSmallTime = (20 * 20);
    private static final int bigTime = (BurnTimeInitial * 3 / 2);
    private static final int SmoulderTime = (5 * 20 * 60);
    private static final int BurnStuff = (CookTime / 2);
    private static final float FireSpreadChance = 0.05F;
    private static final float RainKill = 0.01F;


    private int burnTimeCountdown = 0;
    private int burnTimeSinceLit = 0;
    private int cookCounter = 0;
    private int smoulderCounter = 0;
    private int cookBurningCounter = 0;

    public TileEntityCampfire() {
        super();
    }
}
