package vaadin.commons.ui.components.view;

import com.vaadin.data.fieldgroup.FieldGroup;
import vaadin.commons.ui.components.container.ContainerDelegate;
import vaadin.commons.ui.components.view.auditing.AuditDeleteListener;
import vaadin.commons.ui.components.view.buttons.DeleteListener;
import vaadin.commons.ui.components.view.entities.EntityInstantiator;
import vaadin.commons.ui.components.view.entities.ReflectionEntityInstantiator;
import vaadin.commons.ui.components.view.form.FormEvent;
import vaadin.commons.ui.components.view.form.ValidationEvent;
import vaadin.commons.ui.components.view.grid.PropertyResolver;
import vaadin.commons.ui.components.view.grid.container.EagerEntityContainerFacade;
import vaadin.commons.ui.components.view.grid.container.EntityContainerFacade;
import vaadin.commons.ui.components.view.grid.container.PagingEntityContainer;
import vaadin.commons.ui.components.view.grid.fields.EntityFieldGroupFactory;
import vaadin.commons.ui.components.view.grid.fields.FieldDefinitionContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Configurator<T> {

    private Class<T> type;
    private ContainerDelegate containerDelegate;
    private FieldDefinitionContainer fieldDefinitions;
    private Set<String> filterFields = null;
    private Set<String> gridFields = null;
    private Set<String> formFields = null;
    private Set<String> exportFields = null;

    private EntityFieldGroupFactory<T> entityFieldGroupFactory;
    private EntityContainerFacade<T> entityContainer;

    private String formLabel = "";
    private List<FormEvent> formEventList = new ArrayList<>();

    private List<ValidationEvent<T>> formOnlyValidators = new ArrayList<>();
    private List<ValidationEvent<T>> importOnlyValidators = new ArrayList<>();
    private List<ValidationEvent<T>> validators = new ArrayList<>();

    private List<DeleteListener<T>> deleteListeners = new ArrayList<>();
    private List<FieldGroup.CommitHandler> commitHandlers = new ArrayList<>();
    private EntityInstantiator<T> entityInstantiator;
    private List<PropertyResolver> gridPropertyResolvers = new ArrayList<>();

    private int pageSize = 10;
    private double viewPageSize = 10 ;
    private boolean writeAccess = true;
    private boolean firstPageOnly = false;
    private boolean hasPagination = true;
    private boolean addAllowed = true;
    private boolean deleteAllowed = true;
    private boolean importAllowed = true;
    private boolean exportAllowed = true;
    private boolean editAllowed = true;

    private String username;

    private Configurator() {
    }

    /**
     * Builds the container of this component. Uses EntityContainerFacade interface for the
     * generic operations done over the entities.
     * @see
     */
    protected EntityContainerFacade<T> buildEntityContainerFacade(){
        if(hasPagination || firstPageOnly){
            return new PagingEntityContainer<T>(getType(),
                    containerDelegate,
                    false,
                    pageSize);
        }else{
            return new EagerEntityContainerFacade<T>(getType(),
                    containerDelegate,
                    false);
        }
    }

    private EntityFieldGroupFactory buildEntityFieldGroupFactory() {
        EntityFieldGroupFactory<T> entityFieldGroupFactory = new EntityFieldGroupFactory<T>(this);
        return entityFieldGroupFactory;
    }

    private Configurator<T> refreshFields(){
        gridFields = getOrDefaults(gridFields, fieldDefinitions.getEntityFields());
        formFields = getOrDefaults(formFields, gridFields);
        exportFields = getOrDefaults(exportFields, formFields);
        return this;
    }
    private Set<String> getOrDefaults(Set<String> fields, Set<String> defaults){
        if(fields == null || fields.isEmpty()){
            return defaults;
        }
        return fields;
    }

    public Class<T> getType() {
        return type;
    }

    public EntityContainerFacade<T> getEntityContainer() {
        if(entityContainer == null){
            entityContainer = buildEntityContainerFacade();
        }
        return entityContainer;
    }

    public FieldDefinitionContainer getFieldDefinitions() {
        return fieldDefinitions;
    }

    public Set<String> getFilterFields() {
        return filterFields;
    }

    public Set<String> getGridFields() {
        return gridFields;
    }

    public Set<String> getFormFields() {
        return formFields;
    }

    public EntityFieldGroupFactory<T> getEntityFieldGroupFactory() {
        if(entityFieldGroupFactory == null){
            entityFieldGroupFactory = buildEntityFieldGroupFactory();
        }
        return entityFieldGroupFactory;
    }

    public boolean isWriteAccess() {
        return writeAccess;
    }

    public String getFormLabel() {
        return formLabel;
    }

    public List<FormEvent> getFormEventList() {
        return formEventList;
    }

    public List<DeleteListener<T>> getDeleteListeners() {
        return deleteListeners;
    }

    public List<FieldGroup.CommitHandler> getCommitHandlers() {
        return commitHandlers;
    }

    public boolean isFirstPageOnly() {
        return firstPageOnly;
    }

    public boolean isHasPagination() {
        return hasPagination;
    }

    public int getPageSize() {
        return pageSize;
    }

    public boolean isAddAllowed() {
        return addAllowed;
    }

    public boolean isDeleteAllowed() {
        return deleteAllowed;
    }

    public boolean isImportAllowed() {
        return importAllowed;
    }

    public boolean isExportAllowed() {
        return exportAllowed;
    }

    public boolean isEditAllowed() {
        return editAllowed;
    }

    public String getUsername() {
        return username;
    }

    public List<PropertyResolver> getGridPropertyResolvers() {
        return gridPropertyResolvers;
    }

    public EntityInstantiator<T> getEntityInstantiator() {
        if(entityInstantiator == null){
            entityInstantiator = new ReflectionEntityInstantiator<>(getType());
        }
        return entityInstantiator;
    }

    public List<ValidationEvent<T>> getFormOnlyValidators() {
        return formOnlyValidators;
    }

    public List<ValidationEvent<T>> getImportOnlyValidators() {
        return importOnlyValidators;
    }

    public List<ValidationEvent<T>> getValidators() {
        return validators;
    }

    public Set<String> getExportFields() {
        return exportFields;
    }

    public double getViewPageSize() {
        return viewPageSize;
    }

    public void addCommitHandler(FieldGroup.CommitHandler commitHandler){
        commitHandlers.add(commitHandler);
    }

    public void addDeleteListener(AuditDeleteListener<T> auditDeleteListener) {
        deleteListeners.add(auditDeleteListener);
    }

    public static interface ContainerDelegateStep<T> {
        FieldDefinitionsStep<T> withContainerDelegate(ContainerDelegate containerDelegate);
    }

    public static interface FieldDefinitionsStep<T> {
        Builder<T> withFieldDefinitions(FieldDefinitionContainer fieldDefinitions);
    }

    public static class Builder<T> implements FieldDefinitionsStep<T>, ContainerDelegateStep<T> {
        private Configurator<T> configurator;

        private Builder(Class type){
            configurator = new Configurator<>();
            configurator.type = type;
        }

        public static ContainerDelegateStep initBuilder(Class type){
            return new Builder(type);
        }

        @Override
        public FieldDefinitionsStep<T> withContainerDelegate(ContainerDelegate containerDelegate) {
            configurator.containerDelegate = containerDelegate;
            return this;
        }

        @Override
        public Builder<T> withFieldDefinitions(FieldDefinitionContainer fieldDefinitions) {
            configurator.fieldDefinitions = fieldDefinitions;
            return this;
        }

        public Builder filterFields(Set<String> filterFields) {
            configurator.filterFields = filterFields;
            return this;
        }

        public Builder gridFields(Set<String> gridFields) {
            configurator.gridFields = gridFields;
            return this;
        }

        public Builder formFields(Set<String> formFields) {
            configurator.formFields = formFields;
            return this;
        }

        public Builder exportFields(Set<String> exportFields) {
            configurator.exportFields = exportFields;
            return this;
        }

        public Builder entityFieldGroupFactory(EntityFieldGroupFactory<T> entityFieldGroupFactory) {
            configurator.entityFieldGroupFactory = entityFieldGroupFactory;
            return this;
        }

        public Builder entityContainer(EntityContainerFacade<T> entityContainer) {
            configurator.entityContainer = entityContainer;
            return this;
        }

        public Builder formLabel(String formLabel) {
            configurator.formLabel = formLabel;
            return this;
        }

        public Builder formEventList(List<FormEvent> formEventList) {
            configurator.formEventList = formEventList;
            return this;
        }

        public Builder formOnlyValidators(List<ValidationEvent<T>> formOnlyValidators) {
            configurator.formOnlyValidators = formOnlyValidators;
            return this;
        }

        public Builder importOnlyValidators(List<ValidationEvent<T>> importOnlyValidators) {
            configurator.importOnlyValidators = importOnlyValidators;
            return this;
        }

        public Builder validators(List<ValidationEvent<T>> validators) {
            configurator.validators = validators;
            return this;
        }

        public Builder deleteListeners(List<DeleteListener<T>> deleteListeners) {
            configurator.deleteListeners = deleteListeners;
            return this;
        }

        public Builder commitHandlers(List<FieldGroup.CommitHandler> commitHandlers) {
            configurator.commitHandlers = commitHandlers;
            return this;
        }

        public Builder entityInstantiator(EntityInstantiator<T> entityInstantiator) {
            configurator.entityInstantiator = entityInstantiator;
            return this;
        }

        public Builder gridPropertyResolvers(List<PropertyResolver> gridPropertyResolvers) {
            configurator.gridPropertyResolvers = gridPropertyResolvers;
            return this;
        }

        public Builder pageSize(int pageSize) {
            configurator.pageSize = pageSize;
            return this;
        }

        public Builder viewPageSize(double viewPageSize) {
            configurator.viewPageSize = viewPageSize;
            return this;
        }

        public Builder writeAccess(boolean writeAccess) {
            configurator.writeAccess = writeAccess;
            return this;
        }

        public Builder firstPageOnly(boolean firstPageOnly) {
            configurator.firstPageOnly = firstPageOnly;
            return this;
        }

        public Builder hasPagination(boolean hasPagination) {
            configurator.hasPagination = hasPagination;
            return this;
        }

        public Builder addAllowed(boolean addAllowed) {
            configurator.addAllowed = addAllowed;
            return this;
        }

        public Builder deleteAllowed(boolean deleteAllowed) {
            configurator.deleteAllowed = deleteAllowed;
            return this;
        }

        public Builder importAllowed(boolean importAllowed) {
            configurator.importAllowed = importAllowed;
            return this;
        }

        public Builder exportAllowed(boolean exportAllowed) {
            configurator.exportAllowed = exportAllowed;
            return this;
        }

        public Builder editAllowed(boolean editAllowed) {
            configurator.editAllowed = editAllowed;
            return this;
        }

        public Builder username(String username) {
            configurator.username = username;
            return this;
        }

        public Configurator build() {
            return configurator.refreshFields();
        }
    }
}
