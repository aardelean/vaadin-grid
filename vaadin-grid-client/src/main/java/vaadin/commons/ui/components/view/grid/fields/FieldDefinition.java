package vaadin.commons.ui.components.view.grid.fields;

public class FieldDefinition {
	private String label;
	private String entityField;
	private boolean readOnly = false;
	private String inputPrompt = null;


	public FieldDefinition(String label, String entityField) {
		this.label = label;
		this.entityField = entityField;
	}

	public FieldDefinition(String label, String entityField, boolean readOnly) {
		this(label, entityField);
		this.readOnly = readOnly;
	}

	public FieldDefinition(String label, String entityField, String inputPrompt) {
		this(label, entityField);
		this.inputPrompt = inputPrompt;
	}

	public FieldDefinition(String label, String entityField, boolean readOnly, String inputPrompt) {
		this(label, entityField, readOnly);
		this.inputPrompt = inputPrompt;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getEntityField() {
		return entityField;
	}

	public void setEntityField(String entityField) {
		this.entityField = entityField;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public String getInputPrompt() {
		return inputPrompt;
	}
}
