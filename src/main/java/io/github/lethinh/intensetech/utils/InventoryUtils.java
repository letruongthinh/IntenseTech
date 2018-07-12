/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.utils;

import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;

public final class InventoryUtils {

	private InventoryUtils() {

	}

	@Nonnull
	public static ItemStack extractStack(IItemHandler inventory, int slot, int amount) {
		return inventory.extractItem(slot, amount, false);
	}

	public static NonNullList<ItemStack> getStacks(IItemHandler inventory) {
		NonNullList<ItemStack> ret = NonNullList.withSize(inventory.getSlots(), ItemStack.EMPTY);
		IntStream.range(0, inventory.getSlots()).forEach(i -> ret.set(i, inventory.getStackInSlot(i)));
		return ret;
	}

	public static boolean isFull(IItemHandler inventory) {
		return getStacks(inventory).stream().allMatch(stack -> stack.getCount() == 64);
	}

	public static boolean isEmpty(IItemHandler inventory) {
		return getStacks(inventory).stream().anyMatch(stack -> stack.isEmpty() || stack.getCount() < 64);
	}

	@Nonnull
	public static ItemStack transferStack(@Nonnull ItemStack src, IItemHandler dst) {
		if (isFull(dst) || src.isEmpty()) {
			return ItemStack.EMPTY;
		}

		int emptySlot = getFirstEmptySlot(dst);

		if (emptySlot == -1) {
			return ItemStack.EMPTY;
		}

		if (!dst.getStackInSlot(emptySlot).isEmpty() && !src.isItemEqual(dst.getStackInSlot(emptySlot))) {
			emptySlot = getFirstEmptySlot(dst, true);
		}

		return dst.insertItem(emptySlot, src, false);
	}

	@Nonnull
	public static ItemStack transferInventory(IItemHandler src, IItemHandler dst) {
//		if (isEmpty(src)) {
//			return ItemStack.EMPTY;
//		}

		if (isFull(dst)) {
			return ItemStack.EMPTY;
		}

		int slot = IntStream.range(0, src.getSlots()).filter(i -> !src.getStackInSlot(i).isEmpty()).findFirst()
				.orElse(-1);

		if (slot == -1) {
			return ItemStack.EMPTY;
		}

		ItemStack extract = src.extractItem(slot, 1, false);

		if (extract.isEmpty()) {
			return ItemStack.EMPTY;
		}

		int emptySlot = getFirstEmptySlot(dst);

		if (emptySlot == -1) {
			return ItemStack.EMPTY;
		}

		if (!dst.getStackInSlot(emptySlot).isEmpty() && !extract.isItemEqual(dst.getStackInSlot(emptySlot))) {
			emptySlot = getFirstEmptySlot(dst, true);
		}

		return dst.insertItem(emptySlot, extract, false);
	}

	public static int getFirstEmptySlot(IItemHandler inventory) {
		return getFirstEmptySlot(inventory, false);
	}

	/**
	 * Get first empty slot in the inventory
	 *
	 * @param inventory The inventory
	 * @param absolute  Set to true if you want the obtaining slot to be completely
	 *                  empty, which means there are no items in. Otherwise set to
	 *                  false, will obtain the slot which is under its max holding
	 *                  limit.
	 * @return
	 */
	public static int getFirstEmptySlot(IItemHandler inventory, boolean absolute) {
		return IntStream.range(0, inventory.getSlots())
				.filter(i -> absolute && inventory.getStackInSlot(i).isEmpty()
						|| inventory.getStackInSlot(i).getCount() < inventory.getSlotLimit(i))
				.findFirst()
				.orElse(-1);
	}

}
