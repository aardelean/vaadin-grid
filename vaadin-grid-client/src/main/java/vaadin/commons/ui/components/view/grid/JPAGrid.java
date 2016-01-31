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
import vaadin.commons.ui.components.view.Configurator;
import vaadin.commons.ui.components.view.grid.container.EntityContainerFacade;
import vaadin.commons.ui.components.view.grid.fields.EntityFieldGroup;
import vaadin.commons.ui.components.view.grid.fields.FieldDefinition;

import java.util.List;
import java.util.Set;

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
	private Configurator<T> configurator;
	private EntityFieldGroup<T> entityFieldGroup;

	public JPAGrid(Configurator<T> configurator){
		super((String)null);
		this.configurator = configurator;
		entityFieldGroup = configurator.getEntityFieldGroupFactory().buildNewEntityFieldGroup();
		init();
	}

	protected JPAGrid<T> init(){
		super.setContainerDataSource((Container.Indexed)decorateContainer(configurator.getEntityContainer()));
		setColumnReorderingAllowed(true);
		errorHandler();
		//no edit allowed for ratanet admin
		setEditorEnabled(configurator.isEditAllowed());
		super.setHeightByRows(configurator.getViewPageSize());
		super.setHeightMode(HeightMode.ROW);
		addItemClickListener(event -> configurator.getEntityInstantiator().setEntity((T)event.getItemId()));
		setSelectionMode(SelectionMode.SINGLE);
		super.setSizeFull();
		super.setImmediate(false);
		addColumns();
		setConverters();
		setRenderers();
		buildHeaderFiltering(configurator.getEntityContainer());
		gridCustomFilters(configurator.getEntityContainer());
		return this;
	}

	private void errorHandler() {
		setErrorHandler(p->logger.error("error during a grid operation: ", p.getThrowable()));
	}


	private void addColumns() {
		Set<String> gridFields = configurator.getGridFields();
		for(String columnName: gridFields){
			if(columnName.contains(".")){
				((BeanItemContainer)configurator.getEntityContainer()).addNestedContainerProperty(columnName);
			}
		}
		setColumns((Object[])gridFields.toArray(new String[0]));
		for(Column column: getColumns()){
			column.setHidable(true);
			FieldDefinition fieldDefinition = configurator.getFieldDefinitions().getByFieldId((String) column.getPropertyId());
			if(fieldDefinition!=null) {
				column.setHeaderCaption(fieldDefinition.getLabel());
			}
		}
	}

	private void setConverters() {
		Set<String> gridFields = configurator.getGridFields();
		for(String columnName: gridFields) {
			if (entityFieldGroup.getFieldMap().get(columnName) != null) {
				if (entityFieldGroup.isDateField(columnName)) {
					getColumn(columnName).setConverter(new DateConverter());
				}
			}
		}
	}

	private void setRenderers() {
		Set<String> gridFields = configurator.getGridFields();
		for(String columnName:gridFields) {
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
		setEditorEnabled(configurator.isEditAllowed());
	}

	@Override
	public void saveEditor() throws FieldGroup.CommitException {
		super.saveEditor();
		T bean = (T) ((BeanItem) getEditorFieldGroup().getItemDataSource()).getBean();
		entityFieldGroup.setItemDataSource(bean);
		entityFieldGroup.commit();
		entityFieldGroup.validateBean();
		configurator.getEntityContainer().mergeEntity(bean);
		configurator.getEntityContainer().refresh();
	}


	/**
	 * Default filtering, by entity fields which are database columns.
	 */
	private void buildHeaderFiltering(EntityContainerFacade<T> container) {
		getFilteringHeader();
		if(configurator.getFilterFields() != null) {
			for (String filtered : configurator.getFilterFields()) {
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
		List<PropertyResolver> propertyResolvers = configurator.getGridPropertyResolvers();
		if(propertyResolvers !=null && !propertyResolvers.isEmpty()) {
			GeneratedPropertyContainer propertyContainer = new GeneratedPropertyContainer((Container.Indexed)container);
			for(PropertyResolver propertyResolver: propertyResolvers){
				propertyContainer.addGeneratedProperty(propertyResolver.getPropertyName(), propertyResolver);
			}
			decoratedContainer = propertyContainer;
		}
		return decoratedContainer;
	}
	public HeaderRow getFilteringHeader() {
		if(filteringHeader == null){
			filteringHeader = appendHeaderRow();
		}
		return filteringHeader;
	}
}
