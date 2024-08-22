package mods.betterwithpatches.util;

import cpw.mods.fml.client.registry.ClientRegistry;
import mods.betterwithpatches.block.tile.CampfireTileEntity;
import mods.betterwithpatches.client.CampfireRenderer;

public class BWPRenderMapper {
    public static void initTileEntityRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(CampfireTileEntity.class, new CampfireRenderer());
}}
