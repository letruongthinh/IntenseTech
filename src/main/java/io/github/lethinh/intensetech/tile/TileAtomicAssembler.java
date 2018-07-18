/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.tile;

import io.github.lethinh.intensetech.capability.CraftMatrixItemHandler;
import io.github.lethinh.intensetech.inventory.container.ContainerAtomicAssembler;
import io.github.lethinh.intensetech.inventory.gui.GuiAtomicAssembler;
import io.github.lethinh.intensetech.manager.RecipesManager;
import io.github.lethinh.intensetech.recipe.IAtomicAssemblerRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class TileAtomicAssembler extends TileMachineBase<CraftMatrixItemHandler> {

	private IAtomicAssemblerRecipe curRecipe;

	public TileAtomicAssembler() {
		super(new CraftMatrixItemHandler(null, 5, 5), 100000, 2000, 2000, 100000);
		inventory.setTile(this);
	}

	/* Work */
	@Override
	public boolean canWork() {
		return super.canWork() && (curRecipe = RecipesManager.findAtomicAssemblerRecipe(inventory)) != null;
	}

	@Override
	public void doWork() {
		// Remove items from the craft matrix
		for (int i = 0; i < inventory.getInputSlots(); ++i) {
			ItemStack stack = inventory.getStackInSlot(i);

			if (stack.isEmpty()) {
				continue;
			}

			extractItem(i, curRecipe.getMatchingStack(stack).getCount());
		}

		// Add output
		insertItem(inventory.getOutputSlot(), curRecipe.getRecipeOutput().copy());
	}

	@Override
	public int getTotalWorkCycles() {
		return 40;
	}

	@Override
	public int getRequiredEnergy() {
		return 100;
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
