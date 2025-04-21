package mods.betterwithpatches.util;

import net.minecraft.util.Vec3;

public class MiscUtils {
    public static Vec3 convertBlockFacingToVector(int iFacing) {
        Vec3 vector = Vec3.createVectorHelper(0.0, 0.0, 0.0);
        switch (iFacing) {
            case 0: {
                vector.yCoord += -1.0;
                break;
            }
            case 1: {
                vector.yCoord += 1.0;
                break;
            }
            case 2: {
                vector.zCoord -= 1.0;
                break;
            }
            case 3: {
                vector.zCoord += 1.0;
                break;
            }
            case 4: {
                vector.xCoord -= 1.0;
                break;
            }
            default: {
                vector.xCoord += 1.0;
            }
        }
        return vector;
    }
}
