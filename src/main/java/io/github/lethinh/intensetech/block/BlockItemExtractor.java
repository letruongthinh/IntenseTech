/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.block;

import io.github.lethinh.intensetech.tile.pipe.item.TileItemExtractor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.IBlockAccess;

public class BlockItemExtractor extends BlockTileBase<TileItemExtractor> {

	public BlockItemExtractor() {
		super("item_extractor", Material.IRON);
		setHardness(5F);
	}

	@Override
	public TileItemExtractor createTileEntity(IBlockAccess world, IBlockState state) {
		return new TileItemExtractor();
	}

}
