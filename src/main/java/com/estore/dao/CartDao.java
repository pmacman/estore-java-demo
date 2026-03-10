package com.estore.dao;

import com.estore.entity.Cart;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Component
public class CartDao extends DaoImpl<Cart> implements Dao<Cart> {
    public Cart getByCustomer(int customerId)
    {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "from Cart where customer.id = :customerId order by id desc";
            return session.createQuery(hql, Cart.class).setParameter("customerId", customerId).setMaxResults(1).uniqueResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}