/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.api.provider;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IGuiTile {

	Object getContainer(int ID, EntityPlayer player, World world, int x, int y, int z);

	Object getGui(int ID, EntityPlayer player, World world, int x, int y, int z);

}
