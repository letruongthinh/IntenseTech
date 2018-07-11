/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.manager;

import java.util.List;

import com.google.common.collect.Lists;

import io.github.lethinh.intensetech.IntenseTech;
import io.github.lethinh.intensetech.block.BlockAtomicAssembler;
import io.github.lethinh.intensetech.block.BlockBase;
import io.github.lethinh.intensetech.block.BlockItemExtractor;
import io.github.lethinh.intensetech.block.BlockItemReceiver;
import io.github.lethinh.intensetech.block.BlockItemTransferPipe;
import io.github.lethinh.intensetech.tile.TileAtomicAssembler;
import io.github.lethinh.intensetech.tile.TileBase;
import io.github.lethinh.intensetech.tile.pipe.item.TileItemExtractor;
import io.github.lethinh.intensetech.tile.pipe.item.TileItemReceiver;
import io.github.lethinh.intensetech.tile.pipe.item.TileItemTransferPipe;
import io.github.lethinh.intensetech.utils.ConstFunctionUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(IntenseTech.MOD_ID)
public class BlocksManager {

	public static final BlockItemExtractor ITEM_EXTRACTOR = null;

	public static final BlockItemTransferPipe ITEM_TRANSFER_PIPE = null;

	public static final BlockItemReceiver ITEM_RECEIVER = null;

	public static final BlockAtomicAssembler ATOMIC_ASSEMBLER = null;

	public static class RegistrationHandler {
		public static final List<BlockBase> BLOCKS = Lists.newArrayList();

		@SubscribeEvent
		public void registerBlocks(RegistryEvent.Register<Block> event) {
			// Register blocks
			BLOCKS.add(new BlockItemExtractor());
			BLOCKS.add(new BlockItemTransferPipe());
			BLOCKS.add(new BlockItemReceiver());
			BLOCKS.add(new BlockAtomicAssembler());

			BLOCKS.forEach(event.getRegistry()::register);

			// Register tile entities
			registerTileEntities();
		}

		@SubscribeEvent
		public void registerItemBlocks(RegistryEvent.Register<Item> event) {
			BLOCKS.stream().map(block -> new ItemBlock(block)).forEach(itemBlock -> event.getRegistry()
					.register(itemBlock.setRegistryName(itemBlock.getBlock().getRegistryName())));
		}

		private void registerTileEntities() {
			registerTileEntity(TileItemExtractor.class, "item_extractor");
			registerTileEntity(TileItemTransferPipe.class, "item_transfer_pipe");
			registerTileEntity(TileItemReceiver.class, "item_receiver");
			registerTileEntity(TileAtomicAssembler.class, "atomic_assembler");
		}

		private void registerTileEntity(Class<? extends TileBase> clazz, String name) {
			GameRegistry.registerTileEntity(clazz, ConstFunctionUtils.prefixResourceLocation(name));
		}
	}

}
