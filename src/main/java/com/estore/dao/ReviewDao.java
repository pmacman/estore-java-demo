package com.estore.dao;

import com.estore.entity.Review;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewDao extends DaoImpl<Review> implements Dao<Review> {
    public List<Review> getReviewsByCustomer(int customerId)
    {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "from Review where customer.id = :customerId";
            return session.createQuery(hql, Review.class).setParameter("customerId", customerId).list();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public List<Review> getReviewsByProduct(int productId)
    {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "from Review where product.id = :productId";
            return session.createQuery(hql, Review.class).setParameter("productId", productId).list();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}