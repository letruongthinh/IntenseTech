/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.inventory.gui;

import io.github.lethinh.intensetech.inventory.container.ContainerAtomicAssembler;
import io.github.lethinh.intensetech.inventory.widget.WidgetEnergyBar;
import io.github.lethinh.intensetech.tile.TileAtomicAssembler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAtomicAssembler extends GuiBase<TileAtomicAssembler, ContainerAtomicAssembler> {

	public GuiAtomicAssembler(InventoryPlayer inventoryPlayer, TileAtomicAssembler tile) {
		super(new ContainerAtomicAssembler(inventoryPlayer, tile), "atomic_assembler");
		setGuiContainerSize(GuiContainerSize.MEDIUM_X.getXSize(), 215);

		// Widget energy bar
		addWidget(new WidgetEnergyBar(25, 114, tile.getEnergyStorage()));
	}

}
