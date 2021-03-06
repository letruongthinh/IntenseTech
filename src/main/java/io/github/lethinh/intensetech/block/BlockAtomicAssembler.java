/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.block;

import io.github.lethinh.intensetech.tile.TileAtomicAssembler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.IBlockAccess;

public class BlockAtomicAssembler extends BlockTileBase<TileAtomicAssembler> {

	public BlockAtomicAssembler() {
		super("atomic_assembler", Material.IRON);
		setHardness(10F);
	}

	@Override
	public TileAtomicAssembler createTileEntity(IBlockAccess world, IBlockState state) {
		return new TileAtomicAssembler();
	}

}
