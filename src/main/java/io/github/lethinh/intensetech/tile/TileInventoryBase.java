/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.tile;

import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import io.github.lethinh.intensetech.api.capability.TileItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandlerModifiable;

public class TileInventoryBase<I extends TileItemHandler> extends TileBase {

	private final int size;

	public TileInventoryBase(int size) {
		this.size = size;
	}

	@Override
	public IItemHandlerModifiable getItemHandler(EnumFacing facing) {
		return new TileItemHandler(this, size);
	}

	/**
	 * Same as {@link TileInventoryBase#getItemHandler(EnumFacing)} but there is no
	 * {@code facing} argument.
	 */
	public I getInventory() {
		return (I) getItemHandler(null);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		getInventory().deserializeNBT(compound.getCompoundTag("Inventory"));

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("Inventory", getInventory().serializeNBT());
		return super.writeToNBT(compound);
	}

	/* Wrappers */
	@Nonnull
	public ItemStack getStackInSlot(int slot) {
		return getInventory().getStackInSlot(slot);
	}

	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		getInventory().setStackInSlot(slot, stack);
	}

	public ItemStack insertItem(int slot, @Nonnull ItemStack stack) {
		return getInventory().insertItem(slot, stack, false);
	}

	public ItemStack extractItem(int slot, int amount) {
		return getInventory().extractItem(slot, amount, false);
	}

	public int getSlots() {
		return getInventory().getSlots();
	}

	public boolean isFull() {
		return IntStream.range(0, getSlots()).allMatch(i -> !getStackInSlot(i).isEmpty());
	}

}
