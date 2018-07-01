/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.tile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class TileBase extends TileEntity {

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		return new SPacketUpdateTileEntity(pos, -1, compound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	@Nonnull
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		return compound;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		IBlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 2);
	}

	/* Capability */
	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (facing != null) {
			if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
				IItemHandlerModifiable itemHandler = getItemHandler(facing);

				if (itemHandler != null) {
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandler);
				}
			} else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
				IFluidHandler fluidHandler = getFluidHandler(facing);

				if (fluidHandler != null) {
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(fluidHandler);
				}
			} else if (capability == CapabilityEnergy.ENERGY) {
				IEnergyStorage energyHandler = getEnergyHandler(facing);

				if (energyHandler != null) {
					return CapabilityEnergy.ENERGY.cast(energyHandler);
				}
			}
		}

		return super.getCapability(capability, facing);
	}

	public IItemHandlerModifiable getItemHandler(EnumFacing facing) {
		return null;
	}

	public IFluidHandler getFluidHandler(EnumFacing facing) {
		return null;
	}

	public IEnergyStorage getEnergyHandler(EnumFacing facing) {
		return null;
	}

	/* Validate Helper */
	public boolean isTileInvalid() {
		return !hasWorld() || isInvalid() || !world.isBlockLoaded(pos) || world.getTileEntity(pos) != this;
	}

	/* Reinforcements */
	@Override
	public boolean equals(Object o) {
		return o instanceof TileBase && ((TileBase) o).pos.equals(pos)
				&& ((TileBase) o).getWorld().provider.getDimensionType() == world.provider.getDimensionType();
	}

	@Override
	public int hashCode() {
		return 31 * pos.hashCode() + world.provider.getDimension();
	}

}
