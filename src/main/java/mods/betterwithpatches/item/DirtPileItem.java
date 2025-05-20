package mods.betterwithpatches.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class DirtPileItem extends Item
{
    public DirtPileItem()
    {
        super();

       // BWsetBellowsBlowDistance(1);
       // setFilterableProperties(FILTERABLE_FINE);

        setUnlocalizedName( "fcItemPileDirt" );
        this.setTextureName("betterwithpatches:dirt_pile");

        setCreativeTab( CreativeTabs.tabMaterials );
    }
    }
