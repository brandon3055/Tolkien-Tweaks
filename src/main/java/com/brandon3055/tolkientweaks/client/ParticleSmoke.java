package com.brandon3055.tolkientweaks.client;

import com.brandon3055.brandonscore.client.particle.BCParticle;
import com.brandon3055.brandonscore.lib.Vec3D;
import net.minecraft.world.World;

/**
 * Created by brandon3055 on 29/10/2016.
 */
public class ParticleSmoke extends BCParticle {

    public ParticleSmoke(World worldIn, Vec3D pos) {
        super(worldIn, pos);
        this.texturesPerRow = 16;
        this.particleGravity = -0.07F;
        this.motionX = (-0.5 + rand.nextDouble()) * 0.05;
        this.motionZ = (-0.5 + rand.nextDouble()) * 0.05;
        this.particleScale = 1.6F + rand.nextFloat();
        this.particleMaxAge = (int) (24.0D / (Math.random() * 0.5D + 0.2D));
        this.particleMaxAge = (int) (particleMaxAge * particleScale);
    }

    @Override
    public void onUpdate() {
        this.prevPosX = posX;
        this.prevPosY = posY;
        this.prevPosZ = posZ;


        if (particleAge >= particleMaxAge)
            setExpired();
        this.particleAge++;

        if (getFXLayer() == 0)
            setParticleTextureIndex(7 - particleAge * 8 / particleMaxAge);
        this.motionY -= 0.04D * particleGravity;
        moveEntity(motionX, motionY, motionZ);

        if (posY == prevPosY) {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }

        this.motionX *= 0.96D;
        this.motionY *= 0.96D;
        this.motionZ *= 0.96D;

        if (isCollided) {
            this.motionX *= 0.67D;
            this.motionZ *= 0.67D;
        }
    }

//    public static class Factory implements IBCParticleFactory {
//
//        @Override
//        public Particle getEntityFX(int particleID, World world, Vec3D pos, Vec3D speed, int... args) {
//            BCParticle particle = new ParticleSmoke(world, pos);
//            return particle;
//        }
//    }
}
