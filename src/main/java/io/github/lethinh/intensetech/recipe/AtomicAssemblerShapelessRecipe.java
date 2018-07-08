/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.recipe;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import io.github.lethinh.intensetech.capability.CraftMatrixItemHandler;
import io.github.lethinh.intensetech.manager.RecipesManager;
import io.github.lethinh.intensetech.utils.ConstFunctionUtils;
import net.minecraft.block.Block;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class AtomicAssemblerShapelessRecipe extends ShapelessOreRecipe {

	public AtomicAssemblerShapelessRecipe(Block result, Object... recipe) {
		super(ConstFunctionUtils.EMPTY_RESOURCE_LOCATION, result, recipe);
	}

	public AtomicAssemblerShapelessRecipe(Item result, Object... recipe) {
		super(ConstFunctionUtils.EMPTY_RESOURCE_LOCATION, result, recipe);
	}

	public AtomicAssemblerShapelessRecipe(NonNullList<Ingredient> input, @Nonnull ItemStack result) {
		super(ConstFunctionUtils.EMPTY_RESOURCE_LOCATION, input, result);
	}

	public AtomicAssemblerShapelessRecipe(@Nonnull ItemStack result, Object... recipe) {
		super(ConstFunctionUtils.EMPTY_RESOURCE_LOCATION, result, recipe);
	}

	public static AtomicAssemblerShapelessRecipe findMatchingRecipe(CraftMatrixItemHandler itemHandler) {
		return RecipesManager.atomicAssemblerShapeless.stream().filter(recipe -> recipe.matches(itemHandler))
				.findFirst().orElse(null);
	}

	/* Helper */
	public boolean matches(CraftMatrixItemHandler itemHandler) {
		int ingredientCount = 0;
		RecipeItemHelper recipeItemHelper = new RecipeItemHelper();
		List<ItemStack> items = Lists.newArrayList();

		for (int i = 0; i < itemHandler.getSlots(); ++i) {
			ItemStack itemstack = itemHandler.getStackInSlot(i);

			if (!itemstack.isEmpty()) {
				++ingredientCount;

				if (isSimple) {
					recipeItemHelper.accountStack(itemstack, 1);
				} else {
					items.add(itemstack);
				}
			}
		}

		if (ingredientCount != this.input.size()) {
			return false;
		}

		if (this.isSimple) {
			return recipeItemHelper.canCraft(this, null);
		}

		return RecipeMatcher.findMatches(items, this.input) != null;
	}

}
