package com.brandon3055.tolkientweaks.tileentity;

import codechicken.lib.colour.Colour;
import codechicken.lib.colour.EnumColour;
import com.brandon3055.brandonscore.client.ResourceHelperBC;
import com.brandon3055.brandonscore.client.particle.BCEffectHandler;
import com.brandon3055.brandonscore.lib.Vec3D;
import com.brandon3055.tolkientweaks.client.ParticleSmoke;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Brandon on 8/02/2015.
 */
public class TileSmokerClient extends TileSmoker implements ITickable {

	public TileSmokerClient() {
		super();
	}



	@Override
	public void update() {
		if (world.isRemote) {
			updateParticles();
		}
	}

	@SideOnly(Side.CLIENT)
	public void updateParticles() {
		if (world.isRemote && world.isBlockIndirectlyGettingPowered(pos) == 0)
		{
			if(this.world.isAirBlock(pos.add(0, 1, 0))) {
				double px = (double)((float)pos.getX() + world.rand.nextFloat());
				double py = (double)((float)pos.getY() + world.rand.nextFloat() * 0.5F + 1.0F);
				double pz = (double)((float)pos.getZ() + world.rand.nextFloat());

				ParticleSmoke particle = new ParticleSmoke(world, new Vec3D(px, py, pz));
				Colour colour = EnumColour.values()[this.colour.value].getColour();
				particle.setColour((colour.r % 0xFF) / 255F, (colour.g % 0xFF) / 255F, (colour.b % 0xFF) / 255F);

				BCEffectHandler.spawnFXDirect(ResourceHelperBC.getResourceRAW("minecraft:textures/particle/particles.png"), particle, 128, true);
			}
		}
	}
}
