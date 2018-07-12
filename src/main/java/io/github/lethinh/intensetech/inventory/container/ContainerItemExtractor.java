/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.inventory.container;

import io.github.lethinh.intensetech.inventory.slot.SlotBase;
import io.github.lethinh.intensetech.tile.pipe.item.TileItemExtractor;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerItemExtractor extends ContainerBase<TileItemExtractor> {

	public ContainerItemExtractor(InventoryPlayer inventoryPlayer, TileItemExtractor tile) {
		super(inventoryPlayer, tile);

		// Add slots
		int xOffset = 16;

		for (int i = 0; i < 10; ++i) {
			addSlotToContainer(new SlotBase(tile.inventory, i, 22 + i * 18, 69));
		}

		// Add player inventory
		addPlayerInventory(inventoryPlayer, 8, 97);
	}

}
