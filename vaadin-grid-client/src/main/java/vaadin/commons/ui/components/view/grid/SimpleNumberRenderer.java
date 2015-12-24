package vaadin.commons.ui.components.view.grid;

import com.vaadin.ui.renderers.NumberRenderer;
import elemental.json.JsonValue;

public class SimpleNumberRenderer extends NumberRenderer {
	@Override
	public JsonValue encode(Number value) {
		if(value!=null) {
			String stringValue = value.toString();
			return super.encode(stringValue, String.class);
		}else{
			return super.encode("", String.class);
		}
	}
}
