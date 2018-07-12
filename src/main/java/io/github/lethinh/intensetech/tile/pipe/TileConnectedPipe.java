/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.tile.pipe;

import java.util.List;

import com.google.common.collect.Lists;

import io.github.lethinh.intensetech.tile.TileBase;
import io.github.lethinh.intensetech.utils.NBTUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

/**
 * Just like a multiblock implementation, core of the pipe transferring system
 *
 * @param <C> The {@link Capability} which this pipe will use to transfer
 */
public abstract class TileConnectedPipe<C extends Capability> extends TileBase implements IPipeModule<C> {

	private final PipeTracker<C> tracker;
	private List<BlockPos> adjacentTilesPos;
	public boolean justTransfered = false;

	public TileConnectedPipe() {
		tracker = new PipeTracker<>(this);
		adjacentTilesPos = Lists.newArrayList();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		tracker.deserializeNBT(compound.getCompoundTag("Tracker"));

		NBTTagList tilePosList = compound.getTagList("TilesPos", Constants.NBT.TAG_COMPOUND);

		if (tilePosList.hasNoTags()) {
			return;
		}

		adjacentTilesPos = Lists.newArrayListWithCapacity(compound.getInteger("TilesPosSize"));

		for (int i = 0; i < tilePosList.tagCount(); ++i) {
			NBTTagCompound tilePosTag = tilePosList.getCompoundTagAt(i);
			int idx = tilePosTag.getInteger("TilePosIdx");
			BlockPos pos = NBTUtils.readBlockPos(tilePosTag, "TilePos");
			adjacentTilesPos.add(idx, pos);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("Tracker", tracker.serializeNBT());

		if (!adjacentTilesPos.isEmpty()) {
			NBTTagList tilePosList = new NBTTagList();

			for (int i = 0; i < adjacentTilesPos.size(); ++i) {
				NBTTagCompound tilePosTag = new NBTTagCompound();
				tilePosTag.setInteger("TilePosIdx", i);
				NBTUtils.writeBlockPos(tilePosTag, "TilePos", adjacentTilesPos.get(i));
				tilePosList.appendTag(tilePosTag);
			}

			compound.setTag("TilesPos", tilePosList);
			compound.setInteger("TilesPosSize", adjacentTilesPos.size());
		}

		return super.writeToNBT(compound);
	}

	@Override
	public void invalidate() {
		super.invalidate();
		tracker.invalidate();
		adjacentTilesPos.clear();
	}

	/* Block Impl */
	@Override
	public void onNeighborBlockChange() {
		if (world.isRemote) {
			return;
		}

		PipeType<C> type = getType();

		for (EnumFacing facing : EnumFacing.values()) {
			BlockPos pos = getPos().offset(facing);
			TileEntity tile = world.getTileEntity(pos);

			if (tile == null) {
				tracker.removeTrackDirection(facing);
				continue;
			}

			if (tile instanceof IPipeModule) {
				TileConnectedPipe<C> pipe = (TileConnectedPipe<C>) tile;

				if (pipe.isTileInvalid()) {
					return;
				}

				tracker.addTrackDirection(facing);
			} else if (getRepresentModule() != NORMAL && type.acceptTile(tile, facing)) {
				adjacentTilesPos.add(pos);
			}
		}

		markDirty();
	}

	protected EnumFacing getNeighborFacing(BlockPos pos, BlockPos neighbor) {
		int dx = pos.getX() - neighbor.getX();

		if (dx == 0) {
			int dz = pos.getZ() - neighbor.getZ();

			if (dz == 0) {
				int dy = pos.getY() - neighbor.getY();
				return dy > 0 ? EnumFacing.UP : EnumFacing.DOWN;
			} else {
				return dz > 0 ? EnumFacing.SOUTH : EnumFacing.NORTH;
			}
		} else {
			return dx > 0 ? EnumFacing.EAST : EnumFacing.WEST;
		}
	}

	/* IPipeModule */
	@Override
	public PipeTracker<C> getTracker() {
		return tracker;
	}

	/* Getters */
	public List<BlockPos> getAdjacentTilesPos() {
		return adjacentTilesPos;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof TileConnectedPipe && ((TileConnectedPipe) o).getRepresentModule() == getRepresentModule()
				&& ((TileConnectedPipe) o).getType().equals(getType())
				&& super.equals(o);
	}

	@Override
	public int hashCode() {
		return super.hashCode() * 31 + getRepresentModule();
	}

}
