package vaadin.commons.ui.components.view.transformers;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.ConverterUtil;
import com.vaadin.ui.Field;
import vaadin.commons.ui.components.view.grid.DateConverter;
import vaadin.commons.ui.components.view.grid.container.EntityContainerFacade;
import vaadin.commons.ui.components.view.grid.fields.EntityFieldGroup;
import vaadin.commons.ui.components.view.grid.fields.EntityFieldGroupFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Set;


public class CSVTransformer implements ContainerTransformer {

	private char delimiter = ';';
	private EntityContainerFacade container;
	private EntityFieldGroupFactory entityFieldGroupFactory;
	private DateConverter dateConverter= new DateConverter();

	public CSVTransformer(EntityContainerFacade container, EntityFieldGroupFactory entityFieldGroupFactory) {
		this(container, entityFieldGroupFactory, null);
	}

	public CSVTransformer(EntityContainerFacade container, EntityFieldGroupFactory entityFieldGroupFactory, Character delimiter) {
		this.container = container;
		this.entityFieldGroupFactory = entityFieldGroupFactory;
		if(delimiter != null){
			this.delimiter = delimiter;
		}
	}

	@Override
	public void entitiesToCsv(OutputStream outputStream, int maxRowNo) throws IOException {
		CsvWriter csvWriter = new CsvWriter(outputStream, delimiter, Charset.defaultCharset());
		Collection<?> itemIds = container.getItemIds();
		Set<String> entityFields = entityFieldGroupFactory.getFormFields().getEntityFields();
		for(String headerColumn: entityFields){
			csvWriter.write(headerColumn);
		}
		EntityFieldGroup entityFieldGroup = entityFieldGroupFactory.buildNewEntityFieldGroup();
		csvWriter.endRecord();
		int index = 0;
		for(Object itemId : itemIds){
			Item item = container.getItem(itemId);
			for(String column: entityFields) {
				Field field = (Field)entityFieldGroup.getFieldMap().get(column);
				Object value = item.getItemProperty(column).getValue();
				String strValue = value !=null ? value.toString() : "";
				if(Date.class.isAssignableFrom(field.getType())){
					strValue = dateConverter.convertToPresentation((Date) item.getItemProperty(column).getValue(), String.class, Locale.getDefault());
				}
				csvWriter.write(strValue);
			}
			csvWriter.endRecord();
			index++;
			if(index >= maxRowNo){
				break;
			}
		}
		csvWriter.close();
	}

	@Override
	public <T> void csvToEntities(InputStream inputStream) throws IOException, FieldGroup.CommitException {
		CsvReader csvReader = new CsvReader(inputStream, delimiter, Charset.defaultCharset());
		csvReader.readHeaders();
		while (csvReader.readRecord()) {
			EntityFieldGroup entityFieldGroup = entityFieldGroupFactory.buildNewEntityFieldGroup(true);
			Collection<?> names = entityFieldGroupFactory.getFormFields().getEntityFields();
			for(Object columnName: names){
				Object value = csvReader.get(columnName.toString());
				Field<Object> field = (Field<Object>) entityFieldGroup.getFieldMap().get(columnName);
				Class<?> type = field.getType();
				Converter converter = ConverterUtil.getConverter(String.class, type, null);
				if(Date.class.isAssignableFrom(type)){
					converter = dateConverter;
				}
				if (converter == null) {
					field.setValue(value);
					field.validate();
				} else {
					field.setValue(converter.convertToModel(value, type, Locale.getDefault()));
					field.validate();
				}
			}
			entityFieldGroup.commit();
			T t = (T) entityFieldGroup.getEntity();
			entityFieldGroup.validateBean();
			container.save(t);

		}
		csvReader.close();
	}


}
