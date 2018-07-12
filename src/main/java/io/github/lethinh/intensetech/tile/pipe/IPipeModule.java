/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.tile.pipe;

import net.minecraftforge.common.capabilities.Capability;

public interface IPipeModule<C extends Capability> {

	/* Represent Modules */
	int NORMAL = 0;
	int INPUT = 1;
	int OUTPUT = 2;

	PipeType<C> getType();

	int getRepresentModule();

	PipeTracker<C> getTracker();

}
