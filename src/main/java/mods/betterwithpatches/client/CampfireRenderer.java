package mods.betterwithpatches.client;

import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.block.CampfireBlock;
import mods.betterwithpatches.block.tile.CampfireTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class CampfireRenderer extends TileEntitySpecialRenderer
{

    public void renderTileEntityAt(TileEntity tileEntity, double xCoord, double yCoord, double zCoord, float fPartialTickCount)
    {
        CampfireTileEntity campfire = (CampfireTileEntity)tileEntity;


        renderCookStack(campfire, xCoord, yCoord, zCoord);
    }


    private void renderCookStack(CampfireTileEntity campfire, double xCoord, double yCoord, double zCoord)
    {
        ItemStack stack = campfire.getCookStack();


        if ( stack != null )
        {

            int iMetadata = campfire.getBlockMetadata();
            boolean bIAligned = BWPRegistry.unlitCampfire.getIsIAligned(iMetadata);

            EntityItem entity = new EntityItem(campfire.getWorldObj(), xCoord, yCoord, zCoord, stack);

            entity.getEntityItem().stackSize = 1;
            entity.hoverStart = 0.0F;


            GL11.glPushMatrix();
            GL11.glTranslatef( (float)xCoord + 0.5F, (float)yCoord + ( 9F / 16F ), (float)zCoord + 0.5F );

            if ( !bIAligned && RenderManager.instance.options.fancyGraphics )
            {
                GL11.glRotatef( 90F, 0.0F, 1.0F, 0.0F);
            }

            RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);

            GL11.glPopMatrix();



        }
    }


}
