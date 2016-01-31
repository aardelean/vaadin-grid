package vaadin.commons.ui.components.view.window;

import com.vaadin.ui.Field;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import vaadin.commons.ui.components.view.Configurator;
import vaadin.commons.ui.components.view.form.FormEvent;
import vaadin.commons.ui.components.view.form.JPAForm;
import vaadin.commons.ui.components.view.grid.container.EntityContainerFacade;
import vaadin.commons.ui.components.view.grid.fields.EntityFieldGroup;

import java.util.Collection;

public class WindowForm<T> extends Window {

	private JPAForm form;
	private Configurator<T> configurator;

	public WindowForm(Configurator<T> configurator){
		super(configurator.getFormLabel());
		this.configurator = configurator;
		init(configurator.getEntityContainer(), configurator.getEntityFieldGroupFactory().buildNewEntityFieldGroup());
	}

	protected void init(EntityContainerFacade<T> container, EntityFieldGroup<T> entityFieldGroup){
		VerticalLayout subContent = new VerticalLayout();
		subContent.setMargin(true);
		subContent.addComponent(initForm(container, entityFieldGroup));
		setContent(subContent);
		center();
		setDraggable(true);
		setResizable(false);
		setPositionX(900);
		setPositionY(350);
	}

	private JPAForm initForm(EntityContainerFacade<T> container, EntityFieldGroup<T> entityFieldGroup){
		form = new JPAForm(container, entityFieldGroup){
			@Override
			protected Field customizeFormField(String fieldId, Field field) {
				return WindowForm.this.customizeFormField(fieldId, field);
			}
		};
		WindowCloser windowCloser = new WindowCloser(this, form);
		form.addActionListener(windowCloser);
		return form;
	}

	protected Field customizeFormField(String fieldId, Field field) {
		return field;
	}

	public void addActionListeners(Collection<FormEvent> formEvents) {
		form.addActionListeners(formEvents);
	}
}
