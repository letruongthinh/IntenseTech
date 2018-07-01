/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech;

import io.github.lethinh.intensetech.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = IntenseTech.MOD_ID, name = IntenseTech.NAME, version = IntenseTech.VERSION, acceptedMinecraftVersions = "[1.12, 1.13)")
public class IntenseTech {

	public static final String NAME = "Intense Tech";
	public static final String MOD_ID = "intensetech";
	public static final String VERSION = "1.0.0";

	@Instance
	public static IntenseTech instance;

	@SidedProxy(serverSide = "io.github.lethinh.intensetech.proxy.CommonProxy", clientSide = "io.github.lethinh.intensetech.proxy.ClientProxy")
	public static CommonProxy proxy;

	public static CreativeTab tab;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		instance = this;
		tab = new CreativeTab();

		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
	}

}
