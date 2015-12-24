package vaadin.commons.ui.components.view.grid;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Grid;

import java.util.Collection;
import java.util.Set;

public abstract class SimpleGrid<T> extends Grid {

	private Class clazz;
	protected BeanItemContainer<T> container;

	public SimpleGrid(String title, Class clazz){
		super(title);
		this.clazz = clazz;
		container = new BeanItemContainer<T>(clazz);
		for(String columnName: getColumnNames()) {
			if(columnName.contains(".")){
				container.addNestedContainerProperty(columnName);
			}
			Column column = addColumn(columnName);
			customizeColumn(column);
		}
		setContainerDataSource(container);
	}

	public void init(Collection<T> list){
		container.removeAllItems();
		container.addAll(list);
	}

	protected void customizeColumn(Column column){

	}

	protected abstract Set<String> getColumnNames();

}
