/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.tile.pipe.item;

import java.util.List;

import io.github.lethinh.intensetech.inventory.container.ContainerItemExtractor;
import io.github.lethinh.intensetech.inventory.gui.GuiItemExtractor;
import io.github.lethinh.intensetech.tile.IGuiTile;
import io.github.lethinh.intensetech.tile.pipe.PipeTracker;
import io.github.lethinh.intensetech.tile.pipe.TileConnectedPipe;
import io.github.lethinh.intensetech.utils.InventoryUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.IItemHandler;

public class TileItemExtractor extends TileItemConnectedPipe implements ITickable, IGuiTile {

	public TileItemExtractor() {
		super(10);
	}

	/* IPipeModule */
	@Override
	public int getRepresentModule() {
		return INPUT;
	}

	/* ITickable */
	@Override
	public void update() {
		Capability<IItemHandler> type = getType().getType();
		PipeTracker<Capability<IItemHandler>> tracker = getTracker();

		// External inventory - Input
		List<BlockPos> adjacentTilesPos = getAdjacentTilesPos();

		if (!adjacentTilesPos.isEmpty()) {
			for (BlockPos pos : adjacentTilesPos) {
				TileEntity tile = world.getTileEntity(pos);

				if (tile == null) {
					continue;
				}

				EnumFacing facing = getNeighborFacing(pos, getPos());
				IItemHandler src = tile.getCapability(type, facing);

				if (src == null) {
					continue;
				}

				InventoryUtils.transferInventory(src, inventory);
			}
		}

		// Input - Item transfer pipe
		List<TileConnectedPipe<Capability<IItemHandler>>> adjacentPipes = tracker.trackNextPipeAllDirections();

		if (!adjacentPipes.isEmpty()) {
			for (TileConnectedPipe<Capability<IItemHandler>> pipe : adjacentPipes) {
				EnumFacing facing = getNeighborFacing(pos, pipe.getPos());
				IItemHandler dst = pipe.getCapability(type, facing);
				InventoryUtils.transferInventory(inventory, dst);
			}
		}
	}

	/* IGuiTile */
	@Override
	public Object getContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new ContainerItemExtractor(player.inventory, this);
	}

	@Override
	public Object getGui(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new GuiItemExtractor(player.inventory, this);
	}

}
