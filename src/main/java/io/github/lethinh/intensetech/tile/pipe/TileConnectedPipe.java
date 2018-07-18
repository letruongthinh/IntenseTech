/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.tile.pipe;

import java.util.List;
import java.util.Objects;

import io.github.lethinh.intensetech.tile.TileBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Just like a multiblock implementation, core of the pipe transferring system
 *
 * @param <C> The {@link Capability} which this pipe will use to transfer
 */
public abstract class TileConnectedPipe<C extends Capability> extends TileBase implements IPipe<C>, ITickable {

	private final PipeTracker<C> tracker;
	public EnumFacing inputDirection;
	private long lastUpdateHash = 0L;
	private int progress;

	public TileConnectedPipe() {
		tracker = new PipeTracker<>(this);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("TrackData", tracker.serializeNBT());
		compound.setLong("LastUpdateHash", lastUpdateHash);
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		tracker.deserializeNBT(compound.getCompoundTag("TrackData"));
		lastUpdateHash = compound.getLong("LastUpdateHash");
	}

	@Override
	public void invalidate() {
		super.invalidate();
		tracker.invalidate();
	}

	// Mark dirty can be convenient, but can cause further FPS drop, so check for
	// the tile if it has changed then perform
	@Override
	public void markDirty() {
		if (lastUpdateHash == getUpdateHash()) {
			return;
		}

		super.markDirty();
		lastUpdateHash = getUpdateHash();
	}

	/* ITickable */
	@Override
	public void update() {
		PipeType<C> pipeType = getType();
		C type = pipeType.getType();

		// Pipes
		List<TileConnectedPipe<C>> pipes = tracker.trackNextPipesAllDirections();

		if (!pipes.isEmpty()) {
			for (TileConnectedPipe<C> pipe : pipes) {
				if (pipe.equals(this) || pipe.isTileInvalid()) {
					continue;
				}

				BlockPos pos = pipe.getPos();
				EnumFacing facing = getNeighborFacing(getPos(), pos);

				if (!canTransferToNextPipe(pipe, pos, facing)) {
					continue;
				}

				if (++progress < getTotalProgress()) {
					return;
				}

				transferToNextPipe(pipe, pos, facing);
				pipe.markDirty();
				setProgress(0);
			}
		}

		// External tiles
		List<BlockPos> externalTilesPos = tracker.getExternalTilesPos();

		if (!externalTilesPos.isEmpty()) {
			for (BlockPos pos : externalTilesPos) {
				TileEntity tile = world.getTileEntity(pos);

				if (tile == null) {
					tracker.removeExternalTilePos(pos);
					continue;
				}

				if (tile instanceof IPipe) {
					continue;
				}

				EnumFacing facing = getNeighborFacing(getPos(), pos);

				if (!pipeType.acceptTile(tile, facing)) {
					continue;
				}

				if (++progress < getTotalProgress()) {
					return;
				}

				if (canInput(pos, facing)) {
					transferInput(pos, facing);
				} else if (canOutput(pos, facing)) {
					transferOutput(pos, facing);
				}

				setProgress(0);
			}
		}

		markDirty();
	}

	/* IPipe */
	@Override
	public PipeTracker<C> getTracker() {
		return tracker;
	}

	@Override
	public boolean canInput(BlockPos neighbor, EnumFacing facing) {
		return false;
	}

	@Override
	public boolean canOutput(BlockPos neighbor, EnumFacing facing) {
		return false;
	}

	@Override
	public boolean canTransferToNextPipe(TileConnectedPipe<C> pipe, BlockPos neighbor, EnumFacing facing) {
		return false;
	}

	@Override
	public void transferInput(BlockPos neighbor, EnumFacing facing) {
	}

	@Override
	public void transferOutput(BlockPos neighbor, EnumFacing facing) {
	}

	@Override
	public void transferToNextPipe(TileConnectedPipe<C> pipe, BlockPos neighbor, EnumFacing facing) {
	}

	@Override
	public long getUpdateHash() {
		return Objects.hash(getType(), tracker);
	}

	@Override
	public int getProgress() {
		return progress;
	}

	@Override
	public void setProgress(int progress) {
		this.progress = progress;
	}

	@Override
	public int getTotalProgress() {
		return 0;
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

			if (tile instanceof IPipe) {
				TileConnectedPipe<C> pipe = (TileConnectedPipe<C>) tile;

				if (pipe.isTileInvalid() || !type.equals(pipe.getType())) {
					return;
				}

				if (pipe.getRepresentModule() == INPUT) {
					inputDirection = facing;
				} else if (pipe.inputDirection != null) {
					inputDirection = pipe.inputDirection.equals(facing) ? pipe.inputDirection
							: getNeighborFacing(getPos(), pos);
				}

				if (inputDirection == null || !facing.equals(inputDirection) || getRepresentModule() == INPUT) {
					tracker.addTrackDirection(facing);

					// Check for unorderly or unchecked adjacent pipe
					pipe.tracker.addTrackDirection(facing);
				}
			} else if (type.acceptTile(tile, facing)) {
				tracker.addExternalTilePos(pos);
			}
		}

		markDirty();
	}

	/* Helpers */
	private EnumFacing getNeighborFacing(BlockPos pos, BlockPos neighbor) {
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

	/* Object */
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
