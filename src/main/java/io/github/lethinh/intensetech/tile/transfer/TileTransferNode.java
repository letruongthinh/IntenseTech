/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.tile.transfer;

import io.github.lethinh.intensetech.api.provider.IGuiTile;
import io.github.lethinh.intensetech.inventory.container.ContainerTransferNode;
import io.github.lethinh.intensetech.inventory.gui.GuiTransferNode;
import io.github.lethinh.intensetech.tile.TileInventoryBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileTransferNode extends TileInventoryBase implements ITickable, IGuiTile {

	public TileTransferNode() {
		super(10);
	}

	/* ITickable */
	@Override
	public void update() {
		if (isFull()) {
			return;
		}

		TileEntity up = world.getTileEntity(pos.offset(EnumFacing.UP));
		TileEntity down = world.getTileEntity(pos.offset(EnumFacing.DOWN));
		TileEntity north = world.getTileEntity(pos.offset(EnumFacing.NORTH));
		TileEntity south = world.getTileEntity(pos.offset(EnumFacing.SOUTH));
		TileEntity east = world.getTileEntity(pos.offset(EnumFacing.EAST));
		TileEntity west = world.getTileEntity(pos.offset(EnumFacing.WEST));

		if (up != null && up.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
			IItemHandler itemHandler = up.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

			for (int i = 0; i < getSlots(); ++i) {
				if (itemHandler.getStackInSlot(i).isEmpty()) {
					continue;
				}

				ItemStack extract = itemHandler.extractItem(i, 1, false);
				insertItem(i, extract);
			}
		}

		if (down != null && down.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)) {
			IItemHandler itemHandler = down.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);

			for (int i = 0; i < getSlots(); ++i) {
				if (itemHandler.getStackInSlot(i).isEmpty()) {
					continue;
				}

				ItemStack extract = itemHandler.extractItem(i, 1, false);
				insertItem(i, extract);
			}
		}

		if (north != null && north.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.SOUTH)) {
			IItemHandler itemHandler = north.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
					EnumFacing.SOUTH);

			for (int i = 0; i < getSlots(); ++i) {
				if (itemHandler.getStackInSlot(i).isEmpty()) {
					continue;
				}

				ItemStack extract = itemHandler.extractItem(i, 1, false);
				insertItem(i, extract);
			}
		}

		if (south != null && south.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)) {
			IItemHandler itemHandler = south.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
					EnumFacing.NORTH);

			for (int i = 0; i < getSlots(); ++i) {
				if (itemHandler.getStackInSlot(i).isEmpty()) {
					continue;
				}

				ItemStack extract = itemHandler.extractItem(i, 1, false);
				insertItem(i, extract);
			}
		}

		if (east != null && east.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.WEST)) {
			IItemHandler itemHandler = east.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
					EnumFacing.WEST);

			for (int i = 0; i < getSlots(); ++i) {
				if (itemHandler.getStackInSlot(i).isEmpty()) {
					continue;
				}

				ItemStack extract = itemHandler.extractItem(i, 1, false);
				insertItem(i, extract);
			}
		}

		if (west != null && west.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.EAST)) {
			IItemHandler itemHandler = west.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
					EnumFacing.EAST);

			for (int i = 0; i < getSlots(); ++i) {
				if (isFull() || itemHandler.getStackInSlot(i).isEmpty()) {
					continue;
				}

				ItemStack extract = itemHandler.extractItem(i, 1, false);
				insertItem(i, extract);
			}
		}
	}

	/* IGuiTile */
	@Override
	public Object getContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new ContainerTransferNode(player.inventory, this);
	}

	@Override
	public Object getGui(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new GuiTransferNode(player.inventory, this);
	}

}
