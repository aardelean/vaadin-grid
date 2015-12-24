package vaadin.commons.ui.components.view.grid;

import com.vaadin.ui.Grid;
import vaadin.commons.ui.components.view.grid.container.EntityContainerFacade;
import vaadin.commons.ui.components.view.grid.fields.EntityFieldGroup;

import java.util.ArrayList;
import java.util.List;

public class JPAGridBuilder<T> {
    private JPAGrid<T> grid = null;

    private EntityFieldGroup<T> entityFieldGroup;
    private double pageSize = 10d;
    /**
     * Multiple columns can derived from existing ones. Add all PropertyResolvers for each column not present in the entity
     * but present in the grid column definition.
     */
    private List<PropertyResolver> additionalPropertyResolvers = new ArrayList<>();
    private boolean editAllowed = false;
    private EntityContainerFacade<T> container = null;

    public JPAGridBuilder entityFieldGroup(EntityFieldGroup<T> entityFieldGroup) {
        this.entityFieldGroup = entityFieldGroup;
        return this;
    }

    public JPAGridBuilder pageSize(double pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public JPAGridBuilder additionalPropertyResolvers(List<PropertyResolver> additionalPropertyResolvers) {
        this.additionalPropertyResolvers = additionalPropertyResolvers;
        return this;
    }

    public JPAGridBuilder editAllowed(boolean editAllowed) {
        this.editAllowed = editAllowed;
        return this;
    }

    public JPAGridBuilder container(EntityContainerFacade<T> container){
        this.container = container;
        return this;
    }

    public JPAGrid<T> build(){
        assert entityFieldGroup !=null;
        assert container != null;
        JPAGrid<T> grid = new JPAGrid<T>(container, entityFieldGroup){
            @Override
            protected void gridCustomFilters(EntityContainerFacade<T> container) {
                JPAGridBuilder.this.customFilters(this, container);
            }
            @Override
            protected void customizeRenderer(Grid.Column column) {
                JPAGridBuilder.this.customizeRenderer(column);
            }
        };
        grid.setEditAllowed(editAllowed);
        grid.setViewPageSize(pageSize);
        grid.setAdditionalPropertyResolvers(additionalPropertyResolvers);
        return grid.init();
    }

    protected void customizeRenderer(Grid.Column column) {

    }

    protected void customFilters(JPAGrid<T> grid, EntityContainerFacade<T> container){

    }
}
