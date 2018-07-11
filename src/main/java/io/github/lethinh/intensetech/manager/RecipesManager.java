/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.manager;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import io.github.lethinh.intensetech.capability.CraftMatrixItemHandler;
import io.github.lethinh.intensetech.recipe.AtomicAssemblerShapedRecipe;
import io.github.lethinh.intensetech.recipe.AtomicAssemblerShapelessRecipe;
import io.github.lethinh.intensetech.recipe.IAtomicAssemblerRecipe;
import io.github.lethinh.intensetech.utils.OreDictUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RecipesManager {

	public static List<IAtomicAssemblerRecipe> atomicAssemblerRecipes;

	public static void registerRecipes() {
		// Register recipes
		atomicAssemblerRecipes = Lists.newArrayList();

		addAtomicAssemblerShapelessRecipe(new ItemStack(ItemsManager.SILVER_INGOT), Items.IRON_INGOT, Items.DIAMOND);
	}

	/* Atomic Assembler */
	public static IAtomicAssemblerRecipe findMatchingAtomicAssemblerRecipe(CraftMatrixItemHandler itemHandler) {
		return atomicAssemblerRecipes.stream().filter(recipe -> recipe.matches(itemHandler)).findFirst().orElse(null);
	}

	public static void addAtomicAssemblerShapedRecipe(Object output, Object... recipe) {
		addAtomicAssemblerShapedRecipe(OreDictUtils.getOreDictItemStack(output), recipe);
	}

	public static void addAtomicAssemblerShapedRecipe(@Nonnull ItemStack output, Object... recipe) {
		atomicAssemblerRecipes.add(new AtomicAssemblerShapedRecipe(output, recipe));
	}

	public static void addAtomicAssemblerShapelessRecipe(Object output, Object... recipe) {
		addAtomicAssemblerShapelessRecipe(OreDictUtils.getOreDictItemStack(output), recipe);
	}

	public static void addAtomicAssemblerShapelessRecipe(@Nonnull ItemStack output, Object... recipe) {
		atomicAssemblerRecipes.add(new AtomicAssemblerShapelessRecipe(output, recipe));
	}

}
