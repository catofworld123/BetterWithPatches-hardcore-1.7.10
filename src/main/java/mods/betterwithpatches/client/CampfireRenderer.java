package mods.betterwithpatches.client;

import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.BetterWithPatches;
import mods.betterwithpatches.block.CampfireBlock;
import mods.betterwithpatches.block.tile.CampfireTileEntity;
import mods.betterwithpatches.util.BWPConstants;
import mods.betterwithpatches.util.ModelWrapperDisplayList;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

import static mods.betterwithpatches.block.Campfire.getFuelState;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class CampfireRenderer extends TileEntitySpecialRenderer
{
    private final ResourceLocation texture3 = new ResourceLocation(BWPConstants.MODID, "textures/models/fcBlockCampfire_burned.png");
    private final ResourceLocation texture2 = new ResourceLocation(BWPConstants.MODID, "textures/models/BlockCampfireNoFire.png");
    private final ResourceLocation texture1 = new ResourceLocation(BWPConstants.MODID, "textures/models/BlockCampfire.png");
    private final ResourceLocation modelPath = new ResourceLocation(BWPConstants.MODID, "textures/models/CampfireModel.obj");
    private final ResourceLocation modelPath2 = new ResourceLocation(BWPConstants.MODID, "textures/models/SpitModel.obj");
    private final IModelCustom smelterModel = new ModelWrapperDisplayList((WavefrontObject) AdvancedModelLoader.loadModel(modelPath));

    private final IModelCustom spitmodel = new ModelWrapperDisplayList((WavefrontObject) AdvancedModelLoader.loadModel(modelPath2));

    public void renderTileEntityAt(TileEntity tileEntity, double xCoord, double yCoord, double zCoord, float fPartialTickCount)
    {




        CampfireTileEntity campfire = (CampfireTileEntity)tileEntity;

        int facing;

        int k = 3;
        facing = campfire.getFacing();
        if (facing == 2) {
            k = 180;
        }
        if (facing == 3) {
            k = 0;
        }
        if (facing == 4) {
            k = 90;
        }
        if (facing == 5) {
            k = -90;
        }
        renderSpitStack(campfire, xCoord, yCoord, zCoord);

        renderCookStack(campfire, xCoord, yCoord, zCoord);
        GL11.glPushMatrix();

        GL11.glTranslated(xCoord + 0.5, yCoord, zCoord + 0.5);
        GL11.glRotatef(k, 0.0F, 1.0F, 0.0F);

        if (getFuelState(campfire.getWorldObj(), campfire.xCoord, campfire.yCoord, campfire.zCoord) == 2) {
            this.bindTexture(texture2);
        }
        if (getFuelState(campfire.getWorldObj(), campfire.xCoord, campfire.yCoord, campfire.zCoord) == 1) {
            this.bindTexture(texture3);
        }
        if (getFuelState(campfire.getWorldObj(), campfire.xCoord, campfire.yCoord, campfire.zCoord) != 2 && getFuelState(campfire.getWorldObj(), campfire.xCoord, campfire.yCoord, campfire.zCoord) != 1) {
            this.bindTexture(texture1);
        }
        smelterModel.renderAll();



        GL11.glPopMatrix();

    }


    private void renderCookStack(CampfireTileEntity campfire, double xCoord, double yCoord, double zCoord)
    {
    int facing;
    int k = 3;
        facing = campfire.getFacing();
        if (facing == 2) {
            k = 180;
        }
        if (facing == 3) {
            k = 0;
        }
        if (facing == 4) {
            k = 90;
        }
        if (facing == 5) {
            k = -90;
        }



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
                GL11.glRotatef(k, 0.0F, 1.0F, 0.0F);
            }

            RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);

            GL11.glPopMatrix();



        }
    }
    private void renderSpitStack(CampfireTileEntity campfire, double xCoord, double yCoord, double zCoord) {
        int facing;
        int k = 3;
        facing = campfire.getFacing();
        if (facing == 2) {
            k = 180;
        }
        if (facing == 3) {
            k = 0;
        }
        if (facing == 4) {
            k = 90;
        }
        if (facing == 5) {
            k = -90;
        }


        ItemStack stack = campfire.getSpitStack();
        if (stack != null){
            GL11.glPushMatrix();

            GL11.glTranslated(xCoord + 0.5, yCoord, zCoord + 0.5);
            GL11.glRotatef(k, 0.0F, 1.0F, 0.0F);
            this.bindTexture(texture1);
            spitmodel.renderAll();

            GL11.glPopMatrix();

        }
    }


}
