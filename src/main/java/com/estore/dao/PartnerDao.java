package com.estore.dao;

import com.estore.entity.Partner;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PartnerDao extends DaoImpl<Partner> implements Dao<Partner> {
    @Override
    public List<Partner> getAll(Class<Partner> type) {
        throw new UnsupportedOperationException();
    }

    public Partner getByContactUserId(String userId) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "select p from Partner as p join p.partnerContacts as pc where pc.userId = :userId";
            return session.createQuery(hql, Partner.class).setParameter("userId", userId).setMaxResults(1).uniqueResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public Partner deactivate(Partner partner)
    {
        partner.setActive(false);
        return update(partner);
    }
}