package mods.betterwithpatches.block;




import betterwithmods.BWRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.block.tile.CampfireTileEntity;
import mods.betterwithpatches.craft.CampFireCraftingManager;
import mods.betterwithpatches.proxy.ClientProxy;
import mods.betterwithpatches.util.BWMaterials;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidBase;

import java.util.Random;

import static mods.betterwithpatches.block.Campfire.getFuelState;


public class CampfireBlock extends BlockContainer
{

    public final int fireLevel;

    public static final int CAMPFIRE_FUEL_STATE_NORMAL = 0;
    public static final int CAMPFIRE_FUEL_STATE_BURNED_OUT = 1;
    public static final int CAMPFIRE_FUEL_STATE_SMOULDERING = 2;
    public static CampfireBlock[] fireLevelBlockArray = new CampfireBlock[4];

    public static boolean campfireChangingState = false; // temporarily true when block is being changed from one block ID to another

    private static final float SPIT_THICKNESS = (1F / 16F );
    private static final float HALF_SPIT_THICKNESS = (SPIT_THICKNESS / 2F );
    private static final float SPIT_HEIGHT = (12F / 16F );
    private static final float SPIT_MIN_Y = (SPIT_HEIGHT - HALF_SPIT_THICKNESS);
    private static final float SPIT_MAX_Y = (SPIT_MIN_Y + SPIT_THICKNESS);

    private static final float SPIT_SUPPORT_WIDTH = (1F / 16F );
    private static final float HALF_SPIT_SUPPORT_WIDTH = (SPIT_SUPPORT_WIDTH / 2F );
    private static final float SPIT_SUPPORT_BORDER = (0.5F / 16F );

    private static final float SPIT_FORK_WIDTH = (1F / 16F );
    private static final float SPIT_FORK_HEIGHT = (3F / 16F );
    private static final float SPIT_FORK_HEIGHT_OFFSET = (1F / 16F );
    private static final float SPIT_FORK_MIN_Y = (SPIT_MIN_Y - SPIT_FORK_HEIGHT_OFFSET);
    private static final float SPIT_FORK_MAX_Y = (SPIT_FORK_MIN_Y + SPIT_FORK_HEIGHT);

    private static final double SPIT_COLLISION_HEIGHT = (SPIT_HEIGHT + 1.5D / 16D );
    private static final double SPIT_COLLISION_WIDTH = (3D / 16D );
    private static final double SPIT_COLLISION_HALF_WIDTH = (SPIT_COLLISION_WIDTH / 2D );

    public CampfireBlock(int iFireLevel )
    {
        super(  Material.circuits );
        fireLevel = iFireLevel;
        fireLevelBlockArray[iFireLevel] = this;
        setHardness( 0.1F );
        setStepSound( soundTypeWood );
        setBlockName( "bwm:fcBlockCampfire" );

    }

    @Override
    public Item getItemDropped( int meta, Random random, int fortune )
    {
        if (fireLevel != 0 || getFuelState(meta) != CAMPFIRE_FUEL_STATE_NORMAL)
        {
            return null;
        }

        return super.getItemDropped( meta, random, fortune );
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta )
    {
        return new CampfireTileEntity();
    }

    @Override
    public void breakBlock( World worldIn, int x, int y, int z, Block blockBroken, int meta )
    {
        if ( !campfireChangingState)
        {
            CampfireTileEntity tileEntity = (CampfireTileEntity)worldIn.getTileEntity(x, y, z );


                tileEntity.ejectContents();


            // only called when not changing state as super kills the tile entity
            super.breakBlock( worldIn, x, y, z, worldIn.getBlock(x,y,z), meta );
        }
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k )
    {
        return null;
    }

