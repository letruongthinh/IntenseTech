/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.block;

import io.github.lethinh.intensetech.IntenseTech;
import io.github.lethinh.intensetech.api.provider.ITabSort;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockBase extends Block implements ITabSort {

	public BlockBase(String name, Material materialIn) {
		super(materialIn);
		setRegistryName(IntenseTech.MOD_ID, name);
		setUnlocalizedName(getRegistryName().toString());
		setCreativeTab(IntenseTech.tab);
	}

	/* ITabSort */
	@Override
	public CreativeTabCategory getTabCategory() {
		return CreativeTabCategory.BLOCK;
	}

}
