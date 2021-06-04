package pl.gkawalec.pgk.api.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import pl.gkawalec.pgk.common.util.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

@Value
public class SearchPageDTO {

    int pageSize;
    int pageNumber;
    List<SortDTO> sorting;

    public SearchPageDTO() {
        this(10, 0, new ArrayList<>());
    }

    public SearchPageDTO(int pageSize, int pageNumber, List<SortDTO> sorting) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.sorting = sorting;
    }

    public PageRequest toPageRequest() {
        int pageSize = this.pageSize == 0 ? Integer.MAX_VALUE : this.pageSize;
        return PageRequest.of(pageNumber, pageSize, getSortingOrder());
    }

    @JsonIgnore
    public Sort getSortingOrder() {
        List<Sort.Order> orders = getOrders();
        return CollectionUtil.isEmpty(orders) ?
                Sort.unsorted() :
                Sort.by(orders);
    }

    @JsonIgnore
    public List<Sort.Order> getOrders() {
        List<Sort.Order> orders = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(sorting)) {
            sorting.forEach(sort -> {
                Sort.Order order = new Sort.Order(sort.getDirection(), sort.getProperty());
                orders.add(order);
            });
        }
        return orders;
    }

}
