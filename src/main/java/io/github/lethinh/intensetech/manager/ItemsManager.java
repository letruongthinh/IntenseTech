/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.manager;

import java.util.List;

import com.google.common.collect.Lists;

import io.github.lethinh.intensetech.item.ItemBase;
import io.github.lethinh.intensetech.item.ItemSilverIngot;
import io.github.lethinh.intensetech.model.IItemModelRegister;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemsManager {

	public static final List<ItemBase> ITEMS = Lists.newArrayList();

	public static ItemSilverIngot silverIngot;

	public static void registerItems() {
		ITEMS.add(silverIngot = new ItemSilverIngot());

		MinecraftForge.EVENT_BUS.register(new RegistrationHandler());
		ITEMS.forEach(IItemModelRegister::registerItemModel);
	}

	public static class RegistrationHandler {
		@SubscribeEvent
		public void registerBlocks(RegistryEvent.Register<Item> registry) {
			ITEMS.forEach(registry.getRegistry()::register);
		}
	}

}
