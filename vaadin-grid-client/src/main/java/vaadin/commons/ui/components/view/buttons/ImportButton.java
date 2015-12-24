package vaadin.commons.ui.components.view.buttons;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import vaadin.commons.ui.components.view.grid.container.EntityContainerFacade;
import vaadin.commons.ui.components.view.grid.fields.EntityFieldGroupFactory;
import vaadin.commons.ui.components.view.transformers.CSVTransformer;
import vaadin.commons.ui.components.view.transformers.ContainerTransformer;

import java.io.*;

public class ImportButton<T> extends CustomComponent {
	private final EntityContainerFacade<T> container;
	private String label = null;
	private char delimiter = ';';
	private ContainerTransformer transformer;
	private EntityFieldGroupFactory<T> entityFieldGroupFactory;


	public ImportButton(EntityContainerFacade<T> container, EntityFieldGroupFactory entityFieldGroupFactory, String label){
		this.container = container;
		if(label!=null) {
			this.label = label;
		}
		this.entityFieldGroupFactory = entityFieldGroupFactory;
		this.transformer = new CSVTransformer(container, entityFieldGroupFactory);
		setSizeFull();
		init();
	}

	public ImportButton(EntityContainerFacade<T> container, EntityFieldGroupFactory entityFieldGroupFactory) {
		this(container, entityFieldGroupFactory, null);
	}

	protected void init(){
		HorizontalLayout mainLayout = new HorizontalLayout();
		mainLayout.setMargin(new MarginInfo(true, true, true, false));
		mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final Upload upload = getUploadComponent(outputStream);
		Button startUpload = getUploadButton(upload);
		mainLayout.addComponent(upload);
		mainLayout.addComponent(startUpload);
        setCompositionRoot(mainLayout);
	}

	private Upload getUploadComponent(final ByteArrayOutputStream outputStream) {
		final Upload upload = new Upload(label, new Upload.Receiver() {
			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {
				return outputStream;
			}
		}){

		};
		upload.setButtonCaption(null);
		upload.addSucceededListener(new Upload.SucceededListener() {
			@Override
			public void uploadSucceeded(Upload.SucceededEvent event) {
				InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
				try {
					transformer.csvToEntities(inputStream);
					container.refresh();
				} catch (FieldGroup.CommitException | IOException e) {
					e.printStackTrace();
				}
			}
		});
		return upload;
	}

	private Button getUploadButton(final Upload upload) {
		Button startUpload = new Button("Start Upload");
		startUpload.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				upload.submitUpload();
			}
		});
		startUpload.setHeight(22, Unit.PIXELS);
		return startUpload;
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

	public char getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(char delimiter) {
		this.delimiter = delimiter;
	}

}
