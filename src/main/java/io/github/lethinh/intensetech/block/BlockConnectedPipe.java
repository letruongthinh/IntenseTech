/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.block;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;

import io.github.lethinh.intensetech.manager.ModelBakeHandler;
import io.github.lethinh.intensetech.model.StandardModelStates;
import io.github.lethinh.intensetech.model.property.FlatBoxModelProperties;
import io.github.lethinh.intensetech.tile.pipe.TileConnectedPipe;
import io.github.lethinh.intensetech.utils.ConstFunctionUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.BlockStateLoader.SubModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelStateComposition;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockConnectedPipe<TE extends TileConnectedPipe<C>, C extends Capability>
		extends BlockTileBase<TE> {

	public static final PropertyBool[] DIRECTIONS = Arrays.stream(EnumFacing.VALUES)
			.map(facing -> PropertyBool.create(facing.getName())).toArray(PropertyBool[]::new);
	public static final AxisAlignedBB[] BOUNDING_BOXES = Arrays.stream(EnumFacing.VALUES).map(facing -> {
		Vec3i directionVec = facing.getDirectionVec();
		return new AxisAlignedBB(getMinBound(directionVec.getX()), getMinBound(directionVec.getY()),
				getMinBound(directionVec.getZ()), getMaxBound(directionVec.getX()), getMaxBound(directionVec.getY()),
				getMaxBound(directionVec.getZ()));
	}).toArray(AxisAlignedBB[]::new);

	private static final float PIPE_MIN_POS = 0.25F;
	private static final float PIPE_MAX_POS = 0.75F;

	private static float getMinBound(int dir) {
		return dir == -1 ? 0 : PIPE_MIN_POS;
	}

	private static float getMaxBound(int dir) {
		return dir == 1 ? 1 : PIPE_MAX_POS;
	}

	public BlockConnectedPipe(String name, Material materialIn) {
		super(name, materialIn);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState) {
		AxisAlignedBB normalBox = new AxisAlignedBB(PIPE_MIN_POS, PIPE_MIN_POS, PIPE_MIN_POS, PIPE_MAX_POS,
				PIPE_MAX_POS, PIPE_MAX_POS);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, normalBox);

		if (!isActualState) {
			state = state.getActualState(worldIn, pos);
		}

		for (EnumFacing facing : EnumFacing.values()) {
			if (state.getValue(DIRECTIONS[facing.getIndex()])) {
				addCollisionBoxToList(pos, entityBox, collidingBoxes, BOUNDING_BOXES[facing.getIndex()]);
			}
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		state = getActualState(state, source, pos);
		return BOUNDING_BOXES[getBoundingBoxIndex(state)];
	}

	private int getBoundingBoxIndex(IBlockState state) {
		int i = 0;

		for (EnumFacing facing : EnumFacing.VALUES) {
			if (state.getValue(DIRECTIONS[facing.getIndex()])) {
				i |= 1 << facing.getHorizontalIndex();
			}
		}

		return i;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		BlockPos neighborPos = pos.offset(facing);
		IBlockState neighborState = world.getBlockState(neighborPos);
		Block neighborBlock = neighborState.getBlock();

		if (neighborBlock.hasTileEntity(neighborState)) {
			TE tile = (TE) world.getTileEntity(pos);
			TileEntity neighborTile = world.getTileEntity(neighborPos);

			boolean connectPipe = neighborBlock instanceof BlockConnectedPipe
					&& ((TE) neighborTile).getType().equals(tile.getType())
					&& neighborBlock.canBeConnectedTo(world, pos, facing);
			boolean connectExternalTile = tile.getType().acceptTile(neighborTile, facing);

			return !neighborTile.isInvalid() && (connectPipe || connectExternalTile);
		}

		return super.canBeConnectedTo(world, neighborPos, facing);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
			EnumFacing side) {
		return true;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, DIRECTIONS);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		for (EnumFacing facing : EnumFacing.values()) {
			return state.withProperty(DIRECTIONS[facing.getIndex()], canBeConnectedTo(worldIn, pos, facing));
		}

		return state;
	}

	/* IBlockModelProperties */
	@SideOnly(Side.CLIENT)
	@Override
	public FlatBoxModelProperties getBoxProperties(ModelLoader modelLoader) {
		// Main model
		ResourceLocation blockstateLocation = ConstFunctionUtils
				.prefixResourceLocation("blockstates/" + getName() + ".json");
		Optional<IModelState> modelState = Optional.of(TRSRTransformation.blockCenterToCorner(
				StandardModelStates.DEFAULT_BLOCK.apply(Optional.empty())
						.orElse(TRSRTransformation.identity())));

		ImmutableMap.Builder<String, String> texturesBuilder = ImmutableMap.builder();
		texturesBuilder.put("centre", "minecraft:blocks/glass");
		texturesBuilder.put("side", "minecraft:blocks/glass");
		ImmutableMap<String, String> textures = texturesBuilder.build();

		// Sub models
		ImmutableMap.Builder<String, SubModel> parts = ImmutableMap.builder();

		for (PropertyBool dir : DIRECTIONS) {
			if (!getBlockState().getBaseState().getValue(dir)) {
				continue;
			}

			int x = 0, y = 0;

			switch (dir.getName().toLowerCase()) {
			case "down":
				x = 90;
				y = 0;
				break;
			case "up":
				x = -90;
				y = 0;
				break;
			case "north":
				break;
			case "south":
				x = 0;
				y = 180;
				break;
			case "east":
				x = 0;
				y = 90;
				break;
			case "west":
				x = 0;
				y = -90;
				break;
			default:
				x = 0;
				y = 0;
				break;
			}

			ModelRotation rotation = ModelRotation.getModelRotation(x, y);
			IModelState state = new ModelStateComposition(modelState.orElse(TRSRTransformation.identity()), rotation);
			SubModel subModel = new SubModel(state, isUvLock(), isSmooth(), isGui3d(), ImmutableMap.of(),
					ModelBakeHandler.PIPE_NORMAL_LOC, ImmutableMap.of());
			parts.put(dir.getName().toLowerCase(), subModel);
		}

		FlatBoxModelProperties properties = new FlatBoxModelProperties(ModelBakeHandler.PIPE_PART_LOC,
				blockstateLocation, modelState, getTexture(), isUvLock(), isSmooth(), isGui3d(), 1, textures,
				parts.build(), ImmutableMap.of());

		return properties;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean isUvLock() {
		return true;
	}

}
