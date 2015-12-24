package vaadin.commons.ui.components.view.form;

import com.vaadin.data.Validator;

public interface ValidationEvent<T> {

	void validateBean(T t)  throws Validator.InvalidValueException;
}
