/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.block;

import io.github.lethinh.intensetech.IntenseTech;
import io.github.lethinh.intensetech.api.provider.IBlockModelProperties;
import io.github.lethinh.intensetech.api.provider.IModelRegister;
import io.github.lethinh.intensetech.api.provider.ITabSort;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBase extends Block implements ITabSort, IModelRegister, IBlockModelProperties {

	private final String name;

	public BlockBase(String name, Material materialIn) {
		super(materialIn);
		this.name = name;
		setRegistryName(IntenseTech.MOD_ID, name);
		setUnlocalizedName(getRegistryName().toString());
		setCreativeTab(IntenseTech.tab);
	}

	public String getName() {
		return name;
	}

	/* ITabSort */
	@Override
	public CreativeTabCategory getTabCategory() {
		return CreativeTabCategory.BLOCK;
	}

	/* IModelRegister */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelResourceLocation model = new ModelResourceLocation(getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, model);
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(this), stack -> model);
	}

	/* IBlockModelProperties */
	@SideOnly(Side.CLIENT)
	@Override
	public boolean isUvLock() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean isSmooth() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean isGui3d() {
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getTexture() {
		return name;
	}

}
