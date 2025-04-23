package mods.betterwithpatches.client;

import mods.betterwithpatches.block.CampfireBlock;
import mods.betterwithpatches.block.VesselBlock;
import mods.betterwithpatches.block.tile.ArcaneVesselTileEntity;
import mods.betterwithpatches.util.BWPConstants;
import mods.betterwithpatches.util.ModelWrapperDisplayList;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

public class VesselRenderer extends TileEntitySpecialRenderer {
    private final ResourceLocation texture2 = new ResourceLocation(BWPConstants.MODID, "textures/blocks/dragon_vessel.png");
    private final ResourceLocation texture1 = new ResourceLocation(BWPConstants.MODID, "textures/blocks/dragon_vessel_bottom.png");
    private final ResourceLocation texture3 = new ResourceLocation(BWPConstants.MODID, "textures/blocks/dragon_vessel_top.png");

    private final ResourceLocation modelPath = new ResourceLocation(BWPConstants.MODID, "textures/models/vesselbottom.obj");
    private final IModelCustom vesselbottom = new ModelWrapperDisplayList((WavefrontObject) AdvancedModelLoader.loadModel(modelPath));
    private final ResourceLocation modelPath1 = new ResourceLocation(BWPConstants.MODID, "textures/models/vesselSide.obj");
    private final IModelCustom vesselSide = new ModelWrapperDisplayList((WavefrontObject) AdvancedModelLoader.loadModel(modelPath1));
    private final ResourceLocation modelPath2 = new ResourceLocation(BWPConstants.MODID, "textures/models/vesselup.obj");
    private final IModelCustom vesselup = new ModelWrapperDisplayList((WavefrontObject) AdvancedModelLoader.loadModel(modelPath2));


    public void renderTileEntityAt(TileEntity tileEntity, double xCoord, double yCoord, double zCoord, float fPartialTickCount)
    {
        if (tileEntity instanceof ArcaneVesselTileEntity){
            int iFacing = (((ArcaneVesselTileEntity) tileEntity).getFacing());
            int k = 0;
            System.out.println(iFacing);
            if (iFacing == 2) {
                k = 1;
            }
            if (iFacing == 3) {
                k = 2;
            }
            if (iFacing == 4) {
                k = 3;
            }
            if (iFacing == 5) {
                k = 4;
            }
            System.out.println(iFacing);
            GL11.glPushMatrix();
            if (k == 1){

                GL11.glTranslated(xCoord + 0.5, yCoord+0.5, zCoord + 1);
                GL11.glRotatef(90, -1.0F, 0.0F, 0.0F);


            }
            else if ( k == 4 ){

                GL11.glTranslated(xCoord + 0, yCoord + 0.5, zCoord + 0.5);
                GL11.glRotatef(90, 0.0F, 0.0F, -1.0F);

            }
            else if ( k == 3 ){

                GL11.glTranslated(xCoord + 1, yCoord + 0.5, zCoord + 0.5);
                GL11.glRotatef(90, 0.0F, 0.0F, 1.0F);

            }
            else if ( k == 2 ){

                GL11.glTranslated(xCoord + 0.5, yCoord + 0.5, zCoord + 0.0);
                GL11.glRotatef(90, 1.0F, 0.0F, 0.0F);

            }

            else GL11.glTranslated(xCoord + 0.5, yCoord, zCoord + 0.5);




        }
        this.bindTexture(texture1);
        vesselbottom.renderAll();
        this.bindTexture(texture2);
        vesselSide.renderAll();
        this.bindTexture(texture3);
        vesselup.renderAll();
        GL11.glPopMatrix();

    }
}
