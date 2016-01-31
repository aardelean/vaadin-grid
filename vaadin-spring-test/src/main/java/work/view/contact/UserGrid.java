package work.view.contact;

import com.google.common.collect.ImmutableSet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import vaadin.commons.ui.components.SpringGrid;
import vaadin.commons.ui.components.view.Configurator;
import vaadin.commons.ui.components.view.grid.fields.FieldDefinitionContainer;
import work.entities.User;

import java.util.Set;

@SpringComponent
@UIScope
public class UserGrid extends SpringGrid<User> {

    private static final FieldDefinitionContainer FIELD_DEFINITIONS = new FieldDefinitionContainer()
                                                                              .addFieldDefinition("First", "firstName")
                                                                              .addFieldDefinition("Last", "lastName")
                                                                              .addFieldDefinition("Valid To", "validTo")
                                                                              .addFieldDefinition("Valid From", "validFrom");
    private static final Set<String> GRID_FIELDS = ImmutableSet.of("firstName", "lastName", "validTo", "validFrom");

    private static final Set<String> FILTER_FIELDS = ImmutableSet.of("firstName", "lastName");


    @Override
    protected Configurator<User> options(Configurator.FieldDefinitionsStep<User> configurator) {
        return configurator.withFieldDefinitions(FIELD_DEFINITIONS)
                .gridFields(GRID_FIELDS).filterFields(FILTER_FIELDS).writeAccess(true).build();
    }
}
