/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.inventory;

import io.github.lethinh.intensetech.api.provider.IGuiTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	public static final int GUI_ID = 0;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == GUI_ID) {
			TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

			if (tile instanceof IGuiTile) {
				return ((IGuiTile) tile).getContainer(ID, player, world, x, y, z);
			}
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == GUI_ID) {
			TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

			if (tile instanceof IGuiTile) {
				return ((IGuiTile) tile).getGui(ID, player, world, x, y, z);
			}
		}

		return null;
	}

}
