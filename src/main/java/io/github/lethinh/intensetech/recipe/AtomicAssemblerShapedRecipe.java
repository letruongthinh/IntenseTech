/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.recipe;

import javax.annotation.Nonnull;

import io.github.lethinh.intensetech.api.capability.CraftMatrixItemHandler;
import io.github.lethinh.intensetech.api.utils.ConstFunctionUtils;
import io.github.lethinh.intensetech.manager.RecipesManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class AtomicAssemblerShapedRecipe extends ShapedOreRecipe {

	public AtomicAssemblerShapedRecipe(Block result, Object... recipe) {
		super(ConstFunctionUtils.EMPTY_RESOURCE_LOCATION, result, recipe);
	}

	public AtomicAssemblerShapedRecipe(Item result, Object... recipe) {
		super(ConstFunctionUtils.EMPTY_RESOURCE_LOCATION, result, recipe);
	}

	public AtomicAssemblerShapedRecipe(@Nonnull ItemStack result, Object... recipe) {
		super(ConstFunctionUtils.EMPTY_RESOURCE_LOCATION, result, recipe);
	}

	public static AtomicAssemblerShapedRecipe findMatchingRecipe(CraftMatrixItemHandler itemHandler) {
		return RecipesManager.atomicAssemblerShaped.stream().filter(recipe -> recipe.matches(itemHandler)).findFirst()
				.orElse(null);
	}

	/* Helper */
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
