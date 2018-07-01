/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.inventory.container;

import io.github.lethinh.intensetech.tile.transfer.TileTransferNode;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerTransferNode extends ContainerBase<TileTransferNode> {

	public ContainerTransferNode(InventoryPlayer inventoryPlayer, TileTransferNode tile) {
		super(inventoryPlayer, tile);

		// Add slots
		int xOffset = 16;

		for (int i = 0; i < 10; ++i) {
			addSlotToContainer(new SlotItemHandler(tile.getInventory(), i, 22 + i * 18, 69));
		}

		// Add player inventory
		addPlayerInventory(inventoryPlayer, 8, 97);
	}

}
