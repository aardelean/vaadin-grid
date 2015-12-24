package vaadin.commons.ui.components.view.buttons;

public interface DeleteListener<T> {

	void beforeDelete(T entity);

	void afterDelete(T entity);
}
