/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.inventory.container;

import io.github.lethinh.intensetech.capability.CraftMatrixItemHandler;
import io.github.lethinh.intensetech.inventory.slot.SlotOutput;
import io.github.lethinh.intensetech.tile.TileAtomicAssembler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerAtomicAssembler extends ContainerBase<TileAtomicAssembler> {

	public ContainerAtomicAssembler(InventoryPlayer inventoryPlayer, TileAtomicAssembler tile) {
		super(inventoryPlayer, tile);

		// Add player inventory
		addPlayerInventory(inventoryPlayer, 8, 133);

		// Add craft matrix
		int x = 12, y = 17;
		CraftMatrixItemHandler craftMatrix = tile.inventory;

		for (int subX = 0; subX < craftMatrix.getWidth(); ++subX) {
			for (int subY = 0; subY < craftMatrix.getHeight(); ++subY) {
				addSlotToContainer(new SlotItemHandler(craftMatrix, subY + subX * craftMatrix.getWidth(), x + subY * 18,
						y + subX * 18));
			}
		}

		// Add output slot
		addSlotToContainer(new SlotOutput(craftMatrix, 25, 142, 53));
	}

}
