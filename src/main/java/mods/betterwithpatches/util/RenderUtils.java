package mods.betterwithpatches.util;

import betterwithmods.BWRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;


public class RenderUtils {
    public static void renderStandardBlockWithTexture(RenderBlocks renderBlocks, Block block, int i, int j, int k, IIcon texture) {
        boolean bHasOverride = renderBlocks.hasOverrideBlockTexture();
        if (!bHasOverride) {
            renderBlocks.setOverrideBlockTexture(texture);
        }
        renderBlocks.renderStandardBlock(block, i, j, k);
        if (!bHasOverride) {
            renderBlocks.clearOverrideBlockTexture();
        }
    }
    public static void setUVRotateBottom(int iValue) {
        RenderBlocks renderBlocks = new RenderBlocks();
        renderBlocks.uvRotateBottom = iValue;
    }
    public static void setUVRotateEast(int iValue) {
        RenderBlocks renderBlocks = new RenderBlocks();
        renderBlocks.uvRotateEast = iValue;
    }

    public static void setUVRotateWest(int iValue) {
        RenderBlocks renderBlocks = new RenderBlocks();
        renderBlocks.uvRotateWest = iValue;
    }

    public static void setUVRotateSouth(int iValue) {
        RenderBlocks renderBlocks = new RenderBlocks();
        renderBlocks.uvRotateSouth = iValue;
    }

    public static void setUVRotateNorth(int iValue) {
        RenderBlocks renderBlocks = new RenderBlocks();
        renderBlocks.uvRotateNorth = iValue;
    }

    public static void setUVRotateTop(int iValue) {
        RenderBlocks renderBlocks = new RenderBlocks();
        renderBlocks.uvRotateTop = iValue;
    }
    public static void clearUVRotation() {
        RenderBlocks renderBlocks = new RenderBlocks();
        renderBlocks.uvRotateEast = 0;
        renderBlocks.uvRotateWest = 0;
        renderBlocks.uvRotateSouth = 0;
        renderBlocks.uvRotateNorth = 0;
        renderBlocks.uvRotateTop = 0;
        renderBlocks.uvRotateBottom = 0;
    }


    public static void renderBlockWithBoundsAndTextureRotation(RenderBlocks renderBlocks, Block block, int i, int j, int k, int iFacing, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        RenderUtils.setRenderBoundsToBlockFacing(renderBlocks, iFacing, minX, minY, minZ, maxX, maxY, maxZ);
        RenderUtils.renderBlockWithTextureRotation(renderBlocks, block, i, j, k, iFacing);
    }

    public static void setRenderBoundsWithAxisAlignment(RenderBlocks renderBlocks, float fMinMinor, float fMinY, float fMinMajor, float fMaxMinor, float fMaxY, float fMaxMajor, boolean bIAligned) {
        if (!bIAligned) {
            renderBlocks.setRenderBounds(fMinMinor, fMinY, fMinMajor, fMaxMinor, fMaxY, fMaxMajor);
        } else {
            renderBlocks.setRenderBounds(fMinMajor, fMinY, fMinMinor, fMaxMajor, fMaxY, fMaxMinor);
        }
    }

