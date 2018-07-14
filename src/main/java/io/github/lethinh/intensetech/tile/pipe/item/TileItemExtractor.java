/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.tile.pipe.item;

import io.github.lethinh.intensetech.inventory.container.ContainerItemExtractor;
import io.github.lethinh.intensetech.inventory.gui.GuiItemExtractor;
import io.github.lethinh.intensetech.tile.IGuiTile;
import io.github.lethinh.intensetech.tile.pipe.TileConnectedPipe;
import io.github.lethinh.intensetech.utils.InventoryUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.IItemHandler;

public class TileItemExtractor extends TileItemConnectedPipe implements ITickable, IGuiTile {

	public TileItemExtractor() {
		super(10);
	}

	/* IPipe */
	@Override
	public int getRepresentModule() {
		return INPUT;
	}

	@Override
	public boolean canTransferInput(BlockPos neighbor, EnumFacing facing) {
		return true;
	}

	@Override
	public boolean canTransferToNextPipe(TileConnectedPipe<Capability<IItemHandler>> pipe, BlockPos neighbor,
			EnumFacing facing) {
		IItemHandler dst = pipe.getCapability(getType().getType(), facing);
		return pipe.getRepresentModule() == NORMAL && !InventoryUtils.isFull(dst);
	}

	@Override
	public void transferInput(BlockPos neighbor, EnumFacing facing) {
		TileEntity tile = world.getTileEntity(neighbor);
		IItemHandler src = tile.getCapability(getType().getType(), facing);
		InventoryUtils.transferInventory(src, inventory);
	}

	@Override
	public void transferToNextPipe(TileConnectedPipe<Capability<IItemHandler>> pipe, BlockPos neighbor,
			EnumFacing facing) {
		IItemHandler dst = pipe.getCapability(getType().getType(), facing);
		InventoryUtils.transferInventory(inventory, dst);
	}

	/* IGuiTile */
	@Override
	public Object getContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new ContainerItemExtractor(player.inventory, this);
	}

	@Override
	public Object getGui(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new GuiItemExtractor(player.inventory, this);
	}

}
