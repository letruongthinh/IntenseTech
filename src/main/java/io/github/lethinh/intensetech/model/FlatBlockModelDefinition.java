/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.github.lethinh.intensetech.api.utils.ConstFunctionUtils;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.VariantList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.BlockStateLoader.SubModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

/**
 * Simple wrapper for {@link ModelBlockDefinition}
 */
public class FlatBlockModelDefinition {

	private final String texture;
	private final boolean uvLock, smooth, gui3d;

	public FlatBlockModelDefinition(String texture) {
		this(texture, false, false, true);
	}

	public FlatBlockModelDefinition(String texture, boolean uvLock, boolean smooth, boolean gui3d) {
		this.texture = texture;
		this.uvLock = uvLock;
		this.smooth = smooth;
		this.gui3d = gui3d;
	}

	public ModelBlockDefinition getDefinition() {
		ResourceLocation blockstateLocation = ConstFunctionUtils
				.prefixResourceLocation("blockstates/" + texture + ".json");
		ResourceLocation parent = new ResourceLocation("block/cube_all");

		Optional<IModelState> state = Optional
				.of(TRSRTransformation.blockCenterToCorner(
						VariantHelper.DEFAULT_BLOCK.apply(Optional.empty()).orElse(TRSRTransformation.identity())));
		List<Variant> mcVars = Lists.newArrayList();
		Map<String, VariantList> variants = Maps.newLinkedHashMap();
		int weight = 1;

		try {
			Class<?> clazz = Class.forName("net.minecraftforge.client.model.BlockStateLoader$ForgeVariant");
			Constructor<?> constructor = clazz.getDeclaredConstructor(ResourceLocation.class,
					ResourceLocation.class, IModelState.class, boolean.class, boolean.class, boolean.class, int.class,
					ImmutableMap.class, ImmutableMap.class, ImmutableMap.class);
			constructor.setAccessible(true);
			Object var = constructor.newInstance(
					blockstateLocation, parent,
					state.orElse(TRSRTransformation.identity()), uvLock, smooth, gui3d, weight,
					ImmutableMap.<String, String>of("all",
							ConstFunctionUtils.prefixResourceLocation("blocks/" + texture).toString()),
					ImmutableMap.<String, SubModel>of(), ImmutableMap.<String, String>of());
			mcVars.add((Variant) var);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}

		variants.put("normal", new VariantList(mcVars));
		variants.put("inventory", new VariantList(mcVars));

		return new ModelBlockDefinition(variants, null);
	}

	/* Getters */
	public String getTexture() {
		return texture;
	}

	public boolean isUvLock() {
		return uvLock;
	}

	public boolean isSmooth() {
		return smooth;
	}

	public boolean isGui3d() {
		return gui3d;
	}

}
