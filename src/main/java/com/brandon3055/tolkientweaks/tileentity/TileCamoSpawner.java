package com.brandon3055.tolkientweaks.tileentity;

import codechicken.lib.data.MCDataInput;
import com.brandon3055.brandonscore.BrandonsCore;
import com.brandon3055.brandonscore.lib.IActivatableTile;
import com.brandon3055.brandonscore.lib.datamanager.*;
import com.brandon3055.tolkientweaks.ForgeEventHandler;
import com.brandon3055.tolkientweaks.TTFeatures;
import com.brandon3055.tolkientweaks.client.gui.GuiCamoSpawner;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

/**
 * Created by Brandon on 8/02/2015.
 */
public class TileCamoSpawner extends TileChameleon implements ITickable, IActivatableTile {

    //    public ManagedString entityTag = register("entityTag", new ManagedString("")).saveToTile().saveToItem().syncViaTile().finish();
    public ManagedShort minSpawnDelay = register("minSpawnDelay", new ManagedShort(200)).saveToTile().syncViaTile().finish();
    public ManagedShort maxSpawnDelay = register("maxSpawnDelay", new ManagedShort(800)).saveToTile().syncViaTile().finish();

    public ManagedShort activationRange = register("activationRange", new ManagedShort(24)).saveToTile().syncViaTile().finish();
    public ManagedShort spawnRange = register("spawnRange", new ManagedShort(4)).saveToTile().syncViaTile().finish();
    public ManagedBool requirePlayer = register("requirePlayer", new ManagedBool(true)).saveToTile().syncViaTile().finish();
    public ManagedBool ignoreSpawnReq = register("ignoreSpawnReq", new ManagedBool(true)).saveToTile().syncViaTile().finish();
    public ManagedBool spawnerParticles = register("spawnerParticles", new ManagedBool(true)).saveToTile().syncViaTile().finish();
    public ManagedByte spawnCount = register("spawnCount", new ManagedByte(4)).saveToTile().syncViaTile().finish();
    public ManagedByte maxCluster = register("maxCluster", new ManagedByte(32)).saveToTile().syncViaTile().finish();
    public ManagedByte clusterRange = register("clusterRange", new ManagedByte(5)).saveToTile().syncViaTile().finish();

    public ManagedShort spawnDelay = register("spawnDelay", new ManagedShort(0)).saveToTile().syncViaTile().finish();
    public ManagedShort startSpawnDelay = register("startSpawnDelay", new ManagedShort(100)).saveToTile().syncViaTile().finish(); //For rendering

    public List<String> entityTags = new ArrayList<>();

    public boolean disableCamo = false;
    public double mobRotation;

    public TileCamoSpawner() {
        super(TTFeatures.camoKeystone);
    }

    @Override
    public void update() {
        super.update();

        updateSpawnerLogic();

        if (world.isRemote) {
            EntityPlayer player = BrandonsCore.proxy.getClientPlayer();

            if (player == null) {
                return;
            }

            ItemStack key = player.getHeldItemMainhand();
            boolean shouldDisableCamo = (!key.isEmpty() && key.getItem() == TTFeatures.key && key.getItemDamage() == 1);
            if (shouldDisableCamo != disableCamo) {
                disableCamo = shouldDisableCamo;
                updateBlock();
            }
        }
    }

