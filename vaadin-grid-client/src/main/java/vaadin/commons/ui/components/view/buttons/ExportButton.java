package vaadin.commons.ui.components.view.buttons;

import com.google.gwt.thirdparty.guava.common.collect.ImmutableSet;
import com.vaadin.data.Property;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import vaadin.commons.ui.components.view.grid.container.EntityContainerFacade;
import vaadin.commons.ui.components.view.grid.fields.EntityFieldGroupFactory;
import vaadin.commons.ui.components.view.transformers.CSVTransformer;
import vaadin.commons.ui.components.view.transformers.ContainerTransformer;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class ExportButton extends CustomComponent {

	private String fileName = "export_items";
	private final EntityContainerFacade container;
	private String label = "export to csv";
	private String dateFormatAsSuffix = "yyyy-MM-dd-hh-mm";
	private char delimiter = ';';
	private Set<Integer> exportRowsOptions = ImmutableSet.of(25, 50, 75, 100);
	private Integer maxRowNo = 25;
	private ContainerTransformer transformer = null;
	private EntityFieldGroupFactory entityFieldGroupFactory;

	public ExportButton(EntityContainerFacade container, EntityFieldGroupFactory entityFieldGroupFactory, String fileName, String dateFormatAsSuffix, String label) {
		this.container = container;
		this.entityFieldGroupFactory = entityFieldGroupFactory;
		if(fileName!=null) {
			this.fileName = fileName;
		}
		if(label!=null) {
			this.label = label;
		}
		if(dateFormatAsSuffix!=null) {
			this.dateFormatAsSuffix = dateFormatAsSuffix;
		}
		init();
	}

	public ExportButton(EntityContainerFacade container, EntityFieldGroupFactory entityFieldGroupFactory, String fileName, String dateFormatAsSuffix){
		this(container, entityFieldGroupFactory, fileName, dateFormatAsSuffix, null);
	}

	public ExportButton(EntityContainerFacade container, EntityFieldGroupFactory entityFieldGroupFactory, String fileName){
		this(container, entityFieldGroupFactory, fileName, null, null);
	}

	public ExportButton(EntityContainerFacade container, EntityFieldGroupFactory entityFieldGroupFactory) {
		this(container, entityFieldGroupFactory, null, null, null);
	}

	protected void init(){
		HorizontalLayout mainLayout = new HorizontalLayout();
		mainLayout.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
		mainLayout.setWidth(250, Unit.PIXELS);
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormatAsSuffix);
		FileDownloader fileDownloader = new FileDownloader(new StreamResource(new StreamResource.StreamSource() {
			@Override
			public InputStream getStream() {
				InputStream is = null;
				try {
					ByteArrayOutputStream ous = new ByteArrayOutputStream();
					transform(ous);
					is = new ByteArrayInputStream(ous.toByteArray());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return is;
			}
		}, fileName+"-"+sdf.format(new Date())));
		Button export = new Button(label);
		fileDownloader.extend(export);
		mainLayout.addComponent(export);
		mainLayout.addComponent(maxRowsOptions());
		setCompositionRoot(mainLayout);
	}

	private ComboBox maxRowsOptions(){
		ComboBox comboBox = new ComboBox();
		comboBox.addItems(exportRowsOptions);
		comboBox.setInputPrompt("rows");
		comboBox.setImmediate(true);
		comboBox.setNewItemsAllowed(true);
		comboBox.setConversionError("please provide a valid number!");
		comboBox.setConverter(Integer.class);
		comboBox.setData(exportRowsOptions.iterator().next());
		comboBox.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				maxRowNo = Integer.parseInt(event.getProperty().getValue().toString());
			}
		});

		comboBox.setWidth(10, Unit.EM);
		return comboBox;
	}

	private void transform(OutputStream outputStream) throws IOException {
		if(getTransformer()==null){
			transformer = new CSVTransformer(container, entityFieldGroupFactory);
		}
		transformer.entitiesToCsv(outputStream, maxRowNo);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ContainerTransformer getTransformer() {
		return transformer;
	}

	public void setTransformer(ContainerTransformer transformer) {
		this.transformer = transformer;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDateFormatAsSuffix() {
		return dateFormatAsSuffix;
	}

	public void setDateFormatAsSuffix(String dateFormatAsSuffix) {
		this.dateFormatAsSuffix = dateFormatAsSuffix;
	}

	public char getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(char delimiter) {
		this.delimiter = delimiter;
	}
}
