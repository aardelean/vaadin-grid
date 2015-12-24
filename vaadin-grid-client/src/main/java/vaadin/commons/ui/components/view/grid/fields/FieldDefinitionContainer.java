package vaadin.commons.ui.components.view.grid.fields;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

public class FieldDefinitionContainer extends LinkedList<FieldDefinition> {

	public Set<String> getEntityFields(){
		Set<String> resultSet = new LinkedHashSet<>(size());
		for(FieldDefinition fieldDefinition : this){
			resultSet.add(fieldDefinition.getEntityField());
		}
		return resultSet;
	}

	public FieldDefinitionContainer addFieldDefinition(String label, String entityField){
		this.add(new FieldDefinition(label, entityField));
		return this;
	}

	public FieldDefinitionContainer addFieldDefinition(String label, String entityField, boolean readOnly){
		this.add(new FieldDefinition(label, entityField, readOnly));
		return this;
	}
	public FieldDefinitionContainer addFieldDefinition(String label, String entityField, boolean readOnly, String inputPrompt){
		this.add(new FieldDefinition(label, entityField, readOnly, inputPrompt));
		return this;
	}
	public FieldDefinitionContainer addFieldDefinition(String label, String entityField, String inputPrompt){
		this.add(new FieldDefinition(label, entityField, inputPrompt));
		return this;
	}

	public FieldDefinition getByFieldId(String fieldId){
		FieldDefinition definition = null;
		for(FieldDefinition fieldDefinition : this){
			if(fieldDefinition.getEntityField().equals(fieldId)){
				definition = fieldDefinition;
				break;
			}
		}
		return definition;
	}

}
