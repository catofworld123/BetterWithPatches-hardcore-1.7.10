package mods.betterwithpatches.block.tile;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
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

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

import static mods.betterwithpatches.block.Campfire.CAMPFIRE_FUEL_STATE_NORMAL;
import static mods.betterwithpatches.block.Campfire.getFuelState;
import static net.minecraftforge.common.util.ForgeDirection.*;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;


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
    private byte facing;

    public CampfireTileEntity()
    {
        super();
    }


    private static final String INV_SPIT_TAG = "InventorySpit";
    private static final String INV_COOK_TAG = "InventoryCook";
    private static final String ROT_TAG = "facing";









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
        writeExtendedData3(nbt);


        nbt.setInteger("fcBurnCounter", burnTimeCountdown);
        nbt.setInteger("fcBurnTime", burnTimeSinceLit);
        nbt.setInteger("fcCookCounter", cookCounter);
        nbt.setInteger("fcSmoulderCounter", smoulderCounter);
        nbt.setInteger("fcCookBurning", cookBurningCounter);
        nbt.setByte("facing", facing);

    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        readExtendedData2(nbt);

        readExtendedData(nbt);

        readExtendedData3(nbt);

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


        if (nbt.hasKey("facing")) {
            facing = nbt.getByte("facing");
            setFacing(facing);
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
    private void writeExtendedData3(NBTTagCompound nbt) {
       NBTTagCompound inventoryTag = new NBTTagCompound();
       inventoryTag.setByte("facing", facing);
       nbt.setTag("facing", inventoryTag);
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
    private void readExtendedData3(NBTTagCompound nbt) {
        if (nbt.hasKey("facing", Constants.NBT.TAG_COMPOUND)) {
            NBTTagCompound inventoryTag = nbt.getCompoundTag("facing");
            facing = inventoryTag.getByte("facing");

        }
    }


    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        writeExtendedData(nbt);
        writeExtendedData2(nbt);
        writeExtendedData3(nbt);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbt);
    }
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        TileEntity tile = worldObj.getTileEntity(packet.func_148856_c(), packet.func_148855_d(), packet.func_148854_e());
        if (tile instanceof CampfireTileEntity) {
            ((CampfireTileEntity) tile).readExtendedData(packet.func_148857_g());
            ((CampfireTileEntity) tile).readExtendedData2(packet.func_148857_g());
            ((CampfireTileEntity) tile).readExtendedData3(packet.func_148857_g());
        }
    }

@Override
public boolean shouldRefresh(Block oldBlock, Block newBlock, int oldMeta, int newMeta, World world, int x, int y, int z)
{
    if (oldBlock != newBlock) {
        return true;
    }
    if (oldMeta != newMeta) {
        return true;
    }
    else return false;
}



    private int getChanceOfNeighborsEncouragingFire(World p_149845_1_, int p_149845_2_, int p_149845_3_, int p_149845_4_)
    {
        byte b0 = 0;

        if (!p_149845_1_.isAirBlock(p_149845_2_, p_149845_3_, p_149845_4_))
        {
            return 0;
        }
        else
        {
            int l = b0;
            l = this.getChanceToEncourageFire(p_149845_1_, p_149845_2_ + 1, p_149845_3_, p_149845_4_, l, WEST );
            l = this.getChanceToEncourageFire(p_149845_1_, p_149845_2_ - 1, p_149845_3_, p_149845_4_, l, EAST );
            l = this.getChanceToEncourageFire(p_149845_1_, p_149845_2_, p_149845_3_ - 1, p_149845_4_, l, UP   );
            l = this.getChanceToEncourageFire(p_149845_1_, p_149845_2_, p_149845_3_ + 1, p_149845_4_, l, DOWN );
            l = this.getChanceToEncourageFire(p_149845_1_, p_149845_2_, p_149845_3_, p_149845_4_ - 1, l, SOUTH);
            l = this.getChanceToEncourageFire(p_149845_1_, p_149845_2_, p_149845_3_, p_149845_4_ + 1, l, NORTH);
            return l;
        }
    }

    public int getChanceToEncourageFire(IBlockAccess world, int x, int y, int z, int oldChance, ForgeDirection face)
    {
        int newChance = world.getBlock(x, y, z).getFireSpreadSpeed(world, x, y, z, face);
        return (newChance > oldChance ? newChance : oldChance);
    }

