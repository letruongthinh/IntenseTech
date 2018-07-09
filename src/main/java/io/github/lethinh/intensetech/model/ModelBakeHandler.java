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
import io.github.lethinh.intensetech.model.baked.BlockstateBakedModel;
import io.github.lethinh.intensetech.model.baked.ItemBlockBakedModel;
import io.github.lethinh.intensetech.model.baked.ItemBakedModel;
import io.github.lethinh.intensetech.model.definition.FlatModelBlockDefinition;
import io.github.lethinh.intensetech.utils.ConstFunctionUtils;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBakeHandler {

	private static final Multimap<ItemBase, TextureAtlasSprite> ITEMS_SPRITES = HashMultimap.create();
	private static final Map<BlockBase, TextureAtlasSprite> BLOCKS_SPIRTES = Maps.newHashMap();

	@SubscribeEvent
	public void onPreTextureStitch(TextureStitchEvent.Pre event) {
		TextureMap textureMap = event.getMap();

		// Items
		for (ItemBase item : ItemsManager.ITEMS) {
			for (String texture : item.getTextures()) {
				ResourceLocation loc = ConstFunctionUtils.prefixResourceLocation("items/" + texture);
				TextureAtlasSprite sprite = textureMap.registerSprite(loc);
				ITEMS_SPRITES.put(item, sprite);
			}
		}

		// Blocks
		for (BlockBase block : BlocksManager.BLOCKS) {
			ResourceLocation loc = ConstFunctionUtils.prefixResourceLocation("blocks/" + block.getTexture());
			TextureAtlasSprite sprite = textureMap.registerSprite(loc);
			BLOCKS_SPIRTES.put(block, sprite);
		}
	}

	// FIXME: ModelBakeEvent or ModelRegistryEvent?
	@SubscribeEvent
	public void onModelBake(ModelBakeEvent event) {
		IRegistry<ModelResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();
		ModelLoader modelLoader = event.getModelLoader();

		// Items
		for (Map.Entry<ItemBase, Collection<TextureAtlasSprite>> entry : ITEMS_SPRITES.asMap().entrySet()) {
			ItemBase item = entry.getKey();
			Collection<TextureAtlasSprite> unsignedSprites = entry.getValue();
			TextureAtlasSprite[] sprites = new TextureAtlasSprite[unsignedSprites.size()];
			sprites = unsignedSprites.toArray(sprites);

			ItemBakedModel itemModel = new ItemBakedModel(item.isAmbientOcclusion(), item.isGui3d(), sprites);
			ModelResourceLocation loc = new ModelResourceLocation(item.getRegistryName(), "inventory");
			modelRegistry.putObject(loc, itemModel);
		}

		// Blocks
		for (Map.Entry<BlockBase, TextureAtlasSprite> entry : BLOCKS_SPIRTES.entrySet()) {
			BlockBase block = entry.getKey();
			TextureAtlasSprite sprite = entry.getValue();

			FlatModelBlockDefinition modelDefinition = new FlatModelBlockDefinition(block.getTexture(),
					block.isUvLock(), block.isSmooth(), block.isGui3d());
			ModelBlockDefinition definition = modelDefinition.create();
			ModelResourceLocation normalLoc = new ModelResourceLocation(block.getRegistryName(), "normal");
			ModelResourceLocation inventoryLoc = new ModelResourceLocation(block.getRegistryName(), "inventory");

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

			// Register Block model
			BlockstateBakedModel normalModel = new BlockstateBakedModel(block.isAmbientOcclusion(), block.isGui3d(), sprite,
					definition.getVariant(normalLoc.getVariant()));
			modelRegistry.putObject(normalLoc, normalModel); // Normal (World) variant

			BlockstateBakedModel inventoryModel = new BlockstateBakedModel(block.isAmbientOcclusion(), block.isGui3d(),
					sprite, definition.getVariant(inventoryLoc.getVariant()));
			modelRegistry.putObject(inventoryLoc, inventoryModel); // Inventory variant

			// Register ItemBlock model
			ItemBlockBakedModel itemBlockModel = new ItemBlockBakedModel(inventoryModel, block.isAmbientOcclusion(),
					block.isGui3d(), sprite);
			Item itemBlock = Item.getItemFromBlock(block);
			modelRegistry.putObject(inventoryLoc, itemBlockModel);
			ModelLoader.setCustomModelResourceLocation(itemBlock, 0, inventoryLoc);
			ModelLoader.setCustomMeshDefinition(itemBlock, stack -> inventoryLoc);
		}
	}

}
