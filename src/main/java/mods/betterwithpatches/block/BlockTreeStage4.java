package mods.betterwithpatches.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.block.tile.TileEntityBlockTreeStage2;
import mods.betterwithpatches.block.tile.TileEntityBlockTreeStage3;
import mods.betterwithpatches.block.tile.TileEntityBlockTreeStage4;
import mods.betterwithpatches.proxy.ClientProxy;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;


public class BlockTreeStage4 extends BlockContainer {

    public BlockTreeStage4() {
        super(Material.wood);
        this.setBlockName("bwm:blockTreeStage4");
        this.setCreativeTab(BWPRegistry.bwpTab);
        this.setHardness(3.1f);
        this.setResistance(2f);
        this.setHarvestLevel("axe", 1);
        this.setStepSound(Block.soundTypeWood);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBlockTreeStage4();
    }
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    @Override
    protected String getTextureName() {
        return "betterwithpatches:StrippedOak";
    }



    @Override
    public int getRenderType() {
        return ClientProxy.renderBlockTreeStage4;
    }



    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return true;
    }



    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }


    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        {
            this.setBlockBounds(0.25F, 0F, 0.25F, 0.75F, 1F, 0.75F);
        }

    }
}