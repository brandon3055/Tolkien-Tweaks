package com.brandon3055.tolkientweaks.entity;

import com.brandon3055.tolkientweaks.ConfigHandler;
import com.brandon3055.tolkientweaks.ModItems;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemExpireEvent;

/**
 * Created by Brandon on 13/01/2015.
 */
public class EntityPalantir extends EntityItem {

	private boolean onLava = false;
	private int ticksOnLava = 0;
	public EntityPlayer player = null;

	public EntityPalantir(World par1World, double par2, double par4, double par6)
	{
		super(par1World, par2, par4, par6);
		this.isImmuneToFire = true;
		this.lifespan = 72000;
	}

	public EntityPalantir(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack)
	{
		this(par1World, par2, par4, par6);
		this.setEntityItemStack(par8ItemStack);
		this.lifespan = 72000;
	}

	public EntityPalantir(World par1World)
	{
		super(par1World);
		this.isImmuneToFire = true;
		this.lifespan = 72000;
	}

	public EntityPalantir(World world, Entity original, ItemStack stack)
	{
		this(world, original.posX, original.posY, original.posZ);
		if (original instanceof EntityItem) this.delayBeforeCanPickup = ((EntityItem)original).delayBeforeCanPickup;
		else this.delayBeforeCanPickup = 20;
		this.motionX = original.motionX;
		this.motionY = original.motionY;
		this.motionZ = original.motionZ;
		this.setEntityItemStack(stack);
		this.lifespan = 72000;
		if (stack.hasTagCompound())
		{
			for (Object p : world.playerEntities)
			{
				if (p instanceof EntityPlayer && ((EntityPlayer) p).getUniqueID().toString().equals(stack.getTagCompound().getString("PlayerUUID"))) {
					player = (EntityPlayer)p;
				}
			}
		}
	}

	@Override
	public boolean attackEntityFrom (DamageSource par1DamageSource, float par2)
	{

		return par1DamageSource.getDamageType().equals("outOfWorld");
	}

	@Override
	public boolean isInRangeToRenderDist(double p_70112_1_) {
		return true;
	}

	@Override
	public boolean isInRangeToRender3d(double p_145770_1_, double p_145770_3_, double p_145770_5_) {
		return true;
	}

