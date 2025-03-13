package mods.betterwithpatches.item;

import mods.betterwithpatches.BWPRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ArcaneScrollItem extends Item {
    public ArcaneScrollItem() {
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
      // * this.setBuoyant();
        //this.setBellowsBlowDistance(3);
       // this.setFilterableProperties(18);
        this.setCreativeTab(CreativeTabs.tabBrewing);

    }


    @Override
    public boolean hasEffect(ItemStack itemStack) {
        return true;
    }


    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List infoList, boolean bAdvamcedToolTips) {
        int iIndex = MathHelper.clamp_int(itemStack.getItemDamage(), 0, Enchantment.enchantmentsList.length - 1);
        Enchantment enchantment = Enchantment.enchantmentsList[iIndex];
        if (enchantment != null) {
            infoList.add(StatCollector.translateToLocal(enchantment.getName()));
        }
    }


    @Override
    public void getSubItems(Item iItemID, CreativeTabs creativeTabs, List list) {
        for(int iTempIndex = 0; iTempIndex < Enchantment.enchantmentsList.length; ++iTempIndex) {
            if (Enchantment.enchantmentsList[iTempIndex] != null) {
                list.add(new ItemStack(BWPRegistry.arcaneScroll, 1, Enchantment.enchantmentsList[iTempIndex].effectId));
            }
        }
    }
}
