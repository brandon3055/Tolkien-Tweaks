package com.brandon3055.tolkientweaks.client.rendering;

import codechicken.lib.texture.TextureUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

/**
 * Created by brandon3055 on 7/02/2017.
 */
public class TTTextureCache implements TextureUtils.IIconRegister{

    public static TextureAtlasSprite keystoneFace;
    public static TextureAtlasSprite keystoneFaceActive;

    @Override
    public void registerIcons(TextureMap textureMap) {
        keystoneFace = textureMap.registerSprite(new ResourceLocation("tolkientweaks:blocks/key_stone"));
        keystoneFaceActive = textureMap.registerSprite(new ResourceLocation("tolkientweaks:blocks/key_stone_active"));
    }
}
