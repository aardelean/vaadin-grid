package vaadin.commons.ui.components.view.grid.fields;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;
import vaadin.commons.ui.components.view.Configurator;
import vaadin.commons.ui.components.view.convertors.SimpleStringToIntegerConvertor;
import vaadin.commons.ui.components.view.form.ValidationEvent;

import java.util.*;


public class EntityFieldGroup<T> extends BeanFieldGroup {


	private Map<String, Field> fieldMap = new LinkedHashMap<String, Field>();
	private Configurator<T> configurator;
	private boolean isImport;

	public EntityFieldGroup(Configurator<T> configurator){
		this(configurator, false);
	}

	public EntityFieldGroup(Configurator<T> configurator, boolean isImport){
		super(configurator.getType());
		setItemDataSource(configurator.getEntityInstantiator().entity());
		this.configurator = configurator;
		super.setItemDataSource(new BeanItem<T>(configurator.getEntityInstantiator().entity()));
		this.isImport = isImport;
		addFieldBindings();
	}

	private void addFieldBindings(){
		for(FieldDefinition fieldDefinition: getFieldDefinition()){
			String fieldId = fieldDefinition.getEntityField();
			this.addField(fieldDefinition.getLabel(),
					             fieldId,
					             fieldDefinition.getInputPrompt());
		}
	}

	public void addField(String label, String property, String inputPrompt){
		if(property.contains(".")){
			getItemDataSource().addNestedProperty(property);
		}
		Class<?> type = getPropertyType(property);
		Field field = build(label, type, Field.class);
		if (field instanceof TextField) {
			TextField textField = (TextField) field;
			if (inputPrompt != null) {
				textField.setInputPrompt(inputPrompt);
			}
			textField.setImmediate(true);
			if (Number.class.isAssignableFrom(type)) {
				numberField(textField, type);
			} else {
				textField.setNullRepresentation("");
			}
		}
		if (isImport) {
			bind(field, property);
		}
		//first one in the form
		if (getFieldMap().isEmpty()) {
			field.focus();
		}
		addFieldTo(property, field);
	}

	private void numberField(TextField textField, Class type) {
		textField.setNullRepresentation("");
		textField.setValidationVisible(true);
		if(type == Integer.class) {
			textField.setConverter(new SimpleStringToIntegerConvertor());
		}
	}


	private void addFieldTo(String property, Field field) {
		fieldMap.put(property, field);
	}

	public T getEntity(){
		return ((BeanItem<T>) getItemDataSource()).getBean();
	}
	public void validateBean(){
		internalValidate(configurator.getValidators());
		if(isImport){
			internalValidate(configurator.getImportOnlyValidators());
		}else{
			internalValidate(configurator.getFormOnlyValidators());
		}
	}

	private void internalValidate(List<ValidationEvent<T>> validators) {
		for(ValidationEvent<T> validationEvent : validators){
			validationEvent.validateBean(getEntity());
		}
	}

	public Map<String, Field> getFieldMap(){
		return fieldMap;
	}


	public FieldDefinitionContainer getFieldDefinition(){
		return configurator.getFieldDefinitions();
	}

	/**
	 * All grid columns binded with reflection to the entity.
	 * Additional to reflection fields, a single or multiple fields can be specified,
	 * but also implemented for them the methods
	 */
	public Set<String> getGridFields(){
		return getFieldDefinition().getEntityFields();
	}

	/**
	 * Definition of all columns that can be filtered and will contain a filtering field in the header.
	 */
	public Set<String> getFilterFields(){
		return null;
	}

	public boolean contains(String columnName){
		return getGridFields().contains(columnName);
	}

	public boolean isDateField(Object columnId){
		return Date.class.isAssignableFrom(getItemProperty(columnId).getType());
	}

	public boolean isNumberField(Object columnId){
		return Number.class.isAssignableFrom(getItemProperty(columnId).getType());
	}

	public Set<String> getFormFields(){
		return configurator.getFormFields();
	}
}
