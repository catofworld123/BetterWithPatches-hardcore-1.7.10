package mods.betterwithpatches.block;

import betterwithmods.BWRegistry;
import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.blocks.BTWBlock;
import betterwithmods.blocks.BlockAxle;
import betterwithmods.blocks.BlockCrank;
import betterwithmods.util.BlockPosition;
import betterwithmods.util.MechanicalUtil;
import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.proxy.ClientProxy;
import mods.betterwithpatches.util.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public abstract class VesselBlock extends BlockContainer implements IMechanicalBlock {
    private static final int TICK_RATE = 1;
    public static final double COLLISION_BOX_HEIGHT = 1.0;
    public static final float MODEL_HEIGHT = 1.0f;
    public static final float MODEL_WIDTH = 0.875f;
    public static final float MODEL_HALF_WIDTH = 0.4375f;
    public static final float MODEL_BAND_HEIGHT = 0.75f;
    public static final float MODEL_BAND_HALF_HEIGHT = 0.375f;
    protected IIcon[] iconWideBandBySideArray;
    protected IIcon[] iconCenterColumnBySideArray;
    protected IIcon[] iconInteriorBySideArray;
    private boolean renderingInterior;
    private boolean renderingWideBand;
    private static final int[] rotatedFacingsAroundJCounterclockwise = new int[]{0, 1, 5, 4, 2, 3};
    private static final int[] rotatedFacingsAroundJClockwise = new int[]{0, 1, 4, 5, 3, 2};

    public VesselBlock(Material material) {
        super(material);
    }


    @Override
    public int tickRate(World world) {
        return 1;
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k) {
        super.onBlockAdded(world, i, j, k);
        world.scheduleBlockUpdate(i, j, k, this, this.tickRate(world));
    }
    @Override
    public int getRenderType() {
        return ClientProxy.renderVessel;
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
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
        return AxisAlignedBB.getBoundingBox(i, j, k, i + 1, (double)j + 1.0, (double)k + 1.0);
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random rand) {
        boolean bWasPowered = this.isMechanicalOn(world, i, j, k);
        boolean bIsPowered = false;
        int iPoweredFromFacing = 0;
        for (int iFacing = 2; iFacing <= 5; ++iFacing) {
            if (!MechanicalUtil.isBlockPoweredByAxleOnSide(world, i, j, k, ForgeDirection.getOrientation(iFacing)) && !MechanicalUtil.isPoweredByCrankOnSide(world, i, j, k,ForgeDirection.getOrientation(iFacing))) continue;
            bIsPowered = true;
            iPoweredFromFacing = iFacing;
            System.out.println(iFacing + " <-");
            this.breakPowerSourceThatOpposePoweredFacing(world, i, j, k, iPoweredFromFacing);
        }
        if (bWasPowered != bIsPowered) {
            world.playSoundEffect((double)i + 0.5, (double)j + 0.5, (double)k + 0.5, "step.gravel", 2.0f + rand.nextFloat() * 0.1f, 0.5f + rand.nextFloat() * 0.1f);
            this.setMechanicalOn(world, i, j, k, bIsPowered);
            if (!bIsPowered) {
                this.setTiltFacing(world, i, j, k, 0);
            } else {
                this.setFacingBasedOnPoweredFromFacing(world, i, j, k, iPoweredFromFacing);
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, Block iBlockID) {
        if (!world.isBlockTickScheduledThisTick(i, j, k, this)) {
            world.scheduleBlockUpdate(i, j, k, this, this.tickRate(world));
        }
    }

    public int getFacing(IBlockAccess blockAccess, int i, int j, int k) {
        int iFacing = 1;
        if (this.isMechanicalOn(blockAccess, i, j, k)) {
            iFacing = this.getTiltFacing(blockAccess, i, j, k);
        }
        return iFacing;
    }
    public boolean isBlockNormalCube(World world, int par1, int par2, int par3) {
        Block block = world.getBlock(par1, par2, par3);
        if (block != null) {
            return block.isNormalCube(world, par1, par2, par3);
        }
        return false;
    }

    public void setFacing(World world, int i, int j, int k, int iFacing) {
    }

    public int getFacing(int iMetadata) {
        return 0;
    }

    public int setFacing(int iMetadata, int iFacing) {
        return iMetadata;
    }

    public static int getOppositeFacing(int iFacing) {
        return iFacing ^ 1;
    }

    @Override
    public boolean canOutputMechanicalPower() {
        return false;
    }

    @Override
    public boolean canInputMechanicalPower() {
        return true;
    }

    @Override
    public boolean isInputtingMechPower(World world, int i, int j, int k) {
        return MechanicalUtil.isBlockPoweredByAxle(world, i, j, k, this);
    }

    @Override
    public boolean canInputPowerToSide(World world, int i, int j, int k, ForgeDirection dir) {
        int facing = this.getFacing(world, i, j, k);
        return dir != ForgeDirection.getOrientation(facing) && dir != ForgeDirection.UP;
    }

    @Override
    public boolean isOutputtingMechPower(World world, int i, int j, int k) {
        return false;
    }

    @Override
    public void overpower(World world, int i, int j, int k) {
    }

    public int getTiltFacing(IBlockAccess iBlockAccess, int i, int j, int k) {
        return (iBlockAccess.getBlockMetadata(i, j, k) & 3) + 2;
    }

    @Override
    public boolean isMechanicalOnFromMeta(int meta){
        return (meta & 4) > 0;
    };



    public void setTiltFacing(World world, int i, int j, int k, int iFacing) {
        int iFlatFacing = iFacing - 2;
        if (iFlatFacing < 0) {
            iFlatFacing = 0;
        }
        int iMetaData = world.getBlockMetadata(i, j, k) & 0xFFFFFFFC;
        world.setBlockMetadataWithNotify(i, j, k, iMetaData |= iFlatFacing & 3,2);
        world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
    }
    public static int rotateFacingAroundY(int iFacing, boolean bReverse) {
        if (bReverse) {
            return rotatedFacingsAroundJCounterclockwise[iFacing];
        }
        return rotatedFacingsAroundJClockwise[iFacing];
    }

    @Override
    public boolean isMechanicalOn(IBlockAccess world, int i, int j, int k) {
        return isMechanicalOnFromMeta(world.getBlockMetadata(i,j,k));
    }

    @Override
    public void setMechanicalOn(World world, int i, int j, int k, boolean bFlag) {
        int iMetaData = world.getBlockMetadata(i, j, k) & 0xFFFFFFFB;
        if (bFlag) {
            iMetaData |= 4;
        }
        world.setBlockMetadataWithNotify(i, j, k, iMetaData,2);
    }

    private void setFacingBasedOnPoweredFromFacing(World world, int i, int j, int k, int iPoweredFromFacing) {
        int iNewFacing = rotateFacingAroundY(iPoweredFromFacing, false);
        this.setTiltFacing(world, i, j, k, iNewFacing);
    }

    private void breakPowerSourceThatOpposePoweredFacing(World world, int i, int j, int k, int iPoweredFromFacing) {
        int iOppositePoweredFromFacing = getOppositeFacing(iPoweredFromFacing);
        for (int iFacing = 2; iFacing <= 5; ++iFacing) {
            BlockPosition tempPos;
            if (iFacing == iPoweredFromFacing) continue;
            boolean bShouldBreak = false;
            if (iFacing == iOppositePoweredFromFacing) {
                if (MechanicalUtil.isBlockPoweredByAxleOnSide(world, i, j, k, ForgeDirection.getOrientation(iFacing))) {
                    bShouldBreak = true;
                }
            } else if (MechanicalUtil.doesBlockHaveAxleOnSide(world, i, j, k, ForgeDirection.getOrientation(iFacing))) {
                bShouldBreak = true;
            }
            if (bShouldBreak) {
                tempPos = new BlockPosition(i, j, k);
                i += Facing.offsetsXForSide[iFacing];
                j += Facing.offsetsYForSide[iFacing];
                k += Facing.offsetsZForSide[iFacing];
                ((BlockAxle) BWRegistry.axle).breakAxle(world, tempPos.x, tempPos.y, tempPos.z);
            }

        }
    }

    public boolean isOpenSideBlocked(World world, int i, int j, int k) {
        int iFacing = this.getFacing(world, i, j, k);
        ForgeDirection iFacingDir = ForgeDirection.getOrientation(iFacing);
        BlockPosition targetPos = new BlockPosition(i, j, k, iFacingDir);
        return isBlockNormalCube(world, targetPos.x, targetPos.y, targetPos.z);
    }

    @Override
    public IIcon getIcon(int iSide, int iMetadata) {
        if (this.renderingInterior) {
            return this.iconInteriorBySideArray[iSide];
        }
        if (this.renderingWideBand) {
            return this.iconWideBandBySideArray[iSide];
        }
        return this.iconCenterColumnBySideArray[iSide];
    }
    @Override
    public final IIcon func_149735_b(int side, int meta)
    {
        return this.getBlockTextureFromSide(side);
    }

    public IIcon getBlockTextureFromSide(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
        int iTextureSide = iSide;
        if (this.isMechanicalOn(blockAccess, i, j, k)) {
            int iFacing = this.getTiltFacing(blockAccess, i, j, k);
            iTextureSide = iFacing == iSide ? 1 : (iSide == getOppositeFacing(iFacing) ? 0 : 2);
        }
        return this.getIcon(iTextureSide, 0);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        this.iconInteriorBySideArray = new IIcon[6];
        this.iconCenterColumnBySideArray = new IIcon[6];
        this.iconWideBandBySideArray = new IIcon[6];
    }

    @Override
    public void randomDisplayTick(World world, int i, int j, int k, Random random) {
        Block iBlockUnderID;
        if (!(this.isMechanicalOn(world, i, j, k) || (iBlockUnderID = world.getBlock(i, j - 1, k)) != Blocks.fire && iBlockUnderID != BWRegistry.stokedFlame)) {
            for (int counter = 0; counter < 1; ++counter) {
                float smokeX = (float)i + random.nextFloat();
                float smokeY = (float)j + random.nextFloat() * 0.5f + 1.0f;
                float smokeZ = (float)k + random.nextFloat();
                world.spawnParticle("fcwhitesmoke", smokeX, smokeY, smokeZ, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
        return true;
    }

    public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
        IBlockAccess blockAccess = renderBlocks.blockAccess;
        int iFacing = this.getFacing(blockAccess, i, j, k);
        Tessellator tesselator = Tessellator.instance;
        RenderUtils.setRenderBoundsToBlockFacing(renderBlocks, iFacing, 0.0625f, 0.0f, 0.0625f, 0.9375f, 1.0f, 0.9375f);
        RenderUtils.setTextureRotationBasedOnBlockFacing(renderBlocks, iFacing);
        tesselator.setBrightness(this.getMixedBrightnessForBlock(blockAccess, i, j, k));
        float fInteriorBrightnessMultiplier = 0.66f;
        int iColorMultiplier = this.colorMultiplier(blockAccess, i, j, k);
        float iColorRed = (float)(iColorMultiplier >> 16 & 0xFF) / 255.0f;
        float iColorGreen = (float)(iColorMultiplier >> 8 & 0xFF) / 255.0f;
        float iColorBlue = (float)(iColorMultiplier & 0xFF) / 255.0f;
        tesselator.setColorOpaque_F(0.66f * iColorRed, 0.66f * iColorGreen, 0.66f * iColorBlue);
        double dInteriorOffset = 0.249;
        renderBlocks.renderFaceXPos(this, (double)i - 1.0 + 0.249, j, k, this.getBlockTextureFromSide(blockAccess, i, j, k, 4));
        renderBlocks.renderFaceXNeg(this, (double)i + 1.0 - 0.249, j, k, this.getBlockTextureFromSide(blockAccess, i, j, k, 5));
        renderBlocks.renderFaceZPos(this, i, j, (double)k - 1.0 + 0.249, this.getBlockTextureFromSide(blockAccess, i, j, k, 2));
        renderBlocks.renderFaceZNeg(this, i, j, (double)k + 1.0 - 0.249, this.getBlockTextureFromSide(blockAccess, i, j, k, 3));
        renderBlocks.renderFaceYPos(this, i, (double)((float)j - 1.0f) + 0.249, k, this.getBlockTextureFromSide(blockAccess, i, j, k, 0));
        renderBlocks.renderFaceYNeg(this, i, (double)((float)j + 1.0f) - 0.249, k, this.getBlockTextureFromSide(blockAccess, i, j, k, 1));
        RenderUtils.clearUVRotation();
        return true;
    }

    public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
        this.renderingInterior = false;
        this.renderingWideBand = true;
        renderBlocks.setRenderBounds(0.0, 0.125, 0.0, 1.0, 0.875, 1.0);
        RenderUtils.renderInvBlockWithMetadata(renderBlocks, this, -0.5f, -0.5f, -0.5f, 0);
        this.renderingWideBand = false;
        renderBlocks.setRenderBounds(0.0625, 0.0, 0.0625, 0.9375, 1.0, 0.9375);
        RenderUtils.renderInvBlockWithMetadata(renderBlocks, this, -0.5f, -0.5f, -0.5f, 0);
        this.renderingInterior = true;
        renderBlocks.setRenderBounds(0.0625, 0.0, 0.0625, 0.9375, 1.0, 0.9375);
        Tessellator tessellator = Tessellator.instance;
        GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
        double dInteriorOffset = 0.249;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        renderBlocks.renderFaceYPos(this, 0.0, -0.751, 0.0, this.getIcon(0, 0));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 0.0f, -1.0f);
        renderBlocks.renderFaceZNeg(this, 0.0, 0.0, 0.751, this.getIcon(3, 0));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 0.0f, 1.0f);
        renderBlocks.renderFaceZPos(this, 0.0, 0.0, -0.751, this.getIcon(2, 0));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0f, 0.0f, 0.0f);
        renderBlocks.renderFaceXNeg(this, 0.751, 0.0, 0.0, this.getIcon(5, 0));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0f, 0.0f, 0.0f);
        renderBlocks.renderFaceXPos(this, -0.751, 0.0, 0.0, this.getIcon(4, 0));
        tessellator.draw();
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);
    }
}


