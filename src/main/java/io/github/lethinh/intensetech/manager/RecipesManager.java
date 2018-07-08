/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.manager;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import io.github.lethinh.intensetech.recipe.AtomicAssemblerShapedRecipe;
import io.github.lethinh.intensetech.recipe.AtomicAssemblerShapelessRecipe;
import io.github.lethinh.intensetech.utils.OreDictUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RecipesManager {

	public static List<AtomicAssemblerShapedRecipe> atomicAssemblerShaped;
	public static List<AtomicAssemblerShapelessRecipe> atomicAssemblerShapeless;

	public static void registerRecipes() {
		atomicAssemblerShaped = Lists.newArrayList();
		atomicAssemblerShapeless = Lists.newArrayList();

		addAtomicAssemblerShapelessRecipe(new ItemStack(ItemsManager.silverIngot), Items.IRON_INGOT, Items.DIAMOND);
	}

	/* Recipe Helper */
	public static void addAtomicAssemblerShapedRecipe(Object output, Object... recipe) {
		addAtomicAssemblerShapedRecipe(OreDictUtils.getOreDictItemStack(output), recipe);
	}

	public static void addAtomicAssemblerShapedRecipe(@Nonnull ItemStack output, Object... recipe) {
		atomicAssemblerShaped.add(new AtomicAssemblerShapedRecipe(output, recipe));
	}

	public static void addAtomicAssemblerShapelessRecipe(Object output, Object... recipe) {
		addAtomicAssemblerShapelessRecipe(OreDictUtils.getOreDictItemStack(output), recipe);
	}

	public static void addAtomicAssemblerShapelessRecipe(@Nonnull ItemStack output, Object... recipe) {
		atomicAssemblerShapeless.add(new AtomicAssemblerShapelessRecipe(output, recipe));
	}

}
