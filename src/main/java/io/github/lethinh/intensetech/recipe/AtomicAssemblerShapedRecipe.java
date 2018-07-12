/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.recipe;

import javax.annotation.Nonnull;

import io.github.lethinh.intensetech.capability.CraftMatrixItemHandler;
import io.github.lethinh.intensetech.utils.ConstFunctionUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class AtomicAssemblerShapedRecipe extends ShapedOreRecipe implements IAtomicAssemblerRecipe {

	public AtomicAssemblerShapedRecipe(Block result, Object... recipe) {
		super(ConstFunctionUtils.EMPTY_RESOURCE_LOCATION, result, recipe);
	}

	public AtomicAssemblerShapedRecipe(Item result, Object... recipe) {
		super(ConstFunctionUtils.EMPTY_RESOURCE_LOCATION, result, recipe);
	}

	public AtomicAssemblerShapedRecipe(@Nonnull ItemStack result, Object... recipe) {
		super(ConstFunctionUtils.EMPTY_RESOURCE_LOCATION, result, recipe);
	}

	/* IAtomicAssemblerRecipe */
	@Override
	public boolean matches(CraftMatrixItemHandler itemHandler) {
		for (int x = 0; x <= itemHandler.getWidth() - getRecipeWidth(); ++x) {
			for (int y = 0; y <= itemHandler.getHeight() - getRecipeHeight(); ++y) {
				if (checkMatch(itemHandler, x, y, true)) {
					return true;
				}

				if (checkMatch(itemHandler, x, y, false)) {
					return true;
				}
			}
		}

		return false;
	}

	@Nonnull
	@Override
	public ItemStack getMatchingStack(@Nonnull ItemStack stackToCheck) {
		if (stackToCheck.isEmpty()) {
			return ItemStack.EMPTY;
		}

		for (int i = 0; i < input.size(); ++i) {
			Ingredient ingredient = input.get(i);

			if (!ingredient.apply(stackToCheck)) {
				continue;
			}

			return ingredient.getMatchingStacks()[0];
		}

		return ItemStack.EMPTY;
	}

	/**
	 * Checks if the region of a crafting inventory is match for the recipe.
	 */
	private boolean checkMatch(CraftMatrixItemHandler itemHandler, int startX, int startY, boolean mirror) {
		for (int x = 0; x < itemHandler.getWidth(); ++x) {
			for (int y = 0; y < itemHandler.getHeight(); ++y) {
				int subX = x - startX;
				int subY = y - startY;
				Ingredient target = Ingredient.EMPTY;

				if (subX >= 0 && subY >= 0 && subX < getRecipeWidth() && subY < getRecipeHeight()) {
					target = mirror ? getIngredients().get(getRecipeWidth() - subX - 1 + subY * getRecipeWidth())
							: getIngredients().get(subX + subY * getRecipeWidth());
				}

				if (!target.apply(itemHandler.getStackInRowAndColumn(x, y))) {
					return false;
				}
			}
		}

		return true;
	}

}
