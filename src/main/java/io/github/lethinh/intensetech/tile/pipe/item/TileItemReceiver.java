/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.tile.pipe.item;

import java.util.List;

import io.github.lethinh.intensetech.tile.pipe.TileConnectedPipe;
import io.github.lethinh.intensetech.utils.InventoryUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.IItemHandler;

public class TileItemReceiver extends TileBasicItemPipe implements ITickable {

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
		List<TileEntity> adjacentTiles = getAdjacentTiles();
		List<TileConnectedPipe<Capability<IItemHandler>>> adjacentPipes = getAdjacentPipes();
		Capability<IItemHandler> type = getPipeType().getType();

		// Item transfer pipe - Output
		if (!adjacentPipes.isEmpty()) {
			for (TileConnectedPipe<Capability<IItemHandler>> pipe : adjacentPipes) {
				EnumFacing facing = getNeighborFacing(pos, pipe.getPos());
				IItemHandler src = pipe.getCapability(type, facing);
				InventoryUtils.transferInventory(src, inventory);
			}
		}

		// Output - External inventory
		if (!adjacentTiles.isEmpty()) {
			for (TileEntity tile : adjacentTiles) {
				EnumFacing facing = getNeighborFacing(pos, tile.getPos());
				IItemHandler dst = tile.getCapability(type, facing);
				InventoryUtils.transferInventory(inventory, dst);
			}
		}
	}

}
