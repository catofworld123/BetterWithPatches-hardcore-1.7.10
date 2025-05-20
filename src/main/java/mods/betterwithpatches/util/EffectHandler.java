package mods.betterwithpatches.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class EffectHandler {
    public static Map<Integer, Effect> effectMap = new HashMap<>();

    public static boolean playEffect(int effectID, Minecraft mcInstance, World world, EntityPlayer player, int x, int y, int z, int data) {
        Effect effect = effectMap.get(effectID);

        if (effect != null) {
            effect.playEffect(mcInstance, world, player, x , y, z, data);
            return true;
        }

        return false;
    }

    public interface Effect {
        void playEffect(Minecraft mcInstance, World world, EntityPlayer player, int x, int y, int z, int data);
    }
}
