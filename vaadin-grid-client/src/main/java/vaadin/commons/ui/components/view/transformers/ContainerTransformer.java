package vaadin.commons.ui.components.view.transformers;

import com.vaadin.data.fieldgroup.FieldGroup;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by aardelean on 21.10.15.
 */
public interface ContainerTransformer {

	void entitiesToCsv(OutputStream outputStream, int maxRowNo) throws IOException;

	<T> void csvToEntities(InputStream inputStream) throws IOException, FieldGroup.CommitException;
}