    @Override
    protected boolean canSilkHarvest()
    {
        return false;
    }



    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z,EntityLivingBase entityliving, ItemStack itemStack)
    {
        byte chestFacing = 0;
        int facing = MathHelper.floor_double((entityliving.rotationYaw * 4F) / 360F + 0.5D) & 3;
        if (facing == 0)
        {
            chestFacing = 2;
        }
        if (facing == 1)
        {
            chestFacing = 5;
        }
        if (facing == 2)
        {
            chestFacing = 3;
        }
        if (facing == 3)
        {
            chestFacing = 4;
        }
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof CampfireTileEntity)
        {
            CampfireTileEntity teic = (CampfireTileEntity) te;
            teic.wasPlaced(entityliving, itemStack);
            teic.setFacing(chestFacing);
            world.markBlockForUpdate(x, y, z);
        }
    }




    @Override
    public int tickRate( World world )
    {
        return 2;
    }






    public boolean getCanBeSetOnFireDirectly(IBlockAccess blockAccess, int i, int j, int k)
    {
        return fireLevel == 0 && getFuelState(blockAccess, i, j, k) == CAMPFIRE_FUEL_STATE_NORMAL;
    }


    public void setOnFireDirectly(World world, int i, int j, int k)
    {
        if ( getCanBeSetOnFireDirectly(world, i, j, k) )
        {
            if ( !isRainingOnCampfire(world, i, j, k) )
            {
                changeFireLevel(world, i, j, k, 1, world.getBlockMetadata(i, j, k));

                CampfireTileEntity tileEntity = (CampfireTileEntity)world.getTileEntity(
                        i, j, k );

                tileEntity.onFirstLit();

                world.playSoundEffect( i + 0.5D, j + 0.5D, k + 0.5D,
                        "mob.ghast.fireball", 1F, world.rand.nextFloat() * 0.4F + 0.8F );

                if ( !Blocks.portal.func_150000_e( world, i, j, k ) )
                {

                    Block iBlockBelowID = world.getBlock( i, j - 1, k );

                    if ( iBlockBelowID == Blocks.netherrack) //|| iBlockBelowID ==  BTWBlocks.fallingNetherrack.blockID )
                    {
                        world.setBlock( i, j, k, Blocks.fire);
                    }
                }

            }
            else
            {
                world.playAuxSFX( 10, i, j, k, 0 );
            }

        }

    }
    @Override
    public boolean  onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
        ItemStack stack = player.getCurrentEquippedItem();
        CampfireBlock campfireBlock = (CampfireBlock) world.getBlock(i, j, k);
        CampfireTileEntity tileEntityNew =
                (CampfireTileEntity)world.getTileEntity( i, j, k );
        ItemStack nocrash = tileEntityNew.getSpitStack();


        if ( stack != null )
        {
            Item item = stack.getItem();

            if ( !getHasSpit(world, i, j, k) )
            {
                if ( item == BWPRegistry.itemPointyStick)
                {
                    setHasSpit(world, i, j, k, true);

                    CampfireTileEntity tileEntity =
                            (CampfireTileEntity)world.getTileEntity( i, j, k );

                    tileEntity.setSpitStack(stack);

                    stack.stackSize--;

                    return true;
                }
            }
            else
            {
                CampfireTileEntity tileEntity =
                        (CampfireTileEntity)world.getTileEntity( i, j, k );

                ItemStack cookStack = tileEntity.getCookStack();

                if ( cookStack == null )
                {
                    if ( isValidCookItem(stack) && nocrash != null )
                    {
                        ItemStack spitStack = tileEntity.getSpitStack();

                        if ( spitStack.getItemDamage() == 0 )
                        {
                            tileEntity.setCookStack(stack);
                        }
                        else
                        {
                            // break the damaged spit when the player attempts to place an item on it
                            // this is to discourage early game exploits involving half damaged sticks.

                            tileEntity.setSpitStack(null);

                            setHasSpit(world, i, j, k, false);


                                ItemStack ejectStack = stack.copy();

                                ejectStack.stackSize = 1;
                            if ( !world.isRemote ) {
                                world.spawnEntityInWorld(new EntityItem(world, i, j, k, ejectStack));


                                world.playAuxSFX(10,
                                        i, j, k, 0);
                            }
                        }

                        stack.stackSize--;

                        return true;
                    }
                }
                else if ( cookStack.getItem() == stack.getItem() &&
                        stack.stackSize < stack.getMaxStackSize() )
                {
                    player.worldObj.playSoundAtEntity( player, "random.pop", 0.2F, 2F );

                    stack.stackSize++;

                    tileEntity.setCookStack(null);

                    return true;
                }
            }
            if (item == Items.flint_and_steel){

                if (fireLevel == 0){
                    if ( !world.isRemote ) {
                        stack.damageItem(2, player);
                        double a = Math.random() * 4;
                        if (a >= 3.0) {
                            campfireBlock.setOnFireDirectly(world, i, j, k);
                            tileEntityNew.getDescriptionPacket();

                        }
                    }


                }
                return true;
            }


            if (fireLevel > 0 || getFuelState(world, i, j, k) == CAMPFIRE_FUEL_STATE_SMOULDERING)
            {





                if ( getCanBeFedDirectlyIntoCampfire(stack) )
                {


                        CampfireTileEntity tileEntity = (CampfireTileEntity)world.getTileEntity( i, j, k );

                        world.playSoundEffect( i + 0.5D, j + 0.5D, k + 0.5D, "mob.ghast.fireball",
                                0.2F + world.rand.nextFloat() * 0.1F,
                                world.rand.nextFloat() * 0.25F + 1.25F );

                        tileEntity.addBurnTime(getCampfireBurnTime(stack));
                    world.markBlockForUpdate(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);

                    stack.stackSize--;

                    return true;
                }

            }
        }
        else // empty hand
        {
            CampfireTileEntity tileEntity =
                    (CampfireTileEntity)world.getTileEntity( i, j, k );

            ItemStack cookStack = tileEntity.getCookStack();

            if ( cookStack != null  )
            {
                if (!world.isRemote) {
                    world.spawnEntityInWorld(new EntityItem(world, i, j, k, new ItemStack(cookStack.getItem())));
                }


                tileEntity.setCookStack(null);

                return true;
            }
            else
            {
                ItemStack spitStack = tileEntity.getSpitStack();

                if ( spitStack != null )
                {
                    if (!world.isRemote) {

                        world.spawnEntityInWorld(new EntityItem(world, i, j, k, spitStack));
                    }
                        setHasSpit(world, i, j, k, false);
                        tileEntity.setSpitStack(null);


                        return true;

                }
            }
        }

        return false;
    }



    public int getCampfireBurnTime(ItemStack stack)
    {
        if( stack.getItem() == BWPRegistry.itemShaft ||  stack.getItem() == BWPRegistry.itemPointyStick){
          return 50;
        }
        if( stack.getItem() == BWRegistry.bark || stack.equals(BWMaterials.getMaterial(BWMaterials.SAWDUST))){
            return 25;
        }
        else return 0;

    }


    public boolean getCanBeFedDirectlyIntoCampfire( ItemStack itemstack) //ill leave it like this for now, going to change it soon
    {
        if (itemstack.getItem() == BWPRegistry.itemPointyStick || itemstack.getItem() == BWPRegistry.itemShaft || itemstack.getItem() == BWRegistry.bark || itemstack.equals(BWMaterials.getMaterial(BWMaterials.SAWDUST))){
            return true;
        }
            return false;
    }



    @Override
    public void onEntityCollidedWithBlock( World world, int i, int j, int k, Entity entity )
    {
        if ( !world.isRemote && entity.isEntityAlive() && (fireLevel > 0 || getFuelState(world, i, j, k) == CAMPFIRE_FUEL_STATE_SMOULDERING) )
        {
            if ( entity instanceof EntityItem)
            {
                EntityItem entityItem = (EntityItem)entity;
                ItemStack targetStack = entityItem.getEntityItem();
                Item item = targetStack.getItem();
                int iBurnTime = getCampfireBurnTime(targetStack);

                if ( iBurnTime > 0  && getCanBeFedDirectlyIntoCampfire(targetStack))
                {
                    iBurnTime *= targetStack.stackSize;

                    CampfireTileEntity tileEntity =
                            (CampfireTileEntity)world.getTileEntity( i, j, k );

                    world.playSoundEffect( i + 0.5D, j + 0.5D, k + 0.5D, "mob.ghast.fireball",
                            world.rand.nextFloat() * 0.1F + 0.2F,
                            world.rand.nextFloat() * 0.25F + 1.25F );

                    tileEntity.addBurnTime(iBurnTime);

                    entity.setDead();
                }
                else {
                    entity.setFire(10);

                }
            }
            else {
                entity.setFire(10);

            }
        }
    }


    public boolean getDoesFireDamageToEntities(World world, int i, int j, int k, Entity entity)
    {
        return fireLevel > 2 || (fireLevel == 2 && entity instanceof EntityLiving );
    }


    public boolean getCanBlockLightItemOnFire(IBlockAccess blockAccess, int i, int j, int k)
    {

        return fireLevel > 0;
    }


    public void onFluidFlowIntoBlock(World world, int i, int j, int k, BlockFluidBase newBlock)
    {
        if (fireLevel > 0 )
        {
            world.playAuxSFX(10, i, j, k, 0 );
        }


    }


    //------------- Class Specific Methods ------------//


    public void setIAligned(World world, int i, int j, int k, boolean bIAligned)
    {
        int iMetadata = setIAligned(world.getBlockMetadata(i, j, k), bIAligned);

        world.setBlockMetadataWithNotify( i, j, k, iMetadata,3 );
    }

    public int setIAligned(int iMetadata, boolean bIAligned)
    {
        if ( bIAligned )
        {
            iMetadata |= 1;
        }
        else
        {
            iMetadata &= (~1);
        }

        return iMetadata;
    }

    public boolean getIsIAligned(IBlockAccess blockAccess, int i, int j, int k)
    {
        return getIsIAligned(blockAccess.getBlockMetadata(i, j, k));
    }

    public boolean getIsIAligned(int iMetadata)
    {
        return ( iMetadata & 1 ) != 0;
    }

    public boolean isFacingIAligned(int iFacing)
    {
        return iFacing >= 4;
    }

    public void setHasSpit(World world, int i, int j, int k, boolean bHasSpit)
    {
        int iMetadata = setHasSpit(world.getBlockMetadata(i, j, k), bHasSpit);

        world.setBlockMetadataWithNotify( i, j, k, iMetadata,3 );
    }

    public int setHasSpit(int iMetadata, boolean bHasSpit)
    {
        if ( bHasSpit )
        {
            iMetadata |= 2;
        }
        else
        {
            iMetadata &= (~2);
        }

        return iMetadata;
    }

    public boolean getHasSpit(IBlockAccess blockAccess, int i, int j, int k)
    {
        return getHasSpit(blockAccess.getBlockMetadata(i, j, k));
    }

    public boolean getHasSpit(int iMetadata)
    {
        return ( iMetadata & 2 ) != 0;
    }

    public void setFuelState(World world, int i, int j, int k, int iCampfireState)
    {
        int iMetadata = setFuelState(world.getBlockMetadata(i, j, k), iCampfireState);

        world.setBlockMetadataWithNotify( i, j, k, iMetadata,3 );
    }

    public int setFuelState(int iMetadata, int iCampfireState)
    {
        iMetadata &= ~12; // filter out old state

        return iMetadata | ( iCampfireState << 2 );
    }

    public int getFuelState(IBlockAccess blockAccess, int i, int j, int k)
    {
        return getFuelState(blockAccess.getBlockMetadata(i, j, k));
    }

    public int getFuelState(int iMetadata)
    {
        return ( iMetadata & 12 ) >> 2;
    }

    public boolean isValidCookItem(ItemStack stack)
    {
        if (CampFireCraftingManager.instance.getRecipeResult(stack.getItem()) != null )
        {
            return true;
        }

        return false;
    }

    public void extinguishFire(World world, int i, int j, int k, boolean bSmoulder)
    {
        int iMetadata = world.getBlockMetadata( i, j, k );

        if ( bSmoulder )
        {
            iMetadata = setFuelState(iMetadata, CAMPFIRE_FUEL_STATE_SMOULDERING);
        }
        else
        {
            iMetadata = setFuelState(iMetadata, CAMPFIRE_FUEL_STATE_BURNED_OUT);
        }

        changeFireLevel(world, i, j, k, 0, iMetadata);

        if ( !world.isRemote )
        {
            world.playAuxSFX( 10, i, j, k, 1 );
        }
    }

    public void relightFire(World world, int i, int j, int k)
    {
        changeFireLevel(world, i, j, k, 1, setFuelState(world.getBlockMetadata(i, j, k), CAMPFIRE_FUEL_STATE_NORMAL));
    }

    public void stopSmouldering(World world, int i, int j, int k)
    {
        setFuelState(world, i, j, k, CAMPFIRE_FUEL_STATE_BURNED_OUT);
    }

    public void changeFireLevel(World world, int i, int j, int k, int iFireLevel, int iMetadata)
    {
TileEntity tileentity = world.getTileEntity(i, j , k);
        CampfireBlock.campfireChangingState = true;

        world.setBlock( i, j, k, CampfireBlock.fireLevelBlockArray[iFireLevel], iMetadata, 3 );

        CampfireBlock.campfireChangingState = false;
        if (tileentity != null) {
            tileentity.validate();
            world.setTileEntity(i, j, k, tileentity);
        }



    }


    public boolean isRainingAtPos(World world, int i, int j, int k)
    {
        if ( world.isRaining() && world.canBlockSeeTheSky( i, j, k ) &&
                j >= world.getPrecipitationHeight( i, k )  )
        {
            BiomeGenBase biome = world.getBiomeGenForCoords( i, k );

            return biome.canSpawnLightningBolt();
        }

        return false;
    }

    public boolean isRainingOnCampfire(World world, int i, int j, int k)
    {
        return isRainingAtPos(world, i, j, k);
    }


    @Override
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
        if(!World.doesBlockHaveSolidTopSurface(world, i, j -1, k)){
            return false;
        }
        return super.canPlaceBlockAt(world, i, j, k);
    }



    @Override
    public void updateTick(World worldIn, int x, int y, int z, Random random){
        if (!World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z)){
            if (fireLevel > 0 )
            {
                worldIn.playAuxSFX( 10, x, y, z, 1 );
            }

            dropBlockAsItem( worldIn, x, y, z, worldIn.getBlockMetadata( x, y, z ), 0 );

            worldIn.setBlockToAir( x, y, z );
        }

    }

    @Override
    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor) {
        if (!World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z)){
            if (fireLevel > 0 )
            {
                worldIn.playAuxSFX( 10, x, y, z, 1 );
            }

            dropBlockAsItem( worldIn, x, y, z, worldIn.getBlockMetadata( x, y, z ), 0 );

            worldIn.setBlockToAir( x, y, z );
        }
    }

    //----------- Client Side Functionality -----------//


    @SideOnly(Side.CLIENT)
    public IIcon[] icons;






    @Override
    public void registerBlockIcons( IIconRegister reg )
    { this.icons = new IIcon[7];
        this.icons[0] = reg.registerIcon( "fcBlockCampfire" );
        this.icons[1] = reg.registerIcon("fcBlockCampfire_spit");
        this.icons[2] = reg.registerIcon("fcBlockCampfire_support");
        this.icons[3] = reg.registerIcon("fcBlockCampfire_burned");
        this.icons[4] = reg.registerIcon("fcOverlayEmbers");
    }

    @Override
    public IIcon getIcon(int side,int meta ) {

        return (((Campfire) BWPRegistry.campfire).icons[1]);
    }
    @Override

    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
        return true;
    }

    @Override
    protected String getTextureName() {
        return "betterwithpatches:OakBark";
    }

    @Override
    public int getRenderType() {
        return ClientProxy.renderBlockCampfire;
    }






    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick( World world, int i, int j, int k, Random rand )
    {
        if (fireLevel > 1 )
        {
            for (int iTempCount = 0; iTempCount < fireLevel; iTempCount++ )
            {
                double xPos = i + rand.nextFloat();
                double yPos = j + 0.5F + ( rand.nextFloat() * 0.5F );
                double zPos = k + rand.nextFloat();

                world.spawnParticle( "smoke", xPos, yPos, zPos, 0D, 0D, 0D );
            }

            CampfireTileEntity tileEntity =
                    (CampfireTileEntity)world.getTileEntity( i, j, k );

            if ( tileEntity.getIsFoodBurning() )
            {
                for ( int iTempCount = 0; iTempCount < 1; ++iTempCount )
                {
                    double xPos = i + 0.375F + rand.nextFloat() * 0.25F;
                    double yPos = j + 0.5F + rand.nextFloat() * 0.5F;
                    double zPos = k + 0.375F + rand.nextFloat() * 0.25F;

                    world.spawnParticle( "largesmoke", xPos, yPos, zPos, 0D, 0D, 0D );
                }
            }
            else if ( tileEntity.getIsCooking() )
            {
                for ( int iTempCount = 0; iTempCount < 1; ++iTempCount )
                {
                    double xPos = i + 0.375F + rand.nextFloat() * 0.25F;
                    double yPos = j + 0.5F + rand.nextFloat() * 0.5F;
                    double zPos = k + 0.375F + rand.nextFloat() * 0.25F;

                    world.spawnParticle( "fcwhitesmoke", xPos, yPos, zPos, 0D, 0D, 0D );
                }
            }
        }
        else if (fireLevel == 1 || getFuelState(world, i, j, k) == CAMPFIRE_FUEL_STATE_SMOULDERING)
        {
            double xPos = (double)i + 0.375D + ( rand.nextDouble() * 0.25D );
            double yPos = (double)j + 0.25D + ( rand.nextDouble() * 0.25D );
            double zPos = (double)k + 0.375D + ( rand.nextDouble() * 0.25D );

            world.spawnParticle( "smoke", xPos, yPos, zPos, 0D, 0D, 0D );
        }

        if (fireLevel > 0 )
        {
            if ( rand.nextInt(24) == 0 )
            {
                float fVolume = (fireLevel * 0.25F ) + rand.nextFloat();

                world.playSound( i + 0.5D, j + 0.5D, k + 0.5D, "fire.fire",
                        fVolume, rand.nextFloat() * 0.7F + 0.3F, false );
            }
        }
    }
}