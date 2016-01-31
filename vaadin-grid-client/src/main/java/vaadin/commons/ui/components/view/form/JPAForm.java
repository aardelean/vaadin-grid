package vaadin.commons.ui.components.view.form;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vaadin.commons.ui.components.view.grid.container.EntityContainerFacade;
import vaadin.commons.ui.components.view.grid.fields.EntityFieldGroup;
import vaadin.commons.ui.components.view.grid.fields.FieldDefinition;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class JPAForm<T> extends CustomComponent {
	private static Logger logger = LoggerFactory.getLogger(JPAForm.class);

	private EntityContainerFacade<T> container;
	private Button save;
	private Button cancel;
	private VerticalLayout mainLayout;
	protected HorizontalLayout header;
	protected FormLayout form;
	protected HorizontalLayout footer;
	private EntityFieldGroup<T> fieldGroup;
	private boolean hasButtons = true;
	private List<FormEvent> formEvents = new LinkedList<>();
	public JPAForm(){
	}


	public JPAForm(EntityContainerFacade<T> container, EntityFieldGroup<T> fieldGroup){
		init(container, fieldGroup);
	}

	protected void init(EntityContainerFacade<T> container, EntityFieldGroup<T> fieldGroup) {
		this.container=container;
		this.fieldGroup = fieldGroup;
		this.footer = new HorizontalLayout();
		this.header = new HorizontalLayout();
		this.form = new FormLayout();
		mainLayout = new VerticalLayout();
		mainLayout.setMargin(new MarginInfo(true, true, true, true));
		mainLayout.addComponent(header);
		mainLayout.addComponent(form);
		for(String fieldEntry : fieldGroup.getFormFields()){
			FieldDefinition entityField = fieldGroup.getFieldDefinition().getByFieldId(fieldEntry);
			addFieldToForm(fieldEntry, fieldGroup.getFieldMap().get(fieldEntry), entityField.isReadOnly());
		}
		additionalFieldsToForm(form);
		mainLayout.addComponent(footer);
		if(hasButtons) {
			addFormButtons();
		}
		setCompositionRoot(mainLayout);
		setSizeUndefined();
	}

	private void addFieldToForm(String fieldId, Field field, boolean readOnly){
		field = customizeFormField(fieldId, field);
		fieldGroup.bind(field, fieldId);
		field.setWidth(8, Unit.EM);
		field.setReadOnly(readOnly);
		form.addComponent(field);
	}

	private void addFormButtons() {
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSizeFull();
		buttons.setSpacing(true);
		save = new Button("Save");
		save.addClickListener(onSave());

		cancel = new Button("Cancel");
		cancel.addClickListener(onCancel());

		buttons.addComponent(save);
		buttons.addComponent(cancel);

		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		buttons.setComponentAlignment(save, Alignment.BOTTOM_RIGHT);
		buttons.setComponentAlignment(cancel, Alignment.BOTTOM_RIGHT);
		footer.getMargin().setMargins(true);
		footer.addComponent(buttons);
	}

	protected Button.ClickListener onSave(){
		return p->{
			try {
				commit();
				T entity = getEntityFieldGroup().getEntity();
				container.save(entity);
				invokeAfterSave(entity);
			} catch (FieldGroup.CommitException e) {
				logger.warn("validation on save entity: ", e);
				Notification.show("could not save entity, validation error, check the fields above.");
			}catch (Exception e){
				logger.error("could not save entity, internal error: ", e);
			}
		};
	}
	private void invokeAfterSave(T entity){
		formEvents.forEach(p->p.afterSaving(JPAForm.this, entity));
	}

	protected Button.ClickListener onCancel(){
		return p->{
			getEntityFieldGroup().discard();

		};
	}
	private void invokeAfterCancel(){
		formEvents.forEach(p->p.afterCanceling(JPAForm.this));
	}
	public void commit() throws FieldGroup.CommitException {
		fieldGroup.commit();
		fieldGroup.validateBean();
	}

	public EntityFieldGroup<T> getEntityFieldGroup(){
		return fieldGroup;
	}

	public void addActionListener(FormEvent formEvent){
		formEvents.add(formEvent);
	}

	public void addActionListeners(Collection<FormEvent> formEvents){
		formEvents.addAll(formEvents);
	}

	protected void additionalFieldsToForm(FormLayout form){}

	protected Field customizeFormField(String fieldId, Field field){
		return field;
	}

	public Layout getHeader(){
		return header;
	}

	public void setHasButtons(boolean hasButtons){
		this.hasButtons = hasButtons;
	}
}