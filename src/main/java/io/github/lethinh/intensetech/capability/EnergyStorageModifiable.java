/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.EnergyStorage;

public class EnergyStorageModifiable extends EnergyStorage implements IEnergyStorageModifiable {

	public EnergyStorageModifiable(int capacity) {
		super(capacity);
	}

	public EnergyStorageModifiable(int capacity, int maxTransfer) {
		super(capacity, maxTransfer);
	}

	public EnergyStorageModifiable(int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
	}

	public EnergyStorageModifiable(int capacity, int maxReceive, int maxExtract, int energy) {
		super(capacity, maxReceive, maxExtract, energy);
	}

	/* IEnergyStorageModifiable */
	@Override
	public void setEnergyStored(int energy) {
		this.energy = energy;

		if (getEnergyStored() > getMaxEnergyStored()) {
			this.energy = getMaxEnergyStored();
		}
	}

	@Override
	public void modifyEnergyStored(int energy) {
		this.energy += energy;

		if (getEnergyStored() > getMaxEnergyStored()) {
			this.energy = getMaxEnergyStored();
		}
	}

	@Override
	public void decrEnergyStored(int energy) {
		this.energy -= energy;

		if (getEnergyStored() > getMaxEnergyStored()) {
			this.energy = getMaxEnergyStored();
		}
	}

	/* NBT */
	public void readFromNBT(NBTTagCompound compound) {
		setEnergyStored(compound.getInteger("Energy"));

		if (getEnergyStored() > getMaxEnergyStored()) {
			setEnergyStored(getMaxEnergyStored());
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("Energy", getEnergyStored());
		return compound;
	}

}
