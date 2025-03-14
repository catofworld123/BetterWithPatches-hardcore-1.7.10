package mods.betterwithpatches.inventory.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;

public class InfernalEnchanterInventory extends InventoryBasic {
    final InfernalEnchanterContainer container;

    public InfernalEnchanterInventory(InfernalEnchanterContainer container, String name, int iNumSlots) {
        super(name, true, iNumSlots);
        this.container = container;
    }

    public void markDirty() {
        super.markDirty();
        this.container.onCraftMatrixChanged(this);
    }
}
