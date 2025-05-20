package mods.betterwithpatches.BTWinfoBatch;

import betterwithmods.util.BlockPosition;
import mods.betterwithpatches.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Optional;

public interface BTWBlockadd {
    default boolean isGroundCover() {
        return true;
    }

    default boolean canGroundCoverRestOnBlock(World world, int i, int j, int k){
        return true;
    };
    default float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k){
        return 0.0f;
    };
    default  boolean isSnowCoveringTopSurface(IBlockAccess blockAccess, int i, int j, int k)
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
    default boolean hasMortar(IBlockAccess blockAccess, int i, int j, int k)
    {
        return false;
    }
    default int getFacing(int iMetadata)
    {
        return 0;
    }
    default boolean hasContactPointToFullFace(IBlockAccess blockAccess, int i, int j, int k, int iFacing)
    {
        Block block = blockAccess.getBlock(i,j,k);
        return block.isNormalCube(blockAccess, i,j,k);
    }

    default boolean hasContactPointToSlabSideFace(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIsSlabUpsideDown)
    {
        return hasContactPointToFullFace(blockAccess, i, j, k, iFacing);
    }
    default boolean isStickyToSnow(IBlockAccess blockAccess, int i, int j, int k)
    {
        return false;
    }
    default boolean hasStickySnowNeighborInContact(World world, int i, int j, int k)
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
    default  boolean hasNeighborWithMortarInContact(World world, int i, int j, int k)
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

    default boolean canMobsSpawnOn(World world, int i, int j, int k)
    {
        Block block = world.getBlock(i,j,k);
        Material blockMaterial = block.getMaterial();
        BTWMaterialAdd materialAdd = (BTWMaterialAdd)blockMaterial;
        return materialAdd.getMobsCanSpawnOn(world.provider.dimensionId) &&
                block.getCollisionBoundingBoxFromPool( world, i, j, k ) != null;
    }
default float mobSpawnOnVerticalOffset(World world, int i, int j, int k)
{
    return 0F;
}
    default boolean shouldRenderNeighborHalfSlabSide(IBlockAccess blockAccess, int i, int j, int k, int iNeighborSlabSide, boolean bNeighborUpsideDown)
    {
        Block block = blockAccess.getBlock(i,j,k);
        return !block.isOpaqueCube();
    }
    default boolean shouldRenderNeighborFullFaceSide(IBlockAccess blockAccess, int i, int j, int k, int iNeighborSide)
    {
        Block block = blockAccess.getBlock(i,j,k);
        return !block.isOpaqueCube();
    }


}

