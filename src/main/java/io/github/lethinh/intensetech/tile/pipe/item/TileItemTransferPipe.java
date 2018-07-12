/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.tile.pipe.item;

import java.util.List;

import io.github.lethinh.intensetech.tile.pipe.PipeTracker;
import io.github.lethinh.intensetech.tile.pipe.TileConnectedPipe;
import io.github.lethinh.intensetech.utils.InventoryUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.IItemHandler;

public class TileItemTransferPipe extends TileItemConnectedPipe implements ITickable {

	public TileItemTransferPipe() {
		super(1);
	}

	/* IPipeModule */
	@Override
	public int getRepresentModule() {
		return NORMAL;
	}

	/* ITickable */
	@Override
	public void update() {
		PipeTracker<Capability<IItemHandler>> tracker = getTracker();
		List<TileConnectedPipe<Capability<IItemHandler>>> adjacentPipes = tracker.trackNextPipeAllDirections();

		if (adjacentPipes.isEmpty()) {
			return;
		}

		for (TileConnectedPipe<Capability<IItemHandler>> pipe : adjacentPipes) {
			if (pipe.getRepresentModule() == INPUT) {
				continue;
			}

			EnumFacing facing = getNeighborFacing(pos, pipe.getPos());
			IItemHandler dst = pipe.getCapability(getType().getType(), facing);
			InventoryUtils.transferInventory(inventory, dst);
		}
	}

}
