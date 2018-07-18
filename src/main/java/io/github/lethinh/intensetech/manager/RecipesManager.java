/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import io.github.lethinh.intensetech.capability.CraftMatrixItemHandler;
import io.github.lethinh.intensetech.recipe.AtomicAssemblerShapedRecipe;
import io.github.lethinh.intensetech.recipe.AtomicAssemblerShapelessRecipe;
import io.github.lethinh.intensetech.recipe.IAtomicAssemblerRecipe;
import io.github.lethinh.intensetech.utils.OreDictUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RecipesManager {

	public static final List<IAtomicAssemblerRecipe> ATOMIC_ASSEMBLER = new ArrayList<>();

	public static void registerRecipes() {
		addAtomicAssemblerShapeless(new ItemStack(ItemsManager.SILVER_INGOT), Items.IRON_INGOT, Items.DIAMOND);
	}

	/* Atomic Assembler */
	public static IAtomicAssemblerRecipe findAtomicAssemblerRecipe(CraftMatrixItemHandler itemHandler) {
		return ATOMIC_ASSEMBLER.stream().filter(recipe -> recipe.matches(itemHandler)).findFirst().orElse(null);
	}

	public static void addAtomicAssemblerShaped(Object output, Object... recipe) {
		addAtomicAssemblerShaped(OreDictUtils.getOreDictItemStack(output), recipe);
	}

	public static void addAtomicAssemblerShaped(@Nonnull ItemStack output, Object... recipe) {
		ATOMIC_ASSEMBLER.add(new AtomicAssemblerShapedRecipe(output, recipe));
	}

	public static void addAtomicAssemblerShapeless(Object output, Object... recipe) {
		addAtomicAssemblerShapeless(OreDictUtils.getOreDictItemStack(output), recipe);
	}

	public static void addAtomicAssemblerShapeless(@Nonnull ItemStack output, Object... recipe) {
		ATOMIC_ASSEMBLER.add(new AtomicAssemblerShapelessRecipe(output, recipe));
	}

}