    public static void setRenderBoundsToBlockFacing(RenderBlocks renderBlocks, int iBlockFacing, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        float newMinX = minX;
        float newMinY = minY;
        float newMinZ = minZ;
        float newMaxX = maxX;
        float newMaxY = maxY;
        float newMaxZ = maxZ;
        switch (iBlockFacing) {
            case 0: {
                newMinY = 1.0f - maxY;
                newMaxY = 1.0f - minY;
                break;
            }
            case 2: {
                newMinZ = 1.0f - maxY;
                newMaxZ = 1.0f - minY;
                newMinY = minZ;
                newMaxY = maxZ;
                break;
            }
            case 3: {
                newMinZ = minY;
                newMaxZ = maxY;
                newMinY = 1.0f - maxZ;
                newMaxY = 1.0f - minZ;
                break;
            }
            case 4: {
                newMinX = 1.0f - maxY;
                newMaxX = 1.0f - minY;
                newMinY = minX;
                newMaxY = maxX;
                break;
            }
            case 5: {
                newMinX = minY;
                newMaxX = maxY;
                newMinY = 1.0f - maxX;
                newMaxY = 1.0f - minX;
            }
        }
        renderBlocks.setRenderBounds(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    public static void renderBlockWithTextureRotation(RenderBlocks renderBlocks, Block block, int i, int j, int k, int iBlockFacing) {
        RenderUtils.setTextureRotationBasedOnBlockFacing(renderBlocks, iBlockFacing);
        renderBlocks.renderStandardBlock(block, i, j, k);
        clearUVRotation();
    }

    public static void setTextureRotationBasedOnBlockFacing(RenderBlocks renderBlocks, int iBlockFacing) {
        switch (iBlockFacing) {
            case 0: {
                setUVRotateEast(3);
                setUVRotateWest(3);
                setUVRotateSouth(3);
                setUVRotateNorth(3);
                break;
            }
            case 2: {
                setUVRotateSouth(1);
                setUVRotateNorth(2);
                setUVRotateEast(3);
                setUVRotateWest(3);
                break;
            }
            case 3: {
                setUVRotateSouth(2);
                setUVRotateNorth(1);
                setUVRotateTop(3);
                setUVRotateBottom(3);
                break;
            }
            case 4: {
                setUVRotateEast(1);
                setUVRotateWest(2);
                setUVRotateTop(2);
                setUVRotateBottom(1);
                setUVRotateNorth(2);
                setUVRotateSouth(1);
                break;
            }
            case 5: {
                setUVRotateEast(2);
                setUVRotateWest(1);
                setUVRotateTop(1);
                setUVRotateBottom(2);
                setUVRotateSouth(1);
                setUVRotateNorth(2);
            }
        }
    }

    public static void renderBlockInteriorWithBoundsAndTextureRotation(RenderBlocks renderBlocks, Block block, int i, int j, int k, int iFacing, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        RenderUtils.setRenderBoundsToBlockFacing(renderBlocks, iFacing, minX, minY, minZ, maxX, maxY, maxZ);
        RenderUtils.setTextureRotationBasedOnBlockFacing(renderBlocks, iFacing);
        RenderUtils.renderBlockWithTextureRotation(renderBlocks, block, i, j, k, iFacing);
        clearUVRotation();
    }

    public static void renderMovingBlockWithTexture(RenderBlocks renderBlocks, Block block, World world, int i, int j, int k, IIcon texture) {
        float f = 0.5f;
        float f1 = 1.0f;
        float f2 = 0.8f;
        float f3 = 0.6f;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, i, j, k));
        float f4 = 1.0f;
        float f5 = 1.0f;
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.setColorOpaque_F(f * f5, f * f5, f * f5);
        renderBlocks.renderFaceYNeg(block, -0.5, -0.5, -0.5, texture);
        f5 = 1.0f;
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.setColorOpaque_F(f1 * f5, f1 * f5, f1 * f5);
        renderBlocks.renderFaceYPos(block, -0.5, -0.5, -0.5, texture);
        f5 = 1.0f;
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.setColorOpaque_F(f2 * f5, f2 * f5, f2 * f5);
        renderBlocks.renderFaceZNeg(block, -0.5, -0.5, -0.5, texture);
        f5 = 1.0f;
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.setColorOpaque_F(f2 * f5, f2 * f5, f2 * f5);
        renderBlocks.renderFaceZPos(block, -0.5, -0.5, -0.5, texture);
        f5 = 1.0f;
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.setColorOpaque_F(f3 * f5, f3 * f5, f3 * f5);
        renderBlocks.renderFaceXNeg(block, -0.5, -0.5, -0.5, texture);
        f5 = 1.0f;
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.setColorOpaque_F(f3 * f5, f3 * f5, f3 * f5);
        renderBlocks.renderFaceXPos(block, -0.5, -0.5, -0.5, texture);
        tessellator.draw();
    }

    public static void renderMovingBlock(RenderBlocks renderBlocks, Block block, World world, int i, int j, int k) {
        RenderUtils.renderMovingBlockWithMetadata(renderBlocks, block, world, i, j, k, 0);
    }

