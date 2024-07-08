package mods.betterwithpatches.mixins.item;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;



@Mixin(BlockLeaves.class)
public abstract class Saplings extends BlockLeavesBase {
    protected Saplings(Material p_i45433_1_, boolean p_i45433_2_) {
        super(p_i45433_1_, p_i45433_2_);
    }

    /**
     * @author catofworld123
     * @reason nerfsaplingspls
     */
    @Shadow
    protected int func_150123_b(int metadata) {
        return 0;
    }

    @Shadow
    protected void func_150124_c(World world, int x, int y, int z, int metadata, int chance){

    }
    /**
     *
     * @reason i needed saplings
     * @author me
     */

    @Overwrite (remap=false)
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        {
            ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
            int chance = this.func_150123_b(metadata);

            if (fortune > 0)
            {
                chance -= 1 << fortune;
                if (chance < 10) chance = 5;
            }

            if (world.rand.nextInt(chance) == 0)
                ret.add(new ItemStack(this.getItemDropped(metadata, world.rand, fortune), 1, this.damageDropped(metadata)));

            chance = 100;
            if (fortune > 0)
            {
                chance -= 5 << fortune;
                if (chance < 40) chance = 20;
            }

            this.captureDrops(true);
            this.func_150124_c(world, x, y, z, metadata, chance);
            ret.addAll(this.captureDrops(false));
            return ret;


        }}}
