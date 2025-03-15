package mods.betterwithpatches;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import mods.betterwithpatches.craft.NewCrafts.CraftingTableStuff;
import mods.betterwithpatches.craft.RecipeManager;
import mods.betterwithpatches.menu.BWPMenuHandler;
import mods.betterwithpatches.network.GuiHandler;
import mods.betterwithpatches.proxy.ClientProxy;
import mods.betterwithpatches.proxy.CommonProxy;
import mods.betterwithpatches.util.BWPRenderMapper;
import mods.betterwithpatches.util.WorldUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.network.play.client.C17PacketCustomPayload;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import static mods.betterwithpatches.util.BWPConstants.MODID;
import static mods.betterwithpatches.util.BWPConstants.MODNAME;





@Mod(modid = MODID, name = MODNAME, version = "${version}", dependencies = "required-after:betterwithmods")
public class BetterWithPatches {
    public static BetterWithPatches instance;





    @SidedProxy(clientSide = "mods.betterwithpatches.proxy.ClientProxy", serverSide = "mods.betterwithpatches.proxy.CommonProxy")
    public static CommonProxy PROXY;

    @Mod.EventHandler
    public static void PreLoad(FMLPreInitializationEvent PreEvent){

        CraftingTableStuff.mainRegistry();
    }
    @Mod.EventHandler
    public static void load(FMLInitializationEvent event){

    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new BWPMenuHandler());
        PROXY.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {

        PROXY.init();
        RecipeManager.addAllModRecipes();

    }



    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        PROXY.postInit();
        BWPRenderMapper.initTileEntityRenderers();
    }

    @Mod.EventHandler
    public void afterInit(FMLLoadCompleteEvent e) {
        PROXY.afterInit();
    }



}


