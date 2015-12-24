package vaadin.commons.ui.components.view.grid;

import com.vaadin.data.util.converter.StringToDateConverter;
import vaadin.commons.ui.components.DateFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateConverter extends StringToDateConverter {
	@Override
	protected DateFormat getFormat(Locale locale) {
		return new SimpleDateFormat(DateFormatter.DEFAULT_DATE_FORMATTER.getValue());
	}
}
