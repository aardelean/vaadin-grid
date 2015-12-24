package vaadin.commons.ui.components.view.auditing;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditCommitEvent implements FieldGroup.CommitHandler {

	private static final Logger logger = LoggerFactory.getLogger(AuditCommitEvent.class);
	private String username;

	public AuditCommitEvent(String username) {
		this.username = username;
	}

	@Override
	public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {

	}

	@Override
	public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
		String className = ((BeanItem)commitEvent.getFieldBinder().getItemDataSource()).getBean().getClass().getSimpleName();
		if(username!=null) {
			logger.info("USER: " +
					            username +
					            " commited : " +
					            className +
					            " with values from the form: " +
					            commitEvent.getFieldBinder().getItemDataSource().toString());
		}else{
			logger.warn("username found null for using a database write operation!");
		}
	}
}