    public static void renderMovingBlockWithMetadata(RenderBlocks renderBlocks, Block block, IBlockAccess blockAccess, int i, int j, int k, int iMetadata) {
        float f = 0.5f;
        float f1 = 1.0f;
        float f2 = 0.8f;
        float f3 = 0.6f;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j, k));
        float f4 = 1.0f;
        float f5 = 1.0f;
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.setColorOpaque_F(f * f5, f * f5, f * f5);
        renderBlocks.renderFaceYNeg(block, -0.5, -0.5, -0.5, block.getIcon(0, iMetadata));
        f5 = 1.0f;
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.setColorOpaque_F(f1 * f5, f1 * f5, f1 * f5);
        renderBlocks.renderFaceYPos(block, -0.5, -0.5, -0.5, block.getIcon(1, iMetadata));
        f5 = 1.0f;
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.setColorOpaque_F(f2 * f5, f2 * f5, f2 * f5);
        renderBlocks.renderFaceZNeg(block, -0.5, -0.5, -0.5, block.getIcon(2, iMetadata));
        f5 = 1.0f;
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.setColorOpaque_F(f2 * f5, f2 * f5, f2 * f5);
        renderBlocks.renderFaceZPos(block, -0.5, -0.5, -0.5, block.getIcon(3, iMetadata));
        f5 = 1.0f;
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.setColorOpaque_F(f3 * f5, f3 * f5, f3 * f5);
        renderBlocks.renderFaceXNeg(block, -0.5, -0.5, -0.5, block.getIcon(4, iMetadata));
        f5 = 1.0f;
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.setColorOpaque_F(f3 * f5, f3 * f5, f3 * f5);
        renderBlocks.renderFaceXPos(block, -0.5, -0.5, -0.5, block.getIcon(5, iMetadata));
        tessellator.draw();
    }

    public static void renderInvBlockWithTexture(RenderBlocks renderBlocks, Block block, float fXOffset, float fYOffset, float fZOffset, IIcon texture) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glTranslatef(fXOffset, fYOffset, fZOffset);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, -1.0f, 0.0f);
        renderBlocks.renderFaceYNeg(block, 0.0, 0.0, 0.0, texture);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        renderBlocks.renderFaceYPos(block, 0.0, 0.0, 0.0, texture);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 0.0f, -1.0f);
        renderBlocks.renderFaceZNeg(block, 0.0, 0.0, 0.0, texture);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 0.0f, 1.0f);
        renderBlocks.renderFaceZPos(block, 0.0, 0.0, 0.0, texture);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0f, 0.0f, 0.0f);
        renderBlocks.renderFaceXNeg(block, 0.0, 0.0, 0.0, texture);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0f, 0.0f, 0.0f);
        renderBlocks.renderFaceXPos(block, 0.0, 0.0, 0.0, texture);
        tessellator.draw();
        GL11.glTranslatef(-fXOffset, -fYOffset, -fZOffset);
    }

    public static void renderInvBlockWithMetadata(RenderBlocks renderBlocks, Block block, float fXOffset, float fYOffset, float fZOffset, int iMetaData) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glTranslatef(fXOffset, fYOffset, fZOffset);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, -1.0f, 0.0f);
        renderBlocks.renderFaceYNeg(block, 0.0, 0.0, 0.0, block.getIcon(0, iMetaData));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        renderBlocks.renderFaceYPos(block, 0.0, 0.0, 0.0, block.getIcon(1, iMetaData));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 0.0f, -1.0f);
        renderBlocks.renderFaceZNeg(block, 0.0, 0.0, 0.0, block.getIcon(2, iMetaData));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 0.0f, 1.0f);
        renderBlocks.renderFaceZPos(block, 0.0, 0.0, 0.0, block.getIcon(3, iMetaData));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0f, 0.0f, 0.0f);
        renderBlocks.renderFaceXNeg(block, 0.0, 0.0, 0.0, block.getIcon(4, iMetaData));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0f, 0.0f, 0.0f);
        renderBlocks.renderFaceXPos(block, 0.0, 0.0, 0.0, block.getIcon(5, iMetaData));
        tessellator.draw();
        GL11.glTranslatef(-fXOffset, -fYOffset, -fZOffset);
    }

    public static boolean renderBlockFullBrightWithTexture(RenderBlocks renderBlocks, IBlockAccess blockAccess, int i, int j, int k, IIcon texture) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
        tessellator.setBrightness(blockAccess.getLightBrightnessForSkyBlocks(i, j, k, 15));
        renderBlocks.renderFaceYNeg(null, i, j, k, texture);
        tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
        tessellator.setBrightness(blockAccess.getLightBrightnessForSkyBlocks(i, j, k, 15));
        renderBlocks.renderFaceYPos(null, i, j, k, texture);
        tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
        tessellator.setBrightness(blockAccess.getLightBrightnessForSkyBlocks(i, j, k, 15));
        renderBlocks.renderFaceZNeg(null, i, j, k, texture);
        tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
        tessellator.setBrightness(blockAccess.getLightBrightnessForSkyBlocks(i, j, k, 15));
        renderBlocks.renderFaceZPos(null, i, j, k, texture);
        tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
        tessellator.setBrightness(blockAccess.getLightBrightnessForSkyBlocks(i, j, k, 15));
        renderBlocks.renderFaceXNeg(null, i, j, k, texture);
        tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
        tessellator.setBrightness(blockAccess.getLightBrightnessForSkyBlocks(i, j, k, 15));
        renderBlocks.renderFaceXPos(null, i, j, k, texture);
        return true;
    }

    public static void renderInvBlockFullBrightWithTexture(RenderBlocks renderBlocks, Block block, float fXOffset, float fYOffset, float fZOffset, IIcon texture) {
        IBlockAccess blockAccess = renderBlocks.blockAccess;
        Tessellator tessellator = Tessellator.instance;
        GL11.glTranslatef(fXOffset, fYOffset, fZOffset);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, -1.0f, 0.0f);
        tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
        tessellator.setBrightness(240);
        renderBlocks.renderFaceYNeg(block, 0.0, 0.0, 0.0, texture);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
        tessellator.setBrightness(240);
        renderBlocks.renderFaceYPos(block, 0.0, 0.0, 0.0, texture);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 0.0f, -1.0f);
        tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
        tessellator.setBrightness(240);
        renderBlocks.renderFaceZNeg(block, 0.0, 0.0, 0.0, texture);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 0.0f, 1.0f);
        tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
        tessellator.setBrightness(240);
        renderBlocks.renderFaceZPos(block, 0.0, 0.0, 0.0, texture);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0f, 0.0f, 0.0f);
        tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
        tessellator.setBrightness(240);
        renderBlocks.renderFaceXNeg(block, 0.0, 0.0, 0.0, texture);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0f, 0.0f, 0.0f);
        tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
        tessellator.setBrightness(240);
        renderBlocks.renderFaceXPos(block, 0.0, 0.0, 0.0, texture);
        tessellator.draw();
        GL11.glTranslatef(-fXOffset, -fYOffset, -fZOffset);
    }

    public static boolean shouldRenderNeighborFullFaceSide(IBlockAccess blockAccess, int i, int j, int k, int iNeighborSide) {
        Block block = blockAccess.getBlock(i, j, k);
        if (block != null) {
            return !block.isOpaqueCube();
        }
        return true;
    }

    public static boolean shouldRenderNeighborHalfSlabSide(IBlockAccess blockAccess, int i, int j, int k, int iNeighborSlabSide, boolean bNeighborUpsideDown) {
        Block block = blockAccess.getBlock(i, j, k);
        if (block != null) {
            return!block.isOpaqueCube();
        }
        return true;
    }

    public static void drawSecondaryCraftingOutputIndicator(Minecraft mc, int iDisplayX, int iDisplayY) {
        GL11.glPushMatrix();
        GL11.glDisable(2896);
        GL11.glEnable(32826);
        GL11.glEnable(2903);
        String sIndicator = "+";
        mc.fontRenderer.drawString(sIndicator, iDisplayX + 1, iDisplayY, 0);
        mc.fontRenderer.drawString(sIndicator, iDisplayX - 1, iDisplayY, 0);
        mc.fontRenderer.drawString(sIndicator, iDisplayX, iDisplayY + 1, 0);
        mc.fontRenderer.drawString(sIndicator, iDisplayX, iDisplayY - 1, 0);
        mc.fontRenderer.drawString(sIndicator, iDisplayX, iDisplayY, 8453920);
        GL11.glPopMatrix();
        GL11.glEnable(2896);
        GL11.glEnable(2929);
    }
}


