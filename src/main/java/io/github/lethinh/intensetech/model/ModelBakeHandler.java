/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.model;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import io.github.lethinh.intensetech.IntenseTech;
import io.github.lethinh.intensetech.block.BlockBase;
import io.github.lethinh.intensetech.item.ItemBase;
import io.github.lethinh.intensetech.manager.BlocksManager;
import io.github.lethinh.intensetech.manager.ItemsManager;
import io.github.lethinh.intensetech.model.baked.FlatBlockstateModel;
import io.github.lethinh.intensetech.model.baked.SimpleItemModel;
import io.github.lethinh.intensetech.utils.ConstFunctionUtils;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

// Item model is the stablest one for now. Blockstate model has worked out, but in inventory doesn't.
public class ModelBakeHandler {

	private static final Multimap<ItemBase, TextureAtlasSprite> ITEMS_SPRITES = HashMultimap.create();
	private static final Map<BlockBase, TextureAtlasSprite> BLOCKS_SPIRTES = Maps.newHashMap();

	@SubscribeEvent
	public void onPreTextureStitch(TextureStitchEvent.Pre event) {
		TextureMap textureMap = event.getMap();

		for (ItemBase item : ItemsManager.ITEMS) {
			for (String texture : item.getTextures()) {
				ResourceLocation location = ConstFunctionUtils.prefixResourceLocation("items/" + texture);
				TextureAtlasSprite sprite = textureMap.registerSprite(location);
				ITEMS_SPRITES.put(item, sprite);
			}
		}

		for (BlockBase block : BlocksManager.BLOCKS) {
			ResourceLocation location = ConstFunctionUtils.prefixResourceLocation("blocks/" + block.getTexture());
			TextureAtlasSprite sprite = textureMap.registerSprite(location);
			BLOCKS_SPIRTES.put(block, sprite);
		}
	}

	@SubscribeEvent
	public void onModelBake(ModelBakeEvent event) {
		IRegistry<ModelResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();
		ModelLoader modelLoader = event.getModelLoader();

		for (Map.Entry<ItemBase, Collection<TextureAtlasSprite>> entry : ITEMS_SPRITES.asMap().entrySet()) {
			ItemBase item = entry.getKey();
			Collection<TextureAtlasSprite> unsignedSprites = entry.getValue();
			TextureAtlasSprite[] sprites = new TextureAtlasSprite[unsignedSprites.size()];
			sprites = unsignedSprites.toArray(sprites);

			SimpleItemModel itemModel = new SimpleItemModel(item.isAmbientOcclusion(), item.isGui3d(),
					sprites);
			ModelResourceLocation modelLocation = new ModelResourceLocation(item.getRegistryName(), "inventory");
			modelRegistry.putObject(modelLocation, itemModel);
		}

		for (Map.Entry<BlockBase, TextureAtlasSprite> entry : BLOCKS_SPIRTES.entrySet()) {
			BlockBase block = entry.getKey();
			TextureAtlasSprite sprite = entry.getValue();

			FlatBlockModelDefinition modelDefinition = new FlatBlockModelDefinition(block.getTexture(),
					block.isUvLock(), block.isSmooth(), block.isGui3d());
			ModelBlockDefinition definition = modelDefinition.getDefinition();
			ModelResourceLocation normal = new ModelResourceLocation(block.getRegistryName(), "normal");
			ModelResourceLocation inventory = new ModelResourceLocation(block.getRegistryName(), "inventory");

			try {
				Field field = modelLoader.getClass().getSuperclass()
						.getDeclaredField(IntenseTech.isDevEnv() ? "blockDefinitions" : "field_177614_t");
				field.setAccessible(true);
				Map<ResourceLocation, ModelBlockDefinition> blockDefinitions = (Map<ResourceLocation, ModelBlockDefinition>) field
						.get(modelLoader);
				blockDefinitions.put(
						new ResourceLocation(IntenseTech.MOD_ID, "blockstates/" + block.getName() + ".json"),
						definition);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

			try {
				modelRegistry.putObject(normal,
						new FlatBlockstateModel(block.isAmbientOcclusion(), block.isGui3d(), sprite,
								definition.getVariant(normal.getVariant())));
				modelRegistry.putObject(inventory,
						new FlatBlockstateModel(block.isAmbientOcclusion(), block.isGui3d(), sprite,
								definition.getVariant(inventory.getVariant())));
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Register ItemBlock model
			SimpleItemModel itemBlockModel = new SimpleItemModel(false, block.isGui3d(), sprite);
			ModelResourceLocation modelLocation = new ModelResourceLocation(block.getRegistryName(), "inventory");
			modelRegistry.putObject(modelLocation, itemBlockModel);
		}
	}

}
