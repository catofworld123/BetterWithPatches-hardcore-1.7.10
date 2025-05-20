package mods.betterwithpatches.block;

import betterwithmods.util.BlockPosition;
import mods.betterwithpatches.BTWinfoBatch.BTWBlockadd;
import mods.betterwithpatches.BTWinfoBatch.BTWMaterialAdd;
import mods.betterwithpatches.util.BWPUtils;
import mods.betterwithpatches.util.RenderUtils;
import mods.betterwithpatches.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class SlabBlock extends Block implements BTWBlockadd
{
        public SlabBlock( Material material )
    {
        super( material );

        setBlockBounds(0F, 0F, 0F, 1F, 0.5F, 1F);

        setLightOpacity( 255 );

    }

    @Override
    public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
        if ( iFacing == 0 || ( iFacing != 1 && fClickY > 0.5F ) )
        {
            if ( canBePlacedUpsideDownAtLocation(world, i, j, k) )
            {
                iMetadata = setIsUpsideDown(iMetadata, true);
            }
        }

        return iMetadata;
    }
    @Override
    public int getMixedBrightnessForBlock(IBlockAccess worldIn, int x, int y, int z){
            Block block = worldIn.getBlock(x,y,z);
            block.useNeighborBrightness = true;
            return block.getMixedBrightnessForBlock(worldIn, x, y, z);

    }

    @Override
    public void setBlockBoundsBasedOnState(
            IBlockAccess blockAccess, int i, int j, int k)
    {
        int iMetadata = blockAccess.getBlockMetadata(i, j, k);
        if ( getIsUpsideDown(iMetadata) )
        {
            this.setBlockBounds(
                    0F, 0.5F, 0F, 1F, 1F, 1F );
        }
        else
        {
            this.setBlockBounds(
                    0F, 0F, 0F, 1F, 0.5F, 1F );
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
    public boolean isSideSolid(IBlockAccess blockAccess, int i, int j, int k, ForgeDirection iFacing )
    {
        boolean bIsUpsideDown = getIsUpsideDown(blockAccess, i, j, k);

        if ( bIsUpsideDown )
        {
            return iFacing.flag == 1;
        }
        else
        {
            return iFacing.flag == 0;
        }
    }

    @Override
    public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k)
    {
        return true;
    }

    @Override
    public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k)
    {
        if ( !getIsUpsideDown(blockAccess, i, j, k) )
        {
            return -0.5F;
        }

        return 0F;
    }

    @Override
    public boolean isSnowCoveringTopSurface(IBlockAccess blockAccess, int i, int j, int k)
    {
        BTWBlockadd blockadd = (BTWBlockadd)blockAccess.getBlock(i,j+1,k);
        if ( !getIsUpsideDown(blockAccess, i, j, k) )
        {
            // bottom half slabs are only covered by offset snow ground cover

            return blockAccess.getBlock( i, j + 1, k ) == Blocks.snow;
        }


        return blockadd.isSnowCoveringTopSurface(blockAccess, i, j, k);
    }

    @Override
    public boolean hasContactPointToFullFace(IBlockAccess blockAccess, int i, int j, int k, int iFacing)
    {
        if ( iFacing < 2 )
        {
            boolean bIsUpsideDown = getIsUpsideDown(blockAccess, i, j, k);

            return bIsUpsideDown == ( iFacing == 1 );
        }

        return true;
    }

    @Override
    public boolean hasContactPointToSlabSideFace(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIsSlabUpsideDown)
    {
        return bIsSlabUpsideDown == getIsUpsideDown(blockAccess, i, j, k);
    }

    @Override
    public boolean hasNeighborWithMortarInContact(World world, int i, int j, int k)
    {
        boolean bIsUpsideDown = getIsUpsideDown(world, i, j, k);

        return hasNeighborWithMortarInContact(world, i, j, k, bIsUpsideDown);
    }

    @Override
    public boolean hasStickySnowNeighborInContact(World world, int i, int j, int k)
    {
        boolean bIsUpsideDown = getIsUpsideDown(world, i, j, k);

        return hasStickySnowNeighborInContact(world, i, j, k, bIsUpsideDown);
    }

    @Override
    protected ItemStack createStackedBlock(int iMetadata )
    {
        iMetadata = setIsUpsideDown(iMetadata, false);

        return super.createStackedBlock( iMetadata );
    }

    @Override
    public boolean canMobsSpawnOn( World world, int i, int j, int k)
    {
        Block block = world.getBlock(i,j,k);
        BTWMaterialAdd materialAdd = (BTWMaterialAdd)block.getMaterial();
        return materialAdd.getMobsCanSpawnOn(world.provider.dimensionId);
    }

    @Override
    public float mobSpawnOnVerticalOffset(World world, int i, int j, int k)
    {
        if ( !getIsUpsideDown(world, i, j, k) )
        {
            return -0.5F;
        }

        return 0F;
    }

    //------------- Class Specific Methods ------------//

    protected boolean hasNeighborWithMortarInContact(World world, int i, int j, int k, boolean bIsUpsideDown)
    {
        if ( bIsUpsideDown )
        {
            if ( WorldUtils.hasNeighborWithMortarInFullFaceContactToFacing(world, i, j, k, 1) )
            {
                return true;
            }
        }
        else
        {
            if ( WorldUtils.hasNeighborWithMortarInFullFaceContactToFacing(world, i, j, k, 0) )
            {
                return true;
            }
        }

        for ( int iTempFacing = 2; iTempFacing < 6; iTempFacing++ )
        {
            if ( WorldUtils.hasNeighborWithMortarInSlabSideContactToFacing(world, i, j, k, iTempFacing, bIsUpsideDown) )
            {
                return true;
            }
        }

        return false;
    }

    protected boolean hasStickySnowNeighborInContact(World world, int i, int j, int k, boolean bIsUpsideDown)
    {
        if ( bIsUpsideDown )
        {
            if ( WorldUtils.hasStickySnowNeighborInFullFaceContactToFacing(world, i, j, k, 1) )
            {
                return true;
            }
        }
        else
        {
            if ( WorldUtils.hasStickySnowNeighborInFullFaceContactToFacing(world, i, j, k, 0) )
            {
                return true;
            }
        }

        for ( int iTempFacing = 2; iTempFacing < 6; iTempFacing++ )
        {
            if ( WorldUtils.hasStickySnowNeighborInSlabSideContactToFacing(world, i, j, k, iTempFacing, bIsUpsideDown) )
            {
                return true;
            }
        }

        return false;
    }

    public boolean canBePlacedUpsideDownAtLocation(World world, int i, int j, int k)
    {
        return true;
    }

    public boolean getIsUpsideDown(IBlockAccess blockAccess, int i, int j, int k)
    {
        return getIsUpsideDown(blockAccess.getBlockMetadata(i, j, k));
    }

    public boolean getIsUpsideDown(int iMetadata)
    {
        return ( iMetadata & 1 ) > 0;
    }

    public void setIsUpsideDown(World world, int i, int j, int k, boolean bUpsideDown)
    {
        int iMetadata = world.getBlockMetadata( i, j, k );

        world.setBlockMetadataWithNotify(i, j, k, setIsUpsideDown(iMetadata, bUpsideDown), 2);
    }

    public int setIsUpsideDown(int iMetadata, boolean bUpsideDown)
    {
        if ( bUpsideDown )
        {
            iMetadata |= 1;
        }
        else
        {
            iMetadata &= (~1);
        }

        return iMetadata;
    }

    public boolean convertToFullBlock(World world, int i, int j, int k)
    {
        int iMetadata = world.getBlockMetadata( i, j, k );

        return world.setBlock(i, j, k, getCombinedBlock(iMetadata), getCombinedMetadata(iMetadata),2);
    }

    public abstract Block getCombinedBlock(int iMetadata);

    public int getCombinedMetadata(int iMetadata)
    {
        return 0;
    }

    protected AxisAlignedBB getBlockBoundsFromPoolFromMetadata(int iMetadata)
    {
        if ( getIsUpsideDown(iMetadata) )
        {
            return AxisAlignedBB.getBoundingBox(
                    0F, 0.5F, 0F, 1F, 1F, 1F );
        }
        else
        {
            return AxisAlignedBB.getBoundingBox(
                    0F, 0F, 0F, 1F, 0.5F, 1F );
        }
    }

    //----------- Client Side Functionality -----------//

    @Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
        BTWBlockadd blockadd = (BTWBlockadd) blockAccess.getBlock(iNeighborI, iNeighborJ, iNeighborK);
        BlockPosition myPos = new BlockPosition(iNeighborI, iNeighborJ, iNeighborK,
                ForgeDirection.getOrientation(blockadd.getFacing(iSide) ));

        boolean bUpsideDown = getIsUpsideDown(blockAccess, myPos.x, myPos.y, myPos.z);

        if ( iSide < 2 )
        {
            if ( iSide == 0 )
            {
                Block block = blockAccess.getBlock( iNeighborI, iNeighborJ, iNeighborK );
                return bUpsideDown || !block.isOpaqueCube();
            }
            else // iSide == 1

            {Block block = blockAccess.getBlock( iNeighborI, iNeighborJ, iNeighborK );
                return !bUpsideDown || !block.isOpaqueCube();
            }
        }

        return RenderUtils.shouldRenderNeighborHalfSlabSide(blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide, bUpsideDown);
    }

    @Override
    public boolean shouldRenderNeighborHalfSlabSide(IBlockAccess blockAccess, int i, int j, int k, int iNeighborSlabSide, boolean bNeighborUpsideDown)
    {
        return getIsUpsideDown(blockAccess, i, j, k) != bNeighborUpsideDown;
    }

    @Override
    public boolean shouldRenderNeighborFullFaceSide(IBlockAccess blockAccess, int i, int j, int k, int iNeighborSide)
    {
        if ( iNeighborSide < 2 )
        {
            boolean bUpsideDown = getIsUpsideDown(blockAccess, i, j, k);

            if ( iNeighborSide == 0 )
            {
                return !bUpsideDown;
            }
            else // iNeighborSide == 1
            {
                return bUpsideDown;
            }
        }

        return true;
    }
}
