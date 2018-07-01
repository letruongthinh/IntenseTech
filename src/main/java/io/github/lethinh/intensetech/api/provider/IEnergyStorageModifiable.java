/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.api.provider;

import net.minecraftforge.energy.IEnergyStorage;

public interface IEnergyStorageModifiable extends IEnergyStorage {

	void setEnergyStored(int energy);

	void modifyEnergyStored(int energy);

	void decrEnergyStored(int energy);

}
