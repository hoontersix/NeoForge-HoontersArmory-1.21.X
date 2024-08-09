package com.hoonter.hoontersarmory.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class WitherCrossbow extends CrossbowItem {
    public WitherCrossbow(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected Projectile createProjectile(Level pLevel, LivingEntity pShooter, ItemStack pWeapon, ItemStack pAmmo, boolean pIsCrit) {
        Vec3 eyePos = pShooter.getEyePosition();
        return new WitherSkull(pLevel, pShooter, eyePos);
    }

    @Override
    protected void shootProjectile(LivingEntity pShooter, Projectile pProjectile, int pIndex, float pVelocity, float pInaccuracy, float pAngle, @Nullable LivingEntity pTarget) {
        super.shootProjectile(pShooter, pProjectile, pIndex, pVelocity, pInaccuracy, pAngle, pTarget);
        pProjectile.setPos(pShooter.getEyePosition().add(0, -0.15F, 0));
    }
}
