package vaadin.commons.ui.components.view.grid.fields;

import vaadin.commons.ui.components.view.Configurator;

import java.util.Set;

public class EntityFieldGroupFactory<T> {
	private Configurator<T> configurator;

	public EntityFieldGroupFactory(Configurator<T> configurator) {
		this.configurator = configurator;
	}
	public EntityFieldGroup<T> buildNewEntityFieldGroup(){
		return buildNewEntityFieldGroup(false);
	}

	public EntityFieldGroup<T> buildNewEntityFieldGroup(boolean isImport){
		EntityFieldGroup<T> entityFieldGroup= new EntityFieldGroup<T>(configurator, isImport);
		return entityFieldGroup;
	}
	public Set<String> getFormFields(){
		return configurator.getFormFields();
	}
}
