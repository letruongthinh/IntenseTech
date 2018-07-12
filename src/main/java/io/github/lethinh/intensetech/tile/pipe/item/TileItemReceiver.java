/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.tile.pipe.item;

import java.util.List;

import io.github.lethinh.intensetech.tile.pipe.PipeTracker;
import io.github.lethinh.intensetech.tile.pipe.TileConnectedPipe;
import io.github.lethinh.intensetech.utils.InventoryUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.IItemHandler;

public class TileItemReceiver extends TileItemConnectedPipe implements ITickable {

	public TileItemReceiver() {
		super(10);
	}

	/* IPipeModule */
	@Override
	public int getRepresentModule() {
		return OUTPUT;
	}

	/* ITickable */
	@Override
	public void update() {

		Capability<IItemHandler> type = getType().getType();
		PipeTracker<Capability<IItemHandler>> tracker = getTracker();

		// Item transfer pipe - Output
		List<TileConnectedPipe<Capability<IItemHandler>>> adjacentPipes = tracker.trackNextPipeAllDirections();

		if (!adjacentPipes.isEmpty()) {
			for (TileConnectedPipe<Capability<IItemHandler>> pipe : adjacentPipes) {
				EnumFacing facing = getNeighborFacing(pipe.getPos(), pos);
				IItemHandler src = pipe.getCapability(type, facing);
				InventoryUtils.transferInventory(src, inventory);
			}
		}

		// Output - External inventory
		List<BlockPos> adjacentTilesPos = getAdjacentTilesPos();

		if (!adjacentTilesPos.isEmpty()) {
			for (BlockPos pos : adjacentTilesPos) {
				TileEntity tile = world.getTileEntity(pos);

				if (tile == null) {
					continue;
				}

				EnumFacing facing = getNeighborFacing(getPos(), pos);
				IItemHandler dst = tile.getCapability(type, facing);

				if (dst == null) {
					continue;
				}

				InventoryUtils.transferInventory(inventory, dst);
			}
		}
	}

}
