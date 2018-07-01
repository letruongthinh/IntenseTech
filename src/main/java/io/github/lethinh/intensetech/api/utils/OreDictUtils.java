/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.api.utils;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public final class OreDictUtils {

	private OreDictUtils() {

	}

	/**
	 * Allows getting {@link ItemStack} from the {@link OreDictionary}.
	 *
	 * <p>
	 * Can be used for recipe and for some other purposes.
	 * </p>
	 */
	@Nonnull
	public static ItemStack getOreDictItemStack(Object oreDict) {
		if (!isOreDictValid((String) oreDict)) {
			return ItemStack.EMPTY;
		}

		if (oreDict instanceof String) {
			return getOreStacks((String) oreDict).stream().findFirst().orElse(ItemStack.EMPTY);
		} else if (oreDict instanceof ItemStack) {
			return (ItemStack) oreDict;
		} else if (oreDict instanceof Item) {
			return new ItemStack((Item) oreDict);
		} else if (oreDict instanceof Block) {
			return new ItemStack((Block) oreDict);
		} else {
			return ItemStack.EMPTY;
		}
	}

	public static NonNullList<ItemStack> getOreStacks(String oreDict) {
		return OreDictionary.getOres(oreDict);
	}

	public static boolean isOreDictValid(String oreDict) {
		return getOreStacks(oreDict).stream().anyMatch(stack -> OreDictionary.doesOreNameExist(oreDict));
	}

}
