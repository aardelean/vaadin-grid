package vaadin.commons.ui.components.view.convertors;

import com.vaadin.data.util.converter.StringToIntegerConverter;

import java.util.Locale;

public class SimpleStringToIntegerConvertor extends StringToIntegerConverter {
	@Override
	public String convertToPresentation(Integer value, Class<? extends String> targetType, Locale locale) throws ConversionException {
		if (value == null) {
			return "";
		}
		return value.toString();
	}
}
