package mods.betterwithpatches.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.tileentity.*;
import sun.security.krb5.internal.NetClient;

import java.io.IOException;

public class NetClientHanler extends NetClient {
    private Minecraft mc;
    private WorldClient worldClient;


    public void handleTileEntityData(Packet132TileEntityData par1Packet132TileEntityData)
    {
        if (this.mc.theWorld.blockExists(par1Packet132TileEntityData.xPosition, par1Packet132TileEntityData.yPosition, par1Packet132TileEntityData.zPosition))
        {
            TileEntity var2 = this.mc.theWorld.getTileEntity(par1Packet132TileEntityData.xPosition, par1Packet132TileEntityData.yPosition, par1Packet132TileEntityData.zPosition);

            if (var2 != null)
            {
                if (par1Packet132TileEntityData.actionType == 1 && var2 instanceof TileEntityMobSpawner)
                {
                    var2.readFromNBT(par1Packet132TileEntityData.customParam1);
                }
                else if (par1Packet132TileEntityData.actionType == 2 && var2 instanceof TileEntityCommandBlock)
                {
                    var2.readFromNBT(par1Packet132TileEntityData.customParam1);
                }
                else if (par1Packet132TileEntityData.actionType == 3 && var2 instanceof TileEntityBeacon)
                {
                    var2.readFromNBT(par1Packet132TileEntityData.customParam1);
                }
                else if (par1Packet132TileEntityData.actionType == 4 && var2 instanceof TileEntitySkull)
                {
                    var2.readFromNBT(par1Packet132TileEntityData.customParam1);
                }
                // FCMOD: Added (client only)
                else if ( par1Packet132TileEntityData.actionType == 1 && ( var2 instanceof TileEntityDataPacketHandler) )
                {
                    ((TileEntityDataPacketHandler)var2).readNBTFromPacket( par1Packet132TileEntityData.customParam1 );
                }
                // END FCMOD
            }
        }
    }
    @Override
    public void send(byte[] data) throws IOException {

    }

    @Override
    public byte[] receive() throws IOException {
        return new byte[0];
    }

    @Override
    public void close() throws IOException {

    }
}
