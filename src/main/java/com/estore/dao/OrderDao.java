package com.estore.dao;

import com.estore.entity.Order;
import com.estore.model.OrderSearch;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import java.util.List;

@Component
public class OrderDao extends DaoImpl<Order> implements Dao<Order> {
    @Override
    public List<Order> getAll(Class<Order> type) {
        throw new UnsupportedOperationException();
    }

    public List<Order> search(OrderSearch filter) {
        try (Session session = HibernateUtil.getSession()) {
            Integer customerId = filter.getCustomerId();
            Integer partnerId = filter.getPartnerId();
            String status = filter.getStatus();
            String hql = "select o from Order as o join o.orderDetails as od ";

            if (customerId != null && customerId > 0) {
                hql += "where o.customer.id = :customerId ";
                if (partnerId != null && partnerId > 0) {
                    hql += "and od.product.partner.id = :partnerId ";
                }
            } else if (partnerId != null && partnerId > 0) {
                hql += "where od.product.partner.id = :partnerId ";
            }

            if (status != null) {
                hql += "and o.status = :status ";
            }

            TypedQuery<Order> query = session.createQuery(hql, Order.class);

            if (customerId != null && customerId > 0) {
                query.setParameter("customerId", customerId);
            }

            if (partnerId != null && partnerId > 0) {
                query.setParameter("partnerId", partnerId);
            }

            if (status != null) {
                query.setParameter("status", status);
            }

            query.setFirstResult(filter.getStart()).setMaxResults(filter.getLength());

            return query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}