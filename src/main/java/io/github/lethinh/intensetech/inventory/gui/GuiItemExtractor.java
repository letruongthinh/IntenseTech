/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.inventory.gui;

import io.github.lethinh.intensetech.inventory.container.ContainerItemExtractor;
import io.github.lethinh.intensetech.tile.pipe.item.TileItemExtractor;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiItemExtractor extends GuiBase<TileItemExtractor, ContainerItemExtractor> {

	public GuiItemExtractor(InventoryPlayer inventoryPlayer, TileItemExtractor tile) {
		super(new ContainerItemExtractor(inventoryPlayer, tile), "Transfer_Node");
	}

}
