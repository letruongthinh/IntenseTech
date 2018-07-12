/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.inventory.slot;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class SlotOutput extends SlotBase {

	public SlotOutput(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(@Nonnull ItemStack stack) {
		return false;
	}

}
