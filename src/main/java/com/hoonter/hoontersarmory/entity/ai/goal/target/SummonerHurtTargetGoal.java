package com.hoonter.hoontersarmory.entity.ai.goal.target;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;
import java.util.Objects;
import java.util.UUID;

public class SummonerHurtTargetGoal extends TargetGoal {

    private final Mob mob;
    private LivingEntity ownerLastHurt;
    private int timestamp;

    public SummonerHurtTargetGoal(Mob mob) {
        super(mob, false);
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }



    @Override
    public boolean canUse() {
        // Retrieve owner UUID
        CompoundTag compoundTag = mob.getPersistentData();
        UUID uuid = compoundTag.getUUID("OwnerUUID");

        Player owner = Objects.requireNonNull(mob.getServer()).getPlayerList().getPlayer(uuid);
        if (owner == null) {
            return false;
        } else {
            this.ownerLastHurt = owner.getLastHurtMob();
            int i = owner.getLastHurtMobTimestamp();
            return i != this.timestamp
                    && this.canAttack(this.ownerLastHurt, TargetingConditions.DEFAULT);
        }
    }

    @Override
    public void start() {
        this.mob.setTarget(this.ownerLastHurt);
        // Retrieve owner UUID
        CompoundTag compoundTag = mob.getPersistentData();
        UUID uuid = compoundTag.getUUID("OwnerUUID");

        Player owner = Objects.requireNonNull(mob.getServer()).getPlayerList().getPlayer(uuid);

        if (owner != null) {
            this.timestamp = owner.getLastHurtMobTimestamp();
        }

        super.start();
    }
}
