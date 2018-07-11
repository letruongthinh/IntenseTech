/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.recipe;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import io.github.lethinh.intensetech.capability.CraftMatrixItemHandler;
import io.github.lethinh.intensetech.utils.ConstFunctionUtils;
import net.minecraft.block.Block;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class AtomicAssemblerShapelessRecipe extends ShapelessOreRecipe implements IAtomicAssemblerRecipe {

	public AtomicAssemblerShapelessRecipe(Block result, Object... recipe) {
		super(ConstFunctionUtils.EMPTY_RESOURCE_LOCATION, result, recipe);
		isSimple = false;
	}

	public AtomicAssemblerShapelessRecipe(Item result, Object... recipe) {
		super(ConstFunctionUtils.EMPTY_RESOURCE_LOCATION, result, recipe);
		isSimple = false;
	}

	public AtomicAssemblerShapelessRecipe(NonNullList<Ingredient> input, @Nonnull ItemStack result) {
		super(ConstFunctionUtils.EMPTY_RESOURCE_LOCATION, input, result);
		isSimple = false;
	}

	public AtomicAssemblerShapelessRecipe(@Nonnull ItemStack result, Object... recipe) {
		super(ConstFunctionUtils.EMPTY_RESOURCE_LOCATION, result, recipe);
		isSimple = false;
	}

	/* IAtomicAssemblerRecipe */
	@Override
	public boolean matches(CraftMatrixItemHandler itemHandler) {
		int ingredientCount = 0;
		RecipeItemHelper recipeItemHelper = new RecipeItemHelper();
		List<ItemStack> stacks = Lists.newArrayList();

		for (int i = 0; i < itemHandler.getSlots(); ++i) {
			ItemStack stack = itemHandler.getStackInSlot(i);

			if (!stack.isEmpty()) {
				++ingredientCount;

				if (isSimple) {
					recipeItemHelper.accountStack(stack, 1);
				} else {
					stacks.add(stack);
				}
			}
		}

		if (ingredientCount != input.size()) {
			return false;
		}

		if (isSimple) {
			return recipeItemHelper.canCraft(this, null);
		}

		return RecipeMatcher.findMatches(stacks, input) != null;
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

}
