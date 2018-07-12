/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.item;

import io.github.lethinh.intensetech.IntenseTech;
import io.github.lethinh.intensetech.creativetab.ITabSort;
import io.github.lethinh.intensetech.model.IItemModelProperties;
import io.github.lethinh.intensetech.model.VariantHelper;
import net.minecraft.item.Item;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBase extends Item implements ITabSort, IItemModelProperties {

	private final String name;

	public ItemBase(String name) {
		this.name = name;
		setRegistryName(IntenseTech.MOD_ID, name);
		setUnlocalizedName(IntenseTech.MOD_ID + '.' + name);
		setCreativeTab(IntenseTech.tab);
		setContainerItem(this);
	}

	public String getName() {
		return name;
	}

	/* ITabSort */
	@Override
	public int getCategory() {
		return ITEM;
	}

	/* IItemModelProperties */
	@SideOnly(Side.CLIENT)
	@Override
	public IModelState getDefaultState() {
		return VariantHelper.DEFAULT_ITEM;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean isGui3d() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String[] getTextures() {
		return new String[] { name };
	}

}
