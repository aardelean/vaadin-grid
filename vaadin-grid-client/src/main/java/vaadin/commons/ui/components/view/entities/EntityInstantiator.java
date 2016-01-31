package vaadin.commons.ui.components.view.entities;

public interface EntityInstantiator<T> {
    T entity();
    T emptyEntity();
    void setEntity(T entity);
}
