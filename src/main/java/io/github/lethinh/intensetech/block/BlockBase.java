/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.block;

import io.github.lethinh.intensetech.IntenseTech;
import io.github.lethinh.intensetech.creativetab.ITabSort;
import io.github.lethinh.intensetech.model.IBlockModelProperties;
import io.github.lethinh.intensetech.model.definition.FlatBoxModelDefinition;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBase extends Block implements ITabSort, IBlockModelProperties {

	private final String name;

	public BlockBase(String name, Material materialIn) {
		super(materialIn);
		this.name = name;
		setRegistryName(IntenseTech.MOD_ID, name);
		setUnlocalizedName(IntenseTech.MOD_ID + '.' + name);
		setCreativeTab(IntenseTech.tab);
	}

	public String getName() {
		return name;
	}

	/* ITabSort */
	@Override
	public int getCategory() {
		return BLOCK;
	}

	/* IBlockModelProperties */
	@SideOnly(Side.CLIENT)
	@Override
	public FlatBoxModelDefinition getModelDefinition() {
		return new FlatBoxModelDefinition(getTexture(), isUvLock(), isSmooth(), isGui3d());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getTexture() {
		return name;
	}

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
	public boolean isAmbientOcclusion() {
		return true;
	}

}
