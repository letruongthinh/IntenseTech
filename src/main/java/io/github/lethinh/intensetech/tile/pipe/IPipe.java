/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.tile.pipe;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

public interface IPipe<C extends Capability> {

	/* Represent Modules */
	int NORMAL = 0;
	int INPUT = 1;

	/* Properties */
	PipeType<C> getType();

	int getRepresentModule();

	PipeTracker<C> getTracker();

	/* Transfer */
	boolean canInput(BlockPos neighbor, EnumFacing facing);

	boolean canOutput(BlockPos neighbor, EnumFacing facing);

	boolean canTransferToNextPipe(TileConnectedPipe<C> pipe, BlockPos neighbor, EnumFacing facing);

	void transferInput(BlockPos neighbor, EnumFacing facing);

	void transferOutput(BlockPos neighbor, EnumFacing facing);

	void transferToNextPipe(TileConnectedPipe<C> pipe, BlockPos neighbor, EnumFacing facing);

	/* Update */
	long getUpdateHash();

	int getProgress();

	void setProgress(int progress);

	int getTotalProgress();

}
