/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.utils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public final class NBTUtils {

	private NBTUtils() {

	}

	public static void writeBlockPos(NBTTagCompound compound, String key, BlockPos pos) {
		compound.setInteger(key + "X", pos.getX());
		compound.setInteger(key + "Y", pos.getY());
		compound.setInteger(key + "Z", pos.getZ());
	}

	public static BlockPos readBlockPos(NBTTagCompound compound, String key) {
		return new BlockPos(compound.getInteger(key + "X"), compound.getInteger(key + "Y"),
				compound.getInteger(key + "Z"));
	}

}
