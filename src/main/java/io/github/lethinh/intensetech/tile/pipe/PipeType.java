/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.tile.pipe;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class PipeType<C extends Capability> {

	/* Default types */
	public static final PipeType<Capability<IItemHandler>> ITEM = new PipeType<>("Item",
			CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
	public static final PipeType<Capability<IFluidHandler>> FLUID = new PipeType<>("Fluid",
			CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);

	private final String name;
	private final C type;

	public PipeType(String name, C type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public C getType() {
		return type;
	}

	/* Helpers */
	/**
	 * Check if the external tile entity (not instance-of {@link TileConnectedPipe})
	 * can be used with the pipe
	 *
	 * @param tile   The {@link TileEntity} going to be checked
	 * @param facing The current side of the tile, when passes to this argument, it
	 *               will become reversed (opposite)
	 * @return
	 */
	public boolean acceptTile(TileEntity tile, EnumFacing facing) {
		// Example:
		// [1][2]
		// Let say I want to check if the 2nd block can be used with the pipe.
		// So if I obtained the 2nd tile entity from the 1st one, to check the 2nd one,
		// the facing is going to be reversed (opposite) of the 1st one
		return !tile.isInvalid() && tile.hasCapability(type, facing.getOpposite());
	}

	/* Object */
	@Override
	public boolean equals(Object obj) {
		return obj instanceof PipeType
				? ((PipeType) obj).name.equalsIgnoreCase(name) && ((PipeType) obj).type.equals(type)
				: super.equals(obj);
	}

}
