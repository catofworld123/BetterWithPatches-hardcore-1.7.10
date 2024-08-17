package mods.betterwithpatches.block.tile;

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

    private int getCurrentFireLevel() {
        Campfire block = (Campfire) worldObj.getBlock(xCoord, yCoord, zCoord);
        return worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
    }

    public boolean getIsFoodBurning() {
        if (cookStack != null && getCurrentFireLevel() >= 4 && getCurrentFireLevel() < 5 || cookStack != null && getCurrentFireLevel() >= 9 && getCurrentFireLevel() < 10) {
            return true;
        }

        return false;
    }


    public boolean getIsCooking() {
        if (cookStack != null && getCurrentFireLevel() >= 2) {
            ItemStack cookResult = CampFireCraftingManager.instance.getRecipeResult(cookStack.getItem());

            if (cookResult != null) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        NBTTagCompound spitTag = tag.getCompoundTag("fcSpitStack");

        if (spitTag != null) {
            spitStack = ItemStack.loadItemStackFromNBT(spitTag);
        }

        NBTTagCompound cookTag = tag.getCompoundTag("fcCookStack");

        if (cookTag != null) {
            cookStack = ItemStack.loadItemStackFromNBT(cookTag);
        }

        if (tag.hasKey("fcBurnCounter")) {
            burnTimeCountdown = tag.getInteger("fcBurnCounter");
        }

        if (tag.hasKey("fcBurnTime")) {
            burnTimeSinceLit = tag.getInteger("fcBurnTime");
        }

        if (tag.hasKey("fcCookCounter")) {
            cookCounter = tag.getInteger("fcCookCounter");
        }

        if (tag.hasKey("fcSmoulderCounter")) {
            smoulderCounter = tag.getInteger("fcSmoulderCounter");
        }

        if (tag.hasKey("fcCookBurning")) {
            cookBurningCounter = tag.getInteger("fcCookBurning");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        if (spitStack != null) {
            NBTTagCompound spitTag = new NBTTagCompound();

            spitStack.writeToNBT(spitTag);

            tag.setTag("fcSpitStack", spitTag);
        }

        if (cookStack != null) {
            NBTTagCompound cookTag = new NBTTagCompound();

            cookStack.writeToNBT(cookTag);

            tag.setTag("fcCookStack", cookTag);
        }

        tag.setInteger("fcBurnCounter", burnTimeCountdown);
        tag.setInteger("fcBurnTime", burnTimeSinceLit);
        tag.setInteger("fcCookCounter", cookCounter);
        tag.setInteger("fcSmoulderCounter", smoulderCounter);
        tag.setInteger("fcCookBurning", cookBurningCounter);
    }
/// TEST ZONE


    private void extinguishFire(boolean bSmoulder)
    {
        if ( bSmoulder )
        {
            smoulderCounter = SmoulderTime;
        }
        else
        {
            smoulderCounter = 0;
        }

        cookCounter = 0; // reset cook counter in case fire is relit later
        cookBurningCounter = 0;

        Campfire block = (Campfire)(worldObj.getBlock( xCoord, yCoord, zCoord ));

        block.extinguishFire(worldObj, xCoord, yCoord, zCoord, bSmoulder);
    }



    @Override
public void updateEntity()
{
    super.updateEntity();

    if ( !worldObj.isRemote )
    {
        int iCurrentFireLevel = getCurrentFireLevel();

        if ( iCurrentFireLevel > 0 )
        {
          //  if ( iCurrentFireLevel > 1 && worldObj.rand.nextFloat() <= FireSpreadChance)
          //  {
          //      checkForFireSpreadFromLocation(worldObj, xCoord, yCoord, zCoord, worldObj.rand, 0);
          //  }

            burnTimeSinceLit++;

            if (burnTimeCountdown > 0 )
            {
                burnTimeCountdown--;

                if ( iCurrentFireLevel == 3 )
                {
                    // blaze burns extra fast

                    burnTimeCountdown--;
                }
            }

            iCurrentFireLevel = validateFireLevel();

            if ( iCurrentFireLevel > 0 )
            {
               // updateCookState();

                if (worldObj.rand.nextFloat() <= RainKill  ) //add && isRainingOnCampfire()
                {
                    extinguishFire(true);
                }
            }
        }
        else if (smoulderCounter > 0 )
        {
            smoulderCounter--;

            if (smoulderCounter == 0 || worldObj.rand.nextFloat() <= RainKill) //add && isRainingOnCampfire()
            {
               if (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) == 2) {
                   worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 2);
               }
                if (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) == 10) {
                    worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 11, 2);
                }
               }
            }
        }
    }

    public void addBurnTime(int iBurnTime)
    {
        burnTimeCountdown += iBurnTime * BurnTimeMultiply * BaseBurnTimeMultiply;

        if (burnTimeCountdown > BurnTimeMax)
        {
            burnTimeCountdown = BurnTimeMax;
        }

        validateFireLevel();
    }

    public void onFirstLit()
    {
        burnTimeCountdown = BurnTimeInitial;
        burnTimeSinceLit = 0;
    }

    public int validateFireLevel()
    {
        int iCurrentFireLevel = getCurrentFireLevel();

        if ( iCurrentFireLevel > 0 )
        {
            //int iFuelState = FCBetterThanWolves.fcBlockCampfireUnlit.GetFuelState( worldObj, xCoord, yCoord, zCoord );

            if (burnTimeCountdown <= 0 )
            {
                extinguishFire(true);

                return 0;
            }
            else
            {
                int iDesiredFireLevel = 2;

                if (burnTimeSinceLit < WarmupTime || burnTimeCountdown < RevertToSmallTime)
                {
                    iDesiredFireLevel = 1;
                }
                else if (burnTimeCountdown > bigTime)
                {
                    iDesiredFireLevel = 3;
                }

                if ( iDesiredFireLevel != iCurrentFireLevel )
                {
                    changeFireLevel(iDesiredFireLevel);

                    if ( iDesiredFireLevel == 1 && iCurrentFireLevel == 2 )
                    {
                        worldObj.playAuxSFX(11, xCoord, yCoord, zCoord, 1 );
                    }

                    return iDesiredFireLevel;
                }
            }

        }
        else // iCurrenFireLevel == 0
        {
            if (burnTimeCountdown > 0 && worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 1 || burnTimeCountdown > 0 && worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 10)
            {
                relightSmouldering();

                return 1;
            }
        }

        return iCurrentFireLevel;
    }


    private void changeFireLevel(int iNewLevel)
    {
        Campfire block = (Campfire)(worldObj.getBlock( xCoord, yCoord, zCoord ));

        block.changeFireLevel(worldObj, xCoord, yCoord, zCoord, iNewLevel, worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
    }

    private void relightSmouldering()
    {
        burnTimeSinceLit = 0;

        Campfire block = (Campfire)(worldObj.getBlock( xCoord, yCoord, zCoord ));

        block.relightFire(worldObj, xCoord, yCoord, zCoord);
    }





}

