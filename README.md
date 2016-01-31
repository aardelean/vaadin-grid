# vaadin-grid
This comes as a completion to the grid component of vaadin.
Even though vaadin grid component is quite powerful, the backing container is missing a lot of features.
Overall, this offers an easy way to configure a grid component in vaadin, having 
 - database as storage for the entities
 - lazy loading page by page (JPAContainer loads entity by entity)
 - pagination (no container in vaadin offers this, PagedTable is not compatible with grid)
 - sorting
 - filtering (by converting to string all filters)
 - separation between server and client (could be in 2 different VM's - vaadin never offers this even though this is the default architecture in java)
 - CRUD operation via a popup window at create.
 - export to CSV file(other than CSV formats might be supported if implementation provided)
 - import from CSV file(other than CSV formats might be supported if implementation provided)
 - highly customizable 
 - enabled both bean validation and complex manual validation on form save and on import.
 - specifying easily which fields should be readonly, prompt input via FieldDefinitionContainer.
 
 Contains:
  - 2 different servers implementation :
    - vaadin-grid-server-hibernate 
    Jpa backed by hibernate pure implementation in   (just JPA criterias). To be used, extend on the server side AbstractContainerDelegate,
    provide EntityManager and expose the bean via some sort of a service (EJB, REST, etc...) to be accessible from the client
    - vaadin-grid-server-spring 
    Uses spring-data-repositories  !!! Requires an entityManager available in spring context!!! on server side.
    To use it, either use delegation, or extention and expose this bean's methods via services.
    No additional implementation on server side besides the communication protocol via a web service if wanted. 
    If both server and client live in the same machine, no additional implementation required at all. It is enough the client side 
    and an entityManager available in the context.
  - 2 different clients : 
    - vaadin-grid-client 
    Vaadin pure client, use it by extending CompleteGrid component and providing the server ContainerDelegate (server encapsulation of the web service).
    - vaadin-grid-client-spring
    Using spring, very useful for the case both client and server are on the same machine. 
    In that case, has good integration with server-spring module. 
    Use it by extending the SpringGrid.
  - vaadin-spring-test
  Offers example of how to use the spring server and client on the same machine (UserGrid).
  
  Easiest example possible:
    - import vaadin-grid-client-spring
    - import vaadin-grid-server-spring
    - create a spring context with an entityManager available
    - create a JPA entity
    - extend SpringContainer like this: 
    
@SpringComponent
@UIScope
public class UserGrid extends SpringGrid<User> {

    private static final FieldDefinitionContainer FIELD_DEFINITIONS = new FieldDefinitionContainer()
                                                                              .addFieldDefinition("First", "firstName")
                                                                              .addFieldDefinition("Last", "lastName")
                                                                              .addFieldDefinition("Email", "email")
                                                                              .addFieldDefinition("Alive", "alive");
    private static final Set<String> GRID_FIELDS = ImmutableSet.of("firstName", "lastName", "email", "alive");

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
        return new User();
    }

    @Override
    protected FieldDefinitionContainer getFormFields() {
        return FIELD_DEFINITIONS;
    }
}

    
    - 
