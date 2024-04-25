package mods.betterwithpatches.mixins;

import net.minecraft.block.BlockOldLeaf;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BlockOldLeaf.class)
public abstract class BlockDropsOldLeaf {
    @Overwrite
    protected void func_150124_c(World p_150124_1_, int p_150124_2_, int p_150124_3_, int p_150124_4_, int p_150124_5_, int p_150124_6_) {}
}

//func_150123_b