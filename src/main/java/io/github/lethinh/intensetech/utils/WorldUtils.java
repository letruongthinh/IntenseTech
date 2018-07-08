/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.utils;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class WorldUtils {

	private WorldUtils() {

	}

	public static List<TileEntity> getSideTiles(BlockPos pos, World world) {
		List<TileEntity> tiles = Lists.newArrayList();
		TileEntity up = world.getTileEntity(pos.offset(EnumFacing.UP));
		TileEntity down = world.getTileEntity(pos.offset(EnumFacing.DOWN));
		TileEntity north = world.getTileEntity(pos.offset(EnumFacing.NORTH));
		TileEntity south = world.getTileEntity(pos.offset(EnumFacing.SOUTH));
		TileEntity east = world.getTileEntity(pos.offset(EnumFacing.EAST));
		TileEntity west = world.getTileEntity(pos.offset(EnumFacing.WEST));

		if (up != null && !up.isInvalid()) {
			tiles.add(up);
		}
		if (down != null && !down.isInvalid()) {
			tiles.add(down);
		}
		if (north != null && !north.isInvalid()) {
			tiles.add(north);
		}
		if (south != null && !south.isInvalid()) {
			tiles.add(south);
		}
		if (east != null && !east.isInvalid()) {
			tiles.add(east);
		}
		if (west != null && !west.isInvalid()) {
			tiles.add(west);
		}

		return tiles;
	}

	public static Map<TileEntity, EnumFacing> getSidedTiles(BlockPos pos, World world) {
		Map<TileEntity, EnumFacing> tiles = Maps.newLinkedHashMap();
		TileEntity up = world.getTileEntity(pos.offset(EnumFacing.UP));
		TileEntity down = world.getTileEntity(pos.offset(EnumFacing.DOWN));
		TileEntity north = world.getTileEntity(pos.offset(EnumFacing.NORTH));
		TileEntity south = world.getTileEntity(pos.offset(EnumFacing.SOUTH));
		TileEntity east = world.getTileEntity(pos.offset(EnumFacing.EAST));
		TileEntity west = world.getTileEntity(pos.offset(EnumFacing.WEST));

		if (up != null && !up.isInvalid()) {
			tiles.put(up, EnumFacing.NORTH);
		}
		if (down != null && !down.isInvalid()) {
			tiles.put(down, EnumFacing.DOWN);
		}
		if (north != null && !north.isInvalid()) {
			tiles.put(north, EnumFacing.NORTH);
		}
		if (south != null && !south.isInvalid()) {
			tiles.put(south, EnumFacing.SOUTH);
		}
		if (east != null && !east.isInvalid()) {
			tiles.put(east, EnumFacing.EAST);
		}
		if (west != null && !west.isInvalid()) {
			tiles.put(west, EnumFacing.WEST);
		}

		return tiles;
	}

}
