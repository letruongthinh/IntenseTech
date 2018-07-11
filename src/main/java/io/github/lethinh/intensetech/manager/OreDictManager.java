/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.manager;

import net.minecraftforge.oredict.OreDictionary;

public class OreDictManager {

	public static void registerOresDict() {
		OreDictionary.registerOre("ingotSilver", ItemsManager.SILVER_INGOT);
	}

}
