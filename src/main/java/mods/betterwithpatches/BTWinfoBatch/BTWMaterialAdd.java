package mods.betterwithpatches.BTWinfoBatch;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public interface BTWMaterialAdd  {
     default boolean getMobsCanSpawnOn(int iDimension)
     {
         if ( iDimension == -1 )
         {
             return true;
         }

         return true;

     }

     default Material setMobsCantSpawnOn()
     {


         return ((Material)(Object)this);
     }



     default Material setNetherMobsCanSpawnOn(){



         return ((Material)(Object)this);
     }

}
