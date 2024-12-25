package com.travelagency.common.storage;

import java.util.List;
import java.util.Optional;

public interface Storage<T, ID> {
    T save(T entity);
    List<T> findAll();
    Optional<T> findById(ID id);
    void delete(ID id);
    boolean exists(ID id);
} 