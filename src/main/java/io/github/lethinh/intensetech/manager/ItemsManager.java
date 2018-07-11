/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.manager;

import java.util.List;

import com.google.common.collect.Lists;

import io.github.lethinh.intensetech.IntenseTech;
import io.github.lethinh.intensetech.item.ItemBase;
import io.github.lethinh.intensetech.item.ItemSilverIngot;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(IntenseTech.MOD_ID)
public class ItemsManager {

	public static final ItemSilverIngot SILVER_INGOT = null;

	public static class RegistrationHandler {
		public static final List<ItemBase> ITEMS = Lists.newArrayList();

		@SubscribeEvent
		public void registerBlocks(RegistryEvent.Register<Item> event) {
			ITEMS.add(new ItemSilverIngot());

			ITEMS.forEach(event.getRegistry()::register);
		}
	}

}
