package vaadin.commons.ui.components.view.grid;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import vaadin.commons.ui.components.view.Configurator;
import vaadin.commons.ui.components.view.buttons.DeleteButton;
import vaadin.commons.ui.components.view.buttons.ExportButton;
import vaadin.commons.ui.components.view.buttons.ImportButton;
import vaadin.commons.ui.components.view.grid.fields.EntityFieldGroup;
import vaadin.commons.ui.components.view.grid.pagination.PaginationLayout;
import vaadin.commons.ui.components.view.window.WindowForm;

public class GridFooter<T> extends HorizontalLayout{

    private JPAGrid<T> grid;
    private Button addButton;
    private DeleteButton<T> deleteButton;
    private Configurator<T> configurator;

    public GridFooter(Configurator<T> configurator){
        this.configurator = configurator;
    }
    public GridFooter init(){
        EntityFieldGroup<T> entityFieldGroup = configurator.getEntityFieldGroupFactory().buildNewEntityFieldGroup();
        setSizeFull();
        VerticalLayout left = new VerticalLayout();
        left.setSizeFull();
        left.setDefaultComponentAlignment(Alignment.TOP_LEFT);
        if(configurator.isWriteAccess()) {
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
        if(configurator.isHasPagination() &&
                configurator.getPageSize() < configurator.getEntityContainer().getTotalSize()){
            left.addComponent(new PaginationLayout<>(configurator.getEntityContainer()));
        }
        addComponent(left);
        VerticalLayout right = new VerticalLayout();
        right.setWidth(400, Unit.PIXELS);
        right.setMargin(new MarginInfo(true, true, true, true));
        if(configurator.isExportAllowed()) {
            right.addComponent(getExporterComponent());
        }
        if(configurator.isWriteAccess() && configurator.isImportAllowed()){
            right.addComponent(getImporterComponent());
        }
        addComponent(right);
        setComponentAlignment(right, Alignment.TOP_RIGHT);
        return this;
    }

    private Component getExporterComponent(){
        return new ExportButton(configurator.getEntityContainer(), configurator.getEntityFieldGroupFactory());
    }

    private Component getImporterComponent(){
        return new ImportButton<T>(configurator.getEntityContainer(), configurator.getEntityFieldGroupFactory());
    }


    private DeleteButton<T> buildDeleteButton(){
        if(configurator.isDeleteAllowed()){
            deleteButton = new DeleteButton<T>(configurator.getEntityContainer(), grid);
            deleteButton.setListeners(configurator.getDeleteListeners());
        }
        return deleteButton;
    }


    /*********** FORM AND IMPORT FIELDS SPECIFIC ***********/

    private Button buildAddButton() {
        if(configurator.isAddAllowed()) {
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
        WindowForm<T> windowForm = new WindowForm<T>(configurator){
            @Override
            protected Field customizeFormField(String fieldId, Field field) {
                return GridFooter.this.customizeFormField(fieldId, field);
            }
        };
        getUI().addWindow(windowForm);
        windowForm.addCloseListener(p->
                addButton.setVisible(true)
        );
    }

    protected Field customizeFormField(String fieldId, Field field){
        return field;
    }

    public GridFooter grid(JPAGrid<T> grid) {
        this.grid = grid;
        return this;
    }

    public DeleteButton<T> getDeleteButton() {
        return deleteButton;
    }
}
