package com.estore.dao;

import java.util.List;

public interface Dao<T> {
    T get(Class<T> type, int id);

    List<T> getAll(Class<T> type);

    T create(T t);

    T update(T t);

    void delete(T t);
}