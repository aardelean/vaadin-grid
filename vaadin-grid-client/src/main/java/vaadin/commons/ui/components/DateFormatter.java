package vaadin.commons.ui.components;

public enum DateFormatter {
	DEFAULT_DATE_FORMATTER("dd.MM.yyyy");
	private String value;

	DateFormatter(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