    private void updateSpawnerLogic() {
        if (!isActive() || entityTags.isEmpty()) return;

        if (world.isRemote && spawnerParticles.value) {
            mobRotation += getRotationSpeed();

            double d3 = (double) ((float) pos.getX() + world.rand.nextFloat());
            double d4 = (double) ((float) pos.getY() + world.rand.nextFloat());
            double d5 = (double) ((float) pos.getZ() + world.rand.nextFloat());
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(EnumParticleTypes.FLAME, d3, d4, d5, 0.0D, 0.0D, 0.0D);
        }
        else {
            if (spawnDelay.value == -1) {
                resetTimer();
            }

            if (spawnDelay.value > 0) {
                spawnDelay.value--;
                return;
            }

            boolean spawnedMob = false;

            for (int i = 0; i < spawnCount.value; i++) {
                double spawnX = pos.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double) this.spawnRange.value + 0.5D;
                double spawnY = pos.getY() + world.rand.nextInt(3) - 1;
                double spawnZ = pos.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double) this.spawnRange.value + 0.5D;
                Entity entity = createEntity(world, spawnX, spawnY, spawnZ);
                entity.setPositionAndRotation(spawnX, spawnY, spawnZ, 0, 0);

                int nearby = world.getEntitiesWithinAABB(entity.getClass(), (new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), (pos.getX() + 1), (pos.getY() + 1), (pos.getZ() + 1))).grow(clusterRange.value)).size();

                if (nearby >= maxCluster.value) {
                    this.resetTimer();
                    return;
                }

                EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving) entity : null;
                entity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, world.rand.nextFloat() * 360.0F, 0.0F);

                boolean canSpawn;
                if (ignoreSpawnReq.value) {
                    Event.Result result = ForgeEventFactory.canEntitySpawn(entityliving, world, (float) entity.posX, (float) entity.posY, (float) entity.posZ);
                    canSpawn = isNotColliding(entity) && (result == Event.Result.DEFAULT || result == Event.Result.ALLOW);
                }
                else {
                    canSpawn = canEntitySpawnSpawner(entityliving, world, (float) entity.posX, (float) entity.posY, (float) entity.posZ);
                }

                if (canSpawn) {
                    if (!requirePlayer.value && entity instanceof EntityLiving) {
                        ((EntityLiving) entity).enablePersistence();
                        entity.getEntityData().setLong("DESpawnedMob", System.currentTimeMillis()); //Leaving this in case some mod wants to use it.
                        ForgeEventHandler.onMobSpawnedBySpawner((EntityLiving) entity);
                    }
                    AnvilChunkLoader.spawnEntity(entity, world);
                    world.playEvent(2004, pos, 0);
                    if (entityliving != null) {
                        entityliving.spawnExplosionParticle();
                    }

                    spawnedMob = true;
                }
            }

            if (spawnedMob) {
                resetTimer();
            }
        }
    }

    private boolean canEntitySpawnSpawner(EntityLiving entity, World world, float x, float y, float z) {
        Event.Result result = ForgeEventFactory.canEntitySpawn(entity, world, x, y, z, true);
        if (result == Event.Result.DEFAULT) {
            boolean isSlime = entity instanceof EntitySlime;
            return (isSlime || entity.getCanSpawnHere()) && entity.isNotColliding();
        }
        else return result == Event.Result.ALLOW;
    }

    public boolean isNotColliding(Entity entity) {
        return !world.containsAnyLiquid(entity.getEntityBoundingBox()) && world.getCollisionBoxes(entity, entity.getEntityBoundingBox()).isEmpty() && world.checkNoEntityCollision(entity.getEntityBoundingBox(), entity);
    }

    private void resetTimer() {
        spawnDelay.value = (short) Math.min(MathHelper.getInt(world.rand, minSpawnDelay.value, maxSpawnDelay.value), Short.MAX_VALUE);
        startSpawnDelay.value = spawnDelay.value;
    }

    private boolean isActive() {
        if (entityTags.isEmpty()) {
            return false;
        }
        return !requirePlayer.value || world.isAnyPlayerWithinRangeAt(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, activationRange.value);
    }

    //region Render

    public double getRotationSpeed() {
        return isActive() ? 0.5 + (1D - ((double) spawnDelay.value / (double) startSpawnDelay.value)) * 4.5 : 0;
    }

    //endregion

    @Override
    public boolean onBlockActivated(IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isCreative() && stack.isEmpty()) {
            if (player.isSneaking()) {
                entityTags.clear();
                if (!world.isRemote) {
                    player.sendMessage(new TextComponentString("All existing entities cleared..."));
                }
            }
            else if (world.isRemote) {
                openGUI();
                player.sendMessage(new TextComponentString("Spawner Entities:"));
                entityTags.forEach(s -> player.sendMessage(new TextComponentString(s)));
            }
            return true;
        }

        if (player.isCreative()) {
            if (stack.getItem() == Items.SPAWN_EGG) {
                NBTTagCompound compound = stack.getSubCompound("EntityTag");
                if (compound != null && compound.hasKey("id")) {
                    entityTags.add(compound.toString());
                    if (!world.isRemote) {
                        player.sendMessage(new TextComponentString("Entity Added"));
                        player.sendMessage(new TextComponentString("Spawner Entities:"));
                        entityTags.forEach(s -> player.sendMessage(new TextComponentString(s)));
                    }
                }
                return true;
            }
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    public void openGUI() {
        Minecraft.getMinecraft().displayGuiScreen(new GuiCamoSpawner(Minecraft.getMinecraft().player, this));
    }

    @Override
    public void receivePacketFromClient(MCDataInput data, EntityPlayerMP client, int id) {
        if (!client.capabilities.isCreativeMode) {
            return;
        }

        if (id == 0) {
            String field = data.readString();
            IManagedData managedData = getDataManager().getDataByName(field);

            if (managedData instanceof ManagedString) {
                ManagedString stringData = (ManagedString) managedData;
                stringData.value = data.readString();
            }
            else if (managedData instanceof ManagedByte) {
                ManagedByte intData = (ManagedByte) managedData;
                intData.value = (byte) data.readInt();
            }
            else if (managedData instanceof ManagedShort) {
                ManagedShort intData = (ManagedShort) managedData;
                intData.value = (short) data.readInt();
            }
            else if (managedData instanceof ManagedInt) {
                ManagedInt intData = (ManagedInt) managedData;
                intData.value = data.readInt();
            }
            else if (managedData instanceof ManagedBool) {
                ManagedBool boolData = (ManagedBool) managedData;
                boolData.value = data.readBoolean();
            }
        }

        dirtyBlock();
        updateBlock();
    }

    @Override
    public void writeExtraNBT(NBTTagCompound compound) {
        super.writeExtraNBT(compound);
        NBTTagList list = new NBTTagList();
        for (String tag : entityTags) {
            list.appendTag(new NBTTagString(tag));
        }
        compound.setTag("EntityList", list);
    }

    @Override
    public void readExtraNBT(NBTTagCompound compound) {
        super.readExtraNBT(compound);
        entityTags.clear();
        NBTTagList list = compound.getTagList("EntityList", 8);
        for (int i = 0; i < list.tagCount(); i++) {
            entityTags.add(list.getStringTagAt(i));
        }
    }

    @Override
    public boolean disableCamo() {
        return disableCamo;
    }

    @Override
    public boolean randomBool() {
        return false;
    }

    //Entity Stuffs

    public Entity createEntity(World world, double x, double y, double z) {
        String randTag = entityTags.get(world.rand.nextInt(entityTags.size()));
        try {
            NBTTagCompound entityData = JsonToNBT.getTagFromJson(randTag);
            if (!entityData.hasKey("id")) return null;
            String id = entityData.getString("id");

            Entity entity = EntityList.createEntityByIDFromName(getCachedRegName(id), world);
            if (entity == null) {
                entity = new EntityPig(world);
            }
            else {
//                entity.setPosition(x, y, z);
                EntityLiving entityliving = (EntityLiving) entity;
                entityliving.rotationYawHead = entityliving.rotationYaw;
                entityliving.renderYawOffset = entityliving.rotationYaw;
                entityliving.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityliving)), null);

                NBTTagCompound nbttagcompound1 = entity.writeToNBT(new NBTTagCompound());
                UUID uuid = entity.getUniqueID();
                nbttagcompound1.merge(entityData);
                entity.readFromNBT(nbttagcompound1);
                entity.setUniqueId(uuid);
            }
            return entity;
        }
        catch (Throwable e) {
            return new EntityPig(world);
        }
    }

    private static Map<String, ResourceLocation> rlCache = new WeakHashMap<>();

    public static ResourceLocation getCachedRegName(String name) {
        return rlCache.computeIfAbsent(name, ResourceLocation::new);
    }
}
