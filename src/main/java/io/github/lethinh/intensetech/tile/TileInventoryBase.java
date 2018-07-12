/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.tile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.github.lethinh.intensetech.capability.TileItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;

public class TileInventoryBase<I extends TileItemHandler> extends TileBase {

	public final I inventory;

	public TileInventoryBase(int size) {
		this((I) new TileItemHandler(null, size));
		inventory.setTile(this);
	}

	public TileInventoryBase(I inventory) {
		this.inventory = inventory;
	}

	@Nullable
	@Override
	public IItemHandler getItemHandler(@Nullable EnumFacing facing) {
		return inventory;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		inventory.deserializeNBT(compound.getCompoundTag("Inventory"));

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("Inventory", inventory.serializeNBT());
		return super.writeToNBT(compound);
	}

	/* Inventory Helpers */
	public int getFirstEmptySlot() {
		return inventory.getFirstEmptySlot();
	}

	@Nonnull
	public ItemStack getStackInSlot(int slot) {
		return inventory.getStackInSlot(slot);
	}

	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		inventory.setStackInSlot(slot, stack);
	}

	@Nonnull
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack) {
		return inventory.insertItem(slot, stack, false);
	}

	@Nonnull
	public ItemStack extractItem(int slot, int amount) {
		return inventory.extractItem(slot, amount, false);
	}

	public int getSlots() {
		return inventory.getSlots();
	}

}
