/**
* Created by Le Thinh
*/

package io.github.lethinh.intensetech.creativetab;

public interface ITabSort {

	CreativeTabCategory getTabCategory();

	/* Creative Tab Category */
	enum CreativeTabCategory {
		ITEM(0),
		MATERIAL_ITEM(1),
		TOOL(2),
		ENERGIZED(3),
		BLOCK(4),
		MATERIAL_BLOCK(5),
		TILE_BLOCK(6);

		private int categoryIndex;

		CreativeTabCategory(int categoryIndex) {
			this.categoryIndex = categoryIndex;
		}

		public int getCategoryIndex() {
			return categoryIndex;
		}
	}

}
