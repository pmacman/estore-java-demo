package com.estore.dao;

import com.estore.entity.State;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StateDao extends DaoImpl<State> implements Dao<State> {
    public List<State> getByCountry(String countryIso2)
    {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "from State where countryIso2 = :countryIso2 order by name";
            return session.createQuery(hql, State.class).setParameter("countryIso2", countryIso2).list();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public State get(Class<State> type, int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<State> getAll(Class<State> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public State create(State state) {
        throw new UnsupportedOperationException();
    }

    @Override
    public State update(State state) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(State state) {
        throw new UnsupportedOperationException();
    }
}