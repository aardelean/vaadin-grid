package vaadin.commons.ui.components.view.grid;

import com.vaadin.data.Container;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Field;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vaadin.commons.ui.components.view.grid.container.EntityContainerFacade;
import vaadin.commons.ui.components.view.grid.fields.EntityFieldGroup;
import vaadin.commons.ui.components.view.grid.fields.FieldDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * Common class for easing up writing new grid components.
 * Supports:
 *  - database direct querying based on entities
 *  - sorting of all columns
 *  - filtering specified columns
 *  - adding new columns not present in the entity, but resolving the value based on existing columns
 *  - pagination via scroll
 *  - adding special filtering via criteria builder
 *
 * Created by aardelean on 14.10.15.
 */
public class JPAGrid<T> extends Grid {
	private static final Logger logger = LoggerFactory.getLogger(JPAGrid.class);

	private HeaderRow filteringHeader;
	private EntityFieldGroup<T> entityFieldGroup;
	private double viewPageSize;
	/**
	 * Multiple columns can derived from existing ones. Add all PropertyResolvers for each column not present in the entity
	 * but present in the grid column definition.
	 */
	private List<PropertyResolver> additionalPropertyResolvers;
	private boolean editAllowed;
	/**
	 * Override this to have a fast one new column resulting from calculation of others.
	 */
	private PropertyResolver propertyResolver;
	private EntityContainerFacade<T> container;

	public JPAGrid(EntityContainerFacade<T> container, EntityFieldGroup<T> entityFieldGroup){
		super((String)null);
		this.entityFieldGroup = entityFieldGroup;
		this.container = container;
	}

	protected JPAGrid<T> init(){
		super.setContainerDataSource((Container.Indexed)decorateContainer(container));
		setColumnReorderingAllowed(true);
		errorHandler();
		//no edit allowed for ratanet admin
		setEditorEnabled(editAllowed);
		super.setHeightByRows(viewPageSize);
		super.setHeightMode(HeightMode.ROW);
		setSelectionMode(SelectionMode.SINGLE);
		super.setSizeFull();
		super.setImmediate(false);
		addColumns();
		setConverters();
		setRenderers();
		buildHeaderFiltering(container);
		gridCustomFilters(container);
		return this;
	}

	private void errorHandler() {
		setErrorHandler(p->logger.error("error during a grid operation: ", p.getThrowable()));
	}


	private void addColumns() {
		for(String columnName: entityFieldGroup.getGridFields()){
			if(columnName.contains(".")){
				((BeanItemContainer)container).addNestedContainerProperty(columnName);
			}
		}
		setColumns((Object[])entityFieldGroup.getGridFields().toArray(new String[entityFieldGroup.getGridFields().size()]));
		for(Column column: getColumns()){
			column.setHidable(true);
			FieldDefinition fieldDefinition = entityFieldGroup.getFormFields().getByFieldId((String) column.getPropertyId());
			if(fieldDefinition!=null) {
				column.setHeaderCaption(fieldDefinition.getLabel());
			}
		}
	}

	private void setConverters() {
		for(String columnName: entityFieldGroup.getGridFields()) {
			if (entityFieldGroup.getFieldMap().get(columnName) != null) {
				if (entityFieldGroup.isDateField(columnName)) {
					getColumn(columnName).setConverter(new DateConverter());
				}
			}
		}
	}

	private void setRenderers() {
		for(String columnName:entityFieldGroup.getGridFields()) {
			if (entityFieldGroup.getFieldMap().get(columnName) != null) {
				if (entityFieldGroup.isNumberField(columnName)) {
					getColumn(columnName).setRenderer(new SimpleNumberRenderer());
				}
			}
			customizeRenderer(getColumn(columnName));
		}
	}
	protected void customizeRenderer(Column column){}

	@Override
	protected void doCancelEditor() {
		super.doCancelEditor();
		setEditorEnabled(editAllowed);
	}

	@Override
	public void saveEditor() throws FieldGroup.CommitException {
		super.saveEditor();
		T bean = (T) ((BeanItem) getEditorFieldGroup().getItemDataSource()).getBean();
		entityFieldGroup.setItemDataSource(bean);
		entityFieldGroup.commit();
		container.refresh();
	}


	/**
	 * Default filtering, by entity fields which are database columns.
	 */
	private void buildHeaderFiltering(EntityContainerFacade<T> container) {
		getFilteringHeader();
		if(entityFieldGroup.getFilterFields() != null) {
			for (String filtered : entityFieldGroup.getFilterFields()) {
				addColumnFiltering(container, filtered);
			}
		}
	}

	private void addColumnFiltering(EntityContainerFacade<T> container, String columnId) {
		// Add new TextFields to each column which filters the data from
		// that column
		Field filter = buildColumnFilter(container, columnId);
		getFilteringHeader().getCell(columnId).setComponent(filter);
		getFilteringHeader().getCell(columnId).setStyleName("filter-header");
	}

	protected Field buildColumnFilter(EntityContainerFacade<T> container, final Object columnId) {
		Field  filter = null;
		filter = buildTextFilter(columnId);
		if(entityFieldGroup.isNumberField(columnId)){
			filter.setWidth(6, Unit.EM);
		}else{
			filter.setWidth(8, Unit.EM);
		}
		return filter;
	}

	private TextField buildTextFilter(final Object columnId) {
		TextField filter = new TextField();

		filter.setInputPrompt("Filter");
		filter.addTextChangeListener(new FieldEvents.TextChangeListener() {
			SimpleStringFilter filter = null;
			@Override
			public void textChange(FieldEvents.TextChangeEvent event) {
				Container.Filterable f = (Container.Filterable) getContainerDataSource();
				// Remove old filter
				if (filter != null) {
					f.removeContainerFilter(filter);
				}
				filter = new SimpleStringFilter(columnId, event.getText(), true, true);
				f.addContainerFilter(filter);
				cancelEditor();
			}
		});
		return filter;
	}


	/**
     * Advanced filtering, with one field to be defined in child class where also can attach/modify jpaContainer
     */
	protected void gridCustomFilters(final EntityContainerFacade<T> container) {

	}

	/**
	 * In the case of more columns than fields, the column renderer can be customized and decorated.
	 */
	protected Container decorateContainer(Container container){
		Container decoratedContainer = container;
		addPropertyResolver(propertyResolver);
		if(getMultiplePropertyResolvers() !=null && !getMultiplePropertyResolvers().isEmpty()) {
			GeneratedPropertyContainer propertyContainer = new GeneratedPropertyContainer((Container.Indexed)container);
			for(PropertyResolver propertyResolver: getMultiplePropertyResolvers()){
				propertyContainer.addGeneratedProperty(propertyResolver.getPropertyName(), propertyResolver);
			}
			decoratedContainer = propertyContainer;
		}
		return decoratedContainer;
	}

	private void addPropertyResolver(PropertyResolver propertyResolver){
		if(propertyResolver!=null) {
			if (additionalPropertyResolvers == null) {
				additionalPropertyResolvers = new ArrayList<>();
			}
			additionalPropertyResolvers.add(propertyResolver);
		}
	}


	protected List<PropertyResolver> getMultiplePropertyResolvers(){
		return additionalPropertyResolvers;
	}

	public HeaderRow getFilteringHeader() {
		if(filteringHeader == null){
			filteringHeader = appendHeaderRow();
		}
		return filteringHeader;
	}

	public void setViewPageSize(double viewPageSize) {
		this.viewPageSize = viewPageSize;
	}

	public void setAdditionalPropertyResolvers(List<PropertyResolver> additionalPropertyResolvers) {
		this.additionalPropertyResolvers = additionalPropertyResolvers;
	}

	public void setEditAllowed(boolean editAllowed) {
		this.editAllowed = editAllowed;
	}

	public void setPropertyResolver(PropertyResolver<T> propertyResolver) {
		this.propertyResolver = propertyResolver;
	}
}
