/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.block;

import io.github.lethinh.intensetech.IntenseTech;
import io.github.lethinh.intensetech.inventory.GuiHandler;
import io.github.lethinh.intensetech.tile.TileBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTileBase<TE extends TileBase> extends BlockBase {

	public BlockTileBase(String name, Material materialIn) {
		super(name, materialIn);
	}

	/* Block */
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, SpawnPlacementType type) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.isSneaking() || worldIn.getTileEntity(pos) instanceof TileBase
				&& ((TileBase) worldIn.getTileEntity(pos)).isTileInvalid()) {
			return false;
		}

		// Still open the gui if the player is playing on server
		if (worldIn.isRemote) {
			return true;
		}

		playerIn.openGui(IntenseTech.instance, GuiHandler.GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
	}

	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
		TileEntity tile = worldIn.getTileEntity(pos);
		return tile == null ? super.eventReceived(state, worldIn, pos, id, param) : tile.receiveClientEvent(id, param);
	}

	/* ITabSort */
	@Override
	public CreativeTabCategory getTabCategory() {
		return CreativeTabCategory.TILE_BLOCK;
	}

	/* Tile Stuffs */
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

}
