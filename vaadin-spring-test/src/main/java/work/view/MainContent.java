package work.view;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import vaadin.commons.ui.components.view.CompleteGrid;
import work.AuthorizationManager;
import work.view.contact.UserGrid;

import javax.annotation.PostConstruct;
@SpringComponent
@UIScope
public class MainContent extends VerticalLayout {

	@Autowired
	private UserGrid userGrid;

	@Autowired
	AuthorizationManager authorizationManager;

	private Panel panel;

	@PostConstruct
	protected void init(){
		panel = new Panel();
		setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		panel.setWidth(975, Unit.PIXELS);
		MenuBar mainMenu = new MenuBar();
		addMenuItem("users", mainMenu, panel, userGrid);
		addComponent(mainMenu);
		mainMenu.setWidth(975, Unit.PIXELS);
		addComponent(panel);
	}

	private void addMenuItem(String label, MenuBar mainMenu,
	                                final Panel parent,final CompleteGrid grid){
		mainMenu.addItem(label, new MenuBar.Command() {
			@Override
			public void menuSelected(MenuBar.MenuItem selectedItem) {
				parent.setContent(grid.start());
			}
		});
	}
}
