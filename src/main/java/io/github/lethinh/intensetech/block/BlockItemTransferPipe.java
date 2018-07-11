/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.block;

import io.github.lethinh.intensetech.tile.pipe.item.TileItemTransferPipe;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockItemTransferPipe extends BlockTileBase<TileItemTransferPipe> {

	public BlockItemTransferPipe() {
		super("item_transfer_pipe", Material.GROUND);
		setHardness(3F);
	}

	@Override
	public TileItemTransferPipe createTileEntity(IBlockAccess world, IBlockState state) {
		return new TileItemTransferPipe();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return false;
	}

}
