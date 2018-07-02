/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.api.provider;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IBlockModelProperties {

	@SideOnly(Side.CLIENT)
	String getTexture();

	@SideOnly(Side.CLIENT)
	boolean isUvLock();

	@SideOnly(Side.CLIENT)
	boolean isSmooth();

	@SideOnly(Side.CLIENT)
	boolean isGui3d();

}
