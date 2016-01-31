package vaadin.commons.ui.components.view.entities;

public class SetterEntityInstantiator<T> implements EntityInstantiator<T>  {

    private T entity;

    @Override
    public T entity() {
        if(entity!=null){
            return entity;
        }
        return emptyEntity();
    }

    @Override
    public T emptyEntity() {
        return null;
    }

    @Override
    public void setEntity(T entity) {
        this.entity = entity;
    }
}
