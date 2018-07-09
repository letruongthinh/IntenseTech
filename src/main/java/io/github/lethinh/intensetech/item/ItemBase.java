/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.item;

import io.github.lethinh.intensetech.IntenseTech;
import io.github.lethinh.intensetech.creativetab.ITabSort;
import io.github.lethinh.intensetech.model.IItemModelProperties;
import io.github.lethinh.intensetech.model.IItemModelRegister;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemBase extends Item implements ITabSort, IItemModelRegister, IItemModelProperties {

	public ItemBase(String name) {
		setRegistryName(IntenseTech.MOD_ID, name);
		setUnlocalizedName(getRegistryName().getResourcePath());
		setCreativeTab(IntenseTech.tab);
		setContainerItem(this);
	}

	/* ITabSort */
	@Override
	public int getCategory() {
		return ITEM;
	}

	/* IItemModelRegister */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerItemModel() {
		ModelResourceLocation model = new ModelResourceLocation(getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(this, 0, model);
		ModelLoader.setCustomMeshDefinition(this, stack -> model);
	}

	/* IItemModelProperties */
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

}
