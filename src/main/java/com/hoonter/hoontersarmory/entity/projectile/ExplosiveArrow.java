package com.hoonter.hoontersarmory.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ExplosiveArrow extends Arrow {


    public ExplosiveArrow(Level pLevel, LivingEntity pOwner, ItemStack pPickupItemStack, @Nullable ItemStack pFiredFromWeapon) {
        super(pLevel, pOwner, pPickupItemStack, pFiredFromWeapon);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        Vec3 pos = pResult.getLocation();
        Level level = pResult.getEntity().getCommandSenderWorld();
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, pos.x, pos.y, pos.z, 3,  0.0, 0.0, 0.0, 0.0);
            this.playSound(SoundEvents.DRAGON_FIREBALL_EXPLODE, 3.0F, 1.0F);
        }
        AABB aabb = new AABB(new BlockPos((int) pos.x, (int) pos.y, (int) pos.z)).inflate(2);
        for (Entity entity : level.getEntities(null, aabb)) {
            if (entity instanceof LivingEntity livingEntity) {
                if (this.getOwner() instanceof Player player) {
                    livingEntity.hurt(level.damageSources().playerAttack(player),10);
                }
                else {
                    livingEntity.hurt(level.damageSources().generic(),10);
                }
            }
        }
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        Vec3 pos = pResult.getLocation();
        Level level = this.level();
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, pos.x, pos.y, pos.z, 3,  0.0, 0.0, 0.0, 0.0);
            this.playSound(SoundEvents.DRAGON_FIREBALL_EXPLODE, 3.0F, 1.0F);
        }
        AABB aabb = new AABB(new BlockPos((int) pos.x, (int) pos.y, (int) pos.z)).inflate(2);
        for (Entity entity : level.getEntities(null, aabb)) {
            if (entity instanceof LivingEntity livingEntity) {
                if (this.getOwner() instanceof Player player) {
                    livingEntity.hurt(level.damageSources().playerAttack(player),10);
                }
                else {
                    livingEntity.hurt(level.damageSources().generic(),10);
                }
            }
        }
        this.remove(RemovalReason.DISCARDED);
    }

    private void generateRandomExplosions() {
        Level level = getCommandSenderWorld();
        if (!level.isClientSide()) {
            for (int i = 0; i < 3; i++) {
                level.explode(
                        this,
                        getX() - 1 + (2 * random.nextDouble()),
                        getY() - 1 + (2 * random.nextDouble()),
                        getZ() + random.nextDouble(),
                        2,
                        false,
                        Level.ExplosionInteraction.NONE);
            }
        }
    }
}