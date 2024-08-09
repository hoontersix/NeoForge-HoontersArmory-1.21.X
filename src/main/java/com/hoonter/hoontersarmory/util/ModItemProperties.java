package com.hoonter.hoontersarmory.util;

import com.hoonter.hoontersarmory.item.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CrossbowItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModItemProperties {
    public static Runnable addCustomItemProperties() {
        return (() -> {
            ItemProperties.register(
                    ModItems.SONIC_CROSSBOW.get(),
                    ResourceLocation.withDefaultNamespace("pull"),
                    (p_351682_, p_351683_, p_351684_, p_351685_) -> {
                        if (p_351684_ == null) {
                            return 0.0F;
                        } else {
                            return CrossbowItem.isCharged(p_351682_)
                                    ? 0.0F
                                    : (float)(p_351682_.getUseDuration(p_351684_) - p_351684_.getUseItemRemainingTicks())
                                    / (float)CrossbowItem.getChargeDuration(p_351682_, p_351684_);
                        }
                    }
            );
            ItemProperties.register(
                    ModItems.SONIC_CROSSBOW.get(),
                    ResourceLocation.withDefaultNamespace("pulling"),
                    (p_174605_, p_174606_, p_174607_, p_174608_) -> p_174607_ != null
                            && p_174607_.isUsingItem()
                            && p_174607_.getUseItem() == p_174605_
                            && !CrossbowItem.isCharged(p_174605_)
                            ? 1.0F
                            : 0.0F
            );
            ItemProperties.register(
                    ModItems.WITHER_CROSSBOW.get(),
                    ResourceLocation.withDefaultNamespace("charged"),
                    (p_275891_, p_275892_, p_275893_, p_275894_) -> CrossbowItem.isCharged(p_275891_) ? 1.0F : 0.0F
            );

            ItemProperties.register(
                    ModItems.WITHER_CROSSBOW.get(),
                    ResourceLocation.withDefaultNamespace("pull"),
                    (p_351682_, p_351683_, p_351684_, p_351685_) -> {
                        if (p_351684_ == null) {
                            return 0.0F;
                        } else {
                            return CrossbowItem.isCharged(p_351682_)
                                    ? 0.0F
                                    : (float)(p_351682_.getUseDuration(p_351684_) - p_351684_.getUseItemRemainingTicks())
                                    / (float)CrossbowItem.getChargeDuration(p_351682_, p_351684_);
                        }
                    }
            );
            ItemProperties.register(
                    ModItems.WITHER_CROSSBOW.get(),
                    ResourceLocation.withDefaultNamespace("pulling"),
                    (p_174605_, p_174606_, p_174607_, p_174608_) -> p_174607_ != null
                            && p_174607_.isUsingItem()
                            && p_174607_.getUseItem() == p_174605_
                            && !CrossbowItem.isCharged(p_174605_)
                            ? 1.0F
                            : 0.0F
            );
            ItemProperties.register(
                    ModItems.SONIC_CROSSBOW.get(),
                    ResourceLocation.withDefaultNamespace("charged"),
                    (p_275891_, p_275892_, p_275893_, p_275894_) -> CrossbowItem.isCharged(p_275891_) ? 1.0F : 0.0F
            );
        });
    }
}
