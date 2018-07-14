/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.tile.pipe.item;

import io.github.lethinh.intensetech.tile.pipe.TileConnectedPipe;
import io.github.lethinh.intensetech.utils.InventoryUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.IItemHandler;

public class TileItemTransferPipe extends TileItemConnectedPipe implements ITickable {

	public TileItemTransferPipe() {
		super(1);
	}

	/* IPipe */
	@Override
	public int getRepresentModule() {
		return NORMAL;
	}

	@Override
	public boolean canTransferOutput(BlockPos neighbor, EnumFacing facing) {
		TileEntity tile = world.getTileEntity(neighbor);
		IItemHandler dst = tile.getCapability(getType().getType(), facing);
		return !InventoryUtils.isFull(dst);
	}

	@Override
	public boolean canTransferToNextPipe(TileConnectedPipe<Capability<IItemHandler>> pipe, BlockPos neighbor,
			EnumFacing facing) {
		IItemHandler dst = pipe.getCapability(getType().getType(), facing);
		return pipe.getRepresentModule() == NORMAL && !InventoryUtils.isFull(dst);
	}

	@Override
	public void transferOutput(BlockPos neighbor, EnumFacing facing) {
		TileEntity tile = world.getTileEntity(neighbor);
		IItemHandler dst = tile.getCapability(getType().getType(), facing);
		InventoryUtils.transferInventory(inventory, dst);
	}

	@Override
	public void transferToNextPipe(TileConnectedPipe<Capability<IItemHandler>> pipe, BlockPos neighbor,
			EnumFacing facing) {
		IItemHandler dst = pipe.getCapability(getType().getType(), facing);
		InventoryUtils.transferInventory(inventory, dst).isEmpty();
	}

}
