package vaadin.commons.ui.components.view.form;

public interface FormEvent {
    void afterSaving(JPAForm form, Object entity) ;

    void afterCanceling(JPAForm form);
}
