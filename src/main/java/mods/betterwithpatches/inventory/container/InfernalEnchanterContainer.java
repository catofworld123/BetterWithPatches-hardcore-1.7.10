package mods.betterwithpatches.inventory.container;

import mods.betterwithpatches.BWPRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

import java.util.Objects;
import java.util.Random;

public class InfernalEnchanterContainer extends Container {
    public IInventory tableInventory;
    private World localWorld;
    private int blockX;
    private int blockY;
    private int blockZ;
    private static final double MAX_INTERACTION_DISTANCE = (double)8.0F;
    private static final double MAX_INTERACTION_DISTANCE_SQ = (double)64.0F;
    private static final int SLOT_SCREEN_WIDTH = 18;
    private static final int SLOT_SCREEN_HEIGHT = 18;
    private static final int SCROLL_SLOT_SCREEN_POS_X = 17;
    private static final int SCROLL_SLOT_SCREEN_POS_Y = 37;
    private static final int ITEM_SLOT_SCREEN_POS_X = 17;
    private static final int ITEM_SLOT_SCREEN_POS_Y = 75;
    private static final int PLAYER_INVENTORY_SCREEN_POS_X = 8;
    private static final int PLAYER_INVENTORY_SCREEN_POS_Y = 129;
    private static final int PLAYER_HOTBAR_SCREEN_POS_Y = 187;
    private static final int HORIZONTAL_BOOK_SHELF_CHECK_DISTANCE = 8;
    private static final int VERTICAL_POSITIVE_BOOK_SHELF_CHECK_DISTANCE = 8;
    private static final int VERTICAL_NEGATIVE_BOOK_SHELF_CHECK_DISTANCE = 8;
    public static final int MAX_ENCHANTMENT_POWER_LEVEL = 5;
    public int[] currentEnchantmentLevels;
    public int maxSurroundingBookshelfLevel;
    public int lastMaxSurroundingBookshelfLevel;
    public long nameSeed;
    Random rand;

    public InfernalEnchanterContainer(InventoryPlayer playerInventory, World world, int i, int j, int k) {
        this.localWorld = world;
        this.blockX = i;
        this.blockY = j;
        this.blockZ = k;
        this.rand = new Random();
        this.nameSeed = this.rand.nextLong();
        this.tableInventory = new InfernalEnchanterInventory(this, "fcInfernalEnchanterInv", 2);
        this.currentEnchantmentLevels = new int[5];
        this.resetEnchantingLevels();
        this.maxSurroundingBookshelfLevel = 0;
        this.lastMaxSurroundingBookshelfLevel = 0;
        this.addSlotToContainer(new Slot(this.tableInventory, 0, 17, 37));
        this.addSlotToContainer(new Slot(this.tableInventory, 1, 17, 75));

        for(int tempSlotY = 0; tempSlotY < 3; ++tempSlotY) {
            for(int tempSlotX = 0; tempSlotX < 9; ++tempSlotX) {
                this.addSlotToContainer(new Slot(playerInventory, tempSlotX + tempSlotY * 9 + 9, 8 + tempSlotX * 18, 129 + tempSlotY * 18));
            }
        }

        for(int tempSlotX = 0; tempSlotX < 9; ++tempSlotX) {
            this.addSlotToContainer(new Slot(playerInventory, tempSlotX, 8 + tempSlotX * 18, 187));
        }

        if (world != null && !world.isRemote) {
            this.checkForSurroundingBookshelves();
        }

    }

