/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.model.baked;

import java.util.List;

import javax.annotation.Nullable;

import io.github.lethinh.intensetech.model.StandardModelStates;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Represent for a simple baked ItemBlock model, blockstate's one is at
 * {@link BlockstateBakedModel}
 */
@SideOnly(Side.CLIENT)
public class ItemBlockBakedModel extends ItemBakedModel {

	/**
	 * Parent blockstate's model.
	 */
	private final IBakedModel parent;

	public ItemBlockBakedModel(IBakedModel parent, boolean ambientOcclusion, boolean gui3d,
			TextureAtlasSprite... textures) {
		super(ambientOcclusion, gui3d, StandardModelStates.DEFAULT_BLOCK, DefaultVertexFormats.ITEM, textures);
		this.parent = parent;
	}

	public ItemBlockBakedModel(IBakedModel parent, boolean ambientOcclusion, boolean gui3d, IModelState defaultState,
			VertexFormat format,
			TextureAtlasSprite... textures) {
		super(ambientOcclusion, gui3d, defaultState, format, textures);
		this.parent = parent;
	}

	public ItemBlockBakedModel(IBakedModel parent, boolean ambientOcclusion, boolean gui3d, ItemOverrideList overrides,
			IModelState defaultState, VertexFormat format, TextureAtlasSprite... textures) {
		super(ambientOcclusion, gui3d, overrides, defaultState, format, textures);
		this.parent = parent;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState blockState, @Nullable EnumFacing side, long rand) {
		return parent.getQuads(blockState, side, rand);
	}

	public IBakedModel getParent() {
		return parent;
	}

}
