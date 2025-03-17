package mods.betterwithpatches.block;

import mods.betterwithpatches.BWPRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.stream.Metadata;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class CandleBlock
        extends Block {
    private int metaDropped;
    private String texture;
    private IIcon iconLit;

    public CandleBlock(int metaDropped, String name, String texture) {
        super(BWPRegistry.candleMaterial);
        this.setHardness(0.0f);
        this.setPicksEffectiveOn(true);
        this.setAxesEffectiveOn(true);
        this.setLightLevel(1.0f);
        this.setStepSound(BTWBlocks.smallObjectStepSound);
        this.setBlockName(name);
        this.setBlockTextureName(texture);
        this.texture = texture;
        this.metaDropped = metaDropped;
    }

    @Override
    public Item getItemDropped(int par1, Random rand, int par3) {
        return Item.getItemFromBlock(BWPRegistry.candle);
    }

    @Override
    public int damageDropped(int meta) {
        return this.metaDropped;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        Block blockBelowID = world.getBlock(x, y - 1, z);
        int blockBelowMetadata = world.getBlockMetadata(x, y - 1, z);
      //  if (blockBelowID == BWPRegistry.aestheticNonOpaque.blockID && blockBelowMetadata == 12) {
        //    return true;
       // }
        return World.doesBlockHaveSolidTopSurface(world,x, y - 1, z);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (player.getCurrentEquippedItem() == null) {
            Random rand = new Random();
            int metadata = world.getBlockMetadata(x, y, z);
            ItemStack stack = new ItemStack(this.getItemDropped(metadata, world.rand, 0), 1, this.damageDropped(metadata));
            if (player.inventory.addItemStackToInventory(stack)) {
                player.worldObj.playSoundAtEntity(player, "random.pop", 0.2f, ((rand.nextFloat() - rand.nextFloat()) * 0.7f + 1.0f) * 2.0f);
            } else if (!player.worldObj.isRemote) {
                EntityItem entityitem = new EntityItem(world, x, y, z, stack);
                float velocityFactor = 0.05f;
                entityitem.motionX = (float)world.rand.nextFloat() * 0.7f + 0.15f;
                entityitem.motionY = (float)world.rand.nextFloat() * 0.2f + 0.1f;
                entityitem.motionZ = (float)world.rand.nextFloat() * 0.7f + 0.15f;
                entityitem.delayBeforeCanPickup = 10;
                world.spawnEntityInWorld(entityitem);
            }
            this.setCandleCount(world, x, y, z, this.getCandleCount(metadata) - 1);
            return true;
        }
        return false;
    }


    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockID) {
        if (!this.canPlaceBlockAt(world, x, y, z)) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlock(x, y, z, Blocks.air);
        }
    }


    public boolean isBlockRestingOnThatBelow(IBlockAccess blockAccess, int x, int y, int z) {
        return true;
    }


    public void onNeighborDisrupted(World world, int x, int y, int z, int toFacing) {
        if (toFacing == 0) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlock(x, y, z, Blocks.air);
        }
    }


    public boolean getPreventsFluidFlow(World world, int x, int y, int z, Block fluidBlock) {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z) {
        int candleCount = this.getCandleCount(worldIn, x, y, z);
        switch (candleCount) {
            case 1: {
                setBlockBounds(0.4375F, 0.0F, 0.4375F, 0.5625F, 0.4375F, 0.5625F);
            }
            case 2: {
                setBlockBounds(0.3125F, 0.0F, 0.375F, 0.6875F, 0.4375F, 0.5625F);
            }
            case 3: {
                setBlockBounds(0.375F, 0.0F, 0.375F, 0.6875F, 0.4375F, 0.6875F);
            }
            case 4: {
                setBlockBounds(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.4375F, 0.625F);
            }
        }
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

    }



    @Override
    public int getLightValue(IBlockAccess blockAccess, int x, int y, int z) {
        if (this.isLit(blockAccess, x, y, z)) {
            int candleCount = this.getCandleCount(blockAccess, x, y, z);
            return (int)((float)this.lightValue * ((float)candleCount / 4.0f));
        }
        return 0;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int metadata, float chance, int fortuneModifier) {
        if (!world.isRemote) {
            ItemStack stack = new ItemStack(this.getItemDropped(metadata, world.rand, 0), 1, this.damageDropped(metadata));
            this.dropBlockAsItem(world, x, y, z, stack);
        }
    }


    public boolean canConvertBlock(ItemStack stack, World world, int x, int y, int z) {
        return true;
    }


    public boolean convertBlock(ItemStack stack, World world, int x, int y, int z, int fromSide) {
        int candleCount = this.getCandleCount(world, x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);
        stack = new ItemStack(this.getItemDropped(metadata, world.rand, 0), 1, this.damageDropped(metadata));
        this.setCandleCount(world, x, y, z, candleCount - 1);
        this.dropBlockAsItem(world, x, y, z, stack);
        return true;
    }


    public boolean getCanBeSetOnFireDirectly(IBlockAccess blockAccess, int x, int y, int z) {
        return !this.isLit(blockAccess, x, y, z);
    }


    public boolean setOnFireDirectly(World world, int x, int y, int z) {
        if (!this.isLit(world, x, y, z)) {
            this.setLit(world, x, y, z, true);
            world.playSoundEffect((double)x + 0.5, (double)y + 0.5, (double)z + 0.5, "mob.ghast.fireball", 0.1f + world.rand.nextFloat() * 0.1f, world.rand.nextFloat() * 0.25f + 1.25f);
            return true;
        }
        return false;
    }


    public boolean getCanBlockLightItemOnFire(IBlockAccess blockAccess, int x, int y, int z) {
        return this.isLit(blockAccess, x, y, z);
    }

    public int getCandleCount(IBlockAccess blockAccess, int x, int y, int z) {
        int meta = blockAccess.getBlockMetadata(x, y, z);
        return this.getCandleCount(meta);
    }

    public int getCandleCount(int metadata) {
        return (metadata & 3) + 1;
    }

    public void setCandleCount(World world, int x, int y, int z, int count) {
        if (count == 0) {
            world.setBlock(x, y, z, Blocks.air);
            return;
        }
        if (count > 4) {
            IllegalArgumentException e = new IllegalArgumentException("Cannot set candle count higher than 4");
            e.printStackTrace();
            count = 4;
        }
        int meta = world.getBlockMetadata(x, y, z);
        int newMeta = meta & 4;
        world.setBlockMetadataWithNotify(x, y, z, newMeta += count - 1,2);
    }

    public boolean isLit(IBlockAccess blockAccess, int x, int y, int z) {
        return blockAccess.getBlockMetadata(x, y, z) >> 2 == 1;
    }

    public void setLit(World world, int x, int y, int z, boolean isLit) {
        int meta = world.getBlockMetadata(x, y, z);
        int newMeta = isLit ? meta | 4 : meta & 3;
        world.setBlockMetadataWithNotify(x, y, z, newMeta,2);
    }

    public int setLit(int meta, boolean isLit) {
        int newMeta = isLit ? meta | 4 : meta & 3;
        return newMeta;
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        super.registerBlockIcons(register);
        this.iconLit = register.registerIcon(this.texture + "_lit");
    }

    @Override
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
        if (this.isLit(blockAccess, x, y, z)) {
            return this.iconLit;
        }
        return this.blockIcon;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        int metadata  = world.getBlockMetadata(x,y,z);
        ItemStack stack = new ItemStack(this.getItemDropped(metadata, world.rand, 0), 1, this.damageDropped(metadata));
        return stack;
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        if (!this.isLit(world, x, y, z)) {
            return;
        }
        int meta = world.getBlockMetadata(x, y, z);
        int candleCount = this.getCandleCount(world, x, y, z);
        double xPos = (float)x + 0.0625f;
        double yPos = (float)y + 0.5f;
        double zPos = (float)z + 0.0625f;
        switch (candleCount) {
            case 1: {
                xPos = (float)x + 0.5f;
                yPos = (float)y + 0.5f;
                zPos = (float)z + 0.5f;
                this.spawnParticles(world, xPos, yPos, zPos);
                break;
            }
            case 2: {
                xPos = (float)x + 0.625f;
                yPos = (float)y + 0.5f;
                zPos = (float)z + 0.5f;
                this.spawnParticles(world, xPos, yPos, zPos);
                xPos = (float)x + 0.375f;
                yPos = (float)y + 0.4375f;
                zPos = (float)z + 0.4375f;
                this.spawnParticles(world, xPos, yPos, zPos);
                break;
            }
            case 3: {
                xPos = (float)x + 0.625f;
                yPos = (float)y + 0.5f;
                zPos = (float)z + 0.5f;
                this.spawnParticles(world, xPos, yPos, zPos);
                xPos = (float)x + 0.4375f;
                yPos = (float)y + 0.4375f;
                zPos = (float)z + 0.4375f;
                this.spawnParticles(world, xPos, yPos, zPos);
                xPos = (float)x + 0.5f;
                yPos = (float)y + 0.3125f;
                zPos = (float)z + 0.625f;
                this.spawnParticles(world, xPos, yPos, zPos);
                break;
            }
            case 4: {
                xPos = (float)x + 0.625f;
                yPos = (float)y + 0.5f;
                zPos = (float)z + 0.375f;
                this.spawnParticles(world, xPos, yPos, zPos);
                xPos = (float)x + 0.4375f;
                yPos = (float)y + 0.4375f;
                zPos = (float)z + 0.375f;
                this.spawnParticles(world, xPos, yPos, zPos);
                xPos = (float)x + 0.5625f;
                yPos = (float)y + 0.3125f;
                zPos = (float)z + 0.5625f;
                this.spawnParticles(world, xPos, yPos, zPos);
                xPos = (float)x + 0.375f;
                yPos = (float)y + 0.375f;
                zPos = (float)z + 0.5625f;
                this.spawnParticles(world, xPos, yPos, zPos);
            }
        }
    }


    private void spawnParticles(World world, double xPos, double yPos, double zPos) {
        world.spawnParticle("fcsmallflame", xPos, yPos, zPos, 0.0, 0.0, 0.0);
        world.spawnParticle("fcsmallflame", xPos, yPos, zPos, 0.0, 0.0, 0.0);
        world.spawnParticle("fcsmallflame", xPos, yPos, zPos, 0.0, 0.0, 0.0);
    }

    public boolean renderBlock(RenderBlocks render, int x, int y, int z) {
        return this.renderBlockCandle(render, this, x, y, z);
    }

    private boolean renderBlockCandle(RenderBlocks render, Block block, int x, int y, int z) {
        Tessellator tess = Tessellator.instance;
        tess.setBrightness(block.getMixedBrightnessForBlock(render.blockAccess, x, y, z));
        tess.setColorOpaque_F(1.0f, 1.0f, 1.0f);
        int meta = render.blockAccess.getBlockMetadata(x, y, z);
        IIcon icon = this.getIcon(render.blockAccess, x, y, z, meta);
        int candleCount = this.getCandleCount(render.blockAccess, x, y, z);
        double minU = icon.getInterpolatedU(0.0);
        double maxU = icon.getInterpolatedU(2.0);
        double minV = icon.getInterpolatedV(8.0);
        double maxV = icon.getInterpolatedV(14.0);
        double minXOffset = 0.0;
        double maxXOffset = 0.125;
        double minYOffset = 0.0;
        double maxYOffset = 0.375;
        double minZOffset = 0.0;
        double maxZOffset = 0.125;
        double minX = (double)x + minXOffset;
        double maxX = (double)x + maxXOffset;
        double minY = (double)y + minYOffset;
        double maxY = (double)y + maxYOffset;
        double minZ = (double)z + minZOffset;
        double maxZ = (double)z + maxZOffset;
        switch (candleCount) {
            case 1: {
                minX = (double)x + 0.4375;
                maxX = (double)x + 0.5625;
                minY = (double)y + 0.0;
                maxY = (double)y + 0.375;
                minZ = (double)z + 0.4375;
                maxZ = (double)z + 0.5625;
                this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
                this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
                break;
            }
            case 2: {
                minX = (double)x + 0.5625;
                maxX = (double)x + 0.6875;
                minY = (double)y + 0.0;
                maxY = (double)y + 0.375;
                minZ = (double)z + 0.4375;
                maxZ = (double)z + 0.5625;
                this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
                this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
                minX = (double)x + 0.3125;
                maxX = (double)x + 0.4375;
                minY = (double)y + 0.0;
                maxY = (double)y + 0.3125;
                minZ = (double)z + 0.375;
                maxZ = (double)z + 0.5;
                maxV = icon.getInterpolatedV(13.0);
                this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
                this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
                break;
            }
            case 3: {
                minX = (double)x + 0.5625;
                maxX = (double)x + 0.6875;
                minY = (double)y + 0.0;
                maxY = (double)y + 0.375;
                minZ = (double)z + 0.4375;
                maxZ = (double)z + 0.5625;
                this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
                this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
                minX = (double)x + 0.375;
                maxX = (double)x + 0.5;
                minY = (double)y + 0.0;
                maxY = (double)y + 0.3125;
                minZ = (double)z + 0.375;
                maxZ = (double)z + 0.5;
                maxV = icon.getInterpolatedV(13.0);
                this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
                this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
                minX = (double)x + 0.4375;
                maxX = (double)x + 0.5625;
                minY = (double)y + 0.0;
                maxY = (double)y + 0.1875;
                minZ = (double)z + 0.5625;
                maxZ = (double)z + 0.6875;
                maxV = icon.getInterpolatedV(11.0);
                this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
                this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
                break;
            }
            case 4: {
                minX = (double)x + 0.5625;
                maxX = (double)x + 0.6875;
                minY = (double)y + 0.0;
                maxY = (double)y + 0.375;
                minZ = (double)z + 0.3125;
                maxZ = (double)z + 0.4375;
                this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
                this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
                minX = (double)x + 0.375;
                maxX = (double)x + 0.5;
                minY = (double)y + 0.0;
                maxY = (double)y + 0.3125;
                minZ = (double)z + 0.3125;
                maxZ = (double)z + 0.4375;
                maxV = icon.getInterpolatedV(13.0);
                this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
                this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
                minX = (double)x + 0.5;
                maxX = (double)x + 0.625;
                minY = (double)y + 0.0;
                maxY = (double)y + 0.1875;
                minZ = (double)z + 0.5;
                maxZ = (double)z + 0.625;
                maxV = icon.getInterpolatedV(11.0);
                this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
                this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
                minX = (double)x + 0.3125;
                maxX = (double)x + 0.4375;
                minY = (double)y + 0.0;
                maxY = (double)y + 0.25;
                minZ = (double)z + 0.5;
                maxZ = (double)z + 0.625;
                maxV = icon.getInterpolatedV(12.0);
                this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
                this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
            }
        }
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
        minU = icon.getMinU();
        maxU = icon.getInterpolatedU(2.0);
        minV = icon.getInterpolatedV(6.0);
        maxV = icon.getInterpolatedV(8.0);
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
        minU = icon.getInterpolatedU(0.0);
        maxU = icon.getInterpolatedU(1.0);
        minV = icon.getInterpolatedV(5.0);
        maxV = icon.getInterpolatedV(6.0);
        maxX = (minX += 0.046875) + 0.03125;
        minY = maxY;
        maxZ = (minZ += 0.046875) + 0.03125;
        tess.addVertexWithUV(minX, minY, minZ, minU, maxV);
        tess.addVertexWithUV(minX, minY, maxZ, maxU, maxV);
        tess.addVertexWithUV(minX, maxY += 0.0625, maxZ, maxU, minV);
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
}

