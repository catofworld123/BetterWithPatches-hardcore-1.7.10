package mods.betterwithpatches.util;

import betterwithmods.BWRegistry;
import betterwithmods.blocks.BlockAxle;
import betterwithmods.blocks.BlockGearbox;
import betterwithmods.util.BlockPosition;
import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.block.MechanicalBlock;
import net.minecraft.block.Block;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class MechPowerUtils {
    public static boolean isBlockPoweredByAxleToSide(World world, int i, int j, int k, int iSide) {
        BlockAxle axleBlock;
        BlockPosition targetPos = new BlockPosition(i, j, k);

        i += Facing.offsetsXForSide[iSide];
        j += Facing.offsetsYForSide[iSide];
        k += Facing.offsetsZForSide[iSide];

        ForgeDirection iSideDir = ForgeDirection.getOrientation(iSide);
        Block iTargetBlockID = world.getBlock(targetPos.x, targetPos.y, targetPos.z);
        return MechPowerUtils.isBlockIDAxle(iTargetBlockID) && (axleBlock = (BlockAxle) world.getBlock(i,j,k)).isAxleOrientedToFacing(world, targetPos.x, targetPos.y, targetPos.z, iSideDir) && axleBlock.getPowerLevel(world, targetPos.x, targetPos.y, targetPos.z) > 0;
    }

    public static boolean isBlockIDAxle(Block iBlockID) {
        return iBlockID == BWRegistry.axle;
    }

    public static boolean doesBlockHaveFacingAxleToSide(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
        BlockAxle axleBlock;
        BlockPosition targetPos = new BlockPosition(i, j, k);

        i += Facing.offsetsXForSide[iSide];
        j += Facing.offsetsYForSide[iSide];
        k += Facing.offsetsZForSide[iSide];

        ForgeDirection iSideDir = ForgeDirection.getOrientation(iSide);
        Block iTargetBlockID = blockAccess.getBlock(targetPos.x, targetPos.y, targetPos.z);
        return MechPowerUtils.isBlockIDAxle(iTargetBlockID) && (axleBlock = (BlockAxle) blockAccess.getBlock(i,j,k)).isAxleOrientedToFacing(blockAccess, targetPos.x, targetPos.y, targetPos.z, iSideDir);
    }

    public static boolean doesBlockHaveAnyFacingAxles(IBlockAccess blockAccess, int i, int j, int k) {
        for (int iFacing = 0; iFacing <= 5; ++iFacing) {
            if (!MechPowerUtils.doesBlockHaveFacingAxleToSide(blockAccess, i, j, k, iFacing)) continue;
            return true;
        }
        return false;
    }

    public static boolean isBlockPoweredByHandCrank(World world, int i, int j, int k) {
        for (int iFacing = 1; iFacing <= 5; ++iFacing) {
            if (!MechPowerUtils.isBlockPoweredByHandCrankToSide(world, i, j, k, iFacing)) continue;
            return true;
        }
        return false;
    }

    public static boolean isBlockPoweredByHandCrankToSide(World world, int i, int j, int k, int iSide) {
        Block targetBlock;
        MechanicalBlock device;
        BlockPosition targetPos = new BlockPosition(i, j, k);

        i += Facing.offsetsXForSide[iSide];
        j += Facing.offsetsYForSide[iSide];
        k += Facing.offsetsZForSide[iSide];

        Block iTargetBlockID = world.getBlock(targetPos.x, targetPos.y, targetPos.z);
        return iTargetBlockID == BWRegistry.handCrank && (device = (MechanicalBlock)((Object)(targetBlock = world.getBlock(i,j,k)))).isOutputtingMechanicalPower(world, targetPos.x, targetPos.y, targetPos.z);
    }

    public static boolean isBlockPoweredByAxle(World world, int i, int j, int k, MechanicalBlock block) {
        for (int iFacing = 0; iFacing <= 5; ++iFacing) {
            if (!block.canInputAxlePowerToFacing(world, i, j, k, iFacing) || !MechPowerUtils.isBlockPoweredByAxleToSide(world, i, j, k, iFacing)) continue;
            return true;
        }
        return false;
    }

    public static void destroyHorizontallyAttachedAxles(World world, int i, int j, int k) {
        for (int iFacing = 2; iFacing <= 5; ++iFacing) {
            BlockAxle axleBlock;
            BlockPosition tempPos = new BlockPosition(i, j, k);

            i += Facing.offsetsXForSide[iFacing];
            j += Facing.offsetsYForSide[iFacing];
            k += Facing.offsetsZForSide[iFacing];

            ForgeDirection ForgeiFacing = ForgeDirection.getOrientation(iFacing);
            Block iTempBlockID = world.getBlock(tempPos.x, tempPos.y, tempPos.z);
            if (!MechPowerUtils.isBlockIDAxle(iTempBlockID) || !(axleBlock = (BlockAxle) world.getBlock(i,j,k)).isAxleOrientedToFacing(world, tempPos.x, tempPos.y, tempPos.z, ForgeiFacing)) continue;
            axleBlock.breakAxle(world, tempPos.x, tempPos.y, tempPos.z);
        }
    }

    public static boolean isPoweredGearBox(IBlockAccess blockAccess, int i, int j, int k) {
        Block iTempBlockID = blockAccess.getBlock(i, j, k);
        if (iTempBlockID == BWRegistry.gearbox || iTempBlockID == BWRegistry.gearbox) { //BWPRegistry.clutch
            BlockGearbox gearBlock = (BlockGearbox) blockAccess.getBlock(i,j,k);
            return gearBlock.isGearboxOn(blockAccess, i, j, k);
        }
        return false;
    }
}

