package vaadin.commons.ui.components.container;

import java.util.List;

/**
 * Generic operations over the database to support views operations.
 *
 */
public interface ContainerDelegate {

	/**
	 * Selects from the clazz entity mapped table all the entities filtered in database
	 * with filters existing in the viewSettings, sorted from viewSettings, with page start
	 * and page number.
	 * @param viewSettings
	 * @param clazz
	 * @param <T>
	 * @return list of items.
	 */
	<T> List<T> getItems(ViewSetting viewSettings, Class<T> clazz);

	/**
	 * Obtains the total size of an select over a table mapped with the clazz,
	 * depending on filtering present in viewSettings. Used for calculating the page numbers.
	 * @param viewSettings
	 * @param clazz
	 * @param <T>
	 * @return the total count of items
	 */
	<T> int size(ViewSetting viewSettings, Class<T> clazz);

	/**
	 * Eagerly fetches all items from table mapped in clazz.
	 * @param clazz
	 * @param <T>
	 * @return all rows
	 */
	<T> List<T> getItems(Class<T> clazz);

	/**
	 * Inserts a new entity into the database.
	 * @param entity
	 * @param <T>
	 */
	<T> void addEntity(T entity);

	/**
	 * Attaches an entity back to the persistence context. Incidently, since the transaction is short
	 * spanned only over this method, coincides with update on the database.
	 * @param entity
	 * @param <T>
	 * @return the new entity updated.
	 */
	<T> T mergeEntity(T entity);

	/**
	 * Removes an entity from database.
	 * @param entity -  to be removed.
	 * @param <T>
	 */
	<T> void deleteEntity(T entity);
}
