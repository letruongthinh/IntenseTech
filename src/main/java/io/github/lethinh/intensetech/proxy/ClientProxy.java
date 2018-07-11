/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.proxy;

import io.github.lethinh.intensetech.IntenseTech;
import io.github.lethinh.intensetech.manager.BlocksManager;
import io.github.lethinh.intensetech.manager.GuiHandler;
import io.github.lethinh.intensetech.manager.ItemsManager;
import io.github.lethinh.intensetech.manager.OreDictManager;
import io.github.lethinh.intensetech.manager.RecipesManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit() {
		MinecraftForge.EVENT_BUS.register(new ItemsManager.RegistrationHandler());
		MinecraftForge.EVENT_BUS.register(new BlocksManager.RegistrationHandler());
	}

	@Override
	public void init() {
		RecipesManager.registerRecipes();
		OreDictManager.registerOresDict();
		NetworkRegistry.INSTANCE.registerGuiHandler(IntenseTech.instance, new GuiHandler());
	}

}
