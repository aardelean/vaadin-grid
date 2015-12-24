package vaadin.commons.ui.components.view.grid.pagination;

import com.vaadin.ui.Button;
import vaadin.commons.ui.components.view.grid.container.EntityContainerFacade;

public class PreviousPageButton<T> extends AbstractPageNumberButton<T> {

	private final static String PREVIOUS_PAGE_LABEL = "PREVIOUS";

	public PreviousPageButton(final PaginationLayout<T> parent){
		super(PREVIOUS_PAGE_LABEL, 1, parent);
		setEnabled(false);
	}

	@Override
	protected Button.ClickListener changePageNo(Integer pageNo, final PaginationLayout<T> parent) {
		return new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				EntityContainerFacade<T> container = parent.getContainer();
				container.currentPageNo(parent.getContainer().getCurrentPageNo()-1);
				parent.refresh();
				container.refresh();
			}
		};
	}
}
