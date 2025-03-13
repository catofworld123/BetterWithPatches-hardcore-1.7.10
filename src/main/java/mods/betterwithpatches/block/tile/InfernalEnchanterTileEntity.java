package mods.betterwithpatches.block.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class InfernalEnchanterTileEntity extends TileEntity {
    private int[] timeSinceLastCandleFlame = new int[4];
    public boolean playerNear;
    private static final int MAX_TIME_BETWEEN_FLAME_UPDATES = 10;

    public InfernalEnchanterTileEntity() {
        for(int iTemp = 0; iTemp < 4; ++iTemp) {
            this.timeSinceLastCandleFlame[iTemp] = 0;
        }

        this.playerNear = false;
    }

    public void updateEntity() {
        super.updateEntity();
        EntityPlayer entityplayer = this.worldObj.getClosestPlayer((double)((float)this.xCoord + 0.5F), (double)((float)this.yCoord + 0.5F), (double)((float)this.zCoord + 0.5F), (double)4.5F);
        if (entityplayer != null) {
            if (!this.playerNear) {
                this.lightCandles();
                this.playerNear = true;
            } else {
                this.updateCandleFlames();
            }
        } else {
            this.playerNear = false;
        }

    }

    private void lightCandles() {
        for(int iTemp = 0; iTemp < 4; ++iTemp) {
            this.displayCandleFlameAtIndex(iTemp);
        }

        this.worldObj.playSoundEffect((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, "mob.ghast.fireball", 1.0F, this.worldObj.rand.nextFloat() * 0.4F + 0.8F);
    }

    private void updateCandleFlames() {
        for(int iTemp = 0; iTemp < 4; ++iTemp) {
            int var10002 = this.timeSinceLastCandleFlame[iTemp]++;
            if (this.timeSinceLastCandleFlame[iTemp] > 10 || this.worldObj.rand.nextInt(5) == 0) {
                this.displayCandleFlameAtIndex(iTemp);
            }
        }

    }

    private void displayCandleFlameAtIndex(int iCandleIndex) {
        double flameX = (double)this.xCoord + (double)0.125F;
        double flameY = (double)((float)this.yCoord + 0.5F + 0.25F + 0.175F);
        double flameZ = (double)this.zCoord + (double)0.125F;
        if (iCandleIndex == 1 || iCandleIndex == 3) {
            flameX = (double)this.xCoord + (double)0.875F;
        }

        if (iCandleIndex == 2 || iCandleIndex == 3) {
            flameZ = (double)this.zCoord + (double)0.875F;
        }

        this.displayCandleFlameAtLoc(flameX, flameY, flameZ);
        this.timeSinceLastCandleFlame[iCandleIndex] = 0;
    }

    private void displayCandleFlameAtLoc(double xCoord, double yCoord, double zCoord) {
        this.worldObj.spawnParticle("smoke", xCoord, yCoord, zCoord, (double)0.0F, (double)0.0F, (double)0.0F);
        this.worldObj.spawnParticle("flame", xCoord, yCoord, zCoord, (double)0.0F, (double)0.0F, (double)0.0F);
    }
}
