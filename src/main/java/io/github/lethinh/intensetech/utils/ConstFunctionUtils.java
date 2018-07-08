/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.utils;

import java.text.NumberFormat;

import javax.annotation.Nonnegative;

import io.github.lethinh.intensetech.IntenseTech;
import io.github.lethinh.intensetech.block.BlockTileBase;
import net.minecraft.util.ResourceLocation;

public final class ConstFunctionUtils {

	private ConstFunctionUtils() {

	}

	/* Constants */
	public static final ResourceLocation EMPTY_RESOURCE_LOCATION = prefixResourceLocation("");

	/* String Format Helper */
	/**
	 * Transforms from the block's unlocalized name to tile's registry name.
	 */
	public static String prefixTileRegistryName(BlockTileBase tileBlock) {
		return tileBlock.getUnlocalizedName().replaceAll("tile.", "").replaceAll(".name", "")
				.replaceAll("block", "tile").replace(".", "_");
	}

	public static String prefixNBTData(String nbt) {
		return !nbt.isEmpty() ? IntenseTech.MOD_ID + nbt : "";
	}

	public static String prefixRFEnergy(@Nonnegative double energy, double capacity) {
		return prefixNumber(energy) + "/" + prefixNumber(capacity) + " RF";
	}

	/**
	 * Format number
	 *
	 * @param d The number will be formatted.
	 * @return the formatted number.
	 */
	public static String prefixNumber(double d) {
		NumberFormat format = NumberFormat.getInstance();

		try {
			return format.format(d);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String prefixTooltip(String itemName, Object tooltip) {
		return "tooltip." + IntenseTech.MOD_ID + "." + itemName + "." + tooltip;
	}

	/* Resource Location Helper */
	public static ResourceLocation prefixResourceLocation(String location) {
		return new ResourceLocation(IntenseTech.MOD_ID, location);
	}

	/**
	 * Prefix the texture's name to make sure it is in the right texture folder.
	 * Must be called in Pre-Init.
	 *
	 * @param textureName The texture's name of the texture will be prefixed.
	 * @return the prefixed texture's name.
	 */
	public static ResourceLocation prefixTextureLocation(String textureName) {
		if (textureName.startsWith("item_")) {
			return prefixResourceLocation("items/" + textureName);
		} else if (textureName.startsWith("block_")) {
			return prefixResourceLocation("blocks/" + textureName);
		}

		return prefixResourceLocation(textureName);
	}

}
