/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.block;

import io.github.lethinh.intensetech.tile.pipe.item.TileItemTransferPipe;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockItemTransferPipe extends BlockTileBase<TileItemTransferPipe> {

	public static final PropertyInteger TIER = PropertyInteger.create("tier", 0, 4);

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

	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		for (int i : TIER.getAllowedValues()) {
			items.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TIER);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TIER);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TIER, meta);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

}
