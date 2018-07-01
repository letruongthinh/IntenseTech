/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.api.capability;

import io.github.lethinh.intensetech.tile.TileBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class TileItemHandler extends ItemStackHandler {

	private final TileBase tile;

	public TileItemHandler(TileBase tile, int size) {
		super(size);
		this.tile = tile;
	}

	public NonNullList<ItemStack> getStacks() {
		return stacks;
	}

	@Override
	protected void onContentsChanged(int slot) {
		tile.markDirty();
	}

}
