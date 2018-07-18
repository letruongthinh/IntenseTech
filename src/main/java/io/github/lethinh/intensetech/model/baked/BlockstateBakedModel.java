/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.model.baked;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import io.github.lethinh.intensetech.model.ModelHelper;
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
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.ModelStateComposition;
import net.minecraftforge.client.model.MultiModelState;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A blockstate model which is baked from a built-in {@link IModel} instance.
 * The instance is created using the {@link ResourceLocation} of the
 * {@link Variant}
 */
@SideOnly(Side.CLIENT)
public class BlockstateBakedModel implements IBakedModel {

	private final boolean ambientOcclusion, gui3d;
	private final TextureAtlasSprite texture;
	private final List<Variant> variants;
	private final List<IModel> modelInstances;
	private final IModelState modelState;

	public BlockstateBakedModel(boolean ambientOcclusion, boolean gui3d, TextureAtlasSprite texture,
			VariantList variants) {
		this.ambientOcclusion = ambientOcclusion;
		this.gui3d = gui3d;
		this.texture = texture;
		this.variants = variants.getVariantList();
		this.modelInstances = new ArrayList<>();
		ImmutableList.Builder<Pair<IModel, IModelState>> builder = ImmutableList.builder();

		for (Variant var : this.variants) {
			ResourceLocation loc = var.getModelLocation();

			/*
			 * Vanilla eats this, which makes it only show variants that have models. But
			 * that doesn't help debugging, so throw the exception
			 */
			IModel model;

			if (loc.equals(ModelHelper.MISSING_MODEL)) {
				// explicit missing location, happens if blockstate has "model"=null
				model = ModelLoaderRegistry.getMissingModel();
			} else {
				model = ModelLoaderRegistry.getModelOrLogError(loc, "IModel instance was't found!");
			}

			// FIXME: is this the place? messes up dependency and texture resolution
			model = var.process(model);

			for (ResourceLocation location : model.getDependencies()) {
				ModelLoaderRegistry.getModelOrMissing(location);
			}

			modelInstances.add(model);

			IModelState modelDefaultState = model.getDefaultState();
			Preconditions.checkNotNull(modelDefaultState, "Model %s returned null as default state", loc);
			builder.add(Pair.of(model, new ModelStateComposition(var.getState(), modelDefaultState)));
		}

		if (modelInstances.isEmpty()) // If all variants are missing, add one with the missing model and default
		// rotation.
		{
			// FIXME: log this?
			IModel missing = ModelLoaderRegistry.getMissingModel();
			modelInstances.add(missing);
			builder.add(Pair.of(missing, TRSRTransformation.identity()));
		}

		modelState = new MultiModelState(builder.build());
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState blockState, @Nullable EnumFacing side, long rand) {
		VertexFormat format = DefaultVertexFormats.ITEM; // Forge uses this, may be it's just simply brighter

		if (variants.size() == 1) {
			IModel model = modelInstances.get(0);
			IBakedModel bakedModel = model.bake(MultiModelState.getPartState(modelState, model, 0), format,
					ModelLoader.defaultTextureGetter());
			return bakedModel.getQuads(blockState, side, rand);
		}

		WeightedBakedModel.Builder builder = new WeightedBakedModel.Builder();

		for (int i = 0; i < variants.size(); ++i) {
			IModel model = modelInstances.get(i);
			IBakedModel bakedModel = model.bake(MultiModelState.getPartState(modelState, model, i), format,
					ModelLoader.defaultTextureGetter());
			builder.add(bakedModel, variants.get(i).getWeight());
		}

		WeightedBakedModel ret = builder.build();
		return ret.getQuads(blockState, side, rand);
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

	public List<Variant> getVariants() {
		return variants;
	}

	public List<IModel> getModelInstances() {
		return modelInstances;
	}

	public IModelState getModelState() {
		return modelState;
	}

}
