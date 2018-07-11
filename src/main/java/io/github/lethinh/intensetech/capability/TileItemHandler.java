/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.capability;

import io.github.lethinh.intensetech.tile.TileBase;
import io.github.lethinh.intensetech.utils.InventoryUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class TileItemHandler extends ItemStackHandler {

	private TileBase tile;

	public TileItemHandler(TileBase tile, int size) {
		super(size);
		this.tile = tile;
	}

	/* IItemHandler */
	@Override
	protected void onContentsChanged(int slot) {
		tile.markDirty();
	}

	/* Helpers */
	public TileBase getTile() {
		return tile;
	}

	public void setTile(TileBase tile) {
		this.tile = tile;
	}

	public NonNullList<ItemStack> getStacks() {
		return stacks;
	}

	public int getFirstEmptySlot() {
		return InventoryUtils.getFirstEmptySlot(this);
	}

}
