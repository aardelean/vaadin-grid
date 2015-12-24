package vaadin.commons.ui.components.view.grid;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import vaadin.commons.ui.components.view.buttons.DeleteButton;
import vaadin.commons.ui.components.view.buttons.DeleteListener;
import vaadin.commons.ui.components.view.buttons.ExportButton;
import vaadin.commons.ui.components.view.buttons.ImportButton;
import vaadin.commons.ui.components.view.grid.container.EntityContainerFacade;
import vaadin.commons.ui.components.view.grid.fields.EntityFieldGroup;
import vaadin.commons.ui.components.view.grid.fields.EntityFieldGroupFactory;
import vaadin.commons.ui.components.view.grid.pagination.PaginationLayout;
import vaadin.commons.ui.components.view.window.WindowForm;

import java.util.List;

public class GridFooter<T> extends HorizontalLayout{

    private EntityContainerFacade<T> entityContainer;
    private EntityFieldGroupFactory<T> entityFieldGroupFactory;
    private JPAGrid<T> grid;
    private EntityFieldGroup<T> entityFieldGroup;
    private boolean writeRights;
    private boolean deleteAllowed;
    private boolean exportAllowed;
    private boolean importAllowed;
    private boolean addAllowed;
    private String formLabel;
    private boolean hasPaginationLayout;
    private List<DeleteListener<T>> deleteListeners;
    private Button addButton;
    private DeleteButton<T> deleteButton;

    public GridFooter(){

    }
    public GridFooter init(){
        entityFieldGroup = entityFieldGroupFactory.buildNewEntityFieldGroup();
        setSizeFull();
        VerticalLayout left = new VerticalLayout();
        left.setSizeFull();
        left.setDefaultComponentAlignment(Alignment.TOP_LEFT);
        if(writeRights) {
            HorizontalLayout writeLayout = new HorizontalLayout();
            writeLayout.setMargin(new MarginInfo(true, true, true, true));
            writeLayout.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
            addButton = buildAddButton();
            deleteButton = buildDeleteButton();
            if(addButton!=null) {
                writeLayout.addComponent(addButton);
            }
            if (deleteButton != null) {
                writeLayout.addComponent(deleteButton);
            }
            left.addComponent(writeLayout);
        }
        if(hasPaginationLayout &&
                   entityContainer.getPageSize() < entityContainer.getTotalSize()){
            left.addComponent(new PaginationLayout<>(entityContainer));
        }
        addComponent(left);
        VerticalLayout right = new VerticalLayout();
        right.setWidth(400, Unit.PIXELS);
        right.setMargin(new MarginInfo(true, true, true, true));
        if(exportAllowed) {
            right.addComponent(getExporterComponent());
        }
        if(writeRights && importAllowed){
            right.addComponent(getImporterComponent());
        }
        addComponent(right);
        setComponentAlignment(right, Alignment.TOP_RIGHT);
        return this;
    }

    private Component getExporterComponent(){
        return new ExportButton(entityContainer, entityFieldGroupFactory);
    }

    private Component getImporterComponent(){
        return new ImportButton<T>(entityContainer, entityFieldGroupFactory);
    }


    private DeleteButton<T> buildDeleteButton(){
        if(deleteAllowed){
            deleteButton = new DeleteButton<T>(entityContainer, grid);
            deleteButton.setListeners(deleteListeners);
        }
        return deleteButton;
    }


    /*********** FORM AND IMPORT FIELDS SPECIFIC ***********/

    private Button buildAddButton() {
        if(addAllowed) {
            addButton = new Button("Add");
            addButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    if (event.getSource() == addButton) {
                        addButton.setVisible(false);
                        showWindowForm();
                    }
                }

            });
        }
        return addButton;
    }

    private void showWindowForm(){
        WindowForm<T> windowForm = new WindowForm<T>(formLabel, entityContainer, entityFieldGroupFactory.buildNewEntityFieldGroup()){
            @Override
            protected Field customizeFormField(String fieldId, Field field) {
                return GridFooter.this.customizeFormField(fieldId, field);
            }
        };
        getUI().addWindow(windowForm);
        windowForm.addCloseListener(new Window.CloseListener() {
            @Override
            public void windowClose(Window.CloseEvent e) {
                addButton.setVisible(true);
            }
        });
    }

    protected Field customizeFormField(String fieldId, Field field){
        return field;
    }

    public GridFooter entityContainer(EntityContainerFacade<T> entityContainer) {
        this.entityContainer = entityContainer;
        return this;
    }

    public GridFooter entityFieldGroupFactory(EntityFieldGroupFactory<T> entityFieldGroupFactory) {
        this.entityFieldGroupFactory = entityFieldGroupFactory;
        return this;
    }

    public GridFooter grid(JPAGrid<T> grid) {
        this.grid = grid;
        return this;
    }

    public GridFooter writeRights(boolean writeRights) {
        this.writeRights = writeRights;
        return this;
    }

    public GridFooter deleteAllowed(boolean deleteAllowed) {
        this.deleteAllowed = deleteAllowed;
        return this;
    }

    public GridFooter exportAllowed(boolean exportAllowed) {
        this.exportAllowed = exportAllowed;
        return this;
    }

    public GridFooter importAllowed(boolean importAllowed) {
        this.importAllowed = importAllowed;
        return this;
    }

    public GridFooter addAllowed(boolean addAllowed) {
        this.addAllowed = addAllowed;
        return this;
    }

    public GridFooter formLabel(String formLabel) {
        this.formLabel = formLabel;
        return this;
    }

    public DeleteButton<T> getDeleteButton() {
        return deleteButton;
    }

    public GridFooter deleteListeners(List<DeleteListener<T>> listeners) {
        this.deleteListeners = listeners;
        return this;
    }

    public GridFooter hasPaginationComponent(boolean hasPaginationLayout) {
        this.hasPaginationLayout = hasPaginationLayout;
        return this;
    }
}
