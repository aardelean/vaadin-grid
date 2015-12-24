package work.view;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by aardelean on 12.10.15.
 */
@SpringUI(path = "/test")
@Theme(value = "chameleon")
public class MainPage extends UI {

	@Autowired
	private MainContent mainContent;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		setContent(mainContent);
	}
}
