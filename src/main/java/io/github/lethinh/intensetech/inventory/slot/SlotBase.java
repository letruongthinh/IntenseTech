/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.inventory.slot;

import io.github.lethinh.intensetech.capability.TileItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBase extends SlotItemHandler {

	public SlotBase(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public void onSlotChanged() {
		IItemHandler itemHandler = getItemHandler();

		if (itemHandler instanceof TileItemHandler) {
			((TileItemHandler) itemHandler).getTile().markDirty();
		}
	}

}
