package mods.betterwithpatches.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import mods.betterwithpatches.block.InfernalEnchanterBlock;
import mods.betterwithpatches.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import static betterwithmods.client.RenderTileEntities.renderItemBlock;

public class RenderInfernalEnchanterBlock implements ISimpleBlockRenderingHandler {
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        renderer.setRenderBounds((float)0.0F, (float)0.0F, (float)0.0F, (float)1.0F, (float)0.5F, (float)1.0F);
        renderItemBlock(block, renderer, metadata);


    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        InfernalEnchanterBlock infernalEnchanterBlock = (InfernalEnchanterBlock)world.getBlock(x,y,z);
       return infernalEnchanterBlock.renderBlock(renderer,x,y,z);
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

