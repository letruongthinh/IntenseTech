/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.item;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSilverIngot extends ItemBase {

	public ItemSilverIngot() {
		super("silver_ingot");
	}

	/* IItemModelProperties */
	@SideOnly(Side.CLIENT)
	@Override
	public String[] getTextures() {
		return new String[] { "silver_ingot" };
	}

}
