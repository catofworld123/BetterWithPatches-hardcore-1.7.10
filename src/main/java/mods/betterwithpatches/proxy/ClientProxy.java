package mods.betterwithpatches.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import mods.betterwithpatches.Config;
import mods.betterwithpatches.block.tile.TileEntityCampfire;
import mods.betterwithpatches.client.*;
import mods.betterwithpatches.craft.HardcoreWoodInteractionExtensions;
import mods.betterwithpatches.features.HCMovement;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    public static int renderAnvil;
    public static int renderBlockTreeStage1;
    public static int renderBlockTreeStage2;
    public static int renderBlockTreeStage3;
    public static int renderBlockTreeStage4;
    public static int renderCampfire;
    public static int renderBlockCampfire;

    @Override
    public void preInit() {
        super.preInit();
    }


    @Override
    public void init() {
        super.init();


        renderAnvil = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(renderAnvil, new RenderSteelAnvil());

        if (Config.enablePenalties) {
            if (Config.HCMovement && Config.removeSpeedPenaltyFOVChanges)
                MinecraftForge.EVENT_BUS.register(new HCMovement.HCMovementFOV());
        }

        renderBlockTreeStage1 = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(renderBlockTreeStage1, new RenderBlockTreeStage1());
        renderBlockTreeStage2 = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(renderBlockTreeStage2, new RenderBlockTreeStage2());
        renderBlockTreeStage3 = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(renderBlockTreeStage3, new RenderBlockTreeStage3());
        renderBlockTreeStage4 = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(renderBlockTreeStage4, new RenderBlockTreeStage4());
        renderCampfire = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(renderCampfire, new RenderCampfire());
        renderBlockCampfire = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(renderBlockCampfire, new RenderBlockCampfire());


        if (Config.enablePenalties) {
            if (Config.HCMovement && Config.removeSpeedPenaltyFOVChanges)
                MinecraftForge.EVENT_BUS.register(new HCMovement.HCMovementFOV());
        }


    }




    @Override
    public void postInit() {
        super.postInit();
    }

    @Override
    public void afterInit() {
        super.afterInit();
        HardcoreWoodInteractionExtensions.fillDisplayMap();
    }




}
