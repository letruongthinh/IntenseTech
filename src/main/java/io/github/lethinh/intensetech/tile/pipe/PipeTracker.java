/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.tile.pipe;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import io.github.lethinh.intensetech.utils.Commons;
import io.github.lethinh.intensetech.utils.NBTUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
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

	public static final String NBT_TRACK_DIRECTIONS = "TrackDirs";
	public static final String NBT_EXTERNAL_TILES_POS = "TilesPos";

	private final TileConnectedPipe<C> pipe;
	private CopyOnWriteArrayList<EnumFacing> trackDirections;
	private CopyOnWriteArrayList<BlockPos> externalTilesPos;

	public PipeTracker(TileConnectedPipe<C> pipe) {
		this.pipe = pipe;
		this.trackDirections = new CopyOnWriteArrayList<>();
		this.externalTilesPos = new CopyOnWriteArrayList<>();
	}

	/* Tracking Helpers */
	/**
	 * @return pipes tracked in all {@code trackDirections}, contains no null pipes
	 */
	public List<TileConnectedPipe<C>> trackNextPipesAllDirections() {
		if (trackDirections.isEmpty()) {
			return Collections.EMPTY_LIST;
		}

		return trackDirections.stream().map(dir -> trackNextPipe(dir)).filter(pipe -> pipe != null)
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

		TileEntity tile = pipe.getWorld().getTileEntity(pipe.getPos().offset(dir));

		if (tile == null) {
			// removeTrackDirection(dir);
			return null;
		}

		if (!(tile instanceof IPipe)) {
			return null;
		}

		TileConnectedPipe<C> ret = (TileConnectedPipe<C>) tile;

		if (ret == null || ret.isTileInvalid() || !ret.getType().equals(pipe.getType()) || ret.equals(pipe)) {
			return null;
		}

		return ret;
	}

	public boolean addTrackDirection(EnumFacing trackDirection) {
		return Commons.addIfNotExists(trackDirections, trackDirection);
	}

	/**
	 * @param trackDirection The direction to be removed
	 * @return true if {@code dirToCheck} was matched and removed with any in
	 *         {@code trackDirections}, otherwise false
	 */
	public boolean removeTrackDirection(EnumFacing trackDirection) {
		return Commons.removeIfExists(trackDirections, trackDirection);
	}

	public boolean addExternalTilePos(BlockPos pos) {
		return Commons.addIfNotExists(externalTilesPos, pos);
	}

	public boolean removeExternalTilePos(BlockPos pos) {
		return Commons.removeIfExists(externalTilesPos, pos);
	}

	public void invalidate() {
		trackDirections.clear();
		externalTilesPos.clear();
	}

	/* INBTSerializable */
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();

		// Track directions
		if (!trackDirections.isEmpty()) {
			Integer[] tracksDirs = trackDirections.stream().map(EnumFacing::getIndex).toArray(Integer[]::new);
			NBTTagIntArray trackDirsTag = new NBTTagIntArray(Commons.toUnboxedIntArray(tracksDirs));
			nbt.setTag(NBT_TRACK_DIRECTIONS, trackDirsTag);
		}

		// External tiles
		if (!externalTilesPos.isEmpty()) {
			NBTTagList tilePosList = new NBTTagList();

			for (int i = 0; i < externalTilesPos.size(); ++i) {
				NBTTagCompound tilePosTag = new NBTTagCompound();
				tilePosTag.setInteger("TilePosIdx", i);
				NBTUtils.writeBlockPos(tilePosTag, "TilePos", externalTilesPos.get(i));
				tilePosList.appendTag(tilePosTag);
			}

			nbt.setTag(NBT_EXTERNAL_TILES_POS, tilePosList);
			nbt.setInteger(NBT_EXTERNAL_TILES_POS + "Size", externalTilesPos.size());
		}

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt == null || nbt.hasNoTags()) {
			return;
		}

		// Track directions
		int[] trackDirs = nbt.getIntArray(NBT_TRACK_DIRECTIONS);

		if (trackDirs.length != 0) {
			EnumFacing[] dirs = Arrays.stream(trackDirs).mapToObj(i -> EnumFacing.getFront(i))
					.toArray(EnumFacing[]::new);
			trackDirections = new CopyOnWriteArrayList<>(dirs);
		}

		// External tiles
		NBTTagList tilePosList = nbt.getTagList(NBT_EXTERNAL_TILES_POS, Constants.NBT.TAG_COMPOUND);

		if (!tilePosList.hasNoTags()) {
			externalTilesPos.clear();

			for (int i = 0; i < tilePosList.tagCount(); ++i) {
				NBTTagCompound tilePosTag = tilePosList.getCompoundTagAt(i);
				int idx = tilePosTag.getInteger("TilePosIdx");
				BlockPos pos = NBTUtils.readBlockPos(tilePosTag, "TilePos");
				externalTilesPos.add(idx, pos);
			}
		}
	}

	/* Object */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PipeTracker) {
			PipeTracker test = (PipeTracker) obj;
			return pipe.equals(test.pipe) && trackDirections.equals(test.trackDirections)
					&& externalTilesPos.equals(test.externalTilesPos);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(pipe, trackDirections, externalTilesPos);
	}

	/* Getters */
	public TileConnectedPipe<C> getPipe() {
		return pipe;
	}

	public CopyOnWriteArrayList<EnumFacing> getTrackDirections() {
		return trackDirections;
	}

	public CopyOnWriteArrayList<BlockPos> getExternalTilesPos() {
		return externalTilesPos;
	}

}
