package mods.betterwithpatches.mixins.block;

import mods.betterwithpatches.BTWinfoBatch.BTWBlockadd;
import mods.betterwithpatches.BTWinfoBatch.BTWMaterialAdd;
import mods.betterwithpatches.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(Block.class)
public abstract class BlockMixin implements BTWBlockadd {
    @Override
    public boolean isGroundCover(){
        return true;
    }
    @Override
    public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k) {
        return 0.0f;
    }
    @Override
    public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
        return world.doesBlockHaveSolidTopSurface(world, i, j, k);
    }
    @Override
    public boolean isSnowCoveringTopSurface(IBlockAccess blockAccess, int i, int j, int k)
    {
        Block iBlockAboveID = blockAccess.getBlock( i, j + 1, k );

        if ( iBlockAboveID != Blocks.air )
        {
            Block blockAbove = iBlockAboveID;

            Material aboveMaterial = blockAbove.getMaterial();
            BTWBlockadd blockadd = (BTWBlockadd)blockAbove;

            if ( aboveMaterial == Material.snow || aboveMaterial == Material.craftedSnow &&
                    blockAbove.isBlockSolid(( blockAccess), i, j + 1, k, 0 ) )
            {
                return true;
            }
            else if (
                    blockadd.groundCoverRestingOnVisualOffset(blockAccess, i, j + 1, k) < -0.99F &&
                            blockAccess.getBlock( i, j + 2, k ) == Blocks.snow)
            {
                // consider snow resting on tall grass and such

                return true;
            }
        }

        return false;
    }
    @Override
    public boolean hasMortar(IBlockAccess blockAccess, int i, int j, int k)
    {
        return false;
    }
    @Override
    public int getFacing(int iMetadata)
    {
        return 0;
    }
    @Override
    public boolean hasContactPointToFullFace(IBlockAccess blockAccess, int i, int j, int k, int iFacing)
    {
        Block block = blockAccess.getBlock(i,j,k);
        return block.isNormalCube(blockAccess, i,j,k);
    }

    @Override
    public boolean hasContactPointToSlabSideFace(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIsSlabUpsideDown)
    {
        return hasContactPointToFullFace(blockAccess, i, j, k, iFacing);
    }

    @Override
    public boolean isStickyToSnow(IBlockAccess blockAccess, int i, int j, int k)
    {
        return false;
    }
    @Override
    public boolean hasStickySnowNeighborInContact(World world, int i, int j, int k)
    {
        for ( int iTempFacing = 0; iTempFacing < 6; iTempFacing++ )
        {
            if ( WorldUtils.hasStickySnowNeighborInFullFaceContactToFacing(world, i, j, k, iTempFacing) )
            {
                return true;
            }
        }

        return false;
    }
    @Override
    public boolean hasNeighborWithMortarInContact(World world, int i, int j, int k)
    {
        for ( int iTempFacing = 0; iTempFacing < 6; iTempFacing++ )
        {
            if ( WorldUtils.hasNeighborWithMortarInFullFaceContactToFacing(world, i, j, k, iTempFacing) )
            {
                return true;
            }
        }

        return false;
    }
    @Override
    public boolean canMobsSpawnOn(World world, int i, int j, int k)
    {
        Block block = world.getBlock(i,j,k);
        Material blockMaterial = block.getMaterial();
        BTWMaterialAdd materialAdd = (BTWMaterialAdd)blockMaterial;
        return materialAdd.getMobsCanSpawnOn(world.provider.dimensionId) &&
                block.getCollisionBoundingBoxFromPool( world, i, j, k ) != null;
    }
    @Override
    public float mobSpawnOnVerticalOffset(World world, int i, int j, int k)
    {
        return 0F;
    }
    @Override
    public boolean shouldRenderNeighborHalfSlabSide(IBlockAccess blockAccess, int i, int j, int k, int iNeighborSlabSide, boolean bNeighborUpsideDown)
    {
        Block block = blockAccess.getBlock(i,j,k);
        return !block.isOpaqueCube();
    }
    @Override
    public boolean shouldRenderNeighborFullFaceSide(IBlockAccess blockAccess, int i, int j, int k, int iNeighborSide)
    {
        Block block = blockAccess.getBlock(i,j,k);
        return !block.isOpaqueCube();
    }






}