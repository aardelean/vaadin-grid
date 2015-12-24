package vaadin.commons.ui.components.view.grid.fields;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;
import vaadin.commons.ui.components.view.convertors.SimpleStringToIntegerConvertor;
import vaadin.commons.ui.components.view.form.ValidationEvent;
import vaadin.commons.ui.components.view.grid.JPAGrid;

import java.util.*;


public abstract class EntityFieldGroup<T> extends BeanFieldGroup {

	public String startValidityDatePropertyName;
	public String endValidityDatePropertyName;


	public final static String PRODUCT_CODE = "productCode";
	private List<ValidationEvent<T>> validationEvents = new ArrayList<ValidationEvent<T>>();
	private Map<String, Field> fieldMap = new LinkedHashMap<String, Field>();
	private boolean isImport = false;

	public EntityFieldGroup(Class<T> clazz){
		this(clazz, false);
	}

	public EntityFieldGroup(Class<T> clazz, boolean isImport){
		super(clazz);
		this.isImport = isImport;
		super.setItemDataSource(new BeanItem<T>(newEntity()));
		addFieldBindings();
		addValidator(new ValidationEvent<T>() {
			@Override
			public void validateBean(T t) throws Validator.InvalidValueException {
				validate(t);
			}
		});
	}

	protected void validate(T t) {

	}

	private void addFieldBindings(){
		for(FieldDefinition fieldDefinition: getFormFields()){
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
		for(ValidationEvent<T> validationEvent : validationEvents){
			validationEvent.validateBean(getEntity());
		}
	}
	public void addValidator(ValidationEvent<T> validationEvent){
		validationEvents.add(validationEvent);
	}

	public Map<String, Field> getFieldMap(){
		return fieldMap;
	}

	protected abstract T newEntity();

	public abstract FieldDefinitionContainer getFormFields();

	/**
	 * All grid columns binded with reflection to the entity.
	 * Additional to reflection fields, a single or multiple fields can be specified,
	 * but also implemented for them the methods
	 * {@link JPAGrid#getPropertyResolver()} or {@link JPAGrid#getMultiplePropertyResolvers()}
	 */
	public Set<String> getGridFields(){
		return getFormFields().getEntityFields();
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

	public void setStartValidityDatePropertyName(String startValidityDatePropertyName) {
		this.startValidityDatePropertyName = startValidityDatePropertyName;
	}

	public void setEndValidityDatePropertyName(String endValidityDatePropertyName) {
		this.endValidityDatePropertyName = endValidityDatePropertyName;
	}

	public String getStartValidityDatePropertyName() {
		return startValidityDatePropertyName;
	}

	public String getEndValidityDatePropertyName() {
		return endValidityDatePropertyName;
	}

}
