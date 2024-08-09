package com.hoonter.hoontersarmory.item;

import com.hoonter.hoontersarmory.HoontersArmory;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HoontersArmory.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_TAB = CREATIVE_MODE_TABS.register("mod_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("creative_tab.mod_tab"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.SONIC_CROSSBOW.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.SONIC_CROSSBOW.get());
                output.accept(ModItems.WITHER_CROSSBOW.get());
                output.accept(ModItems.ENDERMAN_SWORD.get());
                output.accept(ModItems.NECROMANCER_SWORD.get());
            }).build());
}
