package mods.betterwithpatches;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.betterwithpatches.block.*;
import mods.betterwithpatches.block.tile.*;
import mods.betterwithpatches.data.BWPCreativeTab;
import mods.betterwithpatches.item.ArcaneScrollItem;
import mods.betterwithpatches.item.ItemOakBark;
import mods.betterwithpatches.item.ItemShaft;
import mods.betterwithpatches.item.PileOfDirt;
import mods.betterwithpatches.item.tool.*;
import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLogic;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.util.EnumHelper;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class BWPRegistry {
    public static final Material candleMaterial = new MaterialLogic(MapColor.airColor).setNoPushMobility().setDoesNotBreakSaw().setRequiresTool();

    public static final CreativeTabs bwpTab = new BWPCreativeTab();
    public static final Item.ToolMaterial SOULFORGED_TOOL = EnumHelper.addToolMaterial("soulforgedSteel", 4, 2250, 10f, 3, 22);
    public static final ItemArmor.ArmorMaterial SOULFORGED_ARMOR = EnumHelper.addArmorMaterial("soulforgedSteel", 40, new int[]{3, 8, 6, 3}, 25),
            DREDGE_HEAVY_ARMOR = EnumHelper.addArmorMaterial("dredgeHeavy", 38, new int[]{3, 8, 6, 3}, 17);
    public static Block steelAnvil;
    public static Block blocktreestage1;
    public static Block blocktreestage2;
    public static Block blocktreestage3;
    public static Block blocktreestage4;
    public static Block campfire;
    public static Block infernalEnchanter;
    public static Block candle;
    public static BWPRegistry instance;


    public static CampfireBlock unlitCampfire;
    public static CampfireBlock smallCampfire;
    public static CampfireBlock mediumCampfire;
    public static CampfireBlock largeCampfire;
    public enum ENUM_IDS
    {
       Nothing(), InfernalEnchanter(),
    }


    public static Item steelAxe, steelHoe, steelPickaxe, itemOakBark, steelShovel, steelSword, steelHelmet, steelChestplate, steelLeggings, steelBoots, itemPointyStick,
            dredgeHeavyHelmet, dredgeHeavyChestplate, dredgeHeavyLeggings, dredgeHeavyBoots, itemShaft, pileOfDirt, arcaneScroll;



    public static void init() {
        steelAnvil = GameRegistry.registerBlock(new BlockSteelAnvil(), ItemBlock.class, "steelAnvil");
        GameRegistry.registerTileEntity(TileEntitySteelAnvil.class, "bwm.steelAnvil");
        blocktreestage1 = GameRegistry.registerBlock(new BlockTreeStage1(), ItemBlock.class, "blockTreeStage1");
        GameRegistry.registerTileEntity(TileEntityBlockTreeStage1.class, "bwm.blockTreeStage1");
        blocktreestage2 = GameRegistry.registerBlock(new BlockTreeStage2(), ItemBlock.class, "blockTreeStage2");
        GameRegistry.registerTileEntity(TileEntityBlockTreeStage2.class, "bwm.blockTreeStage2");
        blocktreestage3 = GameRegistry.registerBlock(new BlockTreeStage3(), ItemBlock.class, "blockTreeStage3");
        GameRegistry.registerTileEntity(TileEntityBlockTreeStage3.class, "bwm.blockTreeStage3");
        blocktreestage4 = GameRegistry.registerBlock(new BlockTreeStage4(), ItemBlock.class, "blockTreeStage4");
        GameRegistry.registerTileEntity(TileEntityBlockTreeStage4.class, "bwm.blockTreeStage4");
        candle = GameRegistry.registerBlock(new CandleBlock(16, "fcBlockCandle_plain", "btw:candle"), ItemBlock.class, "fcBlockCandle_plain");


        campfire = GameRegistry.registerBlock(new Campfire(0).setCreativeTab((BWPRegistry.bwpTab)), ItemBlock.class, "campfire");
        GameRegistry.registerTileEntity(TileEntityCampfire.class, "bwm.campfire");
        infernalEnchanter = GameRegistry.registerBlock(new InfernalEnchanterBlock().setCreativeTab(BWPRegistry.bwpTab), ItemBlock.class,"infernalenchanter");
        GameRegistry.registerTileEntity(InfernalEnchanterTileEntity.class,"bwm.infernalenchanter");


        unlitCampfire = (CampfireBlock)GameRegistry.registerBlock(new CampfireBlock( 0).setCreativeTab((BWPRegistry.bwpTab)), ItemBlock.class, "unlitcampfire");
        smallCampfire = (CampfireBlock)GameRegistry.registerBlock(new CampfireBlock( 1).setLightLevel(0.25F), ItemBlock.class, "Campfirewithsmallfire");
        mediumCampfire = (CampfireBlock)GameRegistry.registerBlock(new CampfireBlock( 2).setLightLevel(0.5F), ItemBlock.class, "Campfirewithmediumfire");
        largeCampfire = (CampfireBlock)GameRegistry.registerBlock(new CampfireBlock( 3).setLightLevel(0.875F), ItemBlock.class, "Campfirewithbigfire");
        GameRegistry.registerTileEntity(CampfireTileEntity.class, "bwm.fcBlockCampfire");

        steelAxe = registerItem("steelAxe", new ItemSoulforgedAxe());
        steelHoe = registerItem("steelHoe", new ItemSoulforgedHoe());
        steelPickaxe = registerItem("steelPickaxe", new ItemSoulforgedPickaxe());
        steelShovel = registerItem("steelShovel", new ItemSoulforgedShovel());
        steelSword = registerItem("steelSword", new ItemSoulforgedSword());
        steelHelmet = registerItem("steelHelmet", new ItemSoulforgedArmor(0));
        steelChestplate = registerItem("steelChestplate", new ItemSoulforgedArmor(1));
        steelLeggings = registerItem("steelLeggings", new ItemSoulforgedArmor(2));
        steelBoots = registerItem("steelBoots", new ItemSoulforgedArmor(3));
        dredgeHeavyHelmet = registerItem("dredgeHeavyHelmet", new ItemDredgeHeavyArmor(0));
        dredgeHeavyChestplate = registerItem("dredgeHeavyChestplate", new ItemDredgeHeavyArmor(1));
        dredgeHeavyLeggings = registerItem("dredgeHeavyLeggings", new ItemDredgeHeavyArmor(2));
        dredgeHeavyBoots = registerItem("dredgeHeavyBoots", new ItemDredgeHeavyArmor(3));
        itemShaft = registerItem("shaft", new ItemShaft());
        pileOfDirt = registerItem("pileOfDirt", new PileOfDirt());
        itemOakBark = registerItem("itemOakBark", new ItemOakBark());
        itemPointyStick = registerItem("ItemPointyStick", new ItemPointyStick());
        arcaneScroll = registerItem("ItemArcaneScroll", new ArcaneScrollItem());

    }

    public static Item registerItem(String id, Item item) {
        item.setUnlocalizedName("bwm:" + id);
        item.setTextureName(String.format("%s:%s", BWPConstants.MODID, id));
        item.setCreativeTab(bwpTab);
        GameRegistry.registerItem(item, id);
        return item;

    }





}
