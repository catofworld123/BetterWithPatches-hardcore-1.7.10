package mods.betterwithpatches.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import mods.betterwithpatches.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import static betterwithmods.client.RenderTileEntities.renderItemBlock;

public class RenderBlockTreeStage1 implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        renderer.setRenderBounds(0.125, 0D, 0.125, 0.875, 0.25, 0.875);
        renderItemBlock(block, renderer, metadata);
        renderer.setRenderBounds(0, 0, 0, 1, 0.0625, 1);
        renderItemBlock(block, renderer, metadata);
        renderer.setRenderBounds(0.0625, 0.0625, 0.0625, 0.9375, 0.9375, 0.9375);
        renderItemBlock(block, renderer, metadata);
        renderer.setRenderBounds(0, 0.9375, 0, 1, 1, 1);
        renderItemBlock(block, renderer, metadata);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        renderer.setRenderBounds(0.125, 0D, 0.125, 0.875, 0.25, 0.875);
            renderer.setRenderBounds(0, 0, 0, 1, 0.0625, 1);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.setRenderBounds(0.0625, 0.0625, 0.0625, 0.9375, 0.9375, 0.9375);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.setRenderBounds(0, 0.9375, 0, 1, 1, 1);
        renderer.renderStandardBlock(block, x, y, z);
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return ClientProxy.renderBlockTreeStage1;
    }
}

