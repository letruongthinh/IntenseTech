/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech;

import java.util.Comparator;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import io.github.lethinh.intensetech.api.provider.ITabSort;
import io.github.lethinh.intensetech.api.provider.ITabSort.CreativeTabCategory;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTab extends CreativeTabs {

	public CreativeTab() {
		super(IntenseTech.MOD_ID);
	}

	@SideOnly(Side.CLIENT)
	@Nonnull
	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(Items.END_CRYSTAL);
	}

	@Override
	public boolean hasSearchBar() {
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void displayAllRelevantItems(NonNullList<ItemStack> stacks) {
		// Loads all the ItemStacks from the registry to the creative tab first before
		// sorting anything.
		super.displayAllRelevantItems(stacks);

		// Sorts the ItemStacks in the creative tab.
		stacks.sort(new CreativeTabSorter());
	}

	/**
	 * Based on Choonster's creative tab sorting way. Link:
	 * <p>
	 * <p>
	 * https://gist.github.com/Choonster/876acc3217229e172e46
	 * </p>
	 * <p>
	 * <p>
	 * This creative tab's sorting method is nearly the same as the Choonster's. But
	 * it doesn't compare any Items or ItemBlocks. It uses the natural number order
	 * and the alphabetical order, whose order is provided by
	 * {@link ITabSort.CreativeTabCategory}.
	 * </p>
	 */
	@ParametersAreNonnullByDefault
	private static class CreativeTabSorter implements Comparator<ItemStack> {
		@Override
		public int compare(ItemStack o1, ItemStack o2) {
			Item item1 = o1.getItem(), item2 = o2.getItem();
			CreativeTabCategory c1 = CreativeTabCategory.ITEM, c2 = CreativeTabCategory.ITEM;

			if (item1 instanceof ITabSort) {
				c1 = ((ITabSort) item1).getTabCategory();
			}

			if (item2 instanceof ITabSort) {
				c2 = ((ITabSort) item2).getTabCategory();
			}

			if (c1.getCategoryIndex() > c2.getCategoryIndex()) {
				// If c1 is greater than c2, it will sort sort item1 before item2.
				// For example: If I had item A in category 1 and B item category 2, it would
				// sort A item before B item
				// (Natural number order).
				return 1;
			} else if (c2.getCategoryIndex() > c1.getCategoryIndex()) {
				// If c2 is greater than c1, it will sort item2 before item.
				// For example: If I had item A in category 1 and B item category 2, it would
				// sort B item before A item
				// (Natural number order).
				return -1;
			}

			// If c1 and c2 have the same category, it will sort the items using their
			// names.
			// For example: If I had A item and B item, it would sort A item before B item
			// (Alphabetical order).
			return o1.getDisplayName().compareToIgnoreCase(o2.getDisplayName());
		}
	}

}
