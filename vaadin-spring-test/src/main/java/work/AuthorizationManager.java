package work;

import org.springframework.stereotype.Component;
import work.view.contact.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthorizationManager {

	private Map<Class<?>, RoleMappings> roles = new HashMap<>();

	@PostConstruct
	public void setup(){
		roles.put(UserGrid.class, new RoleMappings(true, true));
	}

	public boolean hasAccessTo(Object resource){
		return roles.get(resource.getClass())!=null?roles.get(resource.getClass()).hasAccess():false;
	}
	public boolean hasWriteAccess(Object resource){
		return roles.get(resource.getClass())!=null?roles.get(resource.getClass()).hasWriteAccess():false;
	}
}
