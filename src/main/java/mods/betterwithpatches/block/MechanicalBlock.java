package mods.betterwithpatches.block;

import net.minecraft.world.World;

public interface MechanicalBlock {
    public boolean canOutputMechanicalPower();

    public boolean canInputMechanicalPower();

    public boolean isInputtingMechanicalPower(World var1, int var2, int var3, int var4);

    public boolean isOutputtingMechanicalPower(World var1, int var2, int var3, int var4);

    public boolean canInputAxlePowerToFacing(World var1, int var2, int var3, int var4, int var5);

    public void overpower(World var1, int var2, int var3, int var4);
}
