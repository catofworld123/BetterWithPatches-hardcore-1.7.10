package mods.betterwithpatches.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.block.tile.TileEntityBlockTreeStage2;
import mods.betterwithpatches.block.tile.TileEntityBlockTreeStage3;
import mods.betterwithpatches.proxy.ClientProxy;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;


public class BlockTreeStage3 extends BlockContainer {

    public BlockTreeStage3() {
        super(Material.wood);
        this.setBlockName("bwm:blockTreeStage3");
        this.setCreativeTab(BWPRegistry.bwpTab);
        this.setHardness(3.1f);
        this.setResistance(2f);
        this.setHarvestLevel("axe", 1);
        this.setStepSound(Block.soundTypeWood);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBlockTreeStage3();
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
        return ClientProxy.renderBlockTreeStage3;
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
            this.setBlockBounds(0.1875F, 0F, 0.1875F, 0.8125F, 1F, 0.8125F);
        }

    }
}