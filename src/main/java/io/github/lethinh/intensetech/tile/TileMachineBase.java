/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.tile;

import io.github.lethinh.intensetech.capability.EnergyStorageModifiable;
import io.github.lethinh.intensetech.capability.IEnergyStorageModifiable;
import io.github.lethinh.intensetech.capability.TileItemHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class TileMachineBase<I extends TileItemHandler> extends TileInventoryBase<I>
		implements IEnergyStorageModifiable, IMachineTile, ITickable, IGuiTile {

	private final EnergyStorageModifiable energyStorage;
	private boolean active = false;
	private int workCycles = 0;

	public TileMachineBase(I inventory, int capacity) {
		super(inventory);
		energyStorage = new EnergyStorageModifiable(capacity);
	}

	public TileMachineBase(I inventory, int capacity, int maxTransfer) {
		super(inventory);
		energyStorage = new EnergyStorageModifiable(capacity, maxTransfer);
	}

	public TileMachineBase(I inventory, int capacity, int maxReceive, int maxExtract) {
		super(inventory);
		energyStorage = new EnergyStorageModifiable(capacity, maxReceive, maxExtract);
	}

	public TileMachineBase(I inventory, int capacity, int maxReceive, int maxExtract, int energy) {
		super(inventory);
		energyStorage = new EnergyStorageModifiable(capacity, maxReceive, maxExtract, energy);
	}

	public TileMachineBase(int size, int capacity) {
		super(size);
		energyStorage = new EnergyStorageModifiable(capacity);
	}

	public TileMachineBase(int size, int capacity, int maxTransfer) {
		super(size);
		energyStorage = new EnergyStorageModifiable(capacity, maxTransfer);
	}

	public TileMachineBase(int size, int capacity, int maxReceive, int maxExtract) {
		super(size);
		energyStorage = new EnergyStorageModifiable(capacity, maxReceive, maxExtract);
	}

	public TileMachineBase(int size, int capacity, int maxReceive, int maxExtract, int energy) {
		super(size);
		energyStorage = new EnergyStorageModifiable(capacity, maxReceive, maxExtract, energy);
	}

	/* ITickable */
	@Override
	public void update() {
		if (!canWork()) {
			return;
		}

		if (++workCycles >= getTotalWorkCycles()) {
			if (isActive()) {
				return;
			}

			setActive(true);
			doWork();
			extractEnergy(getRequiredEnergy());
			resetWorkCycles();
			markDirty(); // Mark dirty so chunk won't skip it
		} else if (isActive()) {
			setActive(false);
			onWorkIdle();
			markDirty(); // This's too
		}
	}

	/* Work */
	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public int getWorkCycles() {
		return workCycles;
	}

	@Override
	public void setWorkCycles(int workCycles) {
		this.workCycles = workCycles;
	}

	@Override
	public void resetWorkCycles() {
		workCycles = 0;
	}

	@Override
	public void onWorkIdle() {
	}

	@Override
	public boolean canWork() {
		return hasEnoughEnergyToRun(getRequiredEnergy());
	}

	/* Energy */
	public abstract int getRequiredEnergy();

	/* TileBase */
	@Override
	public IEnergyStorage getEnergyHandler(EnumFacing facing) {
		return energyStorage;
	}

	public EnergyStorageModifiable getEnergyStorage() {
		return energyStorage;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		energyStorage.readFromNBT(compound);
		workCycles = compound.getInteger("WorkCycles");
		active = compound.getBoolean("Active");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		energyStorage.writeToNBT(compound);
		compound.setInteger("WorkCycles", workCycles);
		compound.setBoolean("Active", active);
		return super.writeToNBT(compound);
	}

	/* IEnergyStorage */
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return energyStorage.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return energyStorage.extractEnergy(maxExtract, simulate);
	}

	@Override
	public int getEnergyStored() {
		return energyStorage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored() {
		return energyStorage.getMaxEnergyStored();
	}

	@Override
	public boolean canExtract() {
		return energyStorage.canExtract();
	}

	@Override
	public boolean canReceive() {
		return energyStorage.canReceive();
	}

	/* IEnergyStorageModifiable */
	@Override
	public void setEnergyStored(int energy) {
		energyStorage.setEnergyStored(energy);
	}

	@Override
	public void modifyEnergyStored(int energy) {
		energyStorage.modifyEnergyStored(energy);
	}

	@Override
	public void decrEnergyStored(int energy) {
		energyStorage.decrEnergyStored(energy);
	}

	/* Helper */
	public int extractEnergy(int maxExtract) {
		return extractEnergy(maxExtract, false);
	}

	public boolean hasEnoughEnergyToRun(int energy) {
		return getEnergyStored() >= energy;
	}

}
