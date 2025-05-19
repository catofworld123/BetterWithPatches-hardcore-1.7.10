package mods.betterwithpatches.block;

import betterwithmods.util.BlockPosition;
import mods.betterwithpatches.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public abstract class AttachedSlabBlock extends SlabBlock {
    protected AttachedSlabBlock(int iBlockID, Material material) {
        super(material);
    }

    public boolean canPlaceBlockOnSide(World world, int i, int j, int k, int iSide) {
        if (iSide != 0 && iSide != 1) {
            if (this.hasValidAnchorToFacing(world, i, j, k, 0) || this.hasValidAnchorToFacing(world, i, j, k, 1)) {
                return super.canPlaceBlockOnSide(world, i, j, k, iSide);
            }
        } else if (this.hasValidAnchorToFacing(world, i, j, k, Block.getOppositeFacing(iSide))) {
            return super.canPlaceBlockOnSide(world, i, j, k, iSide);
        }

        return false;
    }

    public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
        if (iFacing == 0) {
            iMetadata = this.setIsUpsideDown(iMetadata, true);
        } else if (iFacing != 1) {
            if ((double)fClickY > (double)0.5F) {
                if (this.hasValidAnchorToFacing(world, i, j, k, 1)) {
                    iMetadata = this.setIsUpsideDown(iMetadata, true);
                }
            } else if (!this.hasValidAnchorToFacing(world, i, j, k, 0)) {
                iMetadata = this.setIsUpsideDown(iMetadata, true);
            }
        }

        return iMetadata;
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
        int iAnchorSide = 0;
        if (this.getIsUpsideDown(world.getBlockMetadata(i, j, k))) {
            iAnchorSide = 1;
        }

        if (!this.hasValidAnchorToFacing(world, i, j, k, iAnchorSide)) {
            this.onAnchorBlockLost(world, i, j, k);
        }

    }

    public boolean isBreakableBarricade(World world, int i, int j, int k) {
        return false;

       // return world.getDifficulty().canZombiesBreakBlocks();
    }

    protected boolean hasValidAnchorToFacing(World world, int i, int j, int k, int iFacing) {
        BlockPosition attachedPos = new BlockPosition(i, j, k, iFacing);
        return world.isSideSolid(world, attachedPos.x, attachedPos.y, attachedPos.z, Block.getOppositeFacing(iFacing), true);
    }

    protected abstract void onAnchorBlockLost(World var1, int var2, int var3, int var4);
}
