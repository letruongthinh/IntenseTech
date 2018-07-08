/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.inventory.gui;

import io.github.lethinh.intensetech.inventory.container.ContainerBase;
import io.github.lethinh.intensetech.tile.TileBase;
import io.github.lethinh.intensetech.utils.ConstFunctionUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBase<TE extends TileBase, C extends ContainerBase<TE>> extends GuiContainer {

	private final String guiName;

	public GuiBase(C inventorySlotsIn, String guiName) {
		super(inventorySlotsIn);
		this.guiName = guiName;
		setGuiContainerSize(GuiContainerSize.DEFAULT);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1F, 1F, 1F);

		// Bind texture
		mc.getTextureManager().bindTexture(
				ConstFunctionUtils.prefixResourceLocation("textures/gui/" + guiName.toLowerCase() + ".png"));

		// Draw texture
		int width = (this.width - getXSize()) / 2, height = (this.height - getYSize()) / 2;
		this.drawTexturedModalRect(width, height, 0, 0, getXSize(), getYSize());
	}

	/* Getter */
	public String getGuiName() {
		return guiName;
	}

	/* Helper */
	/**
	 * Portable method for setting gui container size.
	 */
	protected void setGuiContainerSize(GuiContainerSize size) {
		this.setGuiContainerSize(size.getXSize(), size.getYSize());
	}

	protected void setGuiContainerSize(int xSize, int ySize) {
		this.xSize = xSize;
		this.ySize = ySize;
	}

	// Some ordinary gui container sizes
	enum GuiContainerSize {
		DEFAULT(176, 166),
		MEDIUM(186, 186),
		MEDIUM_X(186, 176),
		MEDIUM_Y(176, 186),
		LARGE(276, 276),
		LARGE_X(276, 266),
		LARGE_Y(266, 276);

		private final int xSize;
		private final int ySize;

		GuiContainerSize(int xSize, int ySize) {
			this.xSize = xSize;
			this.ySize = ySize;
		}

		public int getXSize() {
			return xSize;
		}

		public int getYSize() {
			return ySize;
		}
	}

}
