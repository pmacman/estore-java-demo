package com.estore.dao;

import com.estore.entity.Country;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Component
public class CountryDao extends DaoImpl<Country> implements Dao<Country> {
    public Country getByIso2Code(String iso2) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(Country.class, iso2);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Country get(Class<Country> type, int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Country create(Country country) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Country update(Country country) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Country country) {
        throw new UnsupportedOperationException();
    }
}