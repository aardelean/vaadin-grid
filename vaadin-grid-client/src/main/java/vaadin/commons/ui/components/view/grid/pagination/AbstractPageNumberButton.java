package vaadin.commons.ui.components.view.grid.pagination;

import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ChameleonTheme;

public class AbstractPageNumberButton<T> extends Button {

	protected final PaginationLayout<T> parent;
	private final Integer pageNo;

	public AbstractPageNumberButton(String caption,final Integer pageNo, final PaginationLayout<T> parent) {
		super(caption);
		this.parent = parent;
		this.pageNo = pageNo;
		setStyleName(ChameleonTheme.BUTTON_LINK);
		addClickListener(changePageNo(pageNo, parent));
	}

	protected ClickListener changePageNo(final Integer pageNo, final PaginationLayout<T> parent) {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				parent.getContainer().currentPageNo(pageNo);
				parent.refresh();
				AbstractPageNumberButton.this.setEnabled(false);
			}
		};
	}

	public void checkEnable(){
		if(parent.getContainer().getCurrentPageNo().equals(pageNo)){
			this.setEnabled(false);
		}else{
			this.setEnabled(true);
		}
	}
}
