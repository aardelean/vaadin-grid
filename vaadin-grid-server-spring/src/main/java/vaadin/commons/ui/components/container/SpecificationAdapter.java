package vaadin.commons.ui.components.container;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Map;
import java.util.Set;

public class SpecificationAdapter<T> implements Specification<T> {

    private ViewSetting viewSetting;

    public SpecificationAdapter(ViewSetting viewSetting) {
        this.viewSetting = viewSetting;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb){
        Set<Map.Entry<String, Object>> filters = viewSetting.getFilters();

        Predicate[] predicates = new Predicate[filters.size()];
        int predicateIndex = 0;
        for (Map.Entry<String, Object> filterEntry : filters) {
            Path<String> paramPath = root.<String>get(filterEntry.getKey());
            String paramValue = filterEntry.getValue() + "%";
            predicates[predicateIndex] = cb.like(cb.lower(paramPath.as(String.class)), paramValue);
            predicateIndex++;
        }
        return cb.and(predicates);
    }
}
