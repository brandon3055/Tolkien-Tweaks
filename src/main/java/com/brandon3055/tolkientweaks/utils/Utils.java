package com.brandon3055.tolkientweaks.utils;

/**
 * Created by Brandon on 25/02/2015.
 */
public class Utils {

//	public static MovingObjectPosition raytraceFromEntity(World world, Entity player, double range) {
//		float f = 1.0F;
//		float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
//		float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
//		double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) f;
//		double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) f;
//		if (!world.isRemote && player instanceof EntityPlayer) d1 += 1.62D;
//		double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) f;
//		Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
//		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
//		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
//		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
//		float f6 = MathHelper.sin(-f1 * 0.017453292F);
//		float f7 = f4 * f5;
//		float f8 = f3 * f5;
//		Vec3 vec31 = vec3.addVector((double) f7 * range, (double) f6 * range, (double) f8 * range);
//		return world.rayTraceBlocks(vec3, vec31);
//	}

}
