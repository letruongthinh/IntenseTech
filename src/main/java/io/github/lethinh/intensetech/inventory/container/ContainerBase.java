/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.inventory.container;

import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import io.github.lethinh.intensetech.tile.TileBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class ContainerBase<TE extends TileBase> extends Container {

	private final int inventoryStart, playerInventoryStart, playerInventoryEnd;
	private final TE tile;

	public ContainerBase(InventoryPlayer inventoryPlayer, TE tile) {
		// The size of the inventory
		this.inventoryStart = tile.getItemHandler(EnumFacing.NORTH).getSlots();
		// The player inventory (without the hotbar)
		this.playerInventoryStart = inventoryStart + 27;
		// The player inventory (with the hotbar)
		this.playerInventoryEnd = playerInventoryStart + 9;

		this.tile = tile;
	}

	/* Container */
	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		if (!canInteractWith(player)) {
			return ItemStack.EMPTY;
		}

		Slot slot = getSlot(index);
		ItemStack currentStack = ItemStack.EMPTY;

		if (slot.getHasStack()) {
			ItemStack existingStack = ItemStack.EMPTY;
			currentStack = existingStack.copy();

			if (index <= playerInventoryStart) {
				if (!mergeItemStack(existingStack, playerInventoryStart, playerInventoryEnd, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(existingStack, currentStack);
			} else if (!mergeItemStack(existingStack, 0, playerInventoryStart, false)) {
				return ItemStack.EMPTY;
			}

			if (existingStack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (existingStack.getCount() == currentStack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, existingStack);
		}

		return currentStack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		BlockPos pos = tile.getPos();
		return !tile.isTileInvalid()
				&& playerIn.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64D;
	}

	/* Slot Helpers */
	/**
	 * Draws the inventory of the player.
	 *
	 * @param inventoryPlayer The inventory of the player will be drawn.
	 * @param x               The X location the player's inventory.
	 * @param y               The Y location of the player's inventory.
	 */
	protected void addPlayerInventory(InventoryPlayer inventoryPlayer, int x, int y) {
		// Main Inventory
		// Basically, draw 3 rows, 9 columns, which means draw 27 slots
		IntStream.range(0, 3).forEach(col -> IntStream.range(0, 9).forEach(row -> addSlotToContainer(
				new Slot(inventoryPlayer, row + col * 9 + 9, x + row * 18, y + col * 18))));

		// Hotbar
		IntStream.range(0, 9)
				.forEach(column -> addSlotToContainer(new Slot(inventoryPlayer, column, x + column * 18, y + 58)));
	}

	/* Getters */
	public int getInventoryStart() {
		return inventoryStart;
	}

	public int getPlayerInventoryStart() {
		return playerInventoryStart;
	}

	public int getPlayerInventoryEnd() {
		return playerInventoryEnd;
	}

	public TE getTile() {
		return tile;
	}

}
