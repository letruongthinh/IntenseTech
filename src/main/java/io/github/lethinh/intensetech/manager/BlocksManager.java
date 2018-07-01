/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.manager;

import java.util.List;

import com.google.common.collect.Lists;

import io.github.lethinh.intensetech.api.utils.ConstFunctionUtils;
import io.github.lethinh.intensetech.block.BlockBase;
import io.github.lethinh.intensetech.block.crafter.BlockAtomicAssembler;
import io.github.lethinh.intensetech.block.transfer.BlockTransferNode;
import io.github.lethinh.intensetech.tile.crafter.TileAtomicAssembler;
import io.github.lethinh.intensetech.tile.transfer.TileTransferNode;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class BlocksManager {

	public static final List<BlockBase> BLOCKS = Lists.newArrayList();

	public static BlockTransferNode transferNode;
	public static BlockAtomicAssembler atomicAssembler;

	public static void registerBlocks() {
		BLOCKS.add(transferNode = new BlockTransferNode());
		BLOCKS.add(atomicAssembler = new BlockAtomicAssembler());

		MinecraftForge.EVENT_BUS.register(new RegistrationHandler());
	}

	public static class RegistrationHandler {
		@SubscribeEvent
		public void registerBlocks(RegistryEvent.Register<Block> registry) {
			BLOCKS.forEach(registry.getRegistry()::register);

			registerTileEntity(TileTransferNode.class, "transfer_node");
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
