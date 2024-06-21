package mods.betterwithpatches.mixins.item;

import net.minecraft.block.BlockNewLeaf;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BlockNewLeaf.class)
public abstract class BlockDropsNewLeaf {
    @Overwrite
    protected void func_150124_c(World p_150124_1_, int p_150124_2_, int p_150124_3_, int p_150124_4_, int p_150124_5_, int p_150124_6_) {}
}
