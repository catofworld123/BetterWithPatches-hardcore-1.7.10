package mods.betterwithpatches.block.tile;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.block.CampfireBlock;
import mods.betterwithpatches.craft.CampFireCraftingManager;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;

import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;


public class CampfireTileEntity extends TileEntity
{


    private static final int CAMPFIRE_BURN_TIME_MULTIPLIER = 8;

    private static final int TIME_TO_COOK = (400 * CAMPFIRE_BURN_TIME_MULTIPLIER * 3 / 2 ); // this line represents efficiency relative to furnace cooking

    private static final int MAX_BURN_TIME = (5 * 1200);

    private static final int INITIAL_BURN_TIME = (50 * 4 * CAMPFIRE_BURN_TIME_MULTIPLIER * 2); // 50 is the furnace burn time of a shaft

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


    private static final String INV_SPIT_TAG = "InventorySpit";
    private static final String INV_COOK_TAG = "InventoryCook";

    /**
     * Данный метод вызывается при записи данных Tile Entity в чанк. Мы не рекомендуем удалять вызов родительского метода,
     * так как это может привести к ошибке загрузки данных Tile Entity.
     *
     * @param nbt данные NBT в которых будет храниться информация о Tile Entity.
     */
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        writeExtendedData(nbt);
        writeExtendedData2(nbt);


        nbt.setInteger("fcBurnCounter", burnTimeCountdown);
        nbt.setInteger("fcBurnTime", burnTimeSinceLit);
        nbt.setInteger("fcCookCounter", cookCounter);
        nbt.setInteger("fcSmoulderCounter", smoulderCounter);
        nbt.setInteger("fcCookBurning", cookBurningCounter);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        readExtendedData2(nbt);

        readExtendedData(nbt);

        if ( nbt.hasKey( "fcBurnCounter" ) )
        {
            burnTimeCountdown = nbt.getInteger("fcBurnCounter");
        }

        if ( nbt.hasKey( "fcBurnTime" ) )
        {
            burnTimeSinceLit = nbt.getInteger("fcBurnTime");
        }

        if ( nbt.hasKey( "fcCookCounter" ) )
        {
            cookCounter = nbt.getInteger("fcCookCounter");
        }

        if ( nbt.hasKey( "fcSmoulderCounter" ) )
        {
            smoulderCounter = nbt.getInteger("fcSmoulderCounter");
        }

        if ( nbt.hasKey( "fcCookBurning" ) )
        {
            cookBurningCounter = nbt.getInteger("fcCookBurning");
        }



    }
    private void writeExtendedData(NBTTagCompound nbt) {
        if (cookStack != null) {
            NBTTagCompound inventoryTag = new NBTTagCompound();
            cookStack.writeToNBT(inventoryTag);
            nbt.setTag(INV_COOK_TAG, inventoryTag);
        }
    }
    private void writeExtendedData2(NBTTagCompound nbt) {
        if (spitStack != null) {
            NBTTagCompound inventoryTag = new NBTTagCompound();
            spitStack.writeToNBT(inventoryTag);
            nbt.setTag(INV_SPIT_TAG, inventoryTag);
        }
    }
    private void readExtendedData(NBTTagCompound nbt) {
        if (nbt.hasKey(INV_COOK_TAG, Constants.NBT.TAG_COMPOUND)) {
            NBTTagCompound inventoryTag = nbt.getCompoundTag(INV_COOK_TAG);
            cookStack = ItemStack.loadItemStackFromNBT(inventoryTag);
        }
    }
    private void readExtendedData2(NBTTagCompound nbt) {
        if (nbt.hasKey(INV_SPIT_TAG, Constants.NBT.TAG_COMPOUND)) {
            NBTTagCompound inventoryTag = nbt.getCompoundTag(INV_SPIT_TAG);
            spitStack = ItemStack.loadItemStackFromNBT(inventoryTag);
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        writeExtendedData(nbt);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbt);
    }
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        TileEntity tile = worldObj.getTileEntity(packet.func_148856_c(), packet.func_148855_d(), packet.func_148854_e());
        if (tile instanceof CampfireTileEntity) {
            ((CampfireTileEntity) tile).readExtendedData(packet.func_148857_g());
        }
    }

@Override
public boolean shouldRefresh(Block oldBlock, Block newBlock, int oldMeta, int newMeta, World world, int x, int y, int z)
{
    if (oldBlock != newBlock) {
        System.out.println("refreshin'");
        return true;
    }
    else return false;
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
              // if ( iCurrentFireLevel > 1 && worldObj.rand.nextFloat() <= CHANCE_OF_FIRE_SPREAD)
              //  {
              //      BlockCustomFire.checkForFireSpreadFromLocation(worldObj, xCoord, yCoord, zCoord, worldObj.rand, 0);
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

            if (stack != null) {
                cookStack = stack.copy();

                cookStack.stackSize = 1;


            } else {
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
            if (!worldObj.isRemote) {
                worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord, zCoord, new ItemStack(spitStack.getItem())));

            }
            spitStack = null;
        }

        if (cookStack != null )
        {
            if (!worldObj.isRemote) {
                worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord, zCoord, new ItemStack(cookStack.getItem())));
            }
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
        System.out.print(burnTimeCountdown);
        int iCurrentFireLevel = getCurrentFireLevel();

        if ( iCurrentFireLevel > 0 )
        {
           // int iFuelState = FCBetterThanWolves.fcBlockCampfireUnlit.GetFuelState( worldObj, xCoord, yCoord, zCoord );

            if (burnTimeCountdown <= 0 )
            {
                extinguishFire(true);
            System.out.println("extinguished fire");

                return 0;
            }
            else
            {

                int iDesiredFireLevel = 2;

                if (burnTimeSinceLit < WARMUP_TIME || burnTimeCountdown < REVERT_TO_SMALL_TIME)
                {
                    System.out.println("changing to 1");
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
                    BWPRegistry.unlitCampfire.getFuelState(worldObj, xCoord, yCoord, zCoord) == CampfireBlock.CAMPFIRE_FUEL_STATE_SMOULDERING)
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
