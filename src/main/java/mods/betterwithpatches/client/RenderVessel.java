package mods.betterwithpatches.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import mods.betterwithpatches.block.InfernalEnchanterBlock;
import mods.betterwithpatches.block.VesselBlock;
import mods.betterwithpatches.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import static betterwithmods.client.RenderTileEntities.renderItemBlock;

public class RenderVessel implements ISimpleBlockRenderingHandler {
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        if (block instanceof VesselBlock){
            ((VesselBlock) block).renderBlockAsItem(renderer, metadata, 1.0f);
        }


    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        VesselBlock vessel = (VesselBlock) world.getBlock(x,y,z);
        return vessel.renderBlock(renderer,x,y,z);
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return ClientProxy.renderVessel;
    }
}
