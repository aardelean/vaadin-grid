package vaadin.commons.ui.components.view.grid.container;

import com.vaadin.data.Container;
import vaadin.commons.ui.components.view.grid.pagination.PaginationLayout;

/**
 * @author aardelean
 * Facade class to delegate to the backend ejb/service the search and persisting operations so
 * the web vaadin application wouldn't be aware of the datasource/persistence to the database.
 * This container is meant to manage the entities operations over the database in a generic way.
 * On top of the Indexed and Filterable, which come from Vaadin framework, there are present also
 * some CRUD operations which can be invoked and will delegate a session bean with the entity as
 * parameter.
 *
 * @see PagingEntityContainer
 * @see EagerEntityContainerFacade
 * @param <T> - entity type for the container.
 */
public interface EntityContainerFacade<T> extends Container.Indexed, Container.Filterable{

	/**
	 * Refreshes the full container with all the components inside. Most meaningful,
	 * should remove all components from local cache and fetch them through a
	 * session bean again from database.
	 */
	void refresh();

	/**
	 * Business logic to decide if merge entity or persist.
	 * @param entity - to be persisted
	 * @return the persisted entity.
	 */
	T save(T entity);

	/**
	 * Business logic to decide if merge entity or persist.
	 * @param entity - to be persisted
	 * @return the persisted entity.
	 */
	T mergeEntity(T entity);

	/**
	 * Delegates a session bean to remove this entity from database
	 * @param t to be deleted
	 */
	void delete(T t);

	void currentPageNo(Integer pageNo);

	Integer getCurrentPageNo();

	void pageSize(Integer pageSize);

	Integer getPageSize();

	Integer getTotalSize();

	void setPaginationLayout(PaginationLayout<T> paginationLayout);
}
