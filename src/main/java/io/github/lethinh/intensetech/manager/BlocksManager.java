/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.manager;

import java.util.List;

import com.google.common.collect.Lists;

import io.github.lethinh.intensetech.block.BlockAtomicAssembler;
import io.github.lethinh.intensetech.block.BlockBase;
import io.github.lethinh.intensetech.block.BlockItemExtractor;
import io.github.lethinh.intensetech.block.BlockItemReceiver;
import io.github.lethinh.intensetech.block.BlockTransferPipe;
import io.github.lethinh.intensetech.tile.TileAtomicAssembler;
import io.github.lethinh.intensetech.tile.pipe.item.TileItemExtractor;
import io.github.lethinh.intensetech.tile.pipe.item.TileItemReceiver;
import io.github.lethinh.intensetech.tile.pipe.item.TileItemTransferPipe;
import io.github.lethinh.intensetech.utils.ConstFunctionUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlocksManager {

	public static final List<BlockBase> BLOCKS = Lists.newArrayList();

	public static BlockItemExtractor itemExtractor;
	public static BlockTransferPipe transferPipe;
	public static BlockItemReceiver itemReceiver;
	public static BlockAtomicAssembler atomicAssembler;

	public static void registerBlocks() {
		BLOCKS.add(itemExtractor = new BlockItemExtractor());
		BLOCKS.add(transferPipe = new BlockTransferPipe());
		BLOCKS.add(itemReceiver = new BlockItemReceiver());
		BLOCKS.add(atomicAssembler = new BlockAtomicAssembler());

		MinecraftForge.EVENT_BUS.register(new RegistrationHandler());
	}

	public static class RegistrationHandler {
		@SubscribeEvent
		public void registerBlocks(RegistryEvent.Register<Block> registry) {
			BLOCKS.forEach(registry.getRegistry()::register);

			registerTileEntity(TileItemExtractor.class, "item_extractor");
			registerTileEntity(TileItemTransferPipe.class, "item_transfer_pipe");
			registerTileEntity(TileItemReceiver.class, "item_receiver");
			registerTileEntity(TileAtomicAssembler.class, "atomic_assembler");
		}

		@SubscribeEvent
		public void registerItemBlocks(RegistryEvent.Register<Item> registry) {
			BLOCKS.stream().map(block -> new ItemBlock(block))
					.forEach(itemBlock -> registry.getRegistry()
							.register(itemBlock.setRegistryName(itemBlock.getBlock().getRegistryName())));
		}

		private void registerTileEntity(Class<? extends TileEntity> clazz, String name) {
			GameRegistry.registerTileEntity(clazz, ConstFunctionUtils.prefixResourceLocation(name));
		}
	}

}
