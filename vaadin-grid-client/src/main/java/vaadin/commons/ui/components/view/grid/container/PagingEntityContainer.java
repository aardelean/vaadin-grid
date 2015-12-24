package vaadin.commons.ui.components.view.grid.container;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import vaadin.commons.ui.components.container.ContainerDelegate;
import vaadin.commons.ui.components.container.ViewSetting;
import vaadin.commons.ui.components.view.grid.pagination.PaginationLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PagingEntityContainer<T> extends BeanItemContainer<T> implements EntityContainerFacade<T>{

	private ContainerDelegate containerDelegate;
	private Object sortedPropertyId = null;
	private boolean asc = false;
	private Integer currentPage = 1;
	private Integer pageSize;
	private int totalSize;
	private Class<T> type;
	private PaginationLayout<T> paginationLayout;

	public PagingEntityContainer(Class<T> type,
	                             ContainerDelegate containerDelegate,
	                             boolean formOnly, int pageSize) throws IllegalArgumentException {
		super(type);
		this.type = type;
		this.pageSize = pageSize;
		this.containerDelegate = containerDelegate;
		if(!formOnly) {
			refresh();
		}
	}

	@Override
	public void refresh() {
		super.removeAllItems();
		totalSize = containerDelegate.size(buildViewSettings(), type);
		ViewSetting viewSetting = buildViewSettings();
		List<T> items = containerDelegate.getItems(viewSetting, type);
		super.addAll(items);
		if(paginationLayout!=null){
			paginationLayout.refresh();
		}
	}

	private ViewSetting buildViewSettings() {
		return ViewSetting.buildViewSettings(convertFilters(), sortedPropertyId, asc, currentPage, pageSize);
	}

	private Map<String, Object> convertFilters(){
		Map<String, Object> result = null;
		if(getFilters()!=null && !getFilters().isEmpty()){
			result = new HashMap<>();
			for(Container.Filter gridFilter: getFilters()){
				if(gridFilter.getClass().isAssignableFrom(SimpleStringFilter.class)){
					SimpleStringFilter simpleStringFilter = (SimpleStringFilter)gridFilter;
					if(!simpleStringFilter.getFilterString().isEmpty()) {
						result.put(simpleStringFilter.getPropertyId().toString(),
											  simpleStringFilter.getFilterString());
					}
				}
			}
		}
		return result;
	}

	@Override
	public void sort(Object[] propertyId, boolean[] ascending) {
		if(propertyId!=null && propertyId.length>0){
			sortedPropertyId = propertyId[0];
			asc = ascending[0];
			refresh();
		}
	}

	@Override
	protected void addFilter(Filter filter) throws UnsupportedFilterException {
		getFilters().add(filter);
		currentPageNo(1);
	}

	@Override
	public T save(T newEntity){
		T result = addEntity(newEntity);
		return result;
	}

	@Override
	public T mergeEntity(T entity) {
		return containerDelegate.mergeEntity(entity);
	}

	private T addEntity(T t) {
		containerDelegate.addEntity(t);
		super.addBean(t);
		return t;
	}

	@Override
	public void delete(T t){
		containerDelegate.deleteEntity(t);
	}

	@Override
	public void currentPageNo(Integer pageNo) {
		this.currentPage = pageNo;
		refresh();
	}

	@Override
	public void pageSize(Integer pageSize) {
		this.pageSize = pageSize;
		refresh();
	}

	@Override
	public Integer getPageSize() {
		return pageSize;
	}

	@Override
	public Integer getTotalSize() {
		return totalSize;
	}

	@Override
	public void setPaginationLayout(PaginationLayout<T> paginationLayout) {
		this.paginationLayout = paginationLayout;
	}

	@Override
	public Integer getCurrentPageNo() {
		return currentPage;
	}
}