@Override
    public void updateEntity()
    {
        Random random = new Random();
        super.updateEntity();


        if ( !worldObj.isRemote )
        {
            int iCurrentFireLevel = getCurrentFireLevel();
            if (  getFuelState(worldObj, xCoord, yCoord, zCoord) == CAMPFIRE_FUEL_STATE_NORMAL  && worldObj.rand.nextFloat() <= CHANCE_OF_FIRE_SPREAD)
                {

                    for(int k =0; k < 2; k++)
                    {
                        for(int j =0; j < 3; j++)
                        {
                            for(int i =0; i < 3; i++)
                            {


                                Block block = worldObj.getBlock(xCoord + j, yCoord + k, zCoord + i);
                                if (block == Blocks.fire)
                                {
                                    CampfireBlock campfireBlock = (CampfireBlock) worldObj.getBlock(xCoord, yCoord, zCoord);
                                   campfireBlock.setOnFireDirectly(worldObj, xCoord, yCoord, zCoord) ;

                                }
                            }

                        }
                    }
                    for(int k = -1; k < 2; k++)
                    {
                        for(int j =-1; j < 2; j++)
                        {
                            for(int i =-1; i < 2; i++)
                            {

                                Block block01 = worldObj.getBlock(xCoord + j, yCoord + k, zCoord + i);
                                if (block01 == BWPRegistry.largeCampfire)
                                {
                                    CampfireBlock campfireBlock = (CampfireBlock) worldObj.getBlock(xCoord, yCoord, zCoord);
                                    double a = Math.random()*200;
                                    if (a <= 4.0) {
                                        campfireBlock.setOnFireDirectly(worldObj, xCoord, yCoord, zCoord) ;
                                    }

                                }
                                if (block01 == BWPRegistry.smallCampfire)
                                {
                                    CampfireBlock campfireBlock = (CampfireBlock) worldObj.getBlock(xCoord, yCoord, zCoord);
                                    double a = Math.random()*200;
                                    if (a <= 4.0) {
                                        campfireBlock.setOnFireDirectly(worldObj, xCoord, yCoord, zCoord) ;
                                    }

                                }
                                if (block01 == BWPRegistry.mediumCampfire)
                                {
                                    CampfireBlock campfireBlock = (CampfireBlock) worldObj.getBlock(xCoord, yCoord, zCoord);
                                    double a = Math.random()*200;
                                    if (a <= 4.0) {
                                        campfireBlock.setOnFireDirectly(worldObj, xCoord, yCoord, zCoord) ;
                                    }

                                }
                                if (block01 == Blocks.lava)
                                {
                                    CampfireBlock campfireBlock = (CampfireBlock) worldObj.getBlock(xCoord, yCoord, zCoord);
                                    double a = Math.random()*200;
                                    if (a <= 4.0) {
                                        campfireBlock.setOnFireDirectly(worldObj, xCoord, yCoord, zCoord) ;
                                    }

                                }
                                if (block01 == Blocks.lava)
                                {
                                    CampfireBlock campfireBlock = (CampfireBlock) worldObj.getBlock(xCoord, yCoord, zCoord);
                                    double a = Math.random()*200;
                                    if (a <= 4.0) {
                                        campfireBlock.setOnFireDirectly(worldObj, xCoord, yCoord, zCoord) ;
                                    }

                                }
                                if (block01 == Blocks.lava)
                                {
                                    CampfireBlock campfireBlock = (CampfireBlock) worldObj.getBlock(xCoord, yCoord, zCoord);
                                    double a = Math.random()*200;
                                    if (a <= 4.0) {
                                        campfireBlock.setOnFireDirectly(worldObj, xCoord, yCoord, zCoord) ;
                                    }

                                }
                            }

                        }
                    }
            }

            if ( iCurrentFireLevel > 0 )
            {
               if ( iCurrentFireLevel > 1 && worldObj.rand.nextFloat() <= CHANCE_OF_FIRE_SPREAD)
                {
                    boolean flag1 = worldObj.isBlockHighHumidity(xCoord, yCoord, zCoord);
                    byte b0 = 0;

                    if (flag1)
                    {
                        b0 = -50;
                    }
                    int l = 12;
                    tryCatchFire(worldObj, xCoord + 1, yCoord, zCoord, 300 + b0, random, l, WEST );
                    tryCatchFire(worldObj, xCoord - 1, yCoord, zCoord, 300 + b0 , random, l, EAST );
                    tryCatchFire(worldObj, xCoord, yCoord - 1, zCoord, 250 + b0 , random, l, UP   );
                    tryCatchFire(worldObj, xCoord, yCoord + 1, zCoord, 250 + b0 , random, l, DOWN );
                    tryCatchFire(worldObj, xCoord, yCoord, zCoord - 1, 300 + b0 , random, l, SOUTH);
                    tryCatchFire(worldObj, xCoord, yCoord, zCoord + 1, 300 + b0 , random, l, NORTH);
                    for (int i1 = xCoord - 1; i1 <= xCoord + 1; ++i1)
                    {
                        for (int j1 = zCoord - 1; j1 <= zCoord + 1; ++j1)
                        {
                            for (int k1 = yCoord - 1; k1 <= yCoord + 4; ++k1)
                            {
                                if (i1 != xCoord || k1 != yCoord || j1 != zCoord)
                                {
                                    int l1 = 100;

                                    if (k1 > yCoord + 1)
                                    {
                                        l1 += (k1 - (yCoord + 1)) * 100;
                                    }

                                    int i2 = getChanceOfNeighborsEncouragingFire(worldObj, i1, k1, j1);

                                    if (i2 > 0)
                                    {
                                        int j2 = (i2 + 40 + worldObj.difficultySetting.getDifficultyId() * 7) / (l + 30);

                                        if (flag1)
                                        {
                                            j2 /= 2;
                                        }

                                        if (j2 > 0 && random.nextInt(l1) <= j2 && (!worldObj.isRaining() || !worldObj.canLightningStrikeAt(i1, k1, j1)) && !worldObj.canLightningStrikeAt(i1 - 1, k1, zCoord) && !worldObj.canLightningStrikeAt(i1 + 1, k1, j1) && !worldObj.canLightningStrikeAt(i1, k1, j1 - 1) && !worldObj.canLightningStrikeAt(i1, k1, j1 + 1))
                                        {
                                            int k2 = l + random.nextInt(5) / 4;

                                            if (k2 > 15)
                                            {
                                                k2 = 15;
                                            }

                                            worldObj.setBlock(i1, k1, j1, Blocks.fire, k2, 3);
                                        }
                                    }
                                }
                            }
                        }
                    }

                }




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

    private void tryCatchFire(World p_149841_1_, int p_149841_2_, int p_149841_3_, int p_149841_4_, int p_149841_5_, Random p_149841_6_, int p_149841_7_, ForgeDirection face)
    {
        int j1 = p_149841_1_.getBlock(p_149841_2_, p_149841_3_, p_149841_4_).getFlammability(p_149841_1_, p_149841_2_, p_149841_3_, p_149841_4_, face);

        if (p_149841_6_.nextInt(p_149841_5_) < j1)
        {
            boolean flag = p_149841_1_.getBlock(p_149841_2_, p_149841_3_, p_149841_4_) == Blocks.tnt;

            if (p_149841_6_.nextInt(p_149841_7_ + 10) < 5 && !p_149841_1_.canLightningStrikeAt(p_149841_2_, p_149841_3_, p_149841_4_))
            {
                int k1 = p_149841_7_ + p_149841_6_.nextInt(5) / 4;

                if (k1 > 15)
                {
                    k1 = 15;
                }

                p_149841_1_.setBlock(p_149841_2_, p_149841_3_, p_149841_4_, Blocks.fire, k1, 3);
            }
            else
            {
                p_149841_1_.setBlockToAir(p_149841_2_, p_149841_3_, p_149841_4_);
            }

            if (flag)
            {
                Blocks.tnt.onBlockDestroyedByPlayer(p_149841_1_, p_149841_2_, p_149841_3_, p_149841_4_, 1);
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
        if (worldObj.getBlock(xCoord, yCoord, zCoord) != Blocks.air) {
            int iCurrentFireLevel = getCurrentFireLevel();

            if (iCurrentFireLevel > 0) {
                if (burnTimeCountdown <= 0) {
                    extinguishFire(true);

                    return 0;
                } else {

                    int iDesiredFireLevel = 2;

                    if (burnTimeSinceLit < WARMUP_TIME || burnTimeCountdown < REVERT_TO_SMALL_TIME) {
                        iDesiredFireLevel = 1;
                    } else if (burnTimeCountdown > BLAZE_TIME) {
                        iDesiredFireLevel = 3;
                    }

                    if (iDesiredFireLevel != iCurrentFireLevel) {
                        changeFireLevel(iDesiredFireLevel);

                        if (iDesiredFireLevel == 1 && iCurrentFireLevel == 2) {
                            worldObj.playAuxSFX(10, xCoord, yCoord, zCoord, 1);
                        }

                        return iDesiredFireLevel;
                    }
                }

            } else // iCurrenFireLevel == 0
            {
                if (burnTimeCountdown > 0 &&
                        BWPRegistry.unlitCampfire.getFuelState(worldObj, xCoord, yCoord, zCoord) == CampfireBlock.CAMPFIRE_FUEL_STATE_SMOULDERING) {
                    relightSmouldering();

                    return 1;
                }
            }

            return iCurrentFireLevel;
        }
        return 0;
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
        if (worldObj.getBlock(xCoord, yCoord,zCoord) != Blocks.air) {
            CampfireBlock block = (CampfireBlock) (worldObj.getBlock(xCoord, yCoord, zCoord));

            return block.fireLevel;
        }
        return 0;
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

    public void wasPlaced(EntityLivingBase entityliving, ItemStack itemStack)
    {
    }
    public void setFacing(byte facing2)
    {
        facing = facing2;
    }
    public byte getFacing()
    {
        return facing;
    }






}
