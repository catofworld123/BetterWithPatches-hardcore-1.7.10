package mods.betterwithpatches.util;


import net.minecraft.nbt.NBTTagCompound;

public interface TileEntityDataPacketHandler
{
    public void readNBTFromPacket( NBTTagCompound nbttagcompound );
}
