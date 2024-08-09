package com.hoonter.hoontersarmory.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SonicCrossbow extends CrossbowItem {

    private boolean startSoundPlayed = false;
    private boolean midLoadSoundPlayed = false;
    private static final SonicCrossbow.ChargingSounds DEFAULT_SOUNDS = new SonicCrossbow.ChargingSounds(
            Optional.of(SoundEvents.TRIDENT_RIPTIDE_1), Optional.of(SoundEvents.TRIDENT_RIPTIDE_2), Optional.of(SoundEvents.TRIDENT_RIPTIDE_3)
    );

    public SonicCrossbow(Properties pProperties) {
        super(pProperties);
    }

    SonicCrossbow.ChargingSounds getChargingSounds() {
        return DEFAULT_SOUNDS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        ChargedProjectiles chargedprojectiles = itemstack.get(DataComponents.CHARGED_PROJECTILES);
        if (chargedprojectiles != null && !chargedprojectiles.isEmpty()) {
            this.performShooting(pLevel, pPlayer, pHand, itemstack, 0, 1.0F, null);
        } else {
            this.startSoundPlayed = false;
            this.midLoadSoundPlayed = false;
            pPlayer.startUsingItem(pHand);
        }
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(Items.ARROW));
        int i = this.getUseDuration(pStack, pEntityLiving) - pTimeLeft;
        float f = getPowerForTime(i, pStack, pEntityLiving);
        if (f >= 1.0F) {
            pStack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(list));
            SonicCrossbow.ChargingSounds soniccrossbow$chargingsounds = this.getChargingSounds();
            soniccrossbow$chargingsounds.end()
                    .ifPresent(
                            p_352852_ -> pLevel.playSound(
                                    null,
                                    pEntityLiving.getX(),
                                    pEntityLiving.getY(),
                                    pEntityLiving.getZ(),
                                    p_352852_.value(),
                                    pEntityLiving.getSoundSource(),
                                    1.0F,
                                    1.0F / (pLevel.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F
                            )
                    );
        }
    }

    private static float getPowerForTime(int pTimeLeft, ItemStack pStack, LivingEntity pShooter) {
        float f = (float)pTimeLeft / (float)getChargeDuration(pStack, pShooter);
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pCount) {
        if (!pLevel.isClientSide) {
            SonicCrossbow.ChargingSounds soniccrossbow$chargingsounds = this.getChargingSounds();
            float f = (float)(pStack.getUseDuration(pLivingEntity) - pCount) / (float)getChargeDuration(pStack, pLivingEntity);
            if (f < 0.2F) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
            }

            if (f >= 0.2F && !this.startSoundPlayed) {
                this.startSoundPlayed = true;
                soniccrossbow$chargingsounds.start()
                        .ifPresent(
                                p_352849_ -> pLevel.playSound(
                                        null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), p_352849_.value(), SoundSource.PLAYERS, 0.5F, 1.0F
                                )
                        );
            }

            if (f >= 0.5F && !this.midLoadSoundPlayed) {
                this.midLoadSoundPlayed = true;
                soniccrossbow$chargingsounds.mid()
                        .ifPresent(
                                p_352855_ -> pLevel.playSound(
                                        null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), p_352855_.value(), SoundSource.PLAYERS, 0.5F, 1.0F
                                )
                        );
            }
        }
    }

    @Override
    public void performShooting(Level pLevel, LivingEntity pShooter, InteractionHand pHand, ItemStack pWeapon, float pVelocity, float pInaccuracy, @Nullable LivingEntity pTarget) {
        Vec3 shooterEyePos = pShooter.getEyePosition();
        Vec3 shootPos = shooterEyePos.add(0, 0, 0);
        Vec3 shootDirection = pShooter.getLookAngle();
        double rayLength = 20;

        // Particles
        int particleCount = (int) (rayLength * 0.5);
        Vec3 step = shootDirection.scale(rayLength / particleCount);

        for (int i = 0; i < particleCount; i++) {
            Vec3 particlePos = shootPos.add(step.scale(i));
            if (pLevel instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, particlePos.x, particlePos.y, particlePos.z, 1, 0.0, 0.0, 0.0, 0.0);
            }
        }

        int hitBoxes = 20;
        step = shootDirection.scale(rayLength / hitBoxes);
        Vec3 lastPos = shootPos;

        for (int i = 0; i < hitBoxes; i++) {
            Vec3 currentPos = shootPos.add(step.scale(i));
            AABB aabb = new AABB(lastPos, currentPos).inflate(1);
            for (Entity entity : pLevel.getEntities(pShooter, aabb)) {
                if (entity instanceof LivingEntity hitEntity) {
                    if (pShooter instanceof Player player) {
                        hitEntity.hurt(pLevel.damageSources().playerAttack(player), 30.0F);
                    }
                    else {
                        hitEntity.hurt(pLevel.damageSources().generic(), 30.0F);
                    }
                    // Knockback
                    double knockbackResistance = hitEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
                    double knockback = 1 * (1.0 - knockbackResistance);
                    hitEntity.push(shootDirection.x() * knockback, shootDirection.y() * knockback, shootDirection.z() * knockback);
                }
            }
            lastPos = new Vec3(currentPos.x, currentPos.y, currentPos.z);
        }

        pShooter.playSound(SoundEvents.WARDEN_SONIC_BOOM, 3.0F, 1.0F);

        pWeapon.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
        pWeapon.hurtAndBreak(100, pShooter, LivingEntity.getSlotForHand(pHand));
    }

    public static record ChargingSounds(Optional<Holder<SoundEvent>> start, Optional<Holder<SoundEvent>> mid, Optional<Holder<SoundEvent>> end) {
        public static final Codec<SonicCrossbow.ChargingSounds> CODEC = RecordCodecBuilder.create(
                p_345672_ -> p_345672_.group(
                                SoundEvent.CODEC.optionalFieldOf("start").forGetter(SonicCrossbow.ChargingSounds::start),
                                SoundEvent.CODEC.optionalFieldOf("mid").forGetter(SonicCrossbow.ChargingSounds::mid),
                                SoundEvent.CODEC.optionalFieldOf("end").forGetter(SonicCrossbow.ChargingSounds::end)
                        )
                        .apply(p_345672_, SonicCrossbow.ChargingSounds::new)
        );
    }
}
