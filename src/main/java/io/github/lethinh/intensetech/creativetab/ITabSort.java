/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.creativetab;

public interface ITabSort {

	/* Categories */
	int ITEM = 0;
	int MATERIAL_ITEM = 1;
	int TOOL = 2;
	int ENERGIZED = 3;
	int BLOCK = 4;
	int MATERIAL_BLOCK = 5;
	int TILE_BLOCK = 6;

	int getCategory();

}
