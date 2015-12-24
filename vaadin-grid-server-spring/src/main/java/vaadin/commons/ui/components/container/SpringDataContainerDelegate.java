package vaadin.commons.ui.components.container;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SpringDataContainerDelegate implements ContainerDelegate {

    @Autowired
    private EntityManager em;

    private Map<Class<?>, SimpleJpaRepository> repositoryRegistry = new HashMap<>();

    @Override
    public <T> List<T> getItems(ViewSetting viewSetting, Class<T> aClass) {
        return convertPage(getRepository(aClass).findAll(new SpecificationAdapter(viewSetting),
                new PageableAdapter(viewSetting)));
    }

    @Override
    public <T> int size(ViewSetting viewSetting, Class<T> aClass) {
        return new Long(getRepository(aClass).count(new SpecificationAdapter<>(viewSetting))).intValue();
    }

    @Override
    public <T> List<T> getItems(Class<T> aClass) {
        return getRepository(aClass).findAll();
    }

    @Override
    @Transactional
    public <T> void addEntity(T t) {
        em.persist(t);
    }

    @Override
    @Transactional
    public <T> T mergeEntity(T t) {
        return em.merge(t);
    }

    @Override
    @Transactional
    public <T> void deleteEntity(T t) {
        em.remove(t);
    }

    private <T> List<T> convertPage(Page<T> page){
        List<T> resultList = new ArrayList<>();
        page.forEach(p-> resultList.add(p));
        return resultList;
    }

    private <T, ID extends Serializable> SimpleJpaRepository<T, ID> getRepository(Class<T> clazz){
        SimpleJpaRepository repository = repositoryRegistry.get(clazz);
        if(repository == null){
            repositoryRegistry.put(clazz, repository = new SimpleJpaRepository(clazz, em));
        }
        return (SimpleJpaRepository<T, ID>)repository;
    }
}
