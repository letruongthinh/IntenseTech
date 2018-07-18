/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.manager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Vector3f;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;

import io.github.lethinh.intensetech.IntenseTech;
import io.github.lethinh.intensetech.block.BlockBase;
import io.github.lethinh.intensetech.item.ItemBase;
import io.github.lethinh.intensetech.model.baked.BlockstateBakedModel;
import io.github.lethinh.intensetech.model.baked.ItemBakedModel;
import io.github.lethinh.intensetech.model.baked.ItemBlockBakedModel;
import io.github.lethinh.intensetech.model.property.FlatBoxModelProperties;
import io.github.lethinh.intensetech.utils.ConstFunctionUtils;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = IntenseTech.MOD_ID)
@SideOnly(Side.CLIENT)
public class ModelBakeHandler {

	private static final Multimap<ItemBase, TextureAtlasSprite> ITEMS_SPRITES = HashMultimap.create();
	private static final Map<BlockBase, TextureAtlasSprite> BLOCKS_SPIRTES = new HashMap<>(); // TODO: Change to
																								// Multimap

	// Pipes
	public static final ResourceLocation PIPE_NORMAL_LOC = ConstFunctionUtils
			.prefixResourceLocation("block/pipe_normal");
	public static final ResourceLocation PIPE_PART_LOC = ConstFunctionUtils
			.prefixResourceLocation("block/pipe_part");

	@SubscribeEvent
	public static void onPreTextureStitch(TextureStitchEvent.Pre event) {
		TextureMap textureMap = event.getMap();

		// Items
		for (ItemBase item : ItemsManager.RegistrationHandler.ITEMS) {
			for (String texture : item.getTextures()) {
				ResourceLocation loc = ConstFunctionUtils.prefixResourceLocation("items/" + texture);
				TextureAtlasSprite sprite = textureMap.registerSprite(loc);
				ITEMS_SPRITES.put(item, sprite);
			}
		}

		// Blocks
		for (BlockBase block : BlocksManager.RegistrationHandler.BLOCKS) {
			ResourceLocation loc = ConstFunctionUtils.prefixResourceLocation("blocks/" + block.getTexture());
			TextureAtlasSprite sprite = textureMap.registerSprite(loc);
			BLOCKS_SPIRTES.put(block, sprite);
		}
	}

	@SubscribeEvent
	public static void onModelBake(ModelBakeEvent event) {
		ModelLoader modelLoader = event.getModelLoader();

		// Main models
		IRegistry<ModelResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();

		// Items
		for (Map.Entry<ItemBase, Collection<TextureAtlasSprite>> entry : ITEMS_SPRITES.asMap().entrySet()) {
			ItemBase item = entry.getKey();
			Collection<TextureAtlasSprite> unsignedSprites = entry.getValue();
			TextureAtlasSprite[] sprites = unsignedSprites.toArray(new TextureAtlasSprite[unsignedSprites.size()]);

			// Register Item model
			ItemBakedModel itemModel = new ItemBakedModel(item.isAmbientOcclusion(), item.isGui3d(),
					item.getDefaultState(), DefaultVertexFormats.ITEM, sprites);
			ModelResourceLocation loc = new ModelResourceLocation(item.getRegistryName(), "inventory");
			modelRegistry.putObject(loc, itemModel);
			ModelLoader.setCustomModelResourceLocation(item, 0, loc);
			ModelLoader.setCustomMeshDefinition(item, stack -> loc);
		}

		// Blocks
		for (Map.Entry<BlockBase, TextureAtlasSprite> entry : BLOCKS_SPIRTES.entrySet()) {
			BlockBase block = entry.getKey();
			TextureAtlasSprite sprite = entry.getValue();

			FlatBoxModelProperties properties = block.getBoxProperties(event.getModelLoader());
			ModelResourceLocation normalLoc = new ModelResourceLocation(block.getRegistryName(), "normal");
			ModelResourceLocation inventoryLoc = new ModelResourceLocation(block.getRegistryName(), "inventory");

			// Register Block model
			BlockstateBakedModel normalModel = new BlockstateBakedModel(block.isAmbientOcclusion(), block.isGui3d(),
					sprite, properties.getVariantList(normalLoc.getVariant()));
			modelRegistry.putObject(normalLoc, normalModel); // Normal (World) variant

			BlockstateBakedModel inventoryModel = new BlockstateBakedModel(block.isAmbientOcclusion(), block.isGui3d(),
					sprite, properties.getVariantList(inventoryLoc.getVariant()));
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

	public static void constructParentPipeModels(ModelLoader modelLoader) {
		// Parent models
		List<BlockPart> blockPipeNormalParts = new ArrayList<>();
		List<BlockPart> blockPipeParts = new ArrayList<>();

		{
			Vector3f from = new Vector3f(4F, 4F, 0F);
			Vector3f to = new Vector3f(12F, 12F, 4F);
			Map<EnumFacing, BlockPartFace> faces = new EnumMap<>(EnumFacing.class);

			for (EnumFacing facing : EnumFacing.VALUES) {
				BlockPartFace face = new BlockPartFace(null, -1, "#side", new BlockFaceUV(null, 0));
				faces.put(facing, face);
			}

			blockPipeParts.add(new BlockPart(from, to, faces, null, true));
		}

		{
			Vector3f from = new Vector3f(4F, 4F, 4F);
			Vector3f to = new Vector3f(12F, 12F, 12F);
			Map<EnumFacing, BlockPartFace> faces = new EnumMap<>(EnumFacing.class);

			for (EnumFacing facing : EnumFacing.VALUES) {
				BlockPartFace face = new BlockPartFace(null, -1, "#centre", new BlockFaceUV(null, 0));
				faces.put(facing, face);
			}

			blockPipeNormalParts.add(new BlockPart(from, to, faces, null, true));
		}

		ModelBlock pipePart = new ModelBlock(new ResourceLocation("block/block"), blockPipeParts,
				ImmutableMap.of("particle", "#side"), true, true,
				ItemCameraTransforms.DEFAULT, ItemOverrideList.NONE.getOverrides());
		ModelBlock pipeNormal = new ModelBlock(new ResourceLocation("block/block"), blockPipeNormalParts,
				ImmutableMap.of("particle", "#centre"), true, true,
				ItemCameraTransforms.DEFAULT, ItemOverrideList.NONE.getOverrides());

		try {
			Field field = modelLoader.getClass().getSuperclass().getDeclaredField("models");
			field.setAccessible(true);
			Map<ResourceLocation, ModelBlock> map = (Map<ResourceLocation, ModelBlock>) field.get(modelLoader);
			map.put(PIPE_NORMAL_LOC, pipeNormal);
			map.put(PIPE_PART_LOC, pipePart);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
