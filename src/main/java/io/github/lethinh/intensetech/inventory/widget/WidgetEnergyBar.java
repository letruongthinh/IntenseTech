/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.inventory.widget;

import org.lwjgl.opengl.GL11;

import io.github.lethinh.intensetech.inventory.gui.GuiBase;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WidgetEnergyBar extends Widget {

	private final IEnergyStorage energyStorage;

	public WidgetEnergyBar(double x, double y, IEnergyStorage energyStorage) {
		super(x, y, 130, 19);
		this.energyStorage = energyStorage;
	}

	@Override
	public void renderBackground(GuiBase parent, int mouseX, int mouseY, float partialTicks) {
		parent.mc.getTextureManager().bindTexture(GuiBase.WIDGETS_LOC);
		parent.drawTexturedModalRect(parent.getGuiLeft() + getX(), parent.getGuiTop() + getY(), 1, 1, getWidth(),
				getHeight());

//		if (energyStorage.getEnergyStored() > 0D) {
		double energyScaled = energyStorage.getEnergyStored() * energyStorage.getMaxEnergyStored() / getWidth();
		drawTexturedModalRect(parent.getGuiLeft() + getX(), parent.getGuiTop() + getY(), 1, 9,
				getWidth(), getHeight(), energyScaled);
//		}
	}

	public void drawTexturedModalRect(double x, double y, double textureX, double textureY, double width,
			double height, double scale) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();

		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x + scale, y + height, 0D).tex((textureX + scale) * f, (textureY + height) * f1).endVertex();
		bufferbuilder.pos(x + width, y + height, 0D).tex((textureX + width) * f, (textureY + height) * f1).endVertex();
		bufferbuilder.pos(x + width, y + scale, 0D).tex((textureX + width) * f, (textureY + scale) * f1).endVertex();
		bufferbuilder.pos(x + scale, y + scale, 0D).tex((textureX + scale) * f, (textureY + scale) * f1).endVertex();
		tessellator.draw();
	}

	/* Getters */
	public IEnergyStorage getEnergyStorage() {
		return energyStorage;
	}

}
