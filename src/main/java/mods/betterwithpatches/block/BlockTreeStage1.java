package mods.betterwithpatches.block;

import betterwithmods.BWRegistry;
import betterwithmods.blocks.BlockAesthetic;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.block.tile.TileEntityBlockTreeStage1;
import mods.betterwithpatches.proxy.ClientProxy;
import mods.betterwithpatches.util.BWPConstants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockTreeStage1 extends BlockContainer {
    public IIcon[] icons = new IIcon[6];

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
    protected String getTextureName() {
        return "betterwithpatches:StrippedOak";
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
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        {
            this.setBlockBounds(0.0F, 0.0F, 0.125F, 1.0F, 1.0F, 0.875F);
        }

    }
}