package com.hoonter.hoontersarmory.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class EndermanSword extends SwordItem {
    public EndermanSword(Tier pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    public EndermanSword(Properties pProperties) {
        super(Tiers.NETHERITE, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        Vec3 playerPos = pPlayer.getEyePosition();
        Vec3 previousPos = playerPos;

        boolean teleport = false;
        for (int i = 0; i < 20; i++) {
            if (i%2 == 0) {
                previousPos = playerPos;
            }
            playerPos = playerPos.add(pPlayer.getLookAngle());

            BlockPos blockPos = new BlockPos((int) playerPos.x, (int) playerPos.y, (int) playerPos.z);
            BlockState blockState = pLevel.getBlockState(blockPos);

            if (blockState.getBlock() != Blocks.AIR) {
                playerPos = previousPos;
                teleport = true;
                break;
            }
        }
        if (teleport) {
            pPlayer.setPos(playerPos.x, playerPos.y, playerPos.z);
            pPlayer.playSound(SoundEvents.ENDERMAN_TELEPORT, 3.0F, 1.0F);
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }

}
