package vaadin.commons.ui.components.view.grid.container;

import com.vaadin.data.util.BeanItemContainer;
import vaadin.commons.ui.components.container.ContainerDelegate;
import vaadin.commons.ui.components.view.grid.pagination.PaginationLayout;

import java.util.List;

/**
 * @author aardelean
 *
 * Container which is used by eagerly fetching all entities into the cache.
 * This is straight-forward but puts pressure over the memory of the server.
 * All the additional operations are easy, filtering, sorting, exporting are done
 * from the cache of this bean.
 *
 **/
public class EagerEntityContainerFacade<T> extends BeanItemContainer<T> implements EntityContainerFacade<T> {

	private ContainerDelegate containerDelegate;

	public EagerEntityContainerFacade(Class clazz, ContainerDelegate containerDelegate,
	                                  boolean formOnly) {
		super(clazz);
		this.containerDelegate = containerDelegate;
		if(!formOnly) {
			populate();
		}
	}

	public void populate(){
		super.addAll((List<T>)containerDelegate.getItems(getBeanType()));
	}

	public void persistEntity(T t) {
		containerDelegate.addEntity(t);
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
		persistEntity(t);
		super.addBean(t);
		return t;
	}

	@Override
	public void delete(T t){
		containerDelegate.deleteEntity(t);
	}

	@Override
	public void refresh(){
		super.removeAllItems();
		populate();
	}
	//No pagination support for eager fetching all entities!
	@Override
	public void currentPageNo(Integer pageNo) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Integer getCurrentPageNo() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void pageSize(Integer pageSize) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Integer getPageSize() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Integer getTotalSize() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPaginationLayout(PaginationLayout<T> paginationLayout) {
		throw new UnsupportedOperationException();
	}


}