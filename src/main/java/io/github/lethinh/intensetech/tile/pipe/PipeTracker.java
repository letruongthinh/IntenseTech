/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.tile.pipe;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * This class is created for the ease of saving to/loading from NBT and used by
 * every pipe, due to saving/loading {@link TileEntity} from/to NBT is a very
 * big deal (recursion issue). And also for helping track the pipes with their
 * right {@link Capability}
 *
 * @param <C> The capability type
 */
public class PipeTracker<C extends Capability> implements INBTSerializable<NBTTagCompound> {

	private final TileConnectedPipe<C> pipe;
	private List<EnumFacing> trackDirections;

	public PipeTracker(TileConnectedPipe<C> pipe) {
		this(pipe, Lists.newArrayList());
	}

	public PipeTracker(TileConnectedPipe<C> pipe, List<EnumFacing> transferDirections) {
		this.pipe = pipe;
		this.trackDirections = transferDirections;
	}

	/* Track */
	/**
	 * @return pipes tracked in all {@code trackDirections}, contains no null pipes
	 */
	public List<TileConnectedPipe<C>> trackNextPipeAllDirections() {
		if (trackDirections.isEmpty()) {
			return Collections.EMPTY_LIST;
		}

		return trackDirections.stream().map(dir -> trackNextPipe(dir))
				.filter(pipe -> pipe != null)
				.collect(Collectors.toList());
	}

	/**
	 * @param dir The direction to track
	 * @return single tracked pipe by {@code facing} presented in
	 *         {@code trackDirections}
	 */
	public TileConnectedPipe<C> trackNextPipe(EnumFacing dir) {
		if (pipe.isTileInvalid() || trackDirections.isEmpty() || !trackDirections.contains(dir)) {
			return null;
		}

		TileConnectedPipe<C> ret = (TileConnectedPipe<C>) pipe.getWorld().getTileEntity(pipe.getPos().offset(dir));

		if (ret == null || ret.isTileInvalid() || !ret.getType().equals(pipe.getType()) || ret.equals(pipe)) {
			return null;
		}

		return ret;
	}

	/**
	 * @param dirToCheck The direction to be removed
	 * @return true if {@code dirToCheck} was matched and removed with any in
	 *         {@code trackDirections}, otherwise false
	 */
	public boolean removeTrackDirection(EnumFacing dirToCheck) {
		return !trackDirections.isEmpty() && trackDirections.removeIf(dir -> dir.equals(dirToCheck));
	}

	public void invalidate() {
		trackDirections.clear();
	}

	/* INBTSerializable */
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound compound = new NBTTagCompound();

		if (!trackDirections.isEmpty()) {
			NBTTagList pipePosTagList = new NBTTagList();

			for (int i = 0; i < trackDirections.size(); ++i) {
				EnumFacing dir = trackDirections.get(i);
				NBTTagCompound pipePosTag = new NBTTagCompound();
				pipePosTag.setInteger("PipeIdx", i);
				pipePosTag.setInteger("PipeDir", dir.getIndex());
				pipePosTagList.appendTag(pipePosTag);
			}

			compound.setTag("PipeDirs", pipePosTagList);
			compound.setInteger("PipeDirsSize", trackDirections.size());
		}

		return compound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt == null || nbt.hasNoTags()) {
			return;
		}

		NBTTagList pipePosTagList = nbt.getTagList("PipeDirs", Constants.NBT.TAG_COMPOUND);

		if (!pipePosTagList.hasNoTags()) {
			trackDirections = Lists.newArrayListWithCapacity(nbt.getInteger("PipeDirsSize"));

			for (int i = 0; i < pipePosTagList.tagCount(); ++i) {
				NBTTagCompound pipePosTag = pipePosTagList.getCompoundTagAt(i);
				int idx = pipePosTag.getInteger("PipeIdx");
				EnumFacing dir = EnumFacing.values()[pipePosTag.getInteger("PipeDir")];
				trackDirections.add(idx, dir);
			}
		}
	}

	/* Getters & Setters */
	public TileConnectedPipe<C> getPipe() {
		return pipe;
	}

	public List<EnumFacing> getTrackDirections() {
		return trackDirections;
	}

	public void setTrackDirections(List<EnumFacing> trackDirections) {
		this.trackDirections = trackDirections;
	}

	public void addTrackDirection(EnumFacing trackDirection) {
		if (trackDirections.contains(trackDirection)) {
			return;
		}

		trackDirections.add(trackDirection);
	}

}
