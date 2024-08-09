package com.hoonter.hoontersarmory.entity.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
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

public class LightningArrow extends Arrow {

    public LightningArrow(Level pLevel, LivingEntity pOwner, ItemStack pPickupItemStack, @Nullable ItemStack pFiredFromWeapon) {
        super(pLevel, pOwner, pPickupItemStack, pFiredFromWeapon);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        generateRandomLightnings();
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        generateRandomLightnings();
        this.remove(RemovalReason.DISCARDED);
    }

    private void generateRandomLightnings() {
        Level level = getCommandSenderWorld();
        if (!level.isClientSide()) {
            for (int i = 0; i < 1; i++) {
                LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                lightningBolt.setDamage(0);
                Vec3 vec3 = getPosition(0);
                vec3.add(
                        getX() - 1 + (2 * random.nextDouble()),
                        getY() - 1 + (2 * random.nextDouble()),
                        getZ() + random.nextDouble()
                );
                lightningBolt.setPos(vec3);
                level.addFreshEntity(lightningBolt);

                AABB aabb = lightningBolt.getBoundingBox();
                for (Entity entity : level.getEntities(null, aabb)) {
                    if (entity instanceof LivingEntity livingEntity) {
                        if (this.getOwner() instanceof Player player) {
                            entity.hurt(damageSources().playerAttack(player), 10);
                        }
                    }
                }
            }
        }
    }

}
