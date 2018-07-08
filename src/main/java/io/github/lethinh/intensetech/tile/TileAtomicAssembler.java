/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.tile;

import io.github.lethinh.intensetech.capability.CraftMatrixItemHandler;
import io.github.lethinh.intensetech.inventory.container.ContainerAtomicAssembler;
import io.github.lethinh.intensetech.inventory.gui.GuiAtomicAssembler;
import io.github.lethinh.intensetech.recipe.AtomicAssemblerShapedRecipe;
import io.github.lethinh.intensetech.recipe.AtomicAssemblerShapelessRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class TileAtomicAssembler extends TileMachineBase<CraftMatrixItemHandler> {

	public TileAtomicAssembler() {
		super(new CraftMatrixItemHandler(null, 5, 5), 100000, 2000, 2000, 100000);
		inventory.setTile(this);
	}

	/* Work */
	@Override
	public void doWork() {
		// Get matching recipe from current items in craft matrix
		AtomicAssemblerShapedRecipe shapedRecipe = AtomicAssemblerShapedRecipe.findMatchingRecipe(inventory);
		AtomicAssemblerShapelessRecipe shapelessRecipe = AtomicAssemblerShapelessRecipe
				.findMatchingRecipe(inventory);

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
