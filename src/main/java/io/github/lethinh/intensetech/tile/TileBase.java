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
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

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
		notifyBlockUpdate();
	}

	/* Capability */
	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (facing != null) {
			if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
				IItemHandler itemHandler = getItemHandler(facing);

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

	@Nullable
	public IItemHandler getItemHandler(@Nullable EnumFacing facing) {
		return null;
	}

	@Nullable
	public IFluidHandler getFluidHandler(@Nullable EnumFacing facing) {
		return null;
	}

	@Nullable
	public IEnergyStorage getEnergyHandler(@Nullable EnumFacing facing) {
		return null;
	}

	/* Helpers */
	public boolean isTileInvalid() {
		return !hasWorld() || isInvalid() || !world.isBlockLoaded(pos) || world.getTileEntity(pos) != this;
	}

	protected void notifyBlockUpdate() {
		IBlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 3);
	}

	/* Object */
	@Override
	public boolean equals(Object o) {
		return o instanceof TileEntity ? ((TileEntity) o).getPos().equals(pos)
				&& ((TileEntity) o).getWorld().provider.getDimensionType() == world.provider.getDimensionType()
				: super.equals(o);
	}

	@Override
	public int hashCode() {
		return 31 * pos.hashCode() + world.provider.getDimension();
	}

	/* Block Impl */
	public void onNeighborTileChange(BlockPos neighbor) {
	}

	public void onNeighborBlockChange() {
	}

}
