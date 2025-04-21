package mods.betterwithpatches.util;

import betterwithmods.util.BlockPosition;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class WorldUtils {
    public WorldUtils() {
    }

    public static boolean isReplaceableBlock(World world, int i, int j, int k) {
        Block block = world.getBlock(i, j, k);
        return block == null || block.getMaterial().isReplaceable();
    }

    public static int rotateFacingForCoordBaseMode(int iFacing, int iCoordBaseMode) {
        if (iCoordBaseMode == 0) {
            if (iFacing == 2) {
                return 3;
            }

            if (iFacing == 3) {
                return 2;
            }
        } else if (iCoordBaseMode == 1) {
            if (iFacing == 2) {
                return 4;
            }

            if (iFacing == 3) {
                return 5;
            }

            if (iFacing == 4) {
                return 2;
            }

            if (iFacing == 5) {
                return 3;
            }
        } else if (iCoordBaseMode == 3) {
            if (iFacing == 2) {
                return 5;
            }

            if (iFacing == 3) {
                return 4;
            }

            if (iFacing == 4) {
                return 2;
            }

            if (iFacing == 5) {
                return 3;
            }
        }

        return iFacing;
    }


    public static void sendPacketToPlayer(NetHandlerPlayServer handler, Packet packet) {
        handler.sendPacket(packet);
    }

}