	@Override
	public void onUpdate() {

		if (age+10 >= lifespan) age = 0;
		if (this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)).getMaterial() == Material.lava && !onLava) {
			onLava = true;
			if (!worldObj.isRemote && !isLocationValid())
			{
				for (int i = 0; i < worldObj.playerEntities.size(); i++)
				{
					EntityPlayer player = (EntityPlayer) worldObj.playerEntities.get(i);
					if (getDistanceAtoB(player.posX, player.posY, player.posZ, posX, posY, posZ) < 20)
					{
						player.addChatComponentMessage(new ChatComponentText(ConfigHandler.wrongLocationMessagePalantir));
					}
				}
			}
		}
		ItemStack stack = this.getDataWatcher().getWatchableObjectItemStack(10);
		if (stack != null && stack.getItem() != null)
		{
			if (stack.getItem().onEntityItemUpdate(this))
			{
				return;
			}
		}

		if (this.getEntityItem() == null)
		{
			this.setDead();
		}
		else
		{
			super.onEntityUpdate();

			if (this.delayBeforeCanPickup > 0)
			{
				--this.delayBeforeCanPickup;
			}

			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			this.motionY -= 0.03999999910593033D;
			if (this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)).getMaterial() == Material.lava)
			{
				setPosition(posX, posY+0.01, posZ);
			}
			if (onLava)
			{
				//setPosition(posX, Math.round(posY), posZ);
				motionX = 0;
				motionY = 0;
				motionZ = 0;
			}
			this.noClip = this.func_145771_j(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D, this.posZ);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);


			float f = 0.98F;

			if (this.onGround)
			{
				f = this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ)).slipperiness * 0.98F;
			}

			this.motionX *= (double)f;
			this.motionY *= 0.9800000190734863D;
			this.motionZ *= (double)f;
			if (this.motionY < -0.5)
			{
			//	this.motionY = -0.5;
			}
			if (onLava)
			{
				motionX = 0;
				motionY = 0;
				motionZ = 0;
			}

			if (this.onGround)
			{
				this.motionY *= -0.5D;
			}

			++this.age;

			ItemStack item = getDataWatcher().getWatchableObjectItemStack(10);

			boolean fullGlow = false;

			if (onLava) // Show the glowing text
			{
				if (!item.hasTagCompound()) item.setTagCompound(new NBTTagCompound());
				float glow = Math.min(item.getTagCompound().getFloat("Glow"), 5f);
				item.getTagCompound().setFloat("Glow", glow += 0.01f);
				fullGlow = glow >= 1f;
			}else if (item.hasTagCompound() && item.getTagCompound().hasKey("Glow"))
			{
				if (item.getTagCompound().getFloat("Glow") > 0f) item.getTagCompound().setFloat("Glow", item.getTagCompound().getFloat("Glow") - 0.01f);
				else item.setTagCompound(null);
			}

			if (fullGlow && isLocationValid()) // Do fancy things
			{
				ticksOnLava++;
				double dist = 0.4d;

				if (ticksOnLava < 50)
				{
					int randF = worldObj.rand.nextInt();
					worldObj.spawnParticle("lava", posX + (Math.cos(randF) * dist), posY, posZ + (Math.sin(randF) * dist), 0.0D, 0.0D, 0.0D);
				}
				else
				{
					int j = ticksOnLava - 50;

					for (int i = 0; i < 1 + j/50; i++)
					{
						int randF = worldObj.rand.nextInt();
						worldObj.spawnParticle("lava", posX + (Math.cos(randF) * dist), posY, posZ + (Math.sin(randF) * dist), 0.0D, 0.0D, 0.0D);
					}
				}

				if (ticksOnLava == 250) this.playSound("tolkientweaks:ringdestroy", 5F, 1.0F);

				if (ticksOnLava >= 250)
				{
					item.getTagCompound().setInteger("ETicks", item.getTagCompound().getInteger("ETicks") + 1);
				}

				if (ticksOnLava >= 270)
				{
					//this.worldObj.spawnParticle("hugeexplosion", posX, posY, posZ, 1.0D, 0.0D, 0.0D);
					worldObj.createExplosion(this, posX, posY, posZ, 6f, false);
					if (!worldObj.isRemote)
					{
						if (player != null)
						{
							EntityItem itemEntity = new EntityItem(worldObj, player.posX, player.posY, player.posZ, new ItemStack(ModItems.palantirShard));
							itemEntity.delayBeforeCanPickup = 0;
							worldObj.spawnEntityInWorld(itemEntity);
						}
						else
						{
							EntityItem itemEntity = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(ModItems.palantirShard));
							itemEntity.delayBeforeCanPickup = 0;
							worldObj.spawnEntityInWorld(itemEntity);
						}
					}
					this.setDead();
				}
			}



			if (!this.worldObj.isRemote && this.age >= lifespan)
			{
				if (item != null)
				{
					ItemExpireEvent event = new ItemExpireEvent(this, (item.getItem() == null ? 6000 : item.getItem().getEntityLifespan(item, worldObj)));
					if (MinecraftForge.EVENT_BUS.post(event))
					{
						lifespan += event.extraLife;
					}
					else
					{
						this.setDead();
					}
				}
				else
				{
					this.setDead();
				}
			}

			if (item != null && item.stackSize <= 0)
			{
				this.setDead();
			}
		}
	}

	private boolean isLocationValid() {
		int x = ConfigHandler.ringDisposalLocation[0];
		int y = ConfigHandler.ringDisposalLocation[1];
		int z = ConfigHandler.ringDisposalLocation[2];

		if (x == 0 && y == 0 && z == 0) return true;

		return getDistanceAtoB(posX, posY, posZ, x, y, z) <= ConfigHandler.ringDisposalRadius;
	}

	public static double getDistanceAtoB(double x1, double y1, double z1, double x2, double y2, double z2){
		double dx = x1-x2;
		double dy = y1-y2;
		double dz = z1-z2;
		return Math.sqrt((dx*dx + dy*dy + dz*dz ));
	}
}
