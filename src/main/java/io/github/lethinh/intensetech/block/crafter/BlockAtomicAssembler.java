/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.block.crafter;

import javax.annotation.Nullable;

import io.github.lethinh.intensetech.block.BlockTileBase;
import io.github.lethinh.intensetech.tile.crafter.TileAtomicAssembler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAtomicAssembler extends BlockTileBase {

	public BlockAtomicAssembler() {
		super("atomic_assembler", Material.IRON);
		setHardness(10F);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileAtomicAssembler();
	}

}
