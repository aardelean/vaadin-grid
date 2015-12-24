package vaadin.commons.ui.components.view.grid.pagination;

import com.vaadin.data.Property;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Slider;
import com.vaadin.ui.VerticalLayout;
import vaadin.commons.ui.components.view.grid.container.EntityContainerFacade;

import java.util.ArrayList;
import java.util.List;

public class PaginationLayout<T> extends VerticalLayout{

	private EntityContainerFacade<T> container;
	private List<PageNumberContainer> numberButtons = new ArrayList<>();
	private Integer maxPageNumber = 0;
	private static final Integer MAX_SHOWN_TOTAL_NO_BUTTONS = 5;
	private HorizontalLayout pageNumbersLayout;

	public PaginationLayout(EntityContainerFacade<T> container) {
		this.container = container;
		container.setPaginationLayout(this);
		setMargin(true);
		refresh();
	}

	public void refresh(){
		removeAllComponents();
		this.pageNumbersLayout = new HorizontalLayout();
		Integer totalSize = container.getTotalSize();
		maxPageNumber = new Double(Math.ceil(totalSize /(double) container.getPageSize())).intValue();
		if(maxPageNumber>1) {
			addButton(new PreviousPageButton<T>(this));
			int startPageNumberButton = Math.min(container.getCurrentPageNo() - 2, maxPageNumber - MAX_SHOWN_TOTAL_NO_BUTTONS + 1);
			Integer firstButtonPage = Math.max(1, startPageNumberButton);
			int lastButtonPage = firstButtonPage + MAX_SHOWN_TOTAL_NO_BUTTONS - 1;
			Integer maxShownButtons = Math.min(maxPageNumber, lastButtonPage);
			for (int index = firstButtonPage; index < maxShownButtons + 1; index++) {
				addButton(new PageNumberButton<T>(index, this));
			}
			addButton(new NextPageButton<T>(this));
			this.addComponent(pageNumbersLayout);
			addSlider(maxPageNumber);
			checkEnable();
		}
	}

	private void addSlider(Integer maxPageNumber) {
		if(maxPageNumber>MAX_SHOWN_TOTAL_NO_BUTTONS) {
			Slider slider = new Slider("Page Numbers", 1, maxPageNumber);
			slider.setValue(container.getCurrentPageNo().doubleValue());
			slider.setWidth(325, Unit.PIXELS);
			slider.addValueChangeListener(new Property.ValueChangeListener() {
				@Override
				public void valueChange(Property.ValueChangeEvent event) {
					container.currentPageNo(((Double) event.getProperty().getValue()).intValue());
				}
			});
			slider.focus();
			this.addComponent(slider);
		}
	}

	private void checkEnable(){
		for(PageNumberContainer pageNumber: numberButtons){
			pageNumber.checkEnable();
		}
	}

	private void addButton(AbstractPageNumberButton<T> button) {
		PageNumberContainer pageNumberContainer = new PageNumberContainer(button);
		numberButtons.add(pageNumberContainer);
		pageNumbersLayout.addComponent(pageNumberContainer);
	}

	public List<PageNumberContainer> getPageNumberButtons(){
		return numberButtons;
	}

	public EntityContainerFacade<T> getContainer() {
		return container;
	}

	public Integer getMaxPageNumber() {
		return maxPageNumber;
	}
}
