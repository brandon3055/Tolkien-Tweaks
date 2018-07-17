package com.brandon3055.tolkientweaks.entity;

import com.brandon3055.tolkientweaks.ConfigHandler;
import com.brandon3055.tolkientweaks.TTFeatures;
import com.brandon3055.tolkientweaks.TTSounds;
import com.brandon3055.tolkientweaks.items.Ring;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
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
public class EntityPalantir extends EntityItem {

    public static final DataParameter<Boolean> ON_LAVA = EntityDataManager.createKey(EntityItem.class, DataSerializers.BOOLEAN);
    private int ticksOnLava = 0;
    public EntityPlayer player = null;

    public EntityPalantir(World par1World, double par2, double par4, double par6) {
        super(par1World, par2, par4, par6);
        this.isImmuneToFire = true;
        this.lifespan = 72000;
    }

    public EntityPalantir(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack) {
        this(par1World, par2, par4, par6);
        this.setItem(par8ItemStack);
        this.lifespan = 72000;
    }

    public EntityPalantir(World par1World) {
        super(par1World);
        this.isImmuneToFire = true;
        this.lifespan = 72000;
    }

    public EntityPalantir(World world, Entity original, ItemStack stack) {
        this(world, original.posX, original.posY, original.posZ);
        if (original instanceof EntityItem) {
            this.pickupDelay = ((EntityItem) original).pickupDelay;
        }
        else this.pickupDelay = 20;
        this.motionX = original.motionX;
        this.motionY = original.motionY;
        this.motionZ = original.motionZ;
        this.setItem(stack);
        this.lifespan = 72000;
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
        if (age + 10 >= lifespan) {
            age = 0;
        }
        if (!getOnLava() && world.isMaterialInBB(this.getEntityBoundingBox().expand(-0.10000000149011612D, 0, -0.10000000149011612D), Material.LAVA)) {//this.world.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))).getMaterial() == Material.LAVA && !getOnLava()) {
            setOnLava();
            posY = MathHelper.floor(this.posY) + 0.9D;
            setPosition(posX, posY, posZ);
            if (!world.isRemote && !isLocationValid()) {
                for (int i = 0; i < world.playerEntities.size(); i++) {
                    EntityPlayer player = (EntityPlayer) world.playerEntities.get(i);
                    if (getDistanceAtoB(player.posX, player.posY, player.posZ, posX, posY, posZ) < 20) {
                        player.sendMessage(new TextComponentString(ConfigHandler.wrongLocationMessage));
                    }
                }
            }
        }

        ItemStack stack = this.getDataManager().get(ITEM);
        if (!stack.isEmpty() && stack.getItem() != null && stack.getItem().onEntityItemUpdate(this)) return;
        if (this.getItem().isEmpty()) {
            this.setDead();
        }
        else {
            if (this.pickupDelay > 0 && this.pickupDelay != 32767) {
                --this.pickupDelay;
            }

            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;

            if (!this.hasNoGravity() && !getOnLava()) {
                this.motionY -= 0.03999999910593033D;
            }

            this.noClip = this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.posZ);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            boolean flag = (int) this.prevPosX != (int) this.posX || (int) this.prevPosY != (int) this.posY || (int) this.prevPosZ != (int) this.posZ;

            float f = 0.98F;

            if (this.onGround) {
                f = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.98F;
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
                    int randF = world.rand.nextInt();
                    world.spawnParticle(EnumParticleTypes.LAVA, posX + (Math.cos(randF) * dist), posY, posZ + (Math.sin(randF) * dist), 0.0D, 0.0D, 0.0D);
                }
                else {
                    int j = ticksOnLava - 50;
                    for (int i = 0; i < 1 + j / 50; i++) {
                        int randF = world.rand.nextInt();
                        world.spawnParticle(EnumParticleTypes.LAVA, posX + (Math.cos(randF) * dist), posY, posZ + (Math.sin(randF) * dist), 0.0D, 0.0D, 0.0D);
                    }
                }
                if (ticksOnLava == 250) {
                    this.playSound(TTSounds.ringDestroy, 5F, 1.0F);
                }
                if (ticksOnLava >= 250) {
                    stack.getTagCompound().setInteger("ETicks", stack.getTagCompound().getInteger("ETicks") + 1);
                }
                if (ticksOnLava >= 270) {
                    world.createExplosion(this, posX, posY, posZ, 6f, false);
                    if (!world.isRemote) {
                        ItemStack dropStack = new ItemStack(TTFeatures.palantirShard, getItem().getCount());
                        if (player != null) {
                            EntityItem itemEntity = new EntityItem(world, player.posX, player.posY, player.posZ, dropStack);
                            itemEntity.pickupDelay = 0;
                            world.spawnEntity(itemEntity);
                        }
                        else {
                            EntityItem itemEntity = new EntityItem(world, posX, posY, posZ, dropStack);
                            itemEntity.pickupDelay = 0;
                            world.spawnEntity(itemEntity);
                        }
                    }
                    this.setDead();
                }
            }


            this.handleWaterMovement();

            ItemStack item = this.getDataManager().get(ITEM);

            if (item.isEmpty()) {
                this.setDead();
            }
        }
    }

    public void onEntityUpdate() {
//		super.onEntityUpdate();
        this.world.profiler.startSection("entityBaseTick");

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

        if (!this.world.isRemote && this.world instanceof WorldServer) {
            this.world.profiler.startSection("portal");

            if (this.inPortal) {
                MinecraftServer minecraftserver = this.world.getMinecraftServer();

                if (minecraftserver.getAllowNether()) {
                    if (!this.isRiding()) {
                        int i = this.getMaxInPortalTime();

                        if (this.portalCounter++ >= i) {
                            this.portalCounter = i;
                            this.timeUntilPortal = this.getPortalCooldown();
                            int j;

                            if (this.world.provider.getDimensionType().getId() == -1) {
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
            this.world.profiler.endSection();
        }

        this.spawnRunningParticles();
        this.handleWaterMovement();


        if (this.posY < -64.0D) {
            this.setDead();
        }

        this.firstUpdate = false;
        this.world.profiler.endSection();
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
