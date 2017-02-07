package com.brandon3055.tolkientweaks.entity;

import com.brandon3055.tolkientweaks.ConfigHandler;
import com.brandon3055.tolkientweaks.TTFeatures;
import com.brandon3055.tolkientweaks.TTSounds;
import com.brandon3055.tolkientweaks.items.Ring;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * Created by Brandon on 13/01/2015.
 */
public class EntityRing extends EntityItem {

    public static final DataParameter<Boolean> ON_LAVA = EntityDataManager.createKey(EntityItem.class, DataSerializers.BOOLEAN);
    private int ticksOnLava = 0;
    public EntityPlayer player = null;

    public EntityRing(World par1World, double par2, double par4, double par6) {
        super(par1World, par2, par4, par6);
        this.isImmuneToFire = true;
        this.lifespan = 72000;
        hoverStart = 0;
    }

    public EntityRing(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack) {
        this(par1World, par2, par4, par6);
        this.setEntityItemStack(par8ItemStack);
        this.lifespan = 72000;
        hoverStart = 0;
    }

    public EntityRing(World par1World) {
        super(par1World);
        this.isImmuneToFire = true;
        this.lifespan = 72000;
        hoverStart = 0;
    }

    public EntityRing(World world, Entity original, ItemStack stack) {
        this(world, original.posX, original.posY, original.posZ);
        if (original instanceof EntityItem) this.delayBeforeCanPickup = ((EntityItem) original).delayBeforeCanPickup;
        else this.delayBeforeCanPickup = 20;
        this.motionX = original.motionX;
        this.motionY = original.motionY;
        this.motionZ = original.motionZ;
        this.setEntityItemStack(stack);
        this.lifespan = 72000;
        hoverStart = 0;
        if (stack.hasTagCompound()) {
            for (Object p : world.playerEntities) {
                if (p instanceof EntityPlayer && ((EntityPlayer) p).getUniqueID().toString().equals(stack.getTagCompound().getString("PlayerUUID"))) {
                    player = (EntityPlayer) p;
                }
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {

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
    protected void entityInit() {
        super.entityInit();
        dataManager.register(ON_LAVA, false);
    }

    public boolean getOnLava() {
        return dataManager.get(ON_LAVA);
    }

    public void setOnLava() {
        dataManager.set(ON_LAVA, true);
    }

    @Override
    public void onUpdate() {
//		if (entityIn instanceof EntityItem) FMLLog.info(entityIn+" "+canUpdate);

//        hoverStart -= 0.1;

        if (age + 10 >= lifespan) {
            age = 0;
        }
        if (!getOnLava() && worldObj.isMaterialInBB(this.getEntityBoundingBox().expand(-0.10000000149011612D, 0, -0.10000000149011612D), Material.LAVA)) {//this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))).getMaterial() == Material.LAVA && !getOnLava()) {
            setOnLava();
            posY = MathHelper.floor_double(this.posY) + 0.8D;
            setPosition(posX, posY, posZ);
            if (!worldObj.isRemote && !isLocationValid()) {
                for (int i = 0; i < worldObj.playerEntities.size(); i++) {
                    EntityPlayer player = (EntityPlayer) worldObj.playerEntities.get(i);
                    if (getDistanceAtoB(player.posX, player.posY, player.posZ, posX, posY, posZ) < 20) {
                        player.addChatComponentMessage(new TextComponentString(ConfigHandler.wrongLocationMessage));
                    }
                }
            }
        }

//		ItemStack stack = this.getDataWatcher().getWatchableObjectItemStack(10);
//		if (stack != null && stack.getItem() != null)
//		{
//			if (stack.getItem().onEntityItemUpdate(this))
//			{
//				return;
//			}
//		}
//
//		if (this.getEntityItem() == null)
//		{
//			this.setDead();
//		}
//		else
//		{
//			super.onEntityUpdate();
//
//			if (this.delayBeforeCanPickup > 0)
//			{
//				--this.delayBeforeCanPickup;
//			}
//
//			this.prevPosX = this.posX;
//			this.prevPosY = this.posY;
//			this.prevPosZ = this.posZ;
//			this.motionY -= 0.03999999910593033D;
//			if (this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)).getMaterial() == Material.lava)
//			{
//				setPosition(posX, posY+0.01, posZ);
//			}
//			if (onLava)
//			{
//				//setPosition(posX, Math.round(posY), posZ);
//				motionX = 0;
//				motionY = 0;
//				motionZ = 0;
//			}
//			this.noClip = this.func_145771_j(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D, this.posZ);
//			this.moveEntity(this.motionX, this.motionY, this.motionZ);
//
//
//			float f = 0.98F;
//
//			if (this.onGround)
//			{
//				f = this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ)).slipperiness * 0.98F;
//			}
//
//			this.motionX *= (double)f;
//			this.motionY *= 0.9800000190734863D;
//			this.motionZ *= (double)f;
//			if (this.motionY < -0.5)
//			{
//			//	this.motionY = -0.5;
//			}
//			if (onLava)
//			{
//				motionX = 0;
//				motionY = 0;
//				motionZ = 0;
//			}
//
//			if (this.onGround)
//			{
//				this.motionY *= -0.5D;
//			}
//
//			++this.age;
//
//			ItemStack item = getDataWatcher().getWatchableObjectItemStack(10);
//
//			boolean fullGlow = false;
//
//			if (onLava) // Show the glowing text
//			{
//				if (!item.hasTagCompound()) item.setTagCompound(new NBTTagCompound());
//				float glow = Math.min(item.getTagCompound().getFloat("Glow"), 5f);
//				item.getTagCompound().setFloat("Glow", glow += 0.01f);
//				fullGlow = glow >= 1f;
//			}else if (item.hasTagCompound() && item.getTagCompound().hasKey("Glow"))
//			{
//				if (item.getTagCompound().getFloat("Glow") > 0f) item.getTagCompound().setFloat("Glow", item.getTagCompound().getFloat("Glow") - 0.01f);
//				else item.setTagCompound(null);
//			}
//
//			if (fullGlow && isLocationValid()) // Do fancy things
//			{
//				ticksOnLava++;
//				double dist = 0.4d;
//
//				if (ticksOnLava < 50)
//				{
//					int randF = worldObj.rand.nextInt();
//					worldObj.spawnParticle("lava", posX + (Math.cos(randF) * dist), posY, posZ + (Math.sin(randF) * dist), 0.0D, 0.0D, 0.0D);
//				}
//				else
//				{
//					int j = ticksOnLava - 50;
//
//					for (int i = 0; i < 1 + j/50; i++)
//					{
//						int randF = worldObj.rand.nextInt();
//						worldObj.spawnParticle("lava", posX + (Math.cos(randF) * dist), posY, posZ + (Math.sin(randF) * dist), 0.0D, 0.0D, 0.0D);
//					}
//				}
//
//				if (ticksOnLava == 250) this.playSound("tolkientweaks:ringdestroy", 5F, 1.0F);
//
//				if (ticksOnLava >= 250)
//				{
//					item.getTagCompound().setInteger("ETicks", item.getTagCompound().getInteger("ETicks") + 1);
//				}
//
//				if (ticksOnLava >= 270)
//				{
//					//this.worldObj.spawnParticle("hugeexplosion", posX, posY, posZ, 1.0D, 0.0D, 0.0D);
//					worldObj.createExplosion(this, posX, posY, posZ, 6f, false);
//					if (!worldObj.isRemote)
//					{
//						if (player != null)
//						{
//							EntityItem itemEntity = new EntityItem(worldObj, player.posX, player.posY, player.posZ, new ItemStack(TTFeatures.soul));
//							itemEntity.delayBeforeCanPickup = 0;
//							worldObj.spawnEntityInWorld(itemEntity);
//						}
//						else
//						{
//							EntityItem itemEntity = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(TTFeatures.soul));
//							itemEntity.delayBeforeCanPickup = 0;
//							worldObj.spawnEntityInWorld(itemEntity);
//						}
//					}
//					this.setDead();
//				}
//			}
//
//
//
//			if (!this.worldObj.isRemote && this.age >= lifespan)
//			{
//				if (item != null)
//				{
//					ItemExpireEvent event = new ItemExpireEvent(this, (item.getItem() == null ? 6000 : item.getItem().getEntityLifespan(item, worldObj)));
//					if (MinecraftForge.EVENT_BUS.post(event))
//					{
//						lifespan += event.extraLife;
//					}
//					else
//					{
//						this.setDead();
//					}
//				}
//				else
//				{
//					this.setDead();
//				}
//			}
//
//			if (item != null && item.stackSize <= 0)
//			{
//				this.setDead();
//			}
//		}

        ItemStack stack = this.getDataManager().get(ITEM).orNull();
        if (stack != null && stack.getItem() != null && stack.getItem().onEntityItemUpdate(this)) return;
        if (this.getEntityItem() == null) {
            this.setDead();
        }
        else {
            if (this.delayBeforeCanPickup > 0 && this.delayBeforeCanPickup != 32767) {
                --this.delayBeforeCanPickup;
            }

            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;

            if (!this.hasNoGravity() && !getOnLava()) {
                this.motionY -= 0.03999999910593033D;
            }

            this.noClip = this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.posZ);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            boolean flag = (int) this.prevPosX != (int) this.posX || (int) this.prevPosY != (int) this.posY || (int) this.prevPosZ != (int) this.posZ;

            float f = 0.98F;

            if (this.onGround) {
                f = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.98F;
            }

            this.motionX *= (double) f;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= (double) f;

            if (this.onGround) {
                this.motionY *= -0.5D;
            }

            if (this.age != -32768) {
                ++this.age;
            }

            if (getOnLava()) {
                motionX = motionY = motionZ = 0;
            }

            boolean fullGlow = false;

            if (getOnLava()) // Show the glowing text
            {
                if (!stack.hasTagCompound()) {
                    stack.setTagCompound(new NBTTagCompound());
                }
                float glow = Math.min(stack.getTagCompound().getFloat("Glow"), 5f);
                stack.getTagCompound().setFloat("Glow", glow += 0.01f);
                fullGlow = glow >= 1f;
                Ring.glow = glow;
            }
            else if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Glow")) {
                if (stack.getTagCompound().getFloat("Glow") > 0f) {
                    stack.getTagCompound().setFloat("Glow", stack.getTagCompound().getFloat("Glow") - 0.01f);
                    Ring.glow = stack.getTagCompound().getFloat("Glow");
                }
                else {
                    stack.setTagCompound(null);
                    Ring.glow = 0;
                }
            }
            if (fullGlow && isLocationValid()) // Do fancy things
            {
                ticksOnLava++;
                double dist = 0.4d;
                if (ticksOnLava < 50) {
                    int randF = worldObj.rand.nextInt();
					worldObj.spawnParticle(EnumParticleTypes.LAVA, posX + (Math.cos(randF) * dist), posY, posZ + (Math.sin(randF) * dist), 0.0D, 0.0D, 0.0D);
                }
                else {
                    int j = ticksOnLava - 50;
                    for (int i = 0; i < 1 + j / 50; i++) {
                        int randF = worldObj.rand.nextInt();
						worldObj.spawnParticle(EnumParticleTypes.LAVA, posX + (Math.cos(randF) * dist), posY, posZ + (Math.sin(randF) * dist), 0.0D, 0.0D, 0.0D);
                    }
                }
				if (ticksOnLava == 250) {
				    this.playSound(TTSounds.ringDestroy, 5F, 1.0F);
                }
                if (ticksOnLava >= 250) {
                    stack.getTagCompound().setInteger("ETicks", stack.getTagCompound().getInteger("ETicks") + 1);
                }
                if (ticksOnLava >= 270) {
                    worldObj.createExplosion(this, posX, posY, posZ, 6f, false);
                    if (!worldObj.isRemote) {
                        if (player != null) {
                            EntityItem itemEntity = new EntityItem(worldObj, player.posX, player.posY, player.posZ, new ItemStack(TTFeatures.soul));
                            itemEntity.delayBeforeCanPickup = 0;
                            worldObj.spawnEntityInWorld(itemEntity);
                        }
                        else {
                            EntityItem itemEntity = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(TTFeatures.soul));
                            itemEntity.delayBeforeCanPickup = 0;
                            worldObj.spawnEntityInWorld(itemEntity);
                        }
                    }
                    this.setDead();
                }
            }


            this.handleWaterMovement();

            ItemStack item = this.getDataManager().get(ITEM).orNull();

            if (item != null && item.stackSize <= 0) {
                this.setDead();
            }
        }
    }

    public void onEntityUpdate() {
//		super.onEntityUpdate();
        this.worldObj.theProfiler.startSection("entityBaseTick");

        if (this.isRiding() && this.getRidingEntity().isDead) {
            this.dismountRidingEntity();
        }

        if (this.rideCooldown > 0) {
            --this.rideCooldown;
        }

        this.prevDistanceWalkedModified = this.distanceWalkedModified;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;

        if (!this.worldObj.isRemote && this.worldObj instanceof WorldServer) {
            this.worldObj.theProfiler.startSection("portal");

            if (this.inPortal) {
                MinecraftServer minecraftserver = this.worldObj.getMinecraftServer();

                if (minecraftserver.getAllowNether()) {
                    if (!this.isRiding()) {
                        int i = this.getMaxInPortalTime();

                        if (this.portalCounter++ >= i) {
                            this.portalCounter = i;
                            this.timeUntilPortal = this.getPortalCooldown();
                            int j;

                            if (this.worldObj.provider.getDimensionType().getId() == -1) {
                                j = 0;
                            }
                            else {
                                j = -1;
                            }

                            this.changeDimension(j);
                        }
                    }

                    this.inPortal = false;
                }
            }
            else {
                if (this.portalCounter > 0) {
                    this.portalCounter -= 4;
                }

                if (this.portalCounter < 0) {
                    this.portalCounter = 0;
                }
            }

            this.decrementTimeUntilPortal();
            this.worldObj.theProfiler.endSection();
        }

        this.spawnRunningParticles();
        this.handleWaterMovement();


        if (this.posY < -64.0D) {
            this.kill();
        }

        this.firstUpdate = false;
        this.worldObj.theProfiler.endSection();
    }

    private boolean isLocationValid() {
        int x = ConfigHandler.ringDisposalLocation[0];
        int y = ConfigHandler.ringDisposalLocation[1];
        int z = ConfigHandler.ringDisposalLocation[2];

        if (x == 0 && y == 0 && z == 0) return true;

        return getDistanceAtoB(posX, posY, posZ, x, y, z) <= ConfigHandler.ringDisposalRadius;
    }

    public static double getDistanceAtoB(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;
        return Math.sqrt((dx * dx + dy * dy + dz * dz));
    }
}
