package vaadin.commons.ui.components.view;

import com.vaadin.data.Validator;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vaadin.commons.ui.components.view.auditing.AuditCommitEvent;
import vaadin.commons.ui.components.view.auditing.AuditDeleteListener;
import vaadin.commons.ui.components.view.grid.GridFooter;
import vaadin.commons.ui.components.view.grid.JPAGrid;
import vaadin.commons.ui.components.view.grid.container.EntityContainerFacade;

import java.lang.reflect.ParameterizedType;

/**
 * @author aardelean
 *
 * Generic class for adding ratanet - like grids all in one easily. Having a CRUD operations for any table
 * with import to csv and export to csv, sortable, filterable, hideble columns would be just as easy as
 * extending this class and overriding the abstract methods which are mandatory.
 *
 *
 * For building the appropriate container @see EntityContainerFacade
 *
 *
 * @param <T>
 */
public abstract class CompleteGrid<T> extends CustomComponent {

	private static final Logger logger = LoggerFactory.getLogger(CompleteGrid.class);

	private VerticalLayout mainLayout;
	protected JPAGrid<T> grid;
	private GridFooter footerButtons;
	private Configurator<T> configurator;

	public CompleteGrid start(){
		this.configurator= configure(Configurator.Builder.initBuilder(getType()));
		configurator.getValidators().add(p->{validate(p);});
		mainLayout = new VerticalLayout();
		grid = buildGrid();
		addAuditingCapabilities(configurator.isWriteAccess(), configurator.getUsername());
		footerButtons = footerActions();
		mainLayout.addComponent(grid);
		mainLayout.addComponent(footerButtons);
		setCompositionRoot(mainLayout);
		return this;
	}

	protected abstract Configurator<T> configure(Configurator.ContainerDelegateStep<T> configurator);

	private void addAuditingCapabilities(boolean writeAccess, String username) {
		if(writeAccess && username != null){
			AuditCommitEvent auditCommitEvent = new AuditCommitEvent(username);
			configurator.addCommitHandler(auditCommitEvent);
			AuditDeleteListener<T> auditDeleteListener = new AuditDeleteListener<>(username);
			configurator.addDeleteListener(auditDeleteListener);
		}
	}

	public Class<T> getType() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	private GridFooter<T> footerActions() {
		return new GridFooter<T>(configurator)
				.grid(grid)
				.init();
	}

	/****************************************GRID CUSTOMIZATION ******************/
	@SuppressWarnings("unchecked")
	private JPAGrid<T> buildGrid(){
		final JPAGrid<T> grid = new JPAGrid<>(configurator);

		if(configurator.isDeleteAllowed()) {
			grid.addSelectionListener(event-> {
					if (footerButtons.getDeleteButton() != null) {
						if (grid.getSelectedRows() != null && grid.getSelectedRows().size() == 1) {
							footerButtons.getDeleteButton().setEnabled(true);
						} else {
							footerButtons.getDeleteButton().setEnabled(false);
						}
					}
			});
		}

		return grid;
	}

	protected void customizeRenderer(Grid.Column column) {

	}

	/**
	 * Override this to validate a full bean before saving it, when it has all properties set.
	 */
	protected void validate(T t) throws Validator.InvalidValueException{}


	/**
	 * Special customization over each field, like additional validation before setter of the entity is invoked.
	 */
	protected Field customizeFormField(String fieldId, Field field) {
		return field;
	}

	protected void gridCustomFilters(JPAGrid<T> grid, EntityContainerFacade<T> container) {	}
}
