/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.tile.crafter;

import io.github.lethinh.intensetech.api.capability.CraftMatrixItemHandler;
import io.github.lethinh.intensetech.inventory.container.ContainerAtomicAssembler;
import io.github.lethinh.intensetech.inventory.gui.GuiAtomicAssembler;
import io.github.lethinh.intensetech.recipe.AtomicAssemblerShapedRecipe;
import io.github.lethinh.intensetech.recipe.AtomicAssemblerShapelessRecipe;
import io.github.lethinh.intensetech.tile.TileMachineBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;

public class TileAtomicAssembler extends TileMachineBase<CraftMatrixItemHandler> {

	public TileAtomicAssembler() {
		super(26, 100000, 2000, 2000, 100000);
	}

	/* Work */
	@Override
	public void doWork() {
		// Get matching recipe from current items in craft matrix
		AtomicAssemblerShapedRecipe shapedRecipe = AtomicAssemblerShapedRecipe.findMatchingRecipe(getInventory());
		AtomicAssemblerShapelessRecipe shapelessRecipe = AtomicAssemblerShapelessRecipe
				.findMatchingRecipe(getInventory());

		if (shapedRecipe != null) {
			// Remove items from the craft matrix
			for (int i = 0; i < getSlots() - 1; ++i) {
				if (getStackInSlot(i).isEmpty()) {
					continue;
				}

				extractItem(i, 1);
			}

			// Add output
			insertItem(25, shapedRecipe.getRecipeOutput().copy());
		} else if (shapelessRecipe != null) {
			// Remove items from the craft matrix
			for (int i = 0; i < getSlots() - 1; ++i) {
				if (getStackInSlot(i).isEmpty()) {
					continue;
				}

				extractItem(i, 1);
			}

			// Add output
			insertItem(25, shapelessRecipe.getRecipeOutput().copy());
		}
	}

	@Override
	public int getTotalWorkCycles() {
		return 20;
	}

	@Override
	public int getRequiredEnergy() {
		return 1000;
	}

	/* TileBase */
	@Override
	public IItemHandlerModifiable getItemHandler(EnumFacing facing) {
		return new CraftMatrixItemHandler(this, 5, 5);
	}

	/* TileInventoryBase */
	@Override
	public CraftMatrixItemHandler getInventory() {
		return new CraftMatrixItemHandler(this, 5, 5);
	}

	/* IGuiTile */
	@Override
	public Object getContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new ContainerAtomicAssembler(player.inventory, this);
	}

	@Override
	public Object getGui(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new GuiAtomicAssembler(player.inventory, this);
	}

}
