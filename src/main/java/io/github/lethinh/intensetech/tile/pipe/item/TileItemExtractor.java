/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.tile.pipe.item;

import java.util.List;

import io.github.lethinh.intensetech.inventory.container.ContainerItemExtractor;
import io.github.lethinh.intensetech.inventory.gui.GuiItemExtractor;
import io.github.lethinh.intensetech.tile.IGuiTile;
import io.github.lethinh.intensetech.tile.pipe.TileConnectedPipe;
import io.github.lethinh.intensetech.utils.InventoryUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.IItemHandler;

public class TileItemExtractor extends TileBasicItemPipe implements ITickable, IGuiTile {

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
		List<TileEntity> adjacentTiles = getAdjacentTiles();
		List<TileConnectedPipe<Capability<IItemHandler>>> adjacentPipes = getAdjacentPipes();
		Capability<IItemHandler> type = getPipeType().getType();

		// External inventory - Input
		if (!adjacentTiles.isEmpty()) {
			for (TileEntity tile : adjacentTiles) {
				EnumFacing facing = getNeighborFacing(pos, tile.getPos());
				IItemHandler src = tile.getCapability(type, facing);
				InventoryUtils.transferInventory(src, inventory);
			}
		}

		// Input - Item transfer pipe
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
