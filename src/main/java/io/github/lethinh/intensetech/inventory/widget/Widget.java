/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.inventory.widget;

import io.github.lethinh.intensetech.inventory.gui.GuiBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class Widget {

	private final double x, y, width, height;

	public Widget(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public abstract void renderBackground(GuiBase parent, int mouseX, int mouseY, float partialTicks);

	public void renderForeground(GuiBase parent, int mouseX, int mouseY) {

	}

	/* Getters */
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

}
