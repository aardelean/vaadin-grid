package vaadin.commons.ui.components;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import vaadin.commons.ui.components.container.ContainerDelegate;
import vaadin.commons.ui.components.view.CompleteGrid;
import vaadin.commons.ui.components.view.Configurator;

@SpringComponent
@UIScope
public abstract class SpringGrid<T> extends CompleteGrid<T> {
    @Autowired
    private ContainerDelegate containerDelegate;

    @Override
    protected Configurator<T> configure(Configurator.ContainerDelegateStep<T> configurator) {
        return options(configurator.withContainerDelegate(containerDelegate));
    }
    protected abstract Configurator<T> options(Configurator.FieldDefinitionsStep<T> configurator);
}
