package mods.betterwithpatches.util;

import net.minecraft.block.Block;

public class ModStepSound
        extends Block.SoundType {
    public final String namespace;
    public final String modSoundName;
    public final boolean hasSeparatePlaceSound;

    public ModStepSound(String sound, float volume, float pitch) {
        this(sound, volume, pitch, false);
    }

    public ModStepSound(String sound, float volume, float pitch, boolean hasSeparatePlaceSound) {
        super(sound, volume, pitch);
        if (sound.contains(":")) {
            String[] splitSound = sound.split(":");
            this.namespace = splitSound[0];
            this.modSoundName = splitSound[1];
        } else {
            this.namespace = "minecraft";
            this.modSoundName = sound;
        }
        this.hasSeparatePlaceSound = hasSeparatePlaceSound;
    }

    @Override
    public String getBreakSound() {
        return this.namespace + ":block." + this.modSoundName + ".break";
    }


    public String getStepSound() {
        return this.namespace + ":block." + this.modSoundName + ".step";
    }


    public String getPlaceSound() {
        if (this.hasSeparatePlaceSound) {
            return this.namespace + ":block." + this.modSoundName + ".place";
        }
        return this.getBreakSound();
    }
}
