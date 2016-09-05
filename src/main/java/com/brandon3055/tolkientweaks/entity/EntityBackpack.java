package com.brandon3055.tolkientweaks.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

/**
 * Created by brandon3055 on 23/4/2016.
 */
public class EntityBackpack extends EntityLiving {

    public EntityBackpack(World p_i1745_1_) {
        super(p_i1745_1_);
        this.setSize(0.6F, 0.8F);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

//    @Override
//    public boolean getAlwaysRenderNameTag() {
//        return super.getAlwaysRenderNameTag();
//    }
}
