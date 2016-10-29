package com.brandon3055.tolkientweaks.client;

import codechicken.lib.model.loader.IBakedModelLoader;
import com.brandon3055.tolkientweaks.TolkienTweaks;
import com.brandon3055.tolkientweaks.utils.LogHelper;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by brandon3055 on 28/10/2016.
 */
public class TTBakedModelProvider implements IBakedModelLoader {

    public static final TTBakedModelProvider INSTANCE = new TTBakedModelProvider();

    public static class TTKeyProvider implements IModKeyProvider {

        public static final TTKeyProvider INSTANCE = new TTKeyProvider();

        @Override
        public String getMod() {
            return TolkienTweaks.MODID.toLowerCase();
        }

        @Override
        public String createKey(ItemStack stack) {


            return null;
        }

        @Override
        public String createKey(IBlockState state) {
            LogHelper.info("Thing! "+state);
            return null;
        }
    }

    @Override
    public IModKeyProvider createKeyProvider() {
        return TTKeyProvider.INSTANCE;
    }

    @Override
    public void addTextures(ImmutableList.Builder<ResourceLocation> builder) {
//        for (Map.Entry<String, Set3<ResourceLocation, ResourceLocation, ResourceLocation>> entry : ToolModelRegistry.itemMap.entrySet()) {
//            builder.add(entry.getValue().getA());
//            builder.add(entry.getValue().getB());
//        }
    }

    @Override
    public IBakedModel bakeModel(String key) {


        return null;
    }
}
