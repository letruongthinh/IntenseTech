/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.block;

import javax.annotation.Nullable;

import io.github.lethinh.intensetech.IntenseTech;
import io.github.lethinh.intensetech.manager.GuiHandler;
import io.github.lethinh.intensetech.tile.TileBase;
import io.github.lethinh.intensetech.tile.TileInventoryBase;
import io.github.lethinh.intensetech.utils.InventoryUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class BlockTileBase<TE extends TileBase> extends BlockBase {

	public BlockTileBase(String name, Material materialIn) {
		super(name, materialIn);
	}

	/* Block */
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		TileEntity tile = world.getTileEntity(pos);

		if (!(tile instanceof TileBase)) {
			return;
		}

		TileBase tileBase = (TileBase) tile;

		if (!tileBase.isTileInvalid()) {
			tileBase.onNeighborTileChange(neighbor);
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		TileEntity tile = worldIn.getTileEntity(pos);

		if (!(tile instanceof TileBase)) {
			return;
		}

		TileBase tileBase = (TileBase) tile;

		if (!tileBase.isTileInvalid()) {
			tileBase.onNeighborBlockChange();
		}
	}

	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, SpawnPlacementType type) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.isSneaking() || worldIn.getTileEntity(pos) instanceof TileBase
				&& ((TileBase) worldIn.getTileEntity(pos)).isTileInvalid()) {
			return false;
		}

		// Still open the gui if the player is playing on server
		if (worldIn.isRemote) {
			return true;
		}

		playerIn.openGui(IntenseTech.instance, GuiHandler.GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (!player.capabilities.isCreativeMode) {
			dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.destroyBlock(pos, true);
		}
	}

	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
		TileEntity tile = worldIn.getTileEntity(pos);
		return tile == null ? super.eventReceived(state, worldIn, pos, id, param) : tile.receiveClientEvent(id, param);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) {
		TileEntity tile = world.getTileEntity(pos);

		if (tile == null) {
			return;
		}

		if (!tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)) {
			return;
		}

		IItemHandler inventory = ((TileInventoryBase) tile).inventory;
		NonNullList<ItemStack> stacks = InventoryUtils.getStacks(inventory);
		NBTTagCompound stackTag = new NBTTagCompound();
		stackTag.setTag("BlockEntityTag", ItemStackHelper.saveAllItems(new NBTTagCompound(), stacks));
		ItemStack drop = new ItemStack(Item.getItemFromBlock(this), 1, damageDropped(state));
		drop.setTagCompound(stackTag);
		drops.add(drop);
	}

	/* ITabSort */
	@Override
	public int getCategory() {
		return TILE_BLOCK;
	}

	/* Tile Stuffs */
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return createTileEntity((IBlockAccess) world, state);
	}

	public abstract TE createTileEntity(IBlockAccess world, IBlockState state);

}
