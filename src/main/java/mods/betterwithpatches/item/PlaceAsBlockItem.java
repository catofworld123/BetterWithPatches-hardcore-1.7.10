package mods.betterwithpatches.item;

import betterwithmods.util.BlockPosition;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class PlaceAsBlockItem
        extends Item {
    protected Block blockID;
    protected int blockMetadata = 0;
    protected boolean requireNoEntitiesInTargetBlock = false;

    public PlaceAsBlockItem(Item iItemID, Block iBlockID) {
        super(iItemID);
        this.blockID = iBlockID;
    }

    public PlaceAsBlockItem(Item iItemID, Block iBlockID, int iBlockMetadata) {
        this(iItemID, iBlockID);
        this.blockMetadata = iBlockMetadata;
    }

    public PlaceAsBlockItem(Item iItemID,Block iBlockID, int iBlockMetadata, String sItemName) {
        this(iItemID, iBlockID, iBlockMetadata);
        this.setUnlocalizedName(sItemName);
    }

    protected PlaceAsBlockItem(Item iItemID) {

        super(iItemID);
        this.blockID = Block.getBlockFromItem(iItemID);
        this.blockMetadata = 0;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ) {
        Block iNewBlockID = this.getBlockIDToPlace(itemStack.getItemDamage(), iFacing, fClickX, fClickY, fClickZ);
        if (itemStack.stackSize == 0 || player != null && !player.canPlayerEdit(i, j, k, iFacing, itemStack) || j == 255 && Block.blocksList[iNewBlockID].blockMaterial.isSolid()) {
            return false;
        }
        BlockPosition targetPos = new BlockPosition(i, j, k);
        Block oldBlock = world.getBlock(i, j, k);
        if (oldBlock != null) {
           // if (oldBlock.isGroundCover()) {
                iFacing = 1;
           // } else if (!oldBlock.getMaterial().isReplaceable()) {
              //  targetPos.addFacingAsOffset(iFacing);
           // }
        }
        if ((!this.requireNoEntitiesInTargetBlock || this.isTargetFreeOfObstructingEntities(world, targetPos.x, targetPos.y, targetPos.z)) && world.canPlaceEntityOnSide(iNewBlockID, targetPos.x, targetPos.y, targetPos.z, false, iFacing, player, itemStack)) {
            Block newBlock = world.getBlock(i,j,k);
            int iNewMetadata = this.getMetadata(itemStack.getItemDamage());
            iNewMetadata = newBlock.onBlockPlaced(world, targetPos.x, targetPos.y, targetPos.z, iFacing, fClickX, fClickY, fClickZ, iNewMetadata);
            if (world.setBlock(targetPos.x, targetPos.y, targetPos.z, iNewBlockID, iNewMetadata = newBlock.onBlockPlaced(world, targetPos.x, targetPos.y, targetPos.z, iNewMetadata, player))) {
                if (world.getBlock(targetPos.x, targetPos.y, targetPos.z) == newBlock) {
                    newBlock.onBlockPlacedBy(world, targetPos.x, targetPos.y, targetPos.z, player, itemStack);
                    newBlock.onPostBlockPlaced(world, targetPos.x, targetPos.y, targetPos.z, iNewMetadata);
                //    world.notifyNearbyAnimalsOfPlayerBlockAddOrRemove(player, newBlock, targetPos.x, targetPos.y, targetPos.z);
                }
                this.playPlaceSound(world, targetPos.x, targetPos.y, targetPos.z, newBlock);
                --itemStack.stackSize;
            }
            return true;
        }
        return false;
    }

    @Override
    public int getMetadata(int iItemDamage) {
        return this.blockMetadata;
    }

    @Override
    public boolean canItemBeUsedByPlayer(World world, int i, int j, int k, ForgeDirection iFacing, EntityPlayer player, ItemStack stack) {
        return this.canPlaceItemBlockOnSide(world, i, j, k, iFacing, player, stack);
    }

    @Override
    public boolean onItemUsedByBlockDispenser(ItemStack stack, World world, int i, int j, int k, ForgeDirection iFacing) {
        BlockPosition targetPos = new BlockPosition(i, j, k, iFacing);
        ForgeDirection iTargetDirection = this.getTargetFacingPlacedByBlockDispenser(iFacing);
        Block newBlock = this.getBlockIDToPlace(stack.getItemDamage(), iTargetDirection, 0.5f, 0.25f, 0.5f);
        if (newBlock != null && world.canPlaceEntityOnSide(newBlock, targetPos.x, targetPos.y, targetPos.z, true, iTargetDirection.flag, null, stack)) {
            int iBlockMetadata = this.getMetadata(stack.getItemDamage());
            iBlockMetadata = newBlock.onBlockPlaced(world, targetPos.x, targetPos.y, targetPos.z, iTargetDirection.flag, 0.5f, 0.25f, 0.5f, iBlockMetadata);
            world.setBlock(targetPos.x, targetPos.y, targetPos.z, newBlock, iBlockMetadata,2);
            newBlock.onPostBlockPlaced(world, targetPos.x, targetPos.y, targetPos.z, iBlockMetadata);
            world.playAuxSFX(2236, i, j, k, 52);
            return true;
        }
        return false;
    }

    public Block getBlockID() {
        return this.blockID;
    }

    public boolean canPlaceItemBlockOnSide(World world, int i, int j, int k, ForgeDirection iFacing, EntityPlayer player, ItemStack stack) {
        Block iTargetBlock = world.getBlock(i, j, k);
        BlockPosition targetPos = new BlockPosition(i, j, k);
       // if (iTargetBlock != null) {
       //     if (iTargetBlock.isGroundCover()) {
       //         iFacing = 1;
       //     } else if (!iTargetBlock.getMaterial().isReplaceable()) {
       //         targetPos.addFacingAsOffset(iFacing);
       //     }
      //  }
        Block iNewBlockID = this.getBlockIDToPlace(stack.getItemDamage(), iFacing, 0.5f, 0.5f, 0.5f);
        return world.canPlaceEntityOnSide(iNewBlockID, targetPos.x, targetPos.y, targetPos.z, false, iFacing.offsetZ, null, stack);
    }

    public PlaceAsBlockItem setAssociatedBlockID(Block iBlockID) {
        this.blockID = iBlockID;
        return this;
    }

    public Block getBlockIDToPlace(int iItemDamage, ForgeDirection iFacing, float fClickX, float fClickY, float fClickZ) {
        return this.getBlockID();
    }

    protected boolean isTargetFreeOfObstructingEntities(World world, int i, int j, int k) {
        AxisAlignedBB blockBounds = AxisAlignedBB.getBoundingBox(i, j, k, i + 1, j + 1, k + 1);
        return world.checkNoEntityCollision(blockBounds);
    }

    protected void playPlaceSound(World world, int i, int j, int k, Block block) {
        Block.SoundType stepSound = block.stepSound;
        world.playSoundEffect((double)i + 0.5, (double)j + 0.5, (double)k + 0.5, stepSound.getBreakSound(), (stepSound.getVolume() + 1.0f) / 2.0f, stepSound.getPitch() * 0.8f);
    }

    public ForgeDirection getTargetFacingPlacedByBlockDispenser(ForgeDirection iDispenserFacing) {
        return Block.getOppositeFacing(iDispenserFacing);
    }

}

