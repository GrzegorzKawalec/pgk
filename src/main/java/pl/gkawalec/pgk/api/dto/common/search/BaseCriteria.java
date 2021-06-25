package pl.gkawalec.pgk.api.dto.common.search;

import lombok.Getter;

@Getter
public class BaseCriteria {

    private final String searchBy;
    private final SearchPageDTO searchPage;

    public BaseCriteria() {
        this("", new SearchPageDTO());
    }

    public BaseCriteria(String searchBy, SearchPageDTO searchPage) {
        this.searchBy = searchBy;
        this.searchPage = searchPage;
    }

}
