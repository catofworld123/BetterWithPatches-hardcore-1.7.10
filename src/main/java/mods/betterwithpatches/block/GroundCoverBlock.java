package mods.betterwithpatches.block;

import betterwithmods.BWMod;
import betterwithmods.util.BlockPosition;
import betterwithmods.util.RayUtils;
import mods.betterwithpatches.BTWinfoBatch.BTWBlockadd;
import mods.betterwithpatches.util.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeCache;
import net.minecraftforge.common.util.ForgeDirection;

public class GroundCoverBlock extends Block implements BTWBlockadd {
    public static final float VISUAL_HEIGHT = 0.125f;

    protected GroundCoverBlock(Material material) {
        super(material);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.setHardness(0.1f);
        this.isToolEffective("shovel",0);
       // this.setBuoyant();
        this.setLightOpacity(0);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }


    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k) {
         Block iBlockBelow = world.getBlock(i, j - 1, k);
        if (iBlockBelow != null) {
            if (iBlockBelow == Blocks.leaves || iBlockBelow == Blocks.leaves2) {
                return true;
            }
            else return World.doesBlockHaveSolidTopSurface(world,i, j-1, k);
        }

        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, Block iNeighborBlock) {
        if (!this.canPlaceBlockAt(world, i, j, k)) {
            this.onSelfRemoval(world, i, j, k);
            world.setBlockToAir(i, j, k);
        }
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
        float fVisualOffset = 0.0f;
        Block blockBelow = world.getBlock(i, j - 1, k);
        if (blockBelow != null) {
            if (blockBelow instanceof BlockSlab && ((BlockSlab)blockBelow).field_150004_a) {

                fVisualOffset = -0.5f;
            }
            else fVisualOffset =0.0f;
        }
        RayUtils rayTrace = new RayUtils(world, i, j, k, startRay, endRay);
        rayTrace.addBoxWithLocalCoordsToIntersectionList(0.0, fVisualOffset, 0.0, 1.0, 0.125f + fVisualOffset, 1.0);
        return rayTrace.getFirstIntersection();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return null;
    }

    @Override
    public boolean isGroundCover() {
        return true;
    }


    @Override
    public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
        return false;
    }
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
        float fVisualOffset = 0.0f;
        Block iBlockBelow = world.getBlock(i, j - 1, k);
        BTWBlockadd blockadd = (BTWBlockadd)iBlockBelow;
        if (iBlockBelow!= null) {
            System.out.println("--->" + blockadd.groundCoverRestingOnVisualOffset(world, i, j-1, k));
            fVisualOffset = blockadd.groundCoverRestingOnVisualOffset(world, i, j - 1, k);
        }
        return super.getSelectedBoundingBoxFromPool(world, i, j, k).offset(i, (float)j + fVisualOffset, k);
    }


    public static void clearAnyGroundCoverRestingOnBlock(World world, int i, int j, int k) {
        Block blockAbove = world.getBlock(i, j + 1, k);
        if (blockAbove != null) {
            Block block2Above;
            BTWBlockadd blockadd = (BTWBlockadd)blockAbove;
            if (blockadd.isGroundCover()) {
                if (blockAbove instanceof GroundCoverBlock) {
                    GroundCoverBlock groundCoverBlock = (GroundCoverBlock)blockAbove;
                    groundCoverBlock.onSelfRemoval(world, i, j + 1, k);
                }
                world.setBlockToAir(i, j + 1, k);
            } else if (blockadd.groundCoverRestingOnVisualOffset(world, i, j + 1, k) < -0.99f && (block2Above = (blockAbove = world.getBlock(i, j + 2, k))) != null ){
                BTWBlockadd blockadd2 = (BTWBlockadd)block2Above;
                if (blockadd2.isGroundCover()) {
                    if (blockAbove instanceof GroundCoverBlock) {
                        GroundCoverBlock groundCoverBlock = (GroundCoverBlock) blockAbove;
                        groundCoverBlock.onSelfRemoval(world, i, j + 2, k);
                    }
                }
                world.setBlockToAir(i, j + 2, k);
            }
        }
    }
    
    public static boolean isGroundCoverRestingOnBlock(World world, int i, int j, int k) {
        Block blockAbove = world.getBlock(i, j + 1, k);
        BTWBlockadd blockadd = (BTWBlockadd)blockAbove;
        if (blockAbove != null) {
            Block block2Above;
            if (blockadd.isGroundCover()) {
                return true;
            }
            if (blockadd.groundCoverRestingOnVisualOffset(world, i, j + 1, k) < -0.99f && (block2Above = (blockAbove = world.getBlock(i, j + 2, k))) != null) {
                BTWBlockadd blockadd2 = (BTWBlockadd)block2Above;
                     if (blockadd2.isGroundCover()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void onSelfRemoval(World world, int i, int j, int k) {
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int iMetadata) {
        world.markBlockRangeForRenderUpdate(i, j - 1, k, i, j - 2, k);
    }



    public boolean renderBlock(RenderBlocks renderBlocks, int x, int y, int z) {

        IBlockAccess blockAccess = renderBlocks.blockAccess;
        BTWBlockadd blockadd = (BTWBlockadd)blockAccess.getBlock(x,y,z);
        if (blockAccess.getBlock(x, y - 1, z) != Blocks.air) {
            int iBlockHeight;
            float fVisualOffset = 0.0f;
            Block blockBelow = blockAccess.getBlock(x, y - 1, z);
            iBlockHeight = (blockAccess.getBlockMetadata(x, y, z) & 7) + 1;
            if (blockBelow != null && (fVisualOffset = blockadd.groundCoverRestingOnVisualOffset(blockAccess, x, y - 1, z)) < 0.0f) {
                --y;
                fVisualOffset += 1.0f;
            }
            float fHeight = 0.125f * (float)iBlockHeight;
            renderBlocks.setRenderBounds(0.0, fVisualOffset, 0.0, 1.0, fHeight + fVisualOffset, 1.0);
            RenderUtils.renderStandardBlockWithTexture(renderBlocks, this, x, y, z, this.blockIcon);
        }
        return true;
    }
    @Override
    public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k){
        return -5f;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
        if (iSide >= 2) {
            if (blockAccess.getBlock(iNeighborI, iNeighborJ, iNeighborK) == this) {
                BlockPosition thisBlockPos = new BlockPosition(iNeighborI, iNeighborJ, iNeighborK, ForgeDirection.getOrientation(iSide ^ 1));
                if (blockAccess.getBlock(thisBlockPos.x, thisBlockPos.y, thisBlockPos.z) == this) {

                     Block iBlockBelowID = blockAccess.getBlock(iNeighborI, iNeighborJ - 1, iNeighborK);
                    BTWBlockadd blockadd = (BTWBlockadd)iBlockBelowID;
                     if (blockAccess.getBlock(iNeighborI, iNeighborJ, iNeighborK).isOpaqueCube()) {
                        return false;
                    }
                    if (blockAccess.getBlockMetadata(thisBlockPos.x, thisBlockPos.y, thisBlockPos.z) <= blockAccess.getBlockMetadata(iNeighborI, iNeighborJ, iNeighborK) && iBlockBelowID != Blocks.air && blockadd.groundCoverRestingOnVisualOffset(blockAccess, iNeighborI, iNeighborJ - 1, iNeighborK) > -0.01f) {
                        return false;
                    }
                }
            }
            return true;
        }
        if (iSide == 1) {
            return true;
        }
        return super.shouldSideBeRendered(blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide);
    }

   // @Override
   // public boolean shouldRenderNeighborFullFaceSide(IBlockAccess blockAccess, int i, int j, int k, int iNeighborSide) {
   //     Block iBlockBelowID = blockAccess.getBlock(i, j - 1, k);
    //    BTWBlockadd blockadd = (BTWBlockadd)iBlockBelowID;
    //    return iNeighborSide != 1 || iBlockBelowID == Blocks.air || !(blockadd.groundCoverRestingOnVisualOffset(blockAccess, i, j - 1, k) > -0.125f);
   // }
}


