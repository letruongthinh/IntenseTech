/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.model.baked;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import io.github.lethinh.intensetech.model.ModelHelper;
import io.github.lethinh.intensetech.model.VariantHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.VariantList;
import net.minecraft.client.renderer.block.model.WeightedBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.MultiModelState;
import net.minecraftforge.common.model.IModelState;

public class FlatBakedBlockstateModel implements IBakedModel {

	private final List<Variant> variants;
	private final List<ResourceLocation> locations;
	private final Set<ResourceLocation> textures;
	private final List<IModel> models;

	private final boolean ambientOcclusion, gui3d;
	private final TextureAtlasSprite texture;

	public FlatBakedBlockstateModel(boolean ambientOcclusion, boolean gui3d, TextureAtlasSprite texture,
			VariantList variants) throws Exception {
		this.ambientOcclusion = ambientOcclusion;
		this.gui3d = gui3d;
		this.texture = texture;
		this.variants = variants.getVariantList();
		this.locations = Lists.newArrayList();
		this.textures = Sets.newHashSet();
		this.models = Lists.newArrayList();

		for (Variant var : this.variants) {
			ResourceLocation loc = var.getModelLocation();
			locations.add(loc);

			/*
			 * Vanilla eats this, which makes it only show variants that have models. But
			 * that doesn't help debugging, so throw the exception
			 */
			IModel model;

			if (loc.equals(ModelHelper.MISSING_MODEL)) {
				// explicit missing location, happens if blockstate has "model"=null
				model = ModelLoaderRegistry.getMissingModel();
			} else {
				model = ModelLoaderRegistry.getModel(loc);
			}

			// FIXME: is this the place? messes up dependency and texture resolution
			model = var.process(model);

			for (ResourceLocation location : model.getDependencies()) {
				ModelLoaderRegistry.getModelOrMissing(location);
			}
			// FMLLog.getLogger().error("Exception resolving indirect dependencies for
			// model" + loc, e);
			textures.addAll(model.getTextures()); // Kick this, just in case.

			models.add(model);

			IModelState modelDefaultState = model.getDefaultState();
			Preconditions.checkNotNull(modelDefaultState, "Model %s returned null as default state", loc);
		}

		if (models.size() == 0) // If all variants are missing, add one with the missing model and default
								// rotation.
		{
			// FIXME: log this?
			IModel missing = ModelLoaderRegistry.getMissingModel();
			models.add(missing);
		}
	}

	private FlatBakedBlockstateModel(boolean ambientOcclusion, boolean gui3d, TextureAtlasSprite texture,
			List<Variant> variants, List<ResourceLocation> locations,
			Set<ResourceLocation> textures, List<IModel> models) {
		this.ambientOcclusion = ambientOcclusion;
		this.gui3d = gui3d;
		this.texture = texture;
		this.variants = variants;
		this.locations = locations;
		this.textures = textures;
		this.models = models;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState blockState, @Nullable EnumFacing side, long rand) {
		VertexFormat format = DefaultVertexFormats.ITEM; // Forge uses this
		IModelState state = VariantHelper.DEFAULT_BLOCK;

		if (!Attributes.moreSpecific(format, Attributes.DEFAULT_BAKED_FORMAT)) {
			throw new IllegalArgumentException(
					"can't bake vanilla weighted models to the format that doesn't fit into the default one: "
							+ format);
		}
		if (variants.size() == 1) {
			IModel model = models.get(0);
			return model.bake(MultiModelState.getPartState(state, model, 0), format,
					ModelLoader.defaultTextureGetter()).getQuads(blockState, side, rand);
		}

		WeightedBakedModel.Builder builder = new WeightedBakedModel.Builder();

		for (int i = 0; i < variants.size(); i++) {
			IModel model = models.get(i);
			builder.add(
					model.bake(MultiModelState.getPartState(state, model, i), format,
							ModelLoader.defaultTextureGetter()),
					variants.get(i).getWeight());
		}

		return builder.build().getQuads(blockState, side, rand);
	}

	@Override
	public boolean isAmbientOcclusion() {
		return ambientOcclusion;
	}

	@Override
	public boolean isGui3d() {
		return gui3d;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return texture;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
	}

}
