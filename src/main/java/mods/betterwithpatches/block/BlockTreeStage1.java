package mods.betterwithpatches.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.block.tile.TileEntityBlockTreeStage1;
import mods.betterwithpatches.proxy.ClientProxy;
import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockTreeStage1 extends BlockContainer {

    public BlockTreeStage1() {
        super(Material.wood);
        this.setBlockName("bwm:blockTreeStage1");
        this.setCreativeTab(BWPRegistry.bwpTab);
        this.setHardness(1.9f);
        this.setResistance(2f);
        this.setHarvestLevel("axe", 1);
        this.setStepSound(Block.soundTypeWood);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBlockTreeStage1();
    }
    @Override
    public boolean isOpaqueCube() {
        return false;
    }


    @Override
    public int getRenderType() {
        return ClientProxy.renderBlockTreeStage1;
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
    protected String getTextureName() {
        return "betterwithpatches:fcBlockLogStrippedOak_side";
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        {
            this.setBlockBounds(0.0F, 0.0F, 0.125F, 1.0F, 1.0F, 0.875F);
        }

    }}
