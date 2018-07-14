/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.tile.pipe;

import java.util.List;

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

	public TileConnectedPipe() {
		tracker = new PipeTracker<>(this);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		tracker.deserializeNBT(compound.getCompoundTag("TrackData"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("TrackData", tracker.serializeNBT());
		return super.writeToNBT(compound);
	}

	@Override
	public void invalidate() {
		super.invalidate();
		tracker.invalidate();
	}

	/* ITickable */
	@Override
	public void update() {
		PipeType<C> pipeType = getType();
		C type = pipeType.getType();

		// Pipes
		List<TileConnectedPipe<C>> pipes = tracker.trackNextPipeAllDirections();

		if (!pipes.isEmpty()) {
			for (TileConnectedPipe<C> pipe : pipes) {
				if (pipe.isTileInvalid()) {
					continue;
				}

				BlockPos pos = pipe.getPos();
				EnumFacing facing = getNeighborFacing(getPos(), pos);

				if (!canTransferToNextPipe(pipe, pos, facing)) {
					continue;
				}

				transferToNextPipe(pipe, pos, facing);
				pipe.markDirty();
			}
		}

		// External tiles
		List<BlockPos> externalTilesPos = tracker.getExternalTilesPos();

		if (!externalTilesPos.isEmpty()) {
			for (BlockPos pos : externalTilesPos) {
				TileEntity tile = world.getTileEntity(pos);
				EnumFacing facing = getNeighborFacing(getPos(), pos);

				if (canTransferInput(pos, facing)) {
					transferInput(pos, facing);
					tile.markDirty();
				} else if (canTransferOutput(pos, facing)) {
					transferOutput(pos, facing);
					tile.markDirty();
				}
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
	public boolean canTransferInput(BlockPos neighbor, EnumFacing facing) {
		return false;
	}

	@Override
	public boolean canTransferOutput(BlockPos neighbor, EnumFacing facing) {
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

			if (tile instanceof IPipe) {
				TileConnectedPipe<C> pipe = (TileConnectedPipe<C>) tile;

				if (pipe.isTileInvalid() || !type.equals(pipe.getType())) {
					return;
				}

				if (pipe.getRepresentModule() == INPUT) {
					inputDirection = facing.getOpposite();
				} else if (pipe.inputDirection != null) {
					BlockPos checkPos = pipe.getPos().offset(inputDirection);
					TileEntity checkTile = world.getTileEntity(checkPos);

					if (checkTile == null) {
						for (EnumFacing checkSide : EnumFacing.values()) {
							if (inputDirection.equals(checkSide)) {
								continue;
							}
						}
					} else {
						inputDirection = pipe.inputDirection;
					}
				}

				tracker.addTrackDirection(facing);
			} else if (type.acceptTile(tile, facing)) {
				tracker.addExternalTilePos(pos);
			}
		}

		markDirty();

	}

	/* Helpers */
	public static EnumFacing getNeighborFacing(BlockPos pos, BlockPos neighbor) {
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
