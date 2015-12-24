package vaadin.commons.ui.components.view.grid.pagination;

import vaadin.commons.ui.components.view.grid.container.EntityContainerFacade;

public class NextPageButton<T> extends AbstractPageNumberButton<T> {

	private final static String NEXT_PAGE_LABEL = "NEXT";

	public NextPageButton(final PaginationLayout<T> parent){
		super(NEXT_PAGE_LABEL, parent.getMaxPageNumber(), parent);
	}

	@Override
	protected ClickListener changePageNo(Integer pageNo,final PaginationLayout<T> parent) {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				EntityContainerFacade<T> container = parent.getContainer();
				container.currentPageNo(container.getCurrentPageNo()+1);
				parent.refresh();
				container.refresh();
			}
		};
	}
}
