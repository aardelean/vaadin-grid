package vaadin.commons.ui.components.view.grid;

import com.vaadin.data.util.PropertyValueGenerator;

import java.lang.reflect.ParameterizedType;

public abstract class PropertyResolver<T> extends PropertyValueGenerator<T> {
	private String propertyName;

	public PropertyResolver(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	@Override
	public Class<T> getType() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
}
