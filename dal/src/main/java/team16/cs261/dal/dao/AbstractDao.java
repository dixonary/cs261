package team16.cs261.dal.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 14/02/15.
 */
public abstract class AbstractDao<E> {

    @Autowired
    public JdbcTemplate jdbcTemplate;

    public void insert(E e) {

    }

    public void insert(Iterable<E> e) {

    }

    public List<E> findAll() {
        return null;
    }

    public E findById(int id) {
        return null;
    }


}
