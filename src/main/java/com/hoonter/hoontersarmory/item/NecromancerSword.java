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
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;

import java.util.UUID;


public class NecromancerSword extends SwordItem {

    public NecromancerSword(Properties pProperties) {
        super(Tiers.IRON, pProperties);
    }


    public static void summonEntity(Entity deadEntity, Player player) {
        Level level = player.level();
        if (!level.isClientSide()) {
            if (deadEntity instanceof Mob deadMob) {
                Mob mob;
                mob = new Zombie(EntityType.ZOMBIE, level);
                int lifeTime = 200;
                // Store data
                storeData(mob, player, lifeTime);
                // Add goals
                addGoals(mob);
                // Mob attributes, effects, equipment and spawn
                setAttributes(deadMob, mob, lifeTime);
                copyEquipment(deadMob, mob);
                level.addFreshEntity(mob);
                // Spawn lightning
                LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                lightningBolt.setVisualOnly(true);
                lightningBolt.setPos(deadEntity.getPosition(0));
                level.addFreshEntity(lightningBolt);
            }
        }
    }

    private static void addGoals(Mob mob) {
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

    private static void setAttributes(Mob deadMob, Mob mob, int lifeTime) {
        // Position, health and effect
        mob.setPos(deadMob.getPosition(0));
        mob.setHealth(mob.getMaxHealth());
        mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, lifeTime, 1));
    }

    private static void copyEquipment(Mob oldMob, Mob newMob) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack item = oldMob.getItemBySlot(slot);
            newMob.setItemSlot(slot, item.copy());
        }
    }

    private static void storeData(Mob mob, Player owner, int lifeTime) {
        CompoundTag summonData = mob.getPersistentData();
        summonData.putUUID("OwnerUUID", owner.getUUID());
        summonData.putInt("LifeTime", lifeTime);
    }
}
