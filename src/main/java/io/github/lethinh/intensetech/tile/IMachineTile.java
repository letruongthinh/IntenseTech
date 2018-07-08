/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.tile;

import net.minecraft.tileentity.TileEntity;

/**
 * Marker for {@link TileEntity} with automatic tasks and delay (machine)
 */
public interface IMachineTile {

	boolean isActive();

	void setActive(boolean active);

	int getWorkCycles();

	int getTotalWorkCycles();

	void setWorkCycles(int workCycles);

	void resetWorkCycles();

	default boolean cyclesRunFinished() {
		return getWorkCycles() >= getTotalWorkCycles();
	}

	/**
	 * Called while work is idle or work cycles are increasing
	 */
	void onWorkIdle();

	/**
	 * Determine if the machine can work
	 *
	 * @return true if it can
	 */
	boolean canWork();

	void doWork();

}
