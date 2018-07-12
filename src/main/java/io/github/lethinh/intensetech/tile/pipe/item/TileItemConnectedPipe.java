/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.tile.pipe.item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.github.lethinh.intensetech.capability.TileItemHandler;
import io.github.lethinh.intensetech.tile.pipe.PipeType;
import io.github.lethinh.intensetech.tile.pipe.TileConnectedPipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.IItemHandler;

public abstract class TileItemConnectedPipe extends TileConnectedPipe<Capability<IItemHandler>> {

	public final TileItemHandler inventory;

	public TileItemConnectedPipe(int size) {
		this.inventory = new TileItemHandler(null, size);
		inventory.setTile(this);
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

	/* IPipeModule */
	@Override
	public PipeType<Capability<IItemHandler>> getType() {
		return PipeType.ITEM;
	}

}
