/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.recipe;

import javax.annotation.Nonnull;

import io.github.lethinh.intensetech.capability.CraftMatrixItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public interface IAtomicAssemblerRecipe extends IRecipe {

	boolean matches(CraftMatrixItemHandler itemHandler);

	@Nonnull
	ItemStack getMatchingStack(@Nonnull ItemStack stackToCheck);

}
