package com.estore.dao;

import com.estore.entity.Customer;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Component
public class CustomerDao extends DaoImpl<Customer> implements Dao<Customer> {
    public Customer getByUserId(String userId) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "from Customer where userId = :userId";
            return session.createQuery(hql, Customer.class).setParameter("userId", userId).setMaxResults(1).uniqueResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public Customer deactivate(Customer customer)
    {
        customer.setActive(false);
        return update(customer);
    }
}