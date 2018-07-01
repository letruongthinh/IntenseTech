/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.block.transfer;

import javax.annotation.Nullable;

import io.github.lethinh.intensetech.block.BlockTileBase;
import io.github.lethinh.intensetech.tile.transfer.TileTransferNode;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTransferNode extends BlockTileBase {

	public BlockTransferNode() {
		super("transfer_node", Material.IRON);
		setHardness(15F);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileTransferNode();
	}

}
