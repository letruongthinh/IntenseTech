/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.inventory.gui;

import io.github.lethinh.intensetech.inventory.container.ContainerTransferNode;
import io.github.lethinh.intensetech.tile.transfer.TileTransferNode;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTransferNode extends GuiBase<TileTransferNode, ContainerTransferNode> {

	public GuiTransferNode(InventoryPlayer inventoryPlayer, TileTransferNode tile) {
		super(new ContainerTransferNode(inventoryPlayer, tile), "Transfer_Node");
	}

}
