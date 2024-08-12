package mods.betterwithpatches.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.block.tile.TileEntityCampfire;
import mods.betterwithpatches.proxy.ClientProxy;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;


public class Campfire extends BlockContainer {
    @SideOnly(Side.CLIENT)
    public IIcon[] icons;


    public Campfire() {
        super(Material.wood);
        this.setBlockName("bwm:campfire");
        this.setCreativeTab(BWPRegistry.bwpTab);
        this.setHardness(1.5f);
        this.setResistance(2f);
        this.setHarvestLevel("axe", 1);
        this.setStepSound(Block.soundTypeWood);



    }
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityCampfire();
    }
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    @Override
    protected String getTextureName() {
        return "betterwithpatches:OakBark";
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        this.icons = new IIcon[7];
        this.icons[0] = reg.registerIcon("placeholder");
        this.icons[1] = reg.registerIcon("betterwithpatches:BlockCampfire");
        this.icons[2] = reg.registerIcon("betterwithpatches:BlockCampfireNoFire");
        this.icons[3] = reg.registerIcon("betterwithpatches:Fireworkaem");
        this.icons[4] = reg.registerIcon("betterwithpatches:Fireworkaemv2");
        this.icons[5] = reg.registerIcon("placeholder");
        this.icons[6] = reg.registerIcon("placeholder");
    }
    @Override
    public IIcon getIcon(int side,int meta ) {

            if (meta == 0) {
                return this.icons[1];
            }
            if (meta == 2) {
                return this.icons[1];
            }
            if (meta == 3) {
            return this.icons[1];
            }
              else return this.icons[0];

    }




    @Override
    public int getRenderType() {
        return ClientProxy.renderCampfire;
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ) {
        return true;
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
            this.setBlockBounds(0F, 0F, 0F, 1F, 0.5F, 1F);
        }

    }

}
