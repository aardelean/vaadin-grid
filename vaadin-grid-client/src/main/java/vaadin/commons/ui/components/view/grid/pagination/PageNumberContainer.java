package vaadin.commons.ui.components.view.grid.pagination;

import com.vaadin.ui.HorizontalLayout;

public class PageNumberContainer extends HorizontalLayout {
	private AbstractPageNumberButton button;

	public PageNumberContainer(AbstractPageNumberButton button) {
		this.button = button;
		setMargin(true);
		addComponent(button);
	}

	public void checkEnable() {
		button.checkEnable();
	}
}
