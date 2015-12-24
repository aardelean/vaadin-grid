package work.view.contact;

import com.google.common.collect.ImmutableSet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import vaadin.commons.ui.components.SpringGrid;
import vaadin.commons.ui.components.view.grid.fields.FieldDefinitionContainer;
import work.entities.User;

import java.util.Set;

@SpringComponent
@UIScope
public class UserGrid extends SpringGrid<User> {

    private static final FieldDefinitionContainer FIELD_DEFINITIONS = new FieldDefinitionContainer()
                                                                              .addFieldDefinition("Vendor", "vendor")
                                                                              .addFieldDefinition("First", "firstName")
                                                                              .addFieldDefinition("Last", "lastName")
                                                                              .addFieldDefinition("Valid To", "validTo")
                                                                              .addFieldDefinition("Valid From", "validFrom");
    private static final Set<String> GRID_FIELDS = ImmutableSet.of("vendor", "firstName", "lastName", "validTo", "validFrom");

    private static final Set<String> FILTER_FIELDS = ImmutableSet.of("firstName", "lastName");

    public UserGrid(){
        setGridFields(GRID_FIELDS);
        setFilterFields(FILTER_FIELDS);
        setInlineEditAllowed(true);
        setHasPagination(true);
        setImportAllowed(true);
        setAddAllowed(true);
    }

    @Override
    protected User newEntity() {
        User user = new User();

        user.setFirstName("marius");
        user.setLastName("simpescu");
        return user;
    }

    @Override
    protected FieldDefinitionContainer getFormFields() {
        return FIELD_DEFINITIONS;
    }
}
