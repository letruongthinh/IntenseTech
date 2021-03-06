/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.capability;

import javax.annotation.Nonnull;

import io.github.lethinh.intensetech.tile.TileBase;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CraftMatrixItemHandler extends TileItemHandler {

	private final int width, height;

	public CraftMatrixItemHandler(TileBase tile, int width, int height) {
		super(tile, width * height + 1);
		this.width = width;
		this.height = height;
	}

	/* Recipe Helpers */
	public int getInputSlots() {
		return getSlots() - 1;
	}

	public int getOutputSlot() {
		return getSlots() - 1;
	}

	/**
	 * Copied from {@link InventoryCrafting#getStackInRowAndColumn}.
	 */
	@Nonnull
	public ItemStack getStackInRowAndColumn(int width, int height) {
		return width < 0 || width >= this.getWidth() || height < 0 || height > this.getHeight() ? ItemStack.EMPTY
				: getStackInSlot(width + height * this.getWidth());
	}

	/**
	 * Copied from {@link InventoryCrafting#fillStackedContents}.
	 */
	@SideOnly(Side.CLIENT)
	public void fillStackedContents(RecipeItemHelper recipeItemHelper) {
		this.stacks.forEach(recipeItemHelper::accountStack);
	}

	/* Getters */
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
