/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.model;

import java.util.Collection;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import io.github.lethinh.intensetech.api.utils.ConstFunctionUtils;
import io.github.lethinh.intensetech.item.ItemBase;
import io.github.lethinh.intensetech.manager.ItemsManager;
import io.github.lethinh.intensetech.model.baked.ItemBakedModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

// Only supports Item for now
public class ModelBakeHandler {

	private static final Multimap<ItemBase, TextureAtlasSprite> SPRITES_MAP = HashMultimap.create();

	@SubscribeEvent
	public void onPreTextureStitch(TextureStitchEvent.Pre event) {
		for (ItemBase item : ItemsManager.ITEMS) {
			for (String texture : item.getTextures()) {
				ResourceLocation location = ConstFunctionUtils.prefixResourceLocation("items/" + texture);
				TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(location);
				SPRITES_MAP.put(item, sprite);
			}
		}
	}

	@SubscribeEvent
	public void onModelBake(ModelBakeEvent event) {
		for (ItemBase item : SPRITES_MAP.keySet()) {
			Collection<TextureAtlasSprite> unsignedSprites = SPRITES_MAP.get(item);
			TextureAtlasSprite[] sprites = new TextureAtlasSprite[unsignedSprites.size()];
			sprites = unsignedSprites.toArray(sprites);

			ItemBakedModel itemModel = new ItemBakedModel(item.isAmbientOcclusion(), item.isGui3d(), sprites);
			ModelResourceLocation modelLocation = new ModelResourceLocation(item.getRegistryName(), "");
			event.getModelRegistry().putObject(modelLocation, itemModel);
		}
	}

}
