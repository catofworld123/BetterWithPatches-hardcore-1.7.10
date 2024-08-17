package mods.betterwithpatches.block.tile;

import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.block.CampfireBlock;
import mods.betterwithpatches.craft.CampFireCraftingManager;
import mods.betterwithpatches.util.Packet132TileEntityData;
import mods.betterwithpatches.util.TileEntityDataPacketHandler;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;

public class CampfireTileEntity extends TileEntity implements TileEntityDataPacketHandler
{
    private static final int CAMPFIRE_BURN_TIME_MULTIPLIER = 8;

    private static final int TIME_TO_COOK = (400 * CAMPFIRE_BURN_TIME_MULTIPLIER *
            3 / 2 ); // this line represents efficiency relative to furnace cooking

    private static final int MAX_BURN_TIME = (5 * 1200);

    private static final int INITIAL_BURN_TIME = (50 * 4 * CAMPFIRE_BURN_TIME_MULTIPLIER *
            2); // 50 is the furnace burn time of a shaft

    private static final int WARMUP_TIME = (10 * 20);
    private static final int REVERT_TO_SMALL_TIME = (20 * 20);
    private static final int BLAZE_TIME = (INITIAL_BURN_TIME * 3 / 2 );

    private static final int SMOULDER_TIME = (5 * 1200); // used to be 2 minutes

    private static final int TIME_TO_BURN_FOOD = (TIME_TO_COOK / 2 );

    private static final float CHANCE_OF_FIRE_SPREAD = 0.05F;
    private static final float CHANCE_OF_GOING_OUT_FROM_RAIN = 0.01F;

    private ItemStack spitStack = null;
    private ItemStack cookStack = null;

    private int burnTimeCountdown = 0;
    private int burnTimeSinceLit = 0;
    private int cookCounter = 0;
    private int smoulderCounter = 0;
    private int cookBurningCounter = 0;

    public CampfireTileEntity()
    {
        super();
    }

    @Override
    public void readFromNBT( NBTTagCompound tag )
    {
        super.readFromNBT( tag );

        NBTTagCompound spitTag = tag.getCompoundTag( "fcSpitStack" );

        if (spitStack.hasTagCompound() )
        {
            spitStack = ItemStack.loadItemStackFromNBT(spitTag);
        }

        NBTTagCompound cookTag = tag.getCompoundTag( "fcCookStack" );

        if (cookStack.hasTagCompound() )
        {
            cookStack = ItemStack.loadItemStackFromNBT(cookTag);
        }

        if ( tag.hasKey( "fcBurnCounter" ) )
        {
            burnTimeCountdown = tag.getInteger("fcBurnCounter");
        }

        if ( tag.hasKey( "fcBurnTime" ) )
        {
            burnTimeSinceLit = tag.getInteger("fcBurnTime");
        }

        if ( tag.hasKey( "fcCookCounter" ) )
        {
            cookCounter = tag.getInteger("fcCookCounter");
        }

        if ( tag.hasKey( "fcSmoulderCounter" ) )
        {
            smoulderCounter = tag.getInteger("fcSmoulderCounter");
        }

        if ( tag.hasKey( "fcCookBurning" ) )
        {
            cookBurningCounter = tag.getInteger("fcCookBurning");
        }
    }

