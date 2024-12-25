package com.travelagency.common.strategy;

import java.util.List;
    /** implementing Strategy pattern on the search functionality **/
public interface SearchStrategy<T> {
    List<T> search(List<T> items, String searchTerm);
}