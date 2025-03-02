package mods.betterwithpatches.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.block.Campfire;
import mods.betterwithpatches.block.CampfireBlock;
import mods.betterwithpatches.block.tile.CampfireTileEntity;
import mods.betterwithpatches.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import static betterwithmods.client.RenderTileEntities.renderItemBlock;
import static mods.betterwithpatches.block.Campfire.getFuelState;
import static mods.betterwithpatches.block.CampfireBlock.CAMPFIRE_FUEL_STATE_BURNED_OUT;
import static mods.betterwithpatches.block.CampfireBlock.CAMPFIRE_FUEL_STATE_SMOULDERING;


public class RenderBlockCampfire implements ISimpleBlockRenderingHandler {
    static final double[] fireAnimationScaleArray = new double[] {0D, 0.25D, 0.5D, 0.875D };
    private static Framebuffer framebuffer;







    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        renderer.setOverrideBlockTexture(((Campfire) BWPRegistry.campfire).icons[1]);
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
        renderer.clearOverrideBlockTexture();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        CampfireBlock blockcampfire = (CampfireBlock)(world.getBlock( x, y, z ));
        CampfireTileEntity campfireTile =  (CampfireTileEntity)world.getTileEntity( x, y, z );
        int facing;
        int k = 3;
        facing = campfireTile.getFacing();
        if (facing == 2) {
            k = 180;
        }
        if (facing == 3) {
            k = 0;
        }
        if (facing == 4) {
            k = 90;
        }
        if (facing == 5) {
            k = -90;
        }





       renderer.renderAllFaces = true;





             if (getFuelState(world, x, y, z) == 2) {
             }
             if (getFuelState(world, x, y, z) == 1) {
             }
             if (getFuelState(world, x, y, z) != 2 && getFuelState(world, x, y, z) != 1) {
             }
             if (blockcampfire.getHasSpit(world, x, y, z)) {
             }


             Tessellator tesselator = Tessellator.instance;


             double dScale = fireAnimationScaleArray[blockcampfire.fireLevel];

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
             tesselator.setBrightness(150);

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
             renderer.renderAllFaces = false;









        return true;





       }



    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return ClientProxy.renderBlockCampfire;
    }


}
