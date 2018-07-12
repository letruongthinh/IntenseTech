/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.model.definition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.github.lethinh.intensetech.model.VariantHelper;
import io.github.lethinh.intensetech.utils.ConstFunctionUtils;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.VariantList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.BlockStateLoader.SubModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Wrapper for {@link ModelBlockDefinition}. Represent for a "cube-all" box,
 * which means with the same textures on all sides of block. By implementing
 * this class, you can create your own blockstate or model without getting into
 * {@link BlockstateModel} nor JSONs
 */
@SideOnly(Side.CLIENT)
public class FlatBoxModelDefinition {

	private final ResourceLocation parent;
	private final Optional<IModelState> modelState;
	private final String texture; // Without extension (.png, .jpg, ...)
	private final boolean uvLock, smooth, gui3d;
	private final int weight;
	private final ImmutableMap<String, String> textures;
	private final ImmutableMap<String, SubModel> parts;
	private final ImmutableMap<String, String> customData;

	// Default "cube-all" block constructor
	public FlatBoxModelDefinition(String texture, boolean uvLock, boolean smooth, boolean gui3d) {
		this(new ResourceLocation("block/cube_all"),
				Optional.of(TRSRTransformation.blockCenterToCorner(
						VariantHelper.DEFAULT_BLOCK.apply(Optional.empty()).orElse(TRSRTransformation.identity()))),
				texture, uvLock, smooth, gui3d, 1,
				ImmutableMap.of("all", ConstFunctionUtils.prefixResourceLocation("blocks/" + texture).toString()),
				ImmutableMap.of(), ImmutableMap.of());
	}

	public FlatBoxModelDefinition(ResourceLocation parent, Optional<IModelState> modelState, String texture,
			boolean uvLock, boolean smooth, boolean gui3d, int weight, ImmutableMap<String, String> textures,
			ImmutableMap<String, SubModel> parts, ImmutableMap<String, String> customData) {
		this.parent = parent;
		this.modelState = modelState;
		this.texture = texture;
		this.uvLock = uvLock;
		this.smooth = smooth;
		this.gui3d = gui3d;
		this.weight = weight;
		this.textures = textures;
		this.parts = parts;
		this.customData = customData;
	}

	public ModelBlockDefinition parse() {
		ResourceLocation blockstateLocation = ConstFunctionUtils
				.prefixResourceLocation("blockstates/" + texture + ".json");
		List<Variant> mcVars = Lists.newArrayList();
		Map<String, VariantList> variants = Maps.newLinkedHashMap();

		try {
			Class<?> clazz = Class.forName("net.minecraftforge.client.model.BlockStateLoader$ForgeVariant");
			Constructor<?> constructor = ReflectionHelper.findConstructor(clazz, ResourceLocation.class,
					ResourceLocation.class, IModelState.class, boolean.class, boolean.class, boolean.class, int.class,
					ImmutableMap.class, ImmutableMap.class, ImmutableMap.class);
			Variant var = (Variant) constructor.newInstance(blockstateLocation, parent,
					modelState.orElse(TRSRTransformation.identity()), uvLock, smooth, gui3d, weight, textures, parts,
					customData);

			mcVars.add(var);
			variants.put("normal", new VariantList(mcVars));
			variants.put("inventory", new VariantList(mcVars));
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}

		return new ModelBlockDefinition(variants, null);
	}

	public VariantList getVariantList(String variant) {
		return parse().getVariant(variant);
	}

	/* Getters */
	public ResourceLocation getParent() {
		return parent;
	}

	public Optional<IModelState> getModelState() {
		return modelState;
	}

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

	public int getWeight() {
		return weight;
	}

	public ImmutableMap<String, String> getTextures() {
		return textures;
	}

	public ImmutableMap<String, SubModel> getParts() {
		return parts;
	}

	public ImmutableMap<String, String> getCustomData() {
		return customData;
	}

}
