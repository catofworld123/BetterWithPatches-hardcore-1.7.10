package mods.betterwithpatches.block;

import cpw.mods.fml.common.network.NetworkRegistry;
import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.BetterWithPatches;
import mods.betterwithpatches.block.tile.InfernalEnchanterTileEntity;
import mods.betterwithpatches.inventory.container.InfernalEnchanterContainer;
import mods.betterwithpatches.proxy.ClientProxy;
import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class InfernalEnchanterBlock extends BlockContainer {
    public static final float BLOCK_HEIGHT = 0.5F;
    public static final float CANDLE_HEIGHT = 0.25F;
    private static final float BLOCK_HARDNESS = 100.0F;
    private static final float BLOCK_EXPLOSION_RESISTANCE = 2000.0F;
    private static final int HORIZONTAL_BOOK_SHELF_CHECK_DISTANCE = 8;
    private static final int VERTICAL_POSITIVE_BOOK_SHELF_CHECK_DISTANCE = 8;
    private static final int VERTICAL_NEGATIVE_BOOK_SHELF_CHECK_DISTANCE = 8;

    private IIcon[] iconBySideArray;

    private IIcon iconCandle;

    public InfernalEnchanterBlock() {
        super(Material.iron);
        this.setBlockBounds((float)0.0F, (float)0.0F, (float)0.0F, (float)1.0F, (float)0.5F, (float)1.0F);
        this.setLightOpacity(0);
        this.setBlockName("bwm:infernalenchanter");
        this.setHardness(100.0F);
        this.setResistance(2000.0F);
        this.setStepSound(soundTypeMetal);
        this.setCreativeTab(BWPRegistry.bwpTab);

    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
        if (!world.isRemote && player instanceof EntityPlayerMP) {
           InfernalEnchanterContainer container = new InfernalEnchanterContainer(player.inventory, world, i, j, k);
            player.openGui(BWPConstants.MODID, BWPRegistry.ENUM_IDS.InfernalEnchanter.ordinal(),world, i,j,k);
        }

        return true;
    }

    public boolean canRotateOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
        return true;
    }

    public boolean canTransmitRotationHorizontallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
        return true;
    }

    public boolean canTransmitRotationVerticallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
        return true;
    }

    private boolean isValidBookshelf(World world, int i, int j, int k) {
        Block iBlockID = world.getBlock(i, j, k);
        return iBlockID == Blocks.bookshelf && (world.isAirBlock(i + 1, j, k) || world.isAirBlock(i - 1, j, k) || world.isAirBlock(i, j, k + 1) || world.isAirBlock(i, j, k - 1));
    }


    public void registerBlockIcons(IIconRegister register) {
        this.iconBySideArray = new IIcon[6];
        IIcon bottomIcon = register.registerIcon("betterwithpatches:infernal_enchanter_bottom");
        this.blockIcon = bottomIcon;
        this.iconBySideArray[0] = bottomIcon;
        this.iconBySideArray[1] = register.registerIcon("betterwithpatches:infernal_enchanter_top");
        IIcon sideIcon = register.registerIcon("betterwithpatches:infernal_enchanter");
        this.iconBySideArray[2] = sideIcon;
        this.iconBySideArray[3] = sideIcon;
        this.iconBySideArray[4] = sideIcon;
        this.iconBySideArray[5] = sideIcon;
        this.iconCandle = register.registerIcon("betterwithpatches:black_candle_lit");
    }


    public IIcon getIcon(int iSide, int iMetadata) {
        return this.iconBySideArray[iSide];
    }


    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
        return true;
    }

    public void randomDisplayTick(World world, int i, int j, int k, Random random) {
        super.randomDisplayTick(world, i, j, k, random);
        this.displayMagicLetters(world, i, j, k, random);
    }


    private void displayMagicLetters(World world, int i, int j, int k, Random rand) {
        TileEntity tileEntity = world.getTileEntity(i, j, k);
        if (tileEntity != null && tileEntity instanceof InfernalEnchanterTileEntity) {
            InfernalEnchanterTileEntity enchanterEntity = (InfernalEnchanterTileEntity)world.getTileEntity(i,j,k);
            if (enchanterEntity.playerNear) {
                for(int iTempCount = 0; iTempCount < 64; ++iTempCount) {
                    int iTargetI = rand.nextInt(17) - 8 + i;
                    int iTargetJ = rand.nextInt(17) - 8 + j;
                    int iTargetK = rand.nextInt(17) - 8 + k;
                    if (this.isValidBookshelf(world, iTargetI, iTargetJ, iTargetK)) {
                        Vec3 velocity = Vec3.createVectorHelper((double)(iTargetI - i), (double)(iTargetJ - j), (double)(iTargetK - k));
                        world.spawnParticle("enchantmenttable", (double)i + (double)0.5F, (double)j + (double)0.5F, (double)k + (double)0.5F, velocity.xCoord, velocity.yCoord, velocity.zCoord);
                    }
                }
            }
        }

    }


    public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
        IBlockAccess blockAccess = renderer.blockAccess;
        renderer.setRenderBounds(this.getBlockBoundsMinX(), this.getBlockBoundsMinY(), this.getBlockBoundsMinZ(), this.getBlockBoundsMaxX(), this.getBlockBoundsMaxY(), this.getBlockBoundsMaxZ());
        renderer.renderStandardBlock(this, i, j, k);
        Tessellator tess = Tessellator.instance;
        tess.setBrightness(this.getMixedBrightnessForBlock(blockAccess, i, j, k));
        tess.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        IIcon icon = this.iconCandle;
        double minX = (double)((float)i + 0.0625F);
        double maxX = (double)((float)i + 0.1875F);
        double minY = (double)((float)j + 0.5F);
        double maxY = (double)((float)j + 0.5F + 0.25F);
        double minZ = (double)((float)k + 0.0625F);
        double maxZ = (double)((float)k + 0.1875F);
        double minU = (double)icon.getInterpolatedU((double)0.0F);
        double maxU = (double)icon.getInterpolatedU((double)2.0F);
        double minV = (double)icon.getInterpolatedV((double)8.0F);
        double maxV = (double)icon.getInterpolatedV((double)14.0F);
        this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
        this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
        minX = (double)((float)i + 0.8125F);
        maxX = (double)((float)i + 0.9375F);
        minZ = (double)((float)k + 0.0625F);
        maxZ = (double)((float)k + 0.1875F);
        this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
        this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
        minX = (double)((float)i + 0.0625F);
        maxX = (double)((float)i + 0.1875F);
        minZ = (double)((float)k + 0.8125F);
        maxZ = (double)((float)k + 0.9375F);
        this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
        this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
        minX = (double)((float)i + 0.8125F);
        maxX = (double)((float)i + 0.9375F);
        minZ = (double)((float)k + 0.8125F);
        maxZ = (double)((float)k + 0.9375F);
        this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
        this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
        return true;
    }


    private boolean drawCandle(Tessellator tess, double minX, double maxX, double minY, double maxY, double minZ, double maxZ, double minU, double maxU, double minV, double maxV, IIcon icon) {
        tess.addVertexWithUV(minX, minY, minZ, minU, maxV);
        tess.addVertexWithUV(minX, minY, maxZ, maxU, maxV);
        tess.addVertexWithUV(minX, maxY, maxZ, maxU, minV);
        tess.addVertexWithUV(minX, maxY, minZ, minU, minV);
        tess.addVertexWithUV(maxX, minY, maxZ, minU, maxV);
        tess.addVertexWithUV(maxX, minY, minZ, maxU, maxV);
        tess.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
        tess.addVertexWithUV(maxX, maxY, maxZ, minU, minV);
        tess.addVertexWithUV(maxX, minY, minZ, minU, maxV);
        tess.addVertexWithUV(minX, minY, minZ, maxU, maxV);
        tess.addVertexWithUV(minX, maxY, minZ, maxU, minV);
        tess.addVertexWithUV(maxX, maxY, minZ, minU, minV);
        tess.addVertexWithUV(minX, minY, maxZ, minU, maxV);
        tess.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
        tess.addVertexWithUV(maxX, maxY, maxZ, maxU, minV);
        tess.addVertexWithUV(minX, maxY, maxZ, minU, minV);
        minU = (double)icon.getMinU();
        maxU = (double)icon.getInterpolatedU((double)2.0F);
        minV = (double)icon.getInterpolatedV((double)6.0F);
        maxV = (double)icon.getInterpolatedV((double)8.0F);
        tess.addVertexWithUV(minX, maxY, maxZ, minU, maxV);
        tess.addVertexWithUV(maxX, maxY, maxZ, maxU, maxV);
        tess.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
        tess.addVertexWithUV(minX, maxY, minZ, minU, minV);
        tess.addVertexWithUV(minX, minY, minZ, minU, minV);
        tess.addVertexWithUV(maxX, minY, minZ, maxU, minV);
        tess.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
        tess.addVertexWithUV(minX, minY, maxZ, minU, maxV);
        return true;
    }

    private boolean drawWick(Tessellator tess, double minX, double maxX, double minY, double maxY, double minZ, double maxZ, double minU, double maxU, double minV, double maxV, IIcon icon) {
        minU = (double)icon.getInterpolatedU((double)0.0F);
        maxU = (double)icon.getInterpolatedU((double)1.0F);
        minV = (double)icon.getInterpolatedV((double)5.0F);
        maxV = (double)icon.getInterpolatedV((double)6.0F);
        minX += (double)0.046875F;
        maxX = minX + (double)0.03125F;
        minY = maxY;
        maxY += (double)0.0625F;
        minZ += (double)0.046875F;
        maxZ = minZ + (double)0.03125F;
        tess.addVertexWithUV(minX, minY, minZ, minU, maxV);
        tess.addVertexWithUV(minX, minY, maxZ, maxU, maxV);
        tess.addVertexWithUV(minX, maxY, maxZ, maxU, minV);
        tess.addVertexWithUV(minX, maxY, minZ, minU, minV);
        tess.addVertexWithUV(maxX, minY, maxZ, minU, maxV);
        tess.addVertexWithUV(maxX, minY, minZ, maxU, maxV);
        tess.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
        tess.addVertexWithUV(maxX, maxY, maxZ, minU, minV);
        tess.addVertexWithUV(maxX, minY, minZ, minU, maxV);
        tess.addVertexWithUV(minX, minY, minZ, maxU, maxV);
        tess.addVertexWithUV(minX, maxY, minZ, maxU, minV);
        tess.addVertexWithUV(maxX, maxY, minZ, minU, minV);
        tess.addVertexWithUV(minX, minY, maxZ, minU, maxV);
        tess.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
        tess.addVertexWithUV(maxX, maxY, maxZ, maxU, minV);
        tess.addVertexWithUV(minX, maxY, maxZ, minU, minV);
        tess.addVertexWithUV(minX, maxY, maxZ, minU, maxV);
        tess.addVertexWithUV(maxX, maxY, maxZ, maxU, maxV);
        tess.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
        tess.addVertexWithUV(minX, maxY, minZ, minU, minV);
        tess.addVertexWithUV(minX, minY, minZ, minU, minV);
        tess.addVertexWithUV(maxX, minY, minZ, maxU, minV);
        tess.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
        tess.addVertexWithUV(minX, minY, maxZ, minU, maxV);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new InfernalEnchanterTileEntity();
    }
    @Override
    public int getRenderType() {
        return ClientProxy.renderInfernalEnchanter;
    }
}
