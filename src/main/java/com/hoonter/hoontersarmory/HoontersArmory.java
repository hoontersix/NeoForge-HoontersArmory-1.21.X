package com.hoonter.hoontersarmory;

import com.hoonter.hoontersarmory.entity.ModEntities;
import com.hoonter.hoontersarmory.item.ModCreativeModeTabs;
import com.hoonter.hoontersarmory.item.ModItems;
import com.hoonter.hoontersarmory.util.ModItemProperties;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(HoontersArmory.MODID)
public class HoontersArmory
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "hoontersarmory";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public HoontersArmory(IEventBus modEventBus, ModContainer modContainer)
    {
        ModItems.ITEMS.register(modEventBus);
        ModCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        ModEntities.ENTITIES.register(modEventBus);

        modEventBus.addListener(this::onClientSetup);
    }

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(ModItemProperties.addCustomItemProperties());
    }
}