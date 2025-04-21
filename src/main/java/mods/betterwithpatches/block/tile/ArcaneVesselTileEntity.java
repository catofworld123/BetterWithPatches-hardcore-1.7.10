package mods.betterwithpatches.block.tile;

import betterwithmods.util.BlockPosition;
import mods.betterwithpatches.block.ArcaneVesselBlock;
import mods.betterwithpatches.util.MiscUtils;
import mods.betterwithpatches.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.List;

public class ArcaneVesselTileEntity extends TileEntity {
    public static final int MAX_CONTAINED_EXPERIENCE = 1000;
    private static final int MIN_TEMPLE_EXPERIENCE = 200;
    private static final int MAX_TEMPLE_EXPERIENCE = 256;
    public static final int MAX_VISUAL_EXPERIENCE_LEVEL = 10;
    private final int xpEjectUnitSize = 20;
    public int visualExperienceLevel = 0;
    private int containedRegularExperience = 0;
    private int containedDragonExperience = 0;
    public int iFacing;

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        writeExtendedData(nbttagcompound);
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("regXP", this.containedRegularExperience);
        nbttagcompound.setInteger("dragXP", this.containedDragonExperience);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        readExtendedData(nbttagcompound);
        super.readFromNBT(nbttagcompound);
        this.containedRegularExperience = nbttagcompound.getInteger("regXP");
        this.containedDragonExperience = nbttagcompound.getInteger("dragXP");
        int iTotalExperience = this.containedRegularExperience + this.containedDragonExperience;
        this.visualExperienceLevel = (int)(10.0f * ((float)iTotalExperience / 1000.0f));
        if (iTotalExperience > 0 && this.visualExperienceLevel == 0) {
            this.visualExperienceLevel = 1;
        }
    }
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeExtendedData(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbttagcompound);
    }
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
    {
        TileEntity tile = worldObj.getTileEntity(packet.func_148856_c(), packet.func_148855_d(), packet.func_148854_e());
        if (tile instanceof ArcaneVesselTileEntity){
            ((ArcaneVesselTileEntity) tile).readExtendedData(packet.func_148857_g());
           // ((ArcaneVesselTileEntity) tile).readExtendedData2(packet.func_148857_g());
           // ((ArcaneVesselTileEntity) tile).readExtendedData3(packet.func_148857_g());
        }
    }

    private void readExtendedData(NBTTagCompound nbt) {
        if (nbt.hasKey("regXPClient", Constants.NBT.TAG_COMPOUND)) {
            NBTTagCompound inventoryTag = nbt.getCompoundTag("regXPClient");
            this.visualExperienceLevel = inventoryTag.getByte("regXPClient");
        }
    }

    private void writeExtendedData(NBTTagCompound nbt) {
            NBTTagCompound inventoryTag = new NBTTagCompound();
            inventoryTag.setByte("regXPClient",(byte)this.visualExperienceLevel);
            nbt.setTag("regXPClient", inventoryTag);
    }

    @Override
    public void updateEntity() {
        if (this.worldObj.isRemote) {
            return;
        }
        Block block = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord);
        if (block == null || !(block instanceof ArcaneVesselBlock)) {
            return;
        }
        ArcaneVesselBlock vesselBlock = (ArcaneVesselBlock)block;
        if (vesselBlock.isMechanicalOn(this.worldObj, this.xCoord, this.yCoord, this.zCoord)) {
            int iTiltFacing = vesselBlock.getTiltFacing(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            this.attemptToSpillXPFromInv(iTiltFacing);
        }
        List list = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox( xCoord, (float)yCoord + 1.0f, zCoord, xCoord + 1, (float)yCoord + 1.0f + 0.05f, zCoord + 1));
        for(int i=0; i < list.size(); i++)

        {

            Entity entity = (Entity) list.get(i);
            if (!worldObj.isRemote && entity instanceof EntityXPOrb) {
                ArcaneVesselBlock arcaneVesselBlock = new ArcaneVesselBlock();
                arcaneVesselBlock.onEntityXPOrbCollidedWithBlock(worldObj, xCoord, yCoord, zCoord, (EntityXPOrb) entity);
                worldObj.func_147453_f(xCoord, yCoord, zCoord, arcaneVesselBlock);

            }
        }


    }


    public int getVisualExperienceLevel() {
        return this.visualExperienceLevel;
    }

    public int getContainedRegularExperience() {
        return this.containedRegularExperience;
    }

    public void setContainedRegularExperience(int iExperience) {
        this.containedRegularExperience = iExperience;
        this.validateVisualExperience();
    }

    public int getContainedDragonExperience() {
        return this.containedDragonExperience;
    }

    public void setContainedDragonExperience(int iExperience) {
        this.containedDragonExperience = iExperience;
        this.validateVisualExperience();
    }

    public int getContainedTotalExperience() {
        return this.containedDragonExperience + this.containedRegularExperience;
    }
    public int getFacing(){
        ArcaneVesselBlock arcaneVesselBlock = new ArcaneVesselBlock();
        return arcaneVesselBlock.getFacing(worldObj, xCoord,yCoord,zCoord);
    }

    public void validateVisualExperience() {
        int iTotalExperience = this.containedRegularExperience + this.containedDragonExperience;
        int iNewVisualExperience = (int)(10.0f * ((float)iTotalExperience / 1000.0f));
        if (iTotalExperience > 0 && iNewVisualExperience == 0) {
            iNewVisualExperience = 1;
        }
        if (iNewVisualExperience != this.visualExperienceLevel) {
            this.visualExperienceLevel = iNewVisualExperience;
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }

    public void initTempleExperience() {
        this.setContainedRegularExperience(this.worldObj.rand.nextInt(56) + 200);
    }

    public boolean attemptToSwallowXPOrb(World world, int i, int j, int k, EntityXPOrb entityXPOrb) {
        int iTotalContainedXP = this.containedRegularExperience + this.containedDragonExperience;
        int iRemainingSpace = 1000 - iTotalContainedXP;
        if (iRemainingSpace > 0) {
            int iXPToAddToInventory = 0;
            boolean bIsDragonOrb = entityXPOrb.field_70135_K;
            if (entityXPOrb.xpValue <= iRemainingSpace) {
                iXPToAddToInventory = entityXPOrb.xpValue;
                entityXPOrb.setDead();
            } else {
                iXPToAddToInventory = iRemainingSpace;
            }
            if (bIsDragonOrb) {
                this.setContainedDragonExperience(this.containedDragonExperience + iXPToAddToInventory);
            } else {
                this.setContainedRegularExperience(this.containedRegularExperience + iXPToAddToInventory);
            }
            return true;
        }
        return false;
    }

    private void attemptToSpillXPFromInv(int iTiltFacing) {
        int iXPToSpill = 0;
        boolean bSpillDragonOrb = false;
        if (this.containedDragonExperience > 0 || this.containedRegularExperience > 0 && !this.isTiltedOutputBlocked(iTiltFacing)) {
            if (this.containedDragonExperience > 0) {
                bSpillDragonOrb = true;
                iXPToSpill = this.containedDragonExperience < 20 ? this.containedDragonExperience : 20;
                this.setContainedDragonExperience(this.containedDragonExperience - iXPToSpill);
            } else {
                iXPToSpill = this.containedRegularExperience < 20 ? this.containedRegularExperience : 20;
                this.setContainedRegularExperience(this.containedRegularExperience - iXPToSpill);
            }
        }
        if (iXPToSpill > 0) {
            this.spillXPOrb(iXPToSpill, bSpillDragonOrb, iTiltFacing);
        }
    }

    private boolean isTiltedOutputBlocked(int iTiltFacing) {
        BlockPosition targetPos = new BlockPosition(this.xCoord, this.yCoord, this.zCoord);
        this.xCoord += Facing.offsetsXForSide[iTiltFacing];
        this.yCoord += Facing.offsetsYForSide[iTiltFacing];
        this.zCoord += Facing.offsetsZForSide[iTiltFacing];
        if (!this.worldObj.isAirBlock(targetPos.x, targetPos.y, targetPos.z) && !WorldUtils.isReplaceableBlock(this.worldObj, targetPos.x, targetPos.y, targetPos.z)) {
            Block targetblock = this.worldObj.getBlock(targetPos.x, targetPos.y, targetPos.z);
            if (targetblock.getMaterial().isSolid()) {
                return true;
            }
        }
        return false;
    }

    public void ejectContentsOnBlockBreak() {
        int iEjectSize;
        while (this.containedRegularExperience > 0) {
            iEjectSize = 20;
            if (this.containedRegularExperience < 20) {
                iEjectSize = this.containedRegularExperience;
            }
            this.ejectXPOrbOnBlockBreak(iEjectSize, false);
            this.containedRegularExperience -= iEjectSize;
        }
        while (this.containedDragonExperience > 0) {
            iEjectSize = 20;
            if (this.containedDragonExperience < 20) {
                iEjectSize = this.containedDragonExperience;
            }
            this.ejectXPOrbOnBlockBreak(iEjectSize, true);
            this.containedDragonExperience -= iEjectSize;
        }
    }

    private void spillXPOrb(int iXPValue, boolean bDragonOrb, int iFacing) {
        Vec3 itemPos = MiscUtils.convertBlockFacingToVector(iFacing);
        itemPos.xCoord *= 0.5;
        itemPos.yCoord *= 0.5;
        itemPos.zCoord *= 0.5;
        itemPos.xCoord += (double)((float)this.xCoord + 0.5f);
        itemPos.yCoord += (double)((float)this.yCoord + 0.25f);
        itemPos.zCoord += (double)((float)this.zCoord + 0.5f + this.worldObj.rand.nextFloat() * 0.3f);
        if (itemPos.xCoord > (double)0.1f || itemPos.xCoord < (double)-0.1f) {
            itemPos.xCoord += (double)(this.worldObj.rand.nextFloat() * 0.5f - 0.25f);
        } else {
            itemPos.zCoord += (double)(this.worldObj.rand.nextFloat() * 0.5f - 0.25f);
        }
        EntityXPOrb xpOrb = new EntityXPOrb(this.worldObj, itemPos.xCoord, itemPos.yCoord, itemPos.zCoord, iXPValue);
        Vec3 itemVel = MiscUtils.convertBlockFacingToVector(iFacing);
        itemVel.xCoord *= (double)0.1f;
        itemVel.yCoord *= (double)0.1f;
        itemVel.zCoord *= (double)0.1f;
        xpOrb.motionX = itemVel.xCoord;
        xpOrb.motionY = itemVel.yCoord;
        xpOrb.motionZ = itemVel.zCoord;
        this.worldObj.spawnEntityInWorld(xpOrb);
    }

    private void ejectXPOrbOnBlockBreak(int iXPValue, boolean bDragonOrb) {
        double xOffset = this.worldObj.rand.nextDouble() * 0.7 + 0.15;
        double yOffset = this.worldObj.rand.nextDouble() * 0.7 + 0.15;
        double zOffset = this.worldObj.rand.nextDouble() * 0.7 + 0.15;
        EntityXPOrb xpOrb = new EntityXPOrb(this.worldObj, (double)this.xCoord + xOffset, (double)this.yCoord + yOffset, (double)this.zCoord + zOffset, iXPValue);
        xpOrb.motionX = (float)this.worldObj.rand.nextGaussian() * 0.05f;
        xpOrb.motionY = (float)this.worldObj.rand.nextGaussian() * 0.05f + 0.2f;
        xpOrb.motionZ = (float)this.worldObj.rand.nextGaussian() * 0.05f;
        this.worldObj.spawnEntityInWorld(xpOrb);
    }
}


