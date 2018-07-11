/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.tile.pipe;

import java.util.List;

import com.google.common.collect.Lists;

import io.github.lethinh.intensetech.tile.TileBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

public abstract class TileConnectedPipe<C extends Capability> extends TileBase
		implements IPipeModule<C> {

	private List<TileEntity> adjacentTiles;
	private List<TileConnectedPipe<C>> adjacentPipes;

	public TileConnectedPipe() {
		adjacentTiles = Lists.newArrayList();
		adjacentPipes = Lists.newArrayList();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		// Adjacent tiles
//		NBTTagList tileTagList = compound.getTagList("Tiles", Constants.NBT.TAG_COMPOUND);
//
//		if (!tileTagList.hasNoTags()) {
//			adjacentTiles = Lists.newArrayListWithCapacity(compound.getInteger("TilesSize"));
//
//			for (int i = 0; i < tileTagList.tagCount(); ++i) {
//				NBTTagCompound tileTag = tileTagList.getCompoundTagAt(i);
//				int tileIndex = tileTag.getInteger("TileIndex");
//				TileEntity tile = TileEntity.create(world, compound);
//
//				for (TileEntity check : adjacentTiles) {
//					if (check.getWorld().provider.getDimensionType().equals(tile.getWorld().provider.getDimensionType())
//							&& check.getPos().equals(tile.getPos())) // Vanilla TileEntity
//																		// doesn't
//					// override equals
//					// method
//					{
//						continue;
//					}
//
//					if (tileIndex > 0 && tileIndex < adjacentTiles.size()) {
//						adjacentTiles.set(tileIndex, tile);
//					}
//				}
//			}
//		}

		// Adjacent pipes
//		NBTTagList pipeTagList = compound.getTagList("Pipes", Constants.NBT.TAG_COMPOUND);
//
//		if (!pipeTagList.hasNoTags()) {
//			adjacentPipes = Lists.newArrayListWithCapacity(compound.getInteger("PipesSize"));
//
//			for (int j = 0; j < pipeTagList.tagCount(); ++j) {
//				NBTTagCompound pipeTag = pipeTagList.getCompoundTagAt(j);
//				int pipeIndex = pipeTag.getInteger("PipeIndex");
//				TileConnectedPipe<C> pipe = (TileConnectedPipe<C>) TileEntity.create(world, pipeTag);
//
//				if (adjacentPipes.contains(pipe) || pipe.adjacentPipes.contains(this)) {
//					continue;
//				}
//
//				if (pipeIndex > 0 && pipeIndex < adjacentPipes.size()) {
//					adjacentPipes.set(pipeIndex, pipe);
//				}
//			}
//		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		// Adjacent tiles
//		if (!adjacentTiles.isEmpty()) {
//			NBTTagList tileTagList = new NBTTagList();
//
//			for (int i = 0; i < adjacentTiles.size(); ++i) {
//				TileEntity tile = adjacentTiles.get(i);
//				NBTTagCompound tileTag = new NBTTagCompound();
//				tileTag.setInteger("TileIndex", i);
//				tile.writeToNBT(tileTag);
//				tileTagList.appendTag(tileTag);
//			}
//
//			compound.setTag("Tiles", tileTagList);
//			compound.setInteger("TilesSize", adjacentTiles.size());
//		}

		// Adjacent pipes
//		if (!adjacentPipes.isEmpty()) {
//			NBTTagList pipeTagList = new NBTTagList();
//
//			for (int i = 0; i < adjacentPipes.size(); ++i) {
//				TileConnectedPipe<C> pipe = adjacentPipes.get(i);
//
//				if (pipe.adjacentPipes.contains(this)) {
//					continue;
//				}
//
//				NBTTagCompound pipeTag = new NBTTagCompound();
//				pipeTag.setInteger("PipeIndex", i);
//				pipe.writeToNBT(pipeTag);
//				pipeTagList.appendTag(pipeTag);
//			}
//
//			compound.setTag("Pipes", pipeTagList);
//			compound.setInteger("PipesSize", adjacentPipes.size());
//		}

		return super.writeToNBT(compound);
	}

	@Override
	public void invalidate() {
		super.invalidate();
		adjacentTiles.clear();
		adjacentPipes.clear();
	}

	/* Block Impl */
	@Override
	public void onNeighborBlockChange() {
		if (world.isRemote) {
			return;
		}

		PipeType<C> type = getPipeType();

		for (EnumFacing facing : EnumFacing.values()) {
			BlockPos pos = getPos().offset(facing);
			TileEntity tile = world.getTileEntity(pos);

			if (tile == null) {
				// Remove pipe from list if is destroyed
				if (!adjacentPipes.isEmpty()) {
					adjacentPipes.removeIf(pipe -> pipe.getPos().equals(pos)
							&& pipe.world.provider.getDimensionType().equals(world.provider.getDimensionType()));
				}

				continue;
			}

			// Add pipe
			if (tile instanceof IPipeModule) {
				TileConnectedPipe<C> pipe = (TileConnectedPipe<C>) tile;

				if (!pipe.isTileInvalid()) {
					adjacentPipes.add(pipe);
				}
			} else if (getRepresentModule() != IPipeModule.NORMAL && type.acceptTile(tile, facing)) {
				adjacentTiles.add(tile);
			}
		}

		notifyBlockUpdate();
		// checkPipes();
		markDirty();
	}

	@Override
	public void onNeighborTileChange(BlockPos neighbor) {

	}

	/* Helpers */
//	protected boolean checkPipes() {
//		if (isTileInvalid() || adjacentPipes.isEmpty()) {
//			return false;
//		}
//
//		Collections.reverse(adjacentPipes); // First-in-first-out
//		return true;
//	}

	protected EnumFacing getNeighborFacing(BlockPos pos, BlockPos neighbor) {
		int dx = pos.getX() - neighbor.getX();

		if (dx == 0) {
			int dz = pos.getZ() - neighbor.getZ();

			if (dz == 0) {
				int dy = pos.getY() - neighbor.getY();
				return dy > 0 ? EnumFacing.DOWN : EnumFacing.UP;
			} else {
				return dz > 0 ? EnumFacing.NORTH : EnumFacing.SOUTH;
			}
		} else {
			return dx > 0 ? EnumFacing.WEST : EnumFacing.EAST;
		}
	}

	/* Getters */
	public List<TileEntity> getAdjacentTiles() {
		return adjacentTiles;
	}

	public List<TileConnectedPipe<C>> getAdjacentPipes() {
		return adjacentPipes;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof TileConnectedPipe && ((TileConnectedPipe<C>) o).getRepresentModule() == getRepresentModule()
				&& super.equals(o);
	}

}
