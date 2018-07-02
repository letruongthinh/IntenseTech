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
import io.github.lethinh.intensetech.api.utils.ConstFunctionUtils;
import io.github.lethinh.intensetech.block.BlockBase;
import io.github.lethinh.intensetech.item.ItemBase;
import io.github.lethinh.intensetech.manager.BlocksManager;
import io.github.lethinh.intensetech.manager.ItemsManager;
import io.github.lethinh.intensetech.model.baked.FlatBakedBlockstateModel;
import io.github.lethinh.intensetech.model.baked.SimpleBakedItemModel;
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

// Only supports Item for now
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

			SimpleBakedItemModel itemModel = new SimpleBakedItemModel(item.isAmbientOcclusion(), item.isGui3d(),
					sprites);
			ModelResourceLocation modelLocation = new ModelResourceLocation(item.getRegistryName(), "inventory");
			modelRegistry.putObject(modelLocation, itemModel);
		}

		for (Map.Entry<BlockBase, TextureAtlasSprite> entry : BLOCKS_SPIRTES.entrySet()) {
			BlockBase block = entry.getKey();
			TextureAtlasSprite sprite = entry.getValue();

			FlatBlockModelDefinition modelDefinition = new FlatBlockModelDefinition(block.getTexture());
			ModelBlockDefinition definition = modelDefinition.getDefinition();
			ModelResourceLocation normal = new ModelResourceLocation(block.getRegistryName(), "normal");
			ModelResourceLocation inventory = new ModelResourceLocation(block.getRegistryName(), "inventory");

			try {
				Field field = modelLoader.getClass().getSuperclass().getDeclaredField("blockDefinitions");
				field.setAccessible(true);
				Map<ResourceLocation, ModelBlockDefinition> blockDefinitions = (Map<ResourceLocation, ModelBlockDefinition>) field
						.get(modelLoader);
				blockDefinitions.put(
						new ResourceLocation(IntenseTech.MOD_ID, "blockstates/" + block.getName() + ".json"),
						definition);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

			// List<Variant> normalVars =
			// definition.getVariant(normal.getVariant()).getVariantList();
			// List<Variant> invVars =
			// definition.getVariant(inventory.getVariant()).getVariantList();

			try {
				modelRegistry.putObject(normal, new FlatBakedBlockstateModel(false, block.isGui3d(), sprite,
						definition.getVariant(normal.getVariant())));
				modelRegistry.putObject(inventory, new FlatBakedBlockstateModel(false, block.isGui3d(), sprite,
						definition.getVariant(inventory.getVariant())));
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Register ItemBlock model
			SimpleBakedItemModel itemBlockModel = new SimpleBakedItemModel(false, block.isGui3d(), sprite);
			ModelResourceLocation modelLocation = new ModelResourceLocation(block.getRegistryName(), "inventory");
			modelRegistry.putObject(modelLocation, itemBlockModel);

//			IModel normalModel = ModelLoaderRegistry.getModelOrLogError(normal,
//					"Model: " + normal.getResourcePath() + " was not found!");
//			IModel inventoryModel = ModelLoaderRegistry.getModelOrLogError(inventory,
//					"Model: " + inventory.getResourcePath() + " was not found!");
//
//			for (Variant var : normalVars) {
//				normalModel = var.process(normalModel);
//				modelRegistry.putObject(normal,
//						normalModel.bake(VariantHelper.DEFAULT_BLOCK, DefaultVertexFormats.BLOCK,
//								ModelLoader.defaultTextureGetter()));
//			}
//
//			for (Variant var : invVars) {
//				inventoryModel = var.process(inventoryModel);
//				modelRegistry.putObject(inventory,
//						inventoryModel.bake(VariantHelper.DEFAULT_BLOCK, DefaultVertexFormats.BLOCK,
//								ModelLoader.defaultTextureGetter()));
//			}

//			try {
//				Class<?> weightedModelClazz = Class
//						.forName("net.minecraftforge.client.model.ModelLoader$WeightedRandomModel");
//				Constructor<?> constructor = weightedModelClazz.getDeclaredConstructor(ModelLoader.class,
//						ResourceLocation.class, VariantList.class);
//				constructor.setAccessible(true);
//				Object weightedModelNormal = constructor.newInstance(event.getModelLoader(), normal,
//						modelDefinition.getDefinition().getVariant(normal.getVariant()));
//				Object weightedModelInv = constructor.newInstance(event.getModelLoader(), inventory,
//						modelDefinition.getDefinition().getVariant(inventory.getVariant()));
//
//
//			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
//					| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//				e.printStackTrace();
//			}
		}
	}

}
