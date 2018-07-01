/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.model.baked;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;

import io.github.lethinh.intensetech.model.ModelHelper;
import io.github.lethinh.intensetech.model.VariantHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class ItemBakedModel implements IBakedModel {

	private final boolean ambientOcclusion, gui3d;
	private final ItemOverrideList overrides;
	private final TextureAtlasSprite[] textures;

	public ItemBakedModel(boolean ambientOcclusion, boolean gui3d, TextureAtlasSprite... textures) {
		this(ambientOcclusion, gui3d, ItemOverrideList.NONE, textures);
	}

	public ItemBakedModel(boolean ambientOcclusion, boolean gui3d, ItemOverrideList overrides,
			TextureAtlasSprite... textures) {
		this.ambientOcclusion = ambientOcclusion;
		this.gui3d = gui3d;
		this.overrides = overrides;
		this.textures = textures;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState blockState, @Nullable EnumFacing side, long rand) {
		IModelState state = VariantHelper.DEFAULT_ITEM;
		ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
		Optional<TRSRTransformation> transform = state.apply(Optional.empty());

		for (int i = 0; i < textures.length; ++i) {
			TextureAtlasSprite texture = textures[i];
			builder.addAll(ModelHelper.getQuadsForSprite(i, texture, DefaultVertexFormats.ITEM, transform));
		}

		return builder.build();
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
		// No more built-in minecraft shits
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return textures[0];
	}

	@Override
	public ItemOverrideList getOverrides() {
		return overrides;
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		return ModelHelper.handlePerspective(this, VariantHelper.DEFAULT_ITEM, cameraTransformType);
	}

}
