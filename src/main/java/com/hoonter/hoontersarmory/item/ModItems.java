package com.hoonter.hoontersarmory.item;

import com.hoonter.hoontersarmory.HoontersArmory;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HoontersArmory.MODID);

    public static final DeferredItem<CrossbowItem> SONIC_CROSSBOW =
            ITEMS.registerItem("sonic_crossbow", SonicCrossbow::new, new Item.Properties().durability(500));

    public static final DeferredItem<CrossbowItem> WITHER_CROSSBOW =
            ITEMS.registerItem("wither_crossbow", WitherCrossbow::new, new Item.Properties().durability(500));

    public static final DeferredItem<SwordItem> ENDERMAN_SWORD =
            ITEMS.registerItem("enderman_sword", EndermanSword::new, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.NETHERITE, 3, -2.4F)));

    public static final DeferredItem<SwordItem> NECROMANCER_SWORD =
            ITEMS.registerItem("necromancer_sword", NecromancerSword::new, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.IRON, 3, -2.4F)));

}