    public void onCraftMatrixChanged(IInventory inventory) {
        if (inventory == this.tableInventory) {
            this.nameSeed = this.rand.nextLong();
            this.resetEnchantingLevels();
            this.computeCurrentEnchantmentLevels();
            if (this.localWorld != null && !this.localWorld.isRemote) {
                this.detectAndSendChanges();
            }
        }

    }

    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        if (this.localWorld != null && !this.localWorld.isRemote) {
            for(int i = 0; i < this.tableInventory.getSizeInventory(); ++i) {
                ItemStack itemstack = this.tableInventory.getStackInSlot(i);
                if (itemstack != null) {
                    player.entityDropItem(itemstack, 0);
                }
            }

        }
    }

    public boolean canInteractWith(EntityPlayer entityplayer) {
        if (this.localWorld != null && !this.localWorld.isRemote) {
            if (this.localWorld.getBlock(this.blockX, this.blockY, this.blockZ) != BWPRegistry.infernalEnchanter) {
                return false;
            } else {
                return entityplayer.getDistanceSq((double)this.blockX + (double)0.5F, (double)this.blockY + (double)0.5F, (double)this.blockZ + (double)0.5F) <= (double)64.0F;
            }
        } else {
            return true;
        }
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int iSlotIndex) {
        ItemStack clickedStack = null;
        Slot slot = (Slot)this.inventorySlots.get(iSlotIndex);
        if (slot != null && slot.getHasStack()) {
            ItemStack processedStack = slot.getStack();
            clickedStack = processedStack.copy();
            if (iSlotIndex <= 1) {
                if (!this.mergeItemStack(processedStack, 2, 38, true)) {
                    return null;
                }
            } else if (processedStack.getItem() == BWPRegistry.arcaneScroll) {
                if (!this.mergeItemStack(processedStack, 0, 1, false)) {
                    return null;
                }
            } else if (this.getMaximumEnchantmentCost(processedStack) > 0) {
                if (!this.mergeItemStack(processedStack, 1, 2, false)) {
                    return null;
                }
            } else if (iSlotIndex >= 2 && iSlotIndex < 29) {
                if (!this.mergeItemStack(processedStack, 29, 38, false)) {
                    return null;
                }
            } else if (iSlotIndex >= 29 && iSlotIndex < 38 && !this.mergeItemStack(processedStack, 2, 29, false)) {
                return null;
            }

            if (processedStack.stackSize == 0) {
                slot.putStack((ItemStack)null);
            } else {
                slot.onSlotChanged();
            }

            if (processedStack.stackSize == clickedStack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, processedStack);
            if (this.localWorld != null && !this.localWorld.isRemote) {
                this.detectAndSendChanges();
            }
        }

        return clickedStack;
    }

    private void checkForSurroundingBookshelves() {
        int iBookshelfCount = 0;

        for(int iTempI = this.blockX - 8; iTempI <= this.blockX + 8; ++iTempI) {
            for(int iTempJ = this.blockY - 8; iTempJ <= this.blockY + 8; ++iTempJ) {
                for(int iTempK = this.blockZ - 8; iTempK <= this.blockZ + 8; ++iTempK) {
                    if (this.isValidBookshelf(iTempI, iTempJ, iTempK)) {
                        ++iBookshelfCount;
                    }
                }
            }
        }

        this.maxSurroundingBookshelfLevel = iBookshelfCount;
    }

    private boolean isValidBookshelf(int i, int j, int k) {
        Block iBlockID = this.localWorld.getBlock(i, j, k);
        return iBlockID == Blocks.bookshelf && (this.localWorld.isAirBlock(i + 1, j, k) || this.localWorld.isAirBlock(i - 1, j, k) || this.localWorld.isAirBlock(i, j, k + 1) || this.localWorld.isAirBlock(i, j, k - 1));
    }

    private void setCurrentEnchantingLevels(int iMaxPowerLevel, int iCostMultiplier, int iMaxBaseCostForItem) {
        this.resetEnchantingLevels();
        if (iMaxPowerLevel == 1) {
            this.currentEnchantmentLevels[0] = 30;
        } else if (iMaxPowerLevel == 2) {
            this.currentEnchantmentLevels[0] = 15;
            this.currentEnchantmentLevels[1] = 30;
        } else if (iMaxPowerLevel == 3) {
            this.currentEnchantmentLevels[0] = 10;
            this.currentEnchantmentLevels[1] = 20;
            this.currentEnchantmentLevels[2] = 30;
        } else if (iMaxPowerLevel == 4) {
            this.currentEnchantmentLevels[0] = 8;
            this.currentEnchantmentLevels[1] = 15;
            this.currentEnchantmentLevels[2] = 23;
            this.currentEnchantmentLevels[3] = 30;
        } else if (iMaxPowerLevel == 5) {
            this.currentEnchantmentLevels[0] = 6;
            this.currentEnchantmentLevels[1] = 12;
            this.currentEnchantmentLevels[2] = 18;
            this.currentEnchantmentLevels[3] = 24;
            this.currentEnchantmentLevels[4] = 30;
        }

        int iCostIncrement = (iCostMultiplier - 1) * 30;

        for(int iTemp = 0; iTemp < 5; ++iTemp) {
            if (this.currentEnchantmentLevels[iTemp] > 0) {
                if (iMaxBaseCostForItem < this.currentEnchantmentLevels[iTemp]) {
                    this.currentEnchantmentLevels[iTemp] = 0;
                } else {
                    int[] var10000 = this.currentEnchantmentLevels;
                    var10000[iTemp] += iCostIncrement;
                }
            }
        }

    }

    private void resetEnchantingLevels() {
        for(int iTemp = 0; iTemp < 5; ++iTemp) {
            this.currentEnchantmentLevels[iTemp] = 0;
        }

    }

    private void computeCurrentEnchantmentLevels() {
        ItemStack scrollStack = this.tableInventory.getStackInSlot(0);
        if (scrollStack != null && scrollStack.getItem() == BWPRegistry.arcaneScroll) {
            ItemStack itemToEnchantStack = this.tableInventory.getStackInSlot(1);
            if (itemToEnchantStack != null) {
                int iMaxEnchantmentCost = this.getMaximumEnchantmentCost(itemToEnchantStack);
                if (iMaxEnchantmentCost > 0) {
                    int iEnchantmentIndex = scrollStack.getItemDamage();
                    if (iEnchantmentIndex >= Enchantment.enchantmentsList.length || Enchantment.enchantmentsList[iEnchantmentIndex] == null) {
                        return;
                    }

                    if (this.isEnchantmentAppropriateForItem(iEnchantmentIndex, itemToEnchantStack) && !this.doesEnchantmentConflictWithExistingOnes(iEnchantmentIndex, itemToEnchantStack)) {
                        int iMaxNumberOfItemEnchants = this.getMaximumNumberOfEnchantments(itemToEnchantStack);
                        int iCurrentNumberOfItemEnchants = 0;
                        NBTTagList enchantmentTagList = itemToEnchantStack.getEnchantmentTagList();
                        if (enchantmentTagList != null) {
                            iCurrentNumberOfItemEnchants = itemToEnchantStack.getEnchantmentTagList().tagCount();
                        }

                        if (iCurrentNumberOfItemEnchants < iMaxNumberOfItemEnchants) {
                            this.setCurrentEnchantingLevels(this.getMaxEnchantmentPowerLevel(iEnchantmentIndex, itemToEnchantStack), iCurrentNumberOfItemEnchants + 1, this.getMaximumEnchantmentCost(itemToEnchantStack));
                        }
                    }
                }
            }
        }

    }

    private int getMaximumEnchantmentCost(ItemStack itemStack) {
      //  return itemStack.getItem().getInfernalMaxEnchantmentCost(); deprectaed (for now)
         return 30;
    }

    private int getMaximumNumberOfEnchantments(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (item != null){
            if ( item instanceof ItemTool){
                ItemTool tool = (ItemTool) itemStack.getItem();
                if (Objects.equals(tool.getToolMaterialName(), "EMERALD")) {
                    return 2;
                }
                else if (Objects.equals(tool.getToolMaterialName(), "soulforgedSteel")) {
                    return 4;
                }
                else return 3;
            }
           else if (item instanceof ItemSword){
               ItemSword itemSword = (ItemSword)itemStack.getItem();
               if (Objects.equals(itemSword.getToolMaterialName(), "EMERALD")) {
                   return 2;
               }
               else if (Objects.equals(itemSword.getToolMaterialName(), "soulforgedSteel")) {
                   return 4;
               }
               else return 3;
           }
            else if (item instanceof ItemArmor){
                ItemArmor armor = (ItemArmor) itemStack.getItem();
                if (armor.getArmorMaterial() == ItemArmor.ArmorMaterial.DIAMOND) {
                    return 2;
                }
                else if (armor.getArmorMaterial() == BWPRegistry.SOULFORGED_ARMOR) {
                    return 4;
                }
                else return 3;

            }
            else return 3;
        }
        else return 0;
    }

    private boolean isEnchantmentAppropriateForItem(int iEnchantmentIndex, ItemStack itemStack) {
        return Enchantment.enchantmentsList[iEnchantmentIndex].canApply(itemStack);
    }

    private boolean doesEnchantmentConflictWithExistingOnes(int iEnchantmentIndex, ItemStack itemStack) {
        NBTTagList enchantmentTagList = itemStack.getEnchantmentTagList();
        if (enchantmentTagList != null) {
            int iCurrentNumberOfItemEnchants = itemStack.getEnchantmentTagList().tagCount();

            for(int iTemp = 0; iTemp < iCurrentNumberOfItemEnchants; ++iTemp) {
                int iTempEnchantmentIndex = ((NBTTagCompound)enchantmentTagList.getCompoundTagAt(iTemp)).getShort("id");
                if (iTempEnchantmentIndex == iEnchantmentIndex) {
                    return true;
                }

                if (iEnchantmentIndex == Enchantment.silkTouch.effectId && iTempEnchantmentIndex == Enchantment.fortune.effectId || iEnchantmentIndex == Enchantment.fortune.effectId && iTempEnchantmentIndex == Enchantment.silkTouch.effectId) {
                    return true;
                }
            }
        }


        return false;
    }

    private int getMaxEnchantmentPowerLevel(int iEnchantmentIndex, ItemStack itemStack) {
        return iEnchantmentIndex == Enchantment.respiration.effectId && itemStack.getItem() == Items.diamond_helmet ? 5 : Enchantment.enchantmentsList[iEnchantmentIndex].getMaxLevel();
    }

    public boolean enchantItem(EntityPlayer player, int iButtonIndex) {
        if (this.localWorld != null && !this.localWorld.isRemote) {
            int iButtonEnchantmentLevel = this.currentEnchantmentLevels[iButtonIndex];
            if (iButtonEnchantmentLevel > 0) {
                boolean bPlayerCapable = iButtonEnchantmentLevel <= player.experienceLevel && iButtonEnchantmentLevel <= this.maxSurroundingBookshelfLevel;
                if (bPlayerCapable) {
                    ItemStack scrollStack = this.tableInventory.getStackInSlot(0);
                    if (scrollStack != null && scrollStack.getItem() == BWPRegistry.arcaneScroll) {
                        ItemStack itemToEnchantStack = this.tableInventory.getStackInSlot(1);
                        if (itemToEnchantStack != null) {
                            int iEnchantmentIndex = scrollStack.getItemDamage();
                            if (iEnchantmentIndex < Enchantment.enchantmentsList.length && Enchantment.enchantmentsList[iEnchantmentIndex] != null) {
                                itemToEnchantStack.addEnchantment(Enchantment.enchantmentsList[iEnchantmentIndex], iButtonIndex + 1);
                                player.addExperienceLevel(-iButtonEnchantmentLevel);
                                this.tableInventory.decrStackSize(0, 1);
                                this.onCraftMatrixChanged(this.tableInventory);
                              //  this.localWorld.playSoundAtEntity(player, BTWSoundManager.VILLAGER_PRIEST_INFUSE.sound(), 5.0F, 1.0F - this.localWorld.rand.nextFloat() * 0.2F);
                                return true;
                            }

                            return false;
                        }
                    }
                }
            }

            return false;
        } else {
            return true;
        }
    }

    public void addCraftingToCrafters(ICrafting craftingInterface) {
        super.addCraftingToCrafters(craftingInterface);
        craftingInterface.sendProgressBarUpdate(this, 0, this.maxSurroundingBookshelfLevel);
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for(ICrafting icrafting : this.crafters) {
            if (this.lastMaxSurroundingBookshelfLevel != this.maxSurroundingBookshelfLevel) {
                icrafting.sendProgressBarUpdate(this, 0, this.maxSurroundingBookshelfLevel);
            }
        }

        this.lastMaxSurroundingBookshelfLevel = this.maxSurroundingBookshelfLevel;
    }

    public void updateProgressBar(int iVariableIndex, int iValue) {
        if (iVariableIndex == 0) {
            this.maxSurroundingBookshelfLevel = iValue;
        }

    }
}

