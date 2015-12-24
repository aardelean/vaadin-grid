package vaadin.commons.ui.components.view.grid.pagination;

public class PageNumberButton<T> extends AbstractPageNumberButton<T> {

	public PageNumberButton(Integer pageNumber, PaginationLayout<T> parent){
		super(Integer.toString(pageNumber), pageNumber, parent);
	}
}
