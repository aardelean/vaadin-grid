package vaadin.commons.ui.components.container;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableAdapter implements Pageable {

    private ViewSetting viewSetting;

    public PageableAdapter(ViewSetting viewSetting) {
        this.viewSetting = viewSetting;
    }

    @Override
    public int getPageNumber() {
        return viewSetting.getPageNo();
    }

    @Override
    public int getPageSize() {
        return viewSetting.getPageSize();
    }

    @Override
    public int getOffset() {
        return (getPageNumber()-1)*getPageSize();
    }

    @Override
    public Sort getSort() {
        Sort sort = null;
        if(viewSetting.hasSort()) {
            Sort.Direction direction = viewSetting.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC;
            Sort.Order order = new Sort.Order(direction, viewSetting.getSort());
            sort = new Sort(order);
        }
        return sort;
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return null;
    }

    @Override
    public Pageable first() {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }
}
