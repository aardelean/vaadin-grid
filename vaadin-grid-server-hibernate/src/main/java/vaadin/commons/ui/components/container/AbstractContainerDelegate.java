package vaadin.commons.ui.components.container;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractContainerDelegate implements ContainerDelegate {

	protected abstract EntityManager getEntityManager();

	@Override
	public <T> List<T> getItems(ViewSetting viewSettings, Class<T> clazz){
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> rootEntry = cq.from(clazz);
		CriteriaQuery<T> all = cq.select(rootEntry);
		Set<Map.Entry<String, Object>> filters = viewSettings.getFilters();
		if(viewSettings.hasFilters() && !filters.isEmpty()) {
			Predicate[] predicates = new Predicate[filters.size()];
			int predicateIndex = 0;
			for (Map.Entry<String, Object> filterEntry : filters) {
				predicates[predicateIndex] = cb.like(
			                                    cb.lower(
		                                            rootEntry.<String>get(
                                                         filterEntry.getKey()
		                                            ).as(String.class)
			                                    ),
			                                    filterEntry.getValue() + "%");
				predicateIndex++;
			}
			all.where(predicates);
		}
		if(viewSettings.hasSort()){
			if(viewSettings.isAsc()){
				all.orderBy(cb.asc(cb.lower(rootEntry.<String>get(viewSettings.getSort()))));
			}else{
				all.orderBy(cb.desc(cb.lower(rootEntry.<String>get(viewSettings.getSort()))));
			}
		}
		TypedQuery<T> allQuery = getEntityManager().createQuery(all);
		allQuery.setMaxResults(viewSettings.getPageSize());
		allQuery.setFirstResult((viewSettings.getPageNo()-1) * viewSettings.getPageSize());
		return allQuery.getResultList();
	}

	@Override
	public <T> int size(ViewSetting viewSettings, Class<T> clazz){
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<T> rootEntry = cq.from(clazz);
		CriteriaQuery<Long> all = cq.select(cb.count(rootEntry));
		Set<Map.Entry<String, Object>> filters = viewSettings.getFilters();
		if(viewSettings.hasFilters() && !filters.isEmpty()) {
			Predicate[] predicates = new Predicate[filters.size()];
			int predicateIndex = 0;
			for (Map.Entry<String, Object> filterEntry : filters) {
				predicates[predicateIndex] = cb.like(
						                      cb.lower(
					                              rootEntry.<String>get(
				                                       filterEntry.getKey()
					                              ).as(String.class)
						                      ),
						                      filterEntry.getValue() + "%");
				predicateIndex++;
			}
			all.where(predicates);
		}
		TypedQuery<Long> allQuery = getEntityManager().createQuery(all);
		int size = allQuery.getSingleResult().intValue();
		return size;
	}

	@Override
	public <T> List<T> getItems(Class<T> clazz) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> rootEntry = cq.from(clazz);
		CriteriaQuery<T> all = cq.select(rootEntry);
		TypedQuery<T> allQuery = em.createQuery(all);
		return allQuery.getResultList();
	}

	@Override
	public <T> void addEntity(T entity) throws UnsupportedOperationException, IllegalStateException {
		getEntityManager().persist(entity);
	}

	@Override
	public <T> T mergeEntity(T entity) {
		return getEntityManager().merge(entity);
	}

	@Override
	public <T> void deleteEntity(T entity) {
		entity =  mergeEntity(entity);
		getEntityManager().remove(entity);
	}
}
