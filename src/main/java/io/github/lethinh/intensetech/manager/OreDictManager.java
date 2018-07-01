/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.manager;

import net.minecraftforge.oredict.OreDictionary;

public class OreDictManager {

	public static void registerOreDicts() {
		OreDictionary.registerOre("ingotSilver", ItemsManager.silverIngot);
	}

}
