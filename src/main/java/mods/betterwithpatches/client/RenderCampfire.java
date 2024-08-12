package mods.betterwithpatches.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import mods.betterwithpatches.block.Campfire;

import static betterwithmods.client.RenderTileEntities.renderItemBlock;

public class RenderCampfire implements ISimpleBlockRenderingHandler {

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
        Tessellator tessellator = Tessellator.instance;
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


        if (world.getBlockMetadata(x, y, z) == 2) {
            renderer.setOverrideBlockTexture(((Campfire) BWPRegistry.campfire).icons[4]);
            renderer.setRenderBounds(0.3125, 0, 0.3125, 0.312501, 0.250, 0.6875);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.setRenderBounds(0.3125, 0, 0.3125, 0.6875, 0.250, 0.312501);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.setRenderBounds(0.6875, 0, 0.3125, 0.687501, 0.250, 0.6875);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.setRenderBounds(0.3125, 0, 0.6875, 0.6875, 0.250, 0.687501);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.clearOverrideBlockTexture();

            renderer.drawCrossedSquares(((Campfire) BWPRegistry.campfire).icons[3], x, y, z, 0.5F);
            renderer.renderAllFaces = true;
            renderer.uvRotateTop = 0;
        }
        if (world.getBlockMetadata(x, y, z) == 3) {
            renderer.setOverrideBlockTexture(((Campfire) BWPRegistry.campfire).icons[4]);
            renderer.setRenderBounds(0.3125, 0, 0.3125, 0.312501, 0.250, 0.6875);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.setRenderBounds(0.3125, 0, 0.3125, 0.6875, 0.250, 0.312501);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.setRenderBounds(0.6875, 0, 0.3125, 0.687501, 0.250, 0.6875);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.setRenderBounds(0.3125, 0, 0.6875, 0.6875, 0.250, 0.687501);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.clearOverrideBlockTexture();

            renderer.drawCrossedSquares(((Campfire) BWPRegistry.campfire).icons[4], x, y, z, 0.75F);
            renderer.renderAllFaces = true;
            renderer.uvRotateTop = 0;
        }
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