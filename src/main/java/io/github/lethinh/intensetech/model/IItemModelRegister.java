package io.github.lethinh.intensetech.model;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IItemModelRegister {

	@SideOnly(Side.CLIENT)
	void registerItemModel();

}
