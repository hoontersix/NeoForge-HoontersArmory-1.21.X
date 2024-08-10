package com.hoonter.hoontersarmory.item;

import com.hoonter.hoontersarmory.entity.ai.goal.target.SummonerHurtTargetGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;

import java.lang.reflect.Constructor;
import java.util.UUID;


public class NecromancerSword extends SwordItem {
    public NecromancerSword(Tier pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    public NecromancerSword(Properties pProperties) {
        super(Tiers.IRON, pProperties);
    }


    public static void summonEntity(Entity deadEntity, Player player) {
        Level level = player.level();
        if (!level.isClientSide()) {
            if (deadEntity instanceof Mob deadMob) {
                Mob mob = getNewMob(deadMob, level);
                int lifeTime = 200;
                // Store data
                storeData(mob, player, lifeTime);
                // Add goals
                addGoals(mob);
                // Mob attributes, effects and spawn
                setAttributes(deadMob, mob, lifeTime);
                level.addFreshEntity(mob);
                // Spawn lightning
                LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                lightningBolt.setVisualOnly(true);
                lightningBolt.setPos(deadEntity.getPosition(0));
                level.addFreshEntity(lightningBolt);
            }
        }
    }

    public static Mob getNewMob(Mob mob, Level level) {
        try {
            Class<? extends Mob> mobClass = mob.getClass();
            Constructor<? extends Mob> constructor = mobClass.getConstructor(EntityType.class, Level.class);

            return constructor.newInstance(mob.getType(), level);
        } catch (Exception e) {
            e.printStackTrace();
            return new Zombie(EntityType.ZOMBIE, level);
        }
    }

    public static void addGoals(Mob mob) {
        mob.targetSelector.removeAllGoals(goal -> true);
        mob.targetSelector.addGoal(
                3, new NearestAttackableTargetGoal<>(mob, Mob.class, 5, false, false,
                        p_28879_ -> {
                            if (p_28879_ instanceof Enemy) {
                                boolean ownerUUIDExists = p_28879_.getPersistentData().contains("OwnerUUID");
                                if (ownerUUIDExists) {
                                    UUID ownerUUID = p_28879_.getPersistentData().getUUID("OwnerUUID");
                                    return !ownerUUID.equals(mob.getPersistentData().getUUID("OwnerUUID"));
                                }
                                return true;
                            }
                            return false;
                        }
                )
        );

        mob.targetSelector.addGoal(2, new SummonerHurtTargetGoal(mob));
    }

    public static void copyEquipment(Mob oldMob, Mob newMob) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack armorItem = oldMob.getItemBySlot(slot);
            newMob.setItemSlot(slot, armorItem.copy());
        }
    }

    public static void setAttributes(Mob deadMob, Mob mob, int lifeTime) {
        mob.setPos(deadMob.getPosition(0));
        mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, lifeTime, 1));
        mob.setHealth(mob.getMaxHealth());
        copyEquipment(deadMob, mob);
        if (deadMob.isBaby()) { mob.setBaby(true); }
    }

    public static void storeData(Mob mob, Player owner, int lifeTime) {
        CompoundTag mobData = mob.getPersistentData();
        mobData.putUUID("OwnerUUID", owner.getUUID());
        mobData.putInt("LifeTime", lifeTime);
    }

}
