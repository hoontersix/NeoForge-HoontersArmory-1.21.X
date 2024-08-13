package com.hoonter.hoontersarmory.event;

import com.hoonter.hoontersarmory.item.NecromancerSword;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.Objects;
import java.util.UUID;


@EventBusSubscriber
public class EventHandler {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        Entity source = event.getSource().getEntity();
        if (source instanceof Player player) {
            ItemStack heldItem = player.getMainHandItem();
            if (heldItem.getItem() instanceof NecromancerSword) {
                NecromancerSword.summonEntity(entity, player);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        Level level = Objects.requireNonNull(entity.level());

        if (!level.isClientSide()) {
            if (entity.getPersistentData().contains("LifeTime")) {
                int lifetime = entity.getPersistentData().getInt("LifeTime");
                lifetime--;
                if (lifetime <= 0) {
                    entity.remove(Entity.RemovalReason.DISCARDED);
                } else {
                    entity.getPersistentData().putInt("LifeTime", lifetime);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        Entity entity = event.getEntity();
        DamageSource damageSource = event.getSource();

        if (entity.getPersistentData().contains("OwnerUUID")) {
            UUID ownerUUID = entity.getPersistentData().getUUID("OwnerUUID");
            Player owner = Objects.requireNonNull(entity.getServer()).getPlayerList().getPlayer(ownerUUID);
            if (damageSource.getEntity() == owner) {
                event.setNewDamage(0);
                return;
            }
        }

        Entity sourceEntity = damageSource.getEntity();
        if (sourceEntity != null) {
            if (sourceEntity.getPersistentData().contains("OwnerUUID")) {
                UUID ownerUUID = damageSource.getEntity().getPersistentData().getUUID("OwnerUUID");
                if (ownerUUID.equals(entity.getUUID())) {
                    event.setNewDamage(0);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        Entity entity = event.getTarget();

        if (entity.getPersistentData().contains("OwnerUUID")) {
            if (entity.getPersistentData().getUUID("OwnerUUID").equals(player.getUUID())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        Entity entity = event.getEntity();
        if (entity.getPersistentData().contains("OwnerUUID")) {
            event.setCanceled(true);
        }
    }
}