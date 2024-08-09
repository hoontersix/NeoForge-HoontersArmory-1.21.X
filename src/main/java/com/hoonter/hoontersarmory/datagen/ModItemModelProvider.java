package com.hoonter.hoontersarmory.datagen;

import com.hoonter.hoontersarmory.HoontersArmory;
import com.hoonter.hoontersarmory.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, HoontersArmory.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicHandheld(ModItems.ENDERMAN_SWORD.getId());
        basicHandheld(ModItems.NECROMANCER_SWORD.getId());
    }

    public ItemModelBuilder basicHandheld(ResourceLocation item) {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + item.getPath()));
    }

}
