package mods.betterwithpatches.block;

import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.block.tile.ArcaneVesselTileEntity;
import mods.betterwithpatches.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ArcaneVesselBlock extends VesselBlock {
    private IIcon iconContents;

    public ArcaneVesselBlock() {
        super(Material.iron);
        this.setHardness(3.5f);
        this.setResistance(10.0f);
        this.setStepSound(Block.soundTypeMetal);
        this.setCreativeTab(BWPRegistry.bwpTab);
        this.setBlockName("bwm:fcBlockArcaneVessel");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new ArcaneVesselTileEntity();
    }

    @Override
    public void breakBlock(World world, int i, int j, int k, Block iBlockID, int iMetadata) {
        TileEntity tileEnt = world.getTileEntity(i, j, k);
        if (tileEnt != null && tileEnt instanceof ArcaneVesselTileEntity) {
            ArcaneVesselTileEntity vesselEnt = (ArcaneVesselTileEntity)tileEnt;
            vesselEnt.ejectContentsOnBlockBreak();
        }
        super.breakBlock(world, i, j, k, iBlockID, iMetadata);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
        if (!world.isRemote && entity instanceof EntityXPOrb) {
            this.onEntityXPOrbCollidedWithBlock(world, i, j, k, (EntityXPOrb) entity);
            world.func_147453_f(i, j, k, this);
        }

    }

    public void onEntityXPOrbCollidedWithBlock(World world, int i, int j, int k, EntityXPOrb entityXPOrb) {
        if (entityXPOrb.isDead) {
            return;
        }
        if (this.isMechanicalOn(world, i, j, k)) {
            return;
        }
        float fVesselHeight = 1.0f;
        AxisAlignedBB collectionZone = AxisAlignedBB.getBoundingBox(i, (float)j + 1.0f, k, i + 1, (float)j + 1.0f + 0.05f, k + 1);
        if (entityXPOrb.boundingBox.intersectsWith(collectionZone)) {
            ArcaneVesselTileEntity vesselTileEnt;
            boolean bSwallowed = false;
            TileEntity tileEnt = world.getTileEntity(i, j, k);
            if (tileEnt instanceof ArcaneVesselTileEntity) {
                ((ArcaneVesselTileEntity) tileEnt).attemptToSwallowXPOrb(world,i,j,k,entityXPOrb);
                world.playAuxSFX(2231, i, j, k, 0);
            }

        }
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
        ArcaneVesselTileEntity tileEntity = (ArcaneVesselTileEntity)par1World.getTileEntity(par2, par3, par4);
        int currentXP = tileEntity.getContainedTotalExperience();
        float xpPercent = (float)currentXP / 1000.0f;
        return MathHelper.floor_float(xpPercent * 14.0f) + (currentXP > 0 ? 1 : 0);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        IIcon sideIcon;
        super.registerBlockIcons(register);
        this.blockIcon = sideIcon = register.registerIcon("betterwithpatches:dragon_vessel");
        this.iconWideBandBySideArray[0] = this.iconCenterColumnBySideArray[0] = register.registerIcon("betterwithpatches:dragon_vessel_bottom");
        this.iconCenterColumnBySideArray[1] = register.registerIcon("betterwithpatches:dragon_vessel_top");
        this.iconWideBandBySideArray[1] = register.registerIcon("betterwithpatches:dragon_vessel_top_band");
        this.iconWideBandBySideArray[2] = this.iconCenterColumnBySideArray[2] = sideIcon;
        this.iconWideBandBySideArray[3] = this.iconCenterColumnBySideArray[3] = sideIcon;
        this.iconWideBandBySideArray[4] = this.iconCenterColumnBySideArray[4] = sideIcon;
        this.iconWideBandBySideArray[5] = this.iconCenterColumnBySideArray[5] = sideIcon;
        this.iconInteriorBySideArray[0] = this.iconWideBandBySideArray[0];
        this.iconInteriorBySideArray[1] = this.iconWideBandBySideArray[0];
        this.iconInteriorBySideArray[2] = this.iconWideBandBySideArray[0];
        this.iconInteriorBySideArray[3] = this.iconWideBandBySideArray[0];
        this.iconInteriorBySideArray[4] = this.iconWideBandBySideArray[0];
        this.iconInteriorBySideArray[5] = this.iconWideBandBySideArray[0];
        this.iconContents = register.registerIcon("betterwithpatches:dragon_vessel_xp");
    }

    @Override
    public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
        ArcaneVesselTileEntity vesselEntity = new ArcaneVesselTileEntity();
        int iContainedExperience;
        TileEntity tileEntity;
        super.renderBlock(renderBlocks, i, j, k);
        IBlockAccess blockAccess = renderBlocks.blockAccess;
        if (this.getFacing(blockAccess, i, j, k) == 1 && (tileEntity = blockAccess.getTileEntity(i, j, k)) instanceof ArcaneVesselTileEntity) {
            iContainedExperience = ((ArcaneVesselTileEntity)tileEntity).visualExperienceLevel;
            float fHeightRatio = (float)iContainedExperience / 10.0f;
            float fBottom = 0.1875f;
            float fTop = fBottom + 0.0625f + (0.875f - (fBottom + 0.0625f)) * fHeightRatio;
            renderBlocks.setRenderBounds(0.125, fBottom, 0.125, 0.875, fTop, 0.875);
            Tessellator tesselator = Tessellator.instance;
            tesselator.setColorOpaque(120,0,0);
            tesselator.setBrightness(240);
            renderBlocks.renderFaceYPos(this, i, j, k, this.iconContents);
        }
        return true;
    }
    @Override
    public int getRenderType() {
        return ClientProxy.renderVessel;
    }


}
