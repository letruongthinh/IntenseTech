/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.model;

import java.util.EnumMap;

import javax.vecmath.Vector3f;

import com.google.common.collect.ImmutableMap;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// TRSR = Translation Rotation Scale Rotation
@SideOnly(Side.CLIENT)
public class StandardModelStates {

	private static TRSRTransformation get(float tx, float ty, float tz, float ax, float ay, float az, float s) {
		return TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
				new Vector3f(tx / 16, ty / 16, tz / 16),
				TRSRTransformation.quatFromXYZDegrees(new Vector3f(ax, ay, az)),
				new Vector3f(s, s, s),
				null));
	}

	private static final TRSRTransformation flipX = new TRSRTransformation(null, null, new Vector3f(-1, 1, 1), null);

	private static TRSRTransformation leftify(TRSRTransformation transform) {
		return TRSRTransformation
				.blockCenterToCorner(flipX.compose(TRSRTransformation.blockCornerToCenter(transform)).compose(flipX));
	}

	public static final IModelState DEFAULT_BLOCK;
	public static final IModelState DEFAULT_ITEM;
	public static final IModelState DEFAULT_TOOL;

	static {
		// block/block (forge:default-block)
		{
			EnumMap<TransformType, TRSRTransformation> map = new EnumMap<>(TransformType.class);
			TRSRTransformation thirdperson = get(0, 2.5F, 0, 75, 45, 0, 0.375F);
			map.put(TransformType.GUI, get(0, 0, 0, 30, 225, 0, 0.625F));
			map.put(TransformType.GROUND, get(0, 3, 0, 0, 0, 0, 0.25F));
			map.put(TransformType.FIXED, get(0, 0, 0, 0, 0, 0, 0.5F));
			map.put(TransformType.THIRD_PERSON_RIGHT_HAND, thirdperson);
			map.put(TransformType.THIRD_PERSON_LEFT_HAND, leftify(thirdperson));
			map.put(TransformType.FIRST_PERSON_RIGHT_HAND, get(0, 0, 0, 0, 45, 0, 0.4F));
			map.put(TransformType.FIRST_PERSON_LEFT_HAND, get(0, 0, 0, 0, 225, 0, 0.4F));
			DEFAULT_BLOCK = new SimpleModelState(ImmutableMap.copyOf(map));
		}

		// item/generated (forge:default-item)
		{
			EnumMap<TransformType, TRSRTransformation> map = new EnumMap<>(TransformType.class);
			TRSRTransformation thirdperson = get(0, 3, 1, 0, 0, 0, 0.55F);
			TRSRTransformation firstperson = get(1.13F, 3.2F, 1.13F, 0, -90, 25, 0.68F);
			map.put(TransformType.GROUND, get(0, 2, 0, 0, 0, 0, 0.5F));
			map.put(TransformType.HEAD, get(0, 13, 7, 0, 180, 0, 1));
			map.put(TransformType.THIRD_PERSON_RIGHT_HAND, thirdperson);
			map.put(TransformType.THIRD_PERSON_LEFT_HAND, leftify(thirdperson));
			map.put(TransformType.FIRST_PERSON_RIGHT_HAND, firstperson);
			map.put(TransformType.FIRST_PERSON_LEFT_HAND, leftify(firstperson));
			map.put(TransformType.FIXED, get(0, 0, 0, 0, 180, 0, 1));
			DEFAULT_ITEM = new SimpleModelState(ImmutableMap.copyOf(map));
		}

		// item/handheld (forge:default-tool)
		{
			EnumMap<TransformType, TRSRTransformation> map = new EnumMap<>(TransformType.class);
			map.put(TransformType.GROUND, get(0, 2, 0, 0, 0, 0, 0.5f));
			map.put(TransformType.HEAD, get(0, 13, 7, 0, 180, 0, 1));
			map.put(TransformType.THIRD_PERSON_RIGHT_HAND, get(0, 4, 0.5F, 0, -90, 55, 0.85F));
			map.put(TransformType.THIRD_PERSON_LEFT_HAND, get(0, 4, 0.5F, 0, 90, -55, 0.85F));
			map.put(TransformType.FIRST_PERSON_RIGHT_HAND, get(1.13F, 3.2F, 1.13F, 0, -90, 25, 0.68F));
			map.put(TransformType.FIRST_PERSON_LEFT_HAND, get(1.13F, 3.2F, 1.13F, 0, 90, -25, 0.68F));
			map.put(TransformType.FIXED, get(0, 0, 0, 0, 180, 0, 1));
			DEFAULT_TOOL = new SimpleModelState(ImmutableMap.copyOf(map));
		}
	}

}
