package vaadin.commons.ui.components.view.entities;


public class ReflectionEntityInstantiator<T> extends SetterEntityInstantiator<T> {

    private Class<T> type;
    public ReflectionEntityInstantiator(Class<T> clazz){
        type = clazz;
    }

    @Override
    public T emptyEntity() {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("could not instantiate entity: " + type);
        }
    }
}
