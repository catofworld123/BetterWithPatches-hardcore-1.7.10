package mods.betterwithpatches.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import mods.betterwithpatches.block.Campfire;

import static betterwithmods.client.RenderTileEntities.renderItemBlock;

public class RenderCampfire implements ISimpleBlockRenderingHandler {
    static final double[] fireAnimationScaleArray = new double[] {0D, 0.25D, 0.5D, 0.875D };

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        renderer.setRenderBounds(0.125, 0D, 0.125, 0.875, 0.25, 0.875);
        renderItemBlock(block, renderer, metadata);
        renderer.setRenderBounds(0.0625, 0, 0, 0.1875, 0.125, 1);
        renderItemBlock(block, renderer, metadata);
        renderer.setRenderBounds(0.8125, 0, 0, 0.9375, 0.125, 1);
        renderItemBlock(block, renderer, metadata);
        renderer.setRenderBounds(0, 0.125, 0.75, 1, 0.250, 0.875);
        renderItemBlock(block, renderer, metadata);
        renderer.setRenderBounds(0, 0.125, 0.125, 1, 0.250, 0.25);
        renderItemBlock(block, renderer, metadata);
        renderer.setRenderBounds(0.1875, 0.25, 0.0625, 0.3125, 0.375, 0.9375);
        renderItemBlock(block, renderer, metadata);
        renderer.setRenderBounds(0.6875, 0.25, 0.0625, 0.8125, 0.375, 0.9375);
        renderItemBlock(block, renderer, metadata);
        renderer.setRenderBounds(0.1875, 0.375, 0.1875, 0.8125, 0.5, 0.3125);
        renderItemBlock(block, renderer, metadata);
        renderer.setRenderBounds(0.1875, 0.375, 0.6875, 0.8125, 0.5, 0.8125);
        renderItemBlock(block, renderer, metadata);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        renderer.renderAllFaces = true;
        renderer.setRenderBounds(0.125, 0D, 0.125, 0.875, 0.25, 0.875);
        renderer.setRenderBounds(0.0625, 0, 0, 0.1875, 0.125, 1);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0.8125, 0, 0, 0.9375, 0.125, 1);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0, 0.125, 0.75, 1, 0.250, 0.875);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0, 0.125, 0.125, 1, 0.250, 0.25);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0.1875, 0.25, 0.0625, 0.3125, 0.375, 0.9375);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0.6875, 0.25, 0.0625, 0.8125, 0.375, 0.9375);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0.125, 0.375, 0.1875, 0.875, 0.5, 0.3125);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0.125, 0.375, 0.6875, 0.875, 0.5, 0.8125);
        renderer.renderStandardBlock(block, x, y, z);
        ///
        IBlockAccess blockAccess = RenderBlocks.getInstance().blockAccess;

        Tessellator tesselator = Tessellator.instance;

        if (world.getBlockMetadata(x, y, z) == 2) {

            double dScale = fireAnimationScaleArray[1];

            double dI = (double) x;
            double dJ = (double) y;
            double dK = (double) z;

            IIcon fireTexture1 = Blocks.fire.getFireIcon(0);
            IIcon fireTexture2 = Blocks.fire.getFireIcon(1);

            if (((x + z) & 1) != 0) {
                fireTexture1 = Blocks.fire.getFireIcon(1);
                fireTexture2 = Blocks.fire.getFireIcon(0);
            }

            tesselator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
            tesselator.setBrightness(100);

            double dMinU = fireTexture1.getMinU();
            double dMinV = fireTexture1.getMinV();
            double dMaxU = fireTexture1.getMaxU();
            double dMaxV = fireTexture1.getMaxV();

            double dFireHeight = 1.4D * dScale;
            double dHorizontalMin = 0.5D - (0.5D * dScale);
            double dHorizontalMax = 0.5D + (0.5D * dScale);

            double dOffset = 0.2D * dScale;

            double var18 = dI + 0.5D + dOffset;
            double var20 = dI + 0.5D - dOffset;
            double var22 = dK + 0.5D + dOffset;
            double var24 = dK + 0.5D - dOffset;

            dOffset = 0.3D * dScale;

            double var26 = dI + 0.5D - dOffset;
            double var28 = dI + 0.5D + dOffset;
            double var30 = dK + 0.5D - dOffset;
            double var32 = dK + 0.5D + dOffset;

            tesselator.addVertexWithUV(var26, (dJ + dFireHeight), (dK + dHorizontalMax), dMaxU, dMinV);
            tesselator.addVertexWithUV(var18, (dJ + 0), (dK + dHorizontalMax), dMaxU, dMaxV);
            tesselator.addVertexWithUV(var18, (dJ + 0), (dK + dHorizontalMin), dMinU, dMaxV);
            tesselator.addVertexWithUV(var26, (dJ + dFireHeight), (dK + dHorizontalMin), dMinU, dMinV);

            tesselator.addVertexWithUV(var28, (dJ + dFireHeight), (dK + dHorizontalMin), dMaxU, dMinV);
            tesselator.addVertexWithUV(var20, (dJ + 0), (dK + dHorizontalMin), dMaxU, dMaxV);
            tesselator.addVertexWithUV(var20, (dJ + 0), (dK + dHorizontalMax), dMinU, dMaxV);
            tesselator.addVertexWithUV(var28, (dJ + dFireHeight), (dK + dHorizontalMax), dMinU, dMinV);

            dMinU = fireTexture2.getMinU();
            dMinV = fireTexture2.getMinV();
            dMaxU = fireTexture2.getMaxU();
            dMaxV = fireTexture2.getMaxV();

            tesselator.addVertexWithUV((dI + dHorizontalMax), (dJ + dFireHeight), var32, dMaxU, dMinV);
            tesselator.addVertexWithUV((dI + dHorizontalMax), (dJ + 0), var24, dMaxU, dMaxV);
            tesselator.addVertexWithUV((dI + dHorizontalMin), (dJ + 0), var24, dMinU, dMaxV);
            tesselator.addVertexWithUV((dI + dHorizontalMin), (dJ + dFireHeight), var32, dMinU, dMinV);

            tesselator.addVertexWithUV((dI + dHorizontalMin), (dJ + dFireHeight), var30, dMaxU, dMinV);
            tesselator.addVertexWithUV((dI + dHorizontalMin), (dJ + 0), var22, dMaxU, dMaxV);
            tesselator.addVertexWithUV((dI + dHorizontalMax), (dJ + 0), var22, dMinU, dMaxV);
            tesselator.addVertexWithUV((dI + dHorizontalMax), (dJ + dFireHeight), var30, dMinU, dMinV);

            dOffset = 0.5D * dScale;

            var18 = dI + 0.5D - dOffset;
            var20 = dI + 0.5D + dOffset;
            var22 = dK + 0.5D - dOffset;
            var24 = dK + 0.5D + dOffset;

            dOffset = 0.4D * dScale;

            var26 = dI + 0.5D - dOffset;
            var28 = dI + 0.5D + dOffset;
            var30 = dK + 0.5D - dOffset;
            var32 = dK + 0.5D + dOffset;

            tesselator.addVertexWithUV(var26, (dJ + dFireHeight), (dK + dHorizontalMin), dMinU, dMinV);
            tesselator.addVertexWithUV(var18, (dJ + 0), (dK + dHorizontalMin), dMinU, dMaxV);
            tesselator.addVertexWithUV(var18, (dJ + 0), (dK + dHorizontalMax), dMaxU, dMaxV);
            tesselator.addVertexWithUV(var26, (dJ + dFireHeight), (dK + dHorizontalMax), dMaxU, dMinV);

            tesselator.addVertexWithUV(var28, (dJ + dFireHeight), (dK + dHorizontalMax), dMinU, dMinV);
            tesselator.addVertexWithUV(var20, (dJ + 0), (dK + dHorizontalMax), dMinU, dMaxV);
            tesselator.addVertexWithUV(var20, (dJ + 0), (dK + dHorizontalMin), dMaxU, dMaxV);
            tesselator.addVertexWithUV(var28, (dJ + dFireHeight), (dK + dHorizontalMin), dMaxU, dMinV);

            dMinU = fireTexture1.getMinU();
            dMinV = fireTexture1.getMinV();
            dMaxU = fireTexture1.getMaxU();
            dMaxV = fireTexture1.getMaxV();

            tesselator.addVertexWithUV((dI + dHorizontalMin), (dJ + dFireHeight), var32, dMinU, dMinV);
            tesselator.addVertexWithUV((dI + dHorizontalMin), (dJ + 0), var24, dMinU, dMaxV);
            tesselator.addVertexWithUV((dI + dHorizontalMax), (dJ + 0), var24, dMaxU, dMaxV);
            tesselator.addVertexWithUV((dI + dHorizontalMax), (dJ + dFireHeight), var32, dMaxU, dMinV);

            tesselator.addVertexWithUV((dI + dHorizontalMax), (dJ + dFireHeight), var30, dMinU, dMinV);
            tesselator.addVertexWithUV((dI + dHorizontalMax), (dJ + 0), var22, dMinU, dMaxV);
            tesselator.addVertexWithUV((dI + dHorizontalMin), (dJ + 0), var22, dMaxU, dMaxV);
            tesselator.addVertexWithUV((dI + dHorizontalMin), (dJ + dFireHeight), var30, dMaxU, dMinV);
        }

        if (world.getBlockMetadata(x, y, z) == 3) {

            double dScale = fireAnimationScaleArray[2];

            double dI = (double) x;
            double dJ = (double) y;
            double dK = (double) z;

            IIcon fireTexture1 = Blocks.fire.getFireIcon(0);
            IIcon fireTexture2 = Blocks.fire.getFireIcon(1);

            if (((x + z) & 1) != 0) {
                fireTexture1 = Blocks.fire.getFireIcon(1);
                fireTexture2 = Blocks.fire.getFireIcon(0);
            }

            tesselator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
            tesselator.setBrightness(100);

            double dMinU = fireTexture1.getMinU();
            double dMinV = fireTexture1.getMinV();
            double dMaxU = fireTexture1.getMaxU();
            double dMaxV = fireTexture1.getMaxV();

            double dFireHeight = 1.4D * dScale;
            double dHorizontalMin = 0.5D - (0.5D * dScale);
            double dHorizontalMax = 0.5D + (0.5D * dScale);

            double dOffset = 0.2D * dScale;

            double var18 = dI + 0.5D + dOffset;
            double var20 = dI + 0.5D - dOffset;
            double var22 = dK + 0.5D + dOffset;
            double var24 = dK + 0.5D - dOffset;

            dOffset = 0.3D * dScale;

            double var26 = dI + 0.5D - dOffset;
            double var28 = dI + 0.5D + dOffset;
            double var30 = dK + 0.5D - dOffset;
            double var32 = dK + 0.5D + dOffset;

            tesselator.addVertexWithUV(var26, (dJ + dFireHeight), (dK + dHorizontalMax), dMaxU, dMinV);
            tesselator.addVertexWithUV(var18, (dJ + 0), (dK + dHorizontalMax), dMaxU, dMaxV);
            tesselator.addVertexWithUV(var18, (dJ + 0), (dK + dHorizontalMin), dMinU, dMaxV);
            tesselator.addVertexWithUV(var26, (dJ + dFireHeight), (dK + dHorizontalMin), dMinU, dMinV);

            tesselator.addVertexWithUV(var28, (dJ + dFireHeight), (dK + dHorizontalMin), dMaxU, dMinV);
            tesselator.addVertexWithUV(var20, (dJ + 0), (dK + dHorizontalMin), dMaxU, dMaxV);
            tesselator.addVertexWithUV(var20, (dJ + 0), (dK + dHorizontalMax), dMinU, dMaxV);
            tesselator.addVertexWithUV(var28, (dJ + dFireHeight), (dK + dHorizontalMax), dMinU, dMinV);

            dMinU = fireTexture2.getMinU();
            dMinV = fireTexture2.getMinV();
            dMaxU = fireTexture2.getMaxU();
            dMaxV = fireTexture2.getMaxV();

            tesselator.addVertexWithUV((dI + dHorizontalMax), (dJ + dFireHeight), var32, dMaxU, dMinV);
            tesselator.addVertexWithUV((dI + dHorizontalMax), (dJ + 0), var24, dMaxU, dMaxV);
            tesselator.addVertexWithUV((dI + dHorizontalMin), (dJ + 0), var24, dMinU, dMaxV);
            tesselator.addVertexWithUV((dI + dHorizontalMin), (dJ + dFireHeight), var32, dMinU, dMinV);

            tesselator.addVertexWithUV((dI + dHorizontalMin), (dJ + dFireHeight), var30, dMaxU, dMinV);
            tesselator.addVertexWithUV((dI + dHorizontalMin), (dJ + 0), var22, dMaxU, dMaxV);
            tesselator.addVertexWithUV((dI + dHorizontalMax), (dJ + 0), var22, dMinU, dMaxV);
            tesselator.addVertexWithUV((dI + dHorizontalMax), (dJ + dFireHeight), var30, dMinU, dMinV);

            dOffset = 0.5D * dScale;

            var18 = dI + 0.5D - dOffset;
            var20 = dI + 0.5D + dOffset;
            var22 = dK + 0.5D - dOffset;
            var24 = dK + 0.5D + dOffset;

            dOffset = 0.4D * dScale;

            var26 = dI + 0.5D - dOffset;
            var28 = dI + 0.5D + dOffset;
            var30 = dK + 0.5D - dOffset;
            var32 = dK + 0.5D + dOffset;

            tesselator.addVertexWithUV(var26, (dJ + dFireHeight), (dK + dHorizontalMin), dMinU, dMinV);
            tesselator.addVertexWithUV(var18, (dJ + 0), (dK + dHorizontalMin), dMinU, dMaxV);
            tesselator.addVertexWithUV(var18, (dJ + 0), (dK + dHorizontalMax), dMaxU, dMaxV);
            tesselator.addVertexWithUV(var26, (dJ + dFireHeight), (dK + dHorizontalMax), dMaxU, dMinV);

            tesselator.addVertexWithUV(var28, (dJ + dFireHeight), (dK + dHorizontalMax), dMinU, dMinV);
            tesselator.addVertexWithUV(var20, (dJ + 0), (dK + dHorizontalMax), dMinU, dMaxV);
            tesselator.addVertexWithUV(var20, (dJ + 0), (dK + dHorizontalMin), dMaxU, dMaxV);
            tesselator.addVertexWithUV(var28, (dJ + dFireHeight), (dK + dHorizontalMin), dMaxU, dMinV);

            dMinU = fireTexture1.getMinU();
            dMinV = fireTexture1.getMinV();
            dMaxU = fireTexture1.getMaxU();
            dMaxV = fireTexture1.getMaxV();

            tesselator.addVertexWithUV((dI + dHorizontalMin), (dJ + dFireHeight), var32, dMinU, dMinV);
            tesselator.addVertexWithUV((dI + dHorizontalMin), (dJ + 0), var24, dMinU, dMaxV);
            tesselator.addVertexWithUV((dI + dHorizontalMax), (dJ + 0), var24, dMaxU, dMaxV);
            tesselator.addVertexWithUV((dI + dHorizontalMax), (dJ + dFireHeight), var32, dMaxU, dMinV);

            tesselator.addVertexWithUV((dI + dHorizontalMax), (dJ + dFireHeight), var30, dMinU, dMinV);
            tesselator.addVertexWithUV((dI + dHorizontalMax), (dJ + 0), var22, dMinU, dMaxV);
            tesselator.addVertexWithUV((dI + dHorizontalMin), (dJ + 0), var22, dMaxU, dMaxV);
            tesselator.addVertexWithUV((dI + dHorizontalMin), (dJ + dFireHeight), var30, dMaxU, dMinV);
        }
        ///
        //meta 0 = normal  meta 2 = lighted up; meta 3 = medium fire; meta 4 = big fire; meta 5 = crackling; meta 6 = burned out

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return ClientProxy.renderCampfire;
    }
}