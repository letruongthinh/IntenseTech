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
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemBakedModel implements IBakedModel {

	private final boolean ambientOcclusion, gui3d;
	private final ItemOverrideList overrides;
	private final IModelState defaultState;
	private final VertexFormat format;
	private final TextureAtlasSprite[] textures;

	public ItemBakedModel(boolean ambientOcclusion, boolean gui3d, TextureAtlasSprite... textures) {
		this(ambientOcclusion, gui3d, VariantHelper.DEFAULT_ITEM, DefaultVertexFormats.ITEM, textures);
	}

	public ItemBakedModel(boolean ambientOcclusion, boolean gui3d, IModelState defaultState, VertexFormat format,
			TextureAtlasSprite... textures) {
		this(ambientOcclusion, gui3d, ItemOverrideList.NONE, defaultState, format, textures);
	}

	public ItemBakedModel(boolean ambientOcclusion, boolean gui3d, ItemOverrideList overrides,
			IModelState defaultState, VertexFormat format, TextureAtlasSprite... textures) {
		this.ambientOcclusion = ambientOcclusion;
		this.gui3d = gui3d;
		this.overrides = overrides;
		this.defaultState = defaultState;
		this.format = format;
		this.textures = textures;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState blockState, @Nullable EnumFacing side, long rand) {
		if (side != null) {
			return ImmutableList.of();
		}

		ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
		Optional<TRSRTransformation> transform = defaultState.apply(Optional.empty());

		for (int i = 0; i < textures.length; ++i) {
			TextureAtlasSprite texture = textures[i];
			builder.addAll(ModelHelper.getQuadsForSprite(i, texture, format, transform));
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

	public IModelState getDefaultState() {
		return defaultState;
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		return ModelHelper.handlePerspective(this, defaultState, cameraTransformType);
	}

}
