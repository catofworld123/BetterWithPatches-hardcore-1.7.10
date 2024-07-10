package mods.betterwithpatches.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import mods.betterwithpatches.Config;
import mods.betterwithpatches.client.RenderBlockTreeStage2;
import mods.betterwithpatches.client.RenderSteelAnvil;
import mods.betterwithpatches.client.RenderBlockTreeStage1;
import mods.betterwithpatches.craft.HardcoreWoodInteractionExtensions;
import mods.betterwithpatches.features.HCMovement;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    public static int renderAnvil;
    public static int renderBlockTreeStage1;
    public static int renderBlockTreeStage2;

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
