/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.inventory.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import io.github.lethinh.intensetech.inventory.container.ContainerBase;
import io.github.lethinh.intensetech.inventory.widget.Widget;
import io.github.lethinh.intensetech.tile.TileBase;
import io.github.lethinh.intensetech.utils.ConstFunctionUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBase<TE extends TileBase, C extends ContainerBase<TE>> extends GuiContainer {

	public static final ResourceLocation WIDGETS_LOC = ConstFunctionUtils
			.prefixResourceLocation("textures/gui/widgets.png");

	private final String guiName;
	private final List<Widget> widgets;

	public GuiBase(C inventorySlotsIn, String guiName) {
		super(inventorySlotsIn);
		this.guiName = guiName;
		this.widgets = new ArrayList<>();
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

		// Draw widgets
		widgets.forEach(widget -> widget.renderBackground(this, mouseX, mouseY, partialTicks));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		widgets.forEach(widget -> widget.renderForeground(this, mouseX, mouseY));
	}

	/* Widget */
	public List<Widget> getWidgets() {
		return widgets;
	}

	public boolean addWidget(Widget widget) {
		return widgets.add(widget);
	}

	/* Getter */
	public String getGuiName() {
		return guiName;
	}

	/* Helpers */
	public void drawTexturedModalRect(double x, double y, double textureX, double textureY, double width,
			double height) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();

		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x, y + height, zLevel).tex(textureX * f, (textureY + height) * f1).endVertex();
		bufferbuilder.pos(x + width, y + height, zLevel).tex((textureX + width) * f, (textureY + height) * f1)
				.endVertex();
		bufferbuilder.pos(x + width, y, zLevel).tex((textureX + width) * f, textureY * f1).endVertex();
		bufferbuilder.pos(x, y, zLevel).tex(textureX * f, textureY * f1).endVertex();
		tessellator.draw();
	}

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