    @Override
    public void writeToNBT( NBTTagCompound tag)
    {
        super.writeToNBT( tag );

        if (spitStack != null)
        {
            NBTTagCompound spitTag = new NBTTagCompound();

            spitStack.writeToNBT(spitTag);

            tag.setTag( "fcSpitStack", spitTag );
        }

        if (cookStack != null)
        {
            NBTTagCompound cookTag = new NBTTagCompound();

            cookStack.writeToNBT(cookTag);

            tag.setTag( "fcCookStack", cookTag );
        }

        tag.setInteger("fcBurnCounter", burnTimeCountdown);
        tag.setInteger("fcBurnTime", burnTimeSinceLit);
        tag.setInteger("fcCookCounter", cookCounter);
        tag.setInteger("fcSmoulderCounter", smoulderCounter);
        tag.setInteger("fcCookBurning", cookBurningCounter);
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
              //  if ( iCurrentFireLevel > 1 && worldObj.rand.nextFloat() <= CHANCE_OF_FIRE_SPREAD)
              //  {
              //      FireBlock.checkForFireSpreadFromLocation(worldObj, xCoord, yCoord, zCoord, worldObj.rand, 0);
               // }

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
                    updateCookState();

                    if (worldObj.rand.nextFloat() <= CHANCE_OF_GOING_OUT_FROM_RAIN && isRainingOnCampfire() )
                    {
                        extinguishFire(false);
                    }
                }
            }
            else if (smoulderCounter > 0 )
            {
                smoulderCounter--;

                if (smoulderCounter == 0 || worldObj.rand.nextFloat() <= CHANCE_OF_GOING_OUT_FROM_RAIN && isRainingOnCampfire() )
                {
                    stopSmouldering();
                }
            }
        }
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();

        if (cookStack != null)
        {
            NBTTagCompound cookTag = new NBTTagCompound();

            cookStack.writeToNBT(cookTag);

            tag.setTag( "tag01", cookTag );
        }

        if (spitStack != null)
        {
            NBTTagCompound spitTag = new NBTTagCompound();

            spitStack.writeToNBT(spitTag);

            tag.setTag( "tag02", spitTag );
        }

        return new Packet132TileEntityData( xCoord, yCoord, zCoord, 1, tag );
    }

    //------------- FCITileEntityDataPacketHandler ------------//

    @Override
    public void readNBTFromPacket( NBTTagCompound tag )
    {
        NBTTagCompound cookTag = tag.getCompoundTag( "tag01" );

        if (cookStack.hasTagCompound() )
        {
            cookStack = ItemStack.loadItemStackFromNBT(cookTag);
        }

        NBTTagCompound spitTag = tag.getCompoundTag( "tag02" );

        if (spitStack.hasTagCompound() )
        {
            spitStack = ItemStack.loadItemStackFromNBT(spitTag);
        }

        // force a visual update for the block since the above variables affect it

        worldObj.markBlockRangeForRenderUpdate( xCoord, yCoord, zCoord, xCoord, yCoord, zCoord );
    }

    //------------- Class Specific Methods ------------//

    public void setSpitStack(ItemStack stack)
    {
        if ( stack != null )
        {
            spitStack = stack.copy();

            spitStack.stackSize = 1;
        }
        else
        {
            spitStack = null;
        }

        worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
    }

    public ItemStack getSpitStack()
    {
        return spitStack;
    }

    public void setCookStack(ItemStack stack)
    {
        if ( stack != null )
        {
            cookStack = stack.copy();

            cookStack.stackSize = 1;
        }
        else
        {
            cookStack = null;

            cookBurningCounter = 0;
        }

        cookCounter = 0;

        worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
    }

    public ItemStack getCookStack()
    {
        return cookStack;
    }

    public void ejectContents()
    {
        if (spitStack != null )
        {
            worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord, zCoord, new ItemStack(spitStack.getItem())));;

            spitStack = null;
        }

        if (cookStack != null )
        {
            worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord, zCoord, new ItemStack(cookStack.getItem())));

            cookStack = null;
        }
    }

    public void addBurnTime(int iBurnTime)
    {
        burnTimeCountdown += iBurnTime * CAMPFIRE_BURN_TIME_MULTIPLIER * 2;

        if (burnTimeCountdown > MAX_BURN_TIME)
        {
            burnTimeCountdown = MAX_BURN_TIME;
        }

        validateFireLevel();
    }

    public void onFirstLit()
    {
        burnTimeCountdown = INITIAL_BURN_TIME;
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

                if (burnTimeSinceLit < WARMUP_TIME || burnTimeCountdown < REVERT_TO_SMALL_TIME)
                {
                    iDesiredFireLevel = 1;
                }
                else if (burnTimeCountdown > BLAZE_TIME)
                {
                    iDesiredFireLevel = 3;
                }

                if ( iDesiredFireLevel != iCurrentFireLevel )
                {
                    changeFireLevel(iDesiredFireLevel);

                    if ( iDesiredFireLevel == 1 && iCurrentFireLevel == 2 )
                    {
                        worldObj.playAuxSFX(10, xCoord, yCoord, zCoord, 1 );
                    }

                    return iDesiredFireLevel;
                }
            }

        }
        else // iCurrenFireLevel == 0
        {
            if (burnTimeCountdown > 0 &&
                    BWPRegistry.unlitCampfire.getFuelState(worldObj, xCoord, yCoord, zCoord) ==
                            CampfireBlock.CAMPFIRE_FUEL_STATE_SMOULDERING)
            {
                relightSmouldering();

                return 1;
            }
        }

        return iCurrentFireLevel;
    }

    private void extinguishFire(boolean bSmoulder)
    {
        if ( bSmoulder )
        {
            smoulderCounter = SMOULDER_TIME;
        }
        else
        {
            smoulderCounter = 0;
        }

        cookCounter = 0; // reset cook counter in case fire is relit later
        cookBurningCounter = 0;

        CampfireBlock block = (CampfireBlock)(worldObj.getBlock( xCoord, yCoord, zCoord ));

        block.extinguishFire(worldObj, xCoord, yCoord, zCoord, bSmoulder);
    }

    private void changeFireLevel(int iNewLevel)
    {
        CampfireBlock block = (CampfireBlock)(worldObj.getBlock( xCoord, yCoord, zCoord ));

        block.changeFireLevel(worldObj, xCoord, yCoord, zCoord, iNewLevel, worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
    }

    private int getCurrentFireLevel()
    {
        CampfireBlock block = (CampfireBlock)(worldObj.getBlock( xCoord, yCoord, zCoord ));

        return block.fireLevel;
    }

    private void updateCookState()
    {
        if (cookStack != null )
        {
            int iFireLevel = getCurrentFireLevel();

            if ( iFireLevel >= 2 )
            {
                ItemStack cookResult = CampFireCraftingManager.instance.getRecipeResult(cookStack.getItem());

                if ( cookResult != null )
                {
                    cookCounter++;

                    if (cookCounter >= TIME_TO_COOK)
                    {
                        setCookStack(cookResult);

                        cookCounter = 0;

                        // don't reset burn counter here, as the food can still burn after cooking
                    }
                }

                if ( iFireLevel >= 3 && cookStack.getItem() != BWPRegistry.itemShaft ) // gonna replace w burned meat
                {
                    cookBurningCounter++;

                    if (cookBurningCounter >= TIME_TO_BURN_FOOD)
                    {
                        setCookStack(new ItemStack(BWPRegistry.itemShaft)); // gonna replace w burned meat

                        cookCounter = 0;
                        cookBurningCounter = 0;
                    }
                }
            }
        }
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

    public boolean getIsFoodBurning()
    {
        if (cookStack != null && getCurrentFireLevel() >= 3 && cookStack.getItem() != BWPRegistry.itemShaft ) // gonna replace w burned meat
        {
            return true;
        }

        return false;
    }

    public boolean isRainingOnCampfire()
    {
        CampfireBlock block = (CampfireBlock)(worldObj.getBlock( xCoord, yCoord, zCoord ));

        return block.isRainingOnCampfire(worldObj, xCoord, yCoord, zCoord);
    }

    private void stopSmouldering()
    {
        smoulderCounter = 0;

        CampfireBlock block = (CampfireBlock)(worldObj.getBlock( xCoord, yCoord, zCoord ));

        block.stopSmouldering(worldObj, xCoord, yCoord, zCoord);
    }

    private void relightSmouldering()
    {
        burnTimeSinceLit = 0;

        CampfireBlock block = (CampfireBlock)(worldObj.getBlock( xCoord, yCoord, zCoord ));

        block.relightFire(worldObj, xCoord, yCoord, zCoord);
    }
}
