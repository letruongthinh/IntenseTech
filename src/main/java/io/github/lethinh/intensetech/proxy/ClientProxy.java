/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.proxy;

import io.github.lethinh.intensetech.IntenseTech;
import io.github.lethinh.intensetech.inventory.GuiHandler;
import io.github.lethinh.intensetech.manager.BlocksManager;
import io.github.lethinh.intensetech.manager.ItemsManager;
import io.github.lethinh.intensetech.manager.OreDictManager;
import io.github.lethinh.intensetech.manager.RecipesManager;
import io.github.lethinh.intensetech.model.ModelBakeHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit() {
		ItemsManager.registerItems();
		BlocksManager.registerBlocks();
		MinecraftForge.EVENT_BUS.register(new ModelBakeHandler());
		OreDictManager.registerOreDicts();
	}

	@Override
	public void init() {
		RecipesManager.registerRecipes();
		NetworkRegistry.INSTANCE.registerGuiHandler(IntenseTech.instance, new GuiHandler());
	}

}
