package mods.betterwithpatches.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.block.tile.TileEntityCampfire;
import mods.betterwithpatches.proxy.ClientProxy;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import javax.swing.*;
import java.util.Random;


public class Campfire extends BlockContainer {
    public final int fireLevel;

    public static final int CAMPFIRE_FUEL_STATE_NORMAL = 0;
    public static final int CAMPFIRE_FUEL_STATE_BURNED_OUT = 1;
    public static final int CAMPFIRE_FUEL_STATE_SMOULDERING = 2;

    @SideOnly(Side.CLIENT)
    public IIcon[] icons;


    public Campfire(int iFireLevel) {

        super(Material.wood);
        fireLevel = iFireLevel;
        this.setBlockName("bwm:campfire");
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
        this.icons[3] = reg.registerIcon("betterwithpatches:OakBark");
        this.icons[4] = reg.registerIcon("placeholder");
        this.icons[5] = reg.registerIcon("betterwithpatches:fcBlockCampfire_burned");
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
              if (meta == 4) {
            return this.icons[1];
              }
              if (meta == 5) {
                    return this.icons[2];
             }
        if (meta == 6) {
            return this.icons[1];
        }
        if (meta == 7) {
            return this.icons[1];
        }
        if (meta == 8) {
            return this.icons[1];
        }
        if (meta == 9) {
            return this.icons[1];
        }
        if (meta == 10) {
            return this.icons[2];
        }


              else return this.icons[0];

    }



    public void randomDisplayTick( World world, int i, int j, int k, Random rand )
    {
        if (world.getBlockMetadata(i, j, k) > 1 && world.getBlockMetadata(i, j, k) < 5)
        {
            for (int iTempCount = 0; iTempCount < world.getBlockMetadata(i, j, k); iTempCount++ )
            {
                double xPos = i + rand.nextFloat();
                double yPos = j + 0.5F + ( rand.nextFloat() * 0.5F );
                double zPos = k + rand.nextFloat();

                world.spawnParticle( "smoke", xPos, yPos, zPos, 0D, 0D, 0D );
            }

            if ( rand.nextInt(24) == 0 )
            {
                float fVolume = (world.getBlockMetadata(i,j,k) * 0.25F ) + rand.nextFloat();

                world.playSound( i + 0.5D, j + 0.5D, k + 0.5D, "fire.fire",
                        fVolume, rand.nextFloat() * 0.7F + 0.3F, false );
            }

            TileEntityCampfire tileEntity = (TileEntityCampfire) world.getTileEntity( i, j, k );



        }
        else if (world.getBlockMetadata(i,j,k) == 5 || getFuelState(world, i, j, k) == CAMPFIRE_FUEL_STATE_SMOULDERING || world.getBlockMetadata(i,j,k) == 10)
        {
            double xPos = (double)i + 0.375D + ( rand.nextDouble() * 0.25D );
            double yPos = (double)j + 0.25D + ( rand.nextDouble() * 0.25D );
            double zPos = (double)k + 0.375D + ( rand.nextDouble() * 0.25D );

            world.spawnParticle( "smoke", xPos, yPos, zPos, 0D, 0D, 0D );
        }






        if (world.getBlockMetadata(i, j, k) == 5)
        {
            for (int iTempCount = 0; iTempCount < world.getBlockMetadata(i, j, k) - 4; iTempCount++ )
            {
                double xPos = i + rand.nextFloat();
                double yPos = j + 0.5F + ( rand.nextFloat() * 0.5F );
                double zPos = k + rand.nextFloat();

                world.spawnParticle( "smoke", xPos, yPos, zPos, 0D, 0D, 0D );
            }




            TileEntityCampfire tileEntity =
                    (TileEntityCampfire) world.getTileEntity( i, j, k );


        }
        else if (world.getBlockMetadata(i,j,k) == 5 || getFuelState(world, i, j, k) == CAMPFIRE_FUEL_STATE_SMOULDERING || world.getBlockMetadata(i,j,k) == 10)
        {
            double xPos = (double)i + 0.375D + ( rand.nextDouble() * 0.25D );
            double yPos = (double)j + 0.25D + ( rand.nextDouble() * 0.25D );
            double zPos = (double)k + 0.375D + ( rand.nextDouble() * 0.25D );

            world.spawnParticle( "smoke", xPos, yPos, zPos, 0D, 0D, 0D );
        }

        if (world.getBlockMetadata(i, j, k) > 6 && world.getBlockMetadata(i,j,k) < 10)
        {
            for (int iTempCount = 0; iTempCount < world.getBlockMetadata(i, j, k) - 6; iTempCount++ )
            {
                double xPos = i + rand.nextFloat();
                double yPos = j + 0.5F + ( rand.nextFloat() * 0.5F );
                double zPos = k + rand.nextFloat();

                world.spawnParticle( "smoke", xPos, yPos, zPos, 0D, 0D, 0D );
            }

            if ( rand.nextInt(24) == 0 )
            {
                float fVolume = ((world.getBlockMetadata(i,j,k)-6) * 0.25F ) + rand.nextFloat();

                world.playSound( i + 0.5D, j + 0.5D, k + 0.5D, "fire.fire",
                        fVolume, rand.nextFloat() * 0.7F + 0.3F, false );
            }

            TileEntityCampfire tileEntity =
                    (TileEntityCampfire) world.getTileEntity( i, j, k );


        }
        else if (world.getBlockMetadata(i,j,k) == 5 || getFuelState(world, i, j, k) == CAMPFIRE_FUEL_STATE_SMOULDERING || world.getBlockMetadata(i,j,k) == 10)
        {
            double xPos = (double)i + 0.375D + ( rand.nextDouble() * 0.25D );
            double yPos = (double)j + 0.25D + ( rand.nextDouble() * 0.25D );
            double zPos = (double)k + 0.375D + ( rand.nextDouble() * 0.25D );

            world.spawnParticle( "smoke", xPos, yPos, zPos, 0D, 0D, 0D );
        }



        if (world.getBlockMetadata(i, j, k) == 10)
        {
            for (int iTempCount = 0; iTempCount < world.getBlockMetadata(i, j, k) - 9; iTempCount++ )
            {
                double xPos = i + rand.nextFloat();
                double yPos = j + 0.5F + ( rand.nextFloat() * 0.5F );
                double zPos = k + rand.nextFloat();

                world.spawnParticle( "smoke", xPos, yPos, zPos, 0D, 0D, 0D );
            }



            TileEntityCampfire tileEntity =
                    (TileEntityCampfire) world.getTileEntity( i, j, k );


        }
        else if (world.getBlockMetadata(i,j,k) == 5 || getFuelState(world, i, j, k) == CAMPFIRE_FUEL_STATE_SMOULDERING || world.getBlockMetadata(i,j,k) == 10)
        {
            double xPos = (double)i + 0.375D + ( rand.nextDouble() * 0.25D );
            double yPos = (double)j + 0.25D + ( rand.nextDouble() * 0.25D );
            double zPos = (double)k + 0.375D + ( rand.nextDouble() * 0.25D );

            world.spawnParticle( "smoke", xPos, yPos, zPos, 0D, 0D, 0D );
        }


    }
    public static int getFuelState(IBlockAccess blockAccess, int x, int y, int z)
    {
        return getFuelState(blockAccess.getBlockMetadata(x, y, z));
    }
    public static int getFuelState(int iMetadata)
    {
        return ( iMetadata & 12 ) >> 2;
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
