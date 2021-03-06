package io.github.lethinh.intensetech.model;

import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IItemModelProperties {

	@SideOnly(Side.CLIENT)
	IModelState getDefaultState();

	@SideOnly(Side.CLIENT)
	boolean isAmbientOcclusion();

	@SideOnly(Side.CLIENT)
	boolean isGui3d();

	@SideOnly(Side.CLIENT)
	String[] getTextures();

}
