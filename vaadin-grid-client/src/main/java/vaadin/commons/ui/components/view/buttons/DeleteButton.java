package vaadin.commons.ui.components.view.buttons;

import com.vaadin.ui.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vaadin.commons.ui.components.view.grid.JPAGrid;
import vaadin.commons.ui.components.view.grid.container.EntityContainerFacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DeleteButton<T> extends Button {

	private static final Logger logger = LoggerFactory.getLogger(DeleteButton.class);

	private EntityContainerFacade<T> container;
	private JPAGrid<T> grid;
	private List<DeleteListener<T>> listeners;

	public DeleteButton(EntityContainerFacade<T> container, JPAGrid<T> grid){
		super("delete");
		this.container = container;
		this.grid = grid;
		setEnabled(false);
		init();
	}

	private void init(){
		addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (event.getSource() == DeleteButton.this) {
					Collection<T> selectedRows = (Collection<T>) grid.getSelectedRows();
					for (T row : selectedRows) {
						executeBeforeListeners(row);
						container.delete(row);
						executeAfterListeners(row);
					}
					container.refresh();
					setEnabled(false);
				}
			}
		});
	}
	private void executeBeforeListeners(T row){
		if(listeners!=null && !listeners.isEmpty()){
			for(DeleteListener<T> listener : listeners){
				listener.beforeDelete(row);
			}
		}
	}
	private void executeAfterListeners(T row){
		if(listeners!=null && !listeners.isEmpty()){
			for(DeleteListener<T> listener : listeners){
				listener.afterDelete(row);
			}
		}
	}

	public void addDeleteListener(DeleteListener<T> deleteListener){
		if(listeners==null){
			listeners = new ArrayList<>();
		}
		listeners.add(deleteListener);
	}

	public void setListeners(List<DeleteListener<T>> listeners) {
		this.listeners = listeners;
	}
}
