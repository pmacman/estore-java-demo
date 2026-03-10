package com.estore.dao;

import com.estore.entity.Product;
import com.estore.model.ProductSearch;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import java.util.List;

@Component
public class ProductDao extends DaoImpl<Product> implements Dao<Product> {

    public List<Product> getAll(Class<Product> type) {
        throw new UnsupportedOperationException();
    }

    public List<Product> search(ProductSearch filter) {
        try (Session session = HibernateUtil.getSession()) {
            String productName = filter.getProductName();
            String status = filter.getStatus();
            Integer partnerId = filter.getPartnerId();
            String hql = "from Product ";

            if (productName != null) {
                hql += "where lower(name) like :productName ";
            }

            if (status != null) {
                if (productName == null) {
                    hql += "where status = :status ";
                } else {
                    hql += "and status = :status ";
                }
            }

            if (partnerId != null && partnerId > 0) {
                if (productName == null && status == null) {
                    hql += "where partner.id = :partnerId ";
                } else {
                    hql += "and partner.id = :partnerId ";
                }
            }

            TypedQuery<Product> query = session.createQuery(hql, Product.class);

            if (productName != null) {
                query.setParameter("productName", "%" + productName.toLowerCase() + "%");
            }

            if (status != null) {
                query.setParameter("status", status);
            }

            if (partnerId != null && partnerId > 0) {
                query.setParameter("partnerId", partnerId);
            }

            query.setFirstResult(filter.getStart()).setMaxResults(filter.getLength());

            return query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public Long count(ProductSearch filter) {
        try (Session session = HibernateUtil.getSession()) {
            String productName = filter.getProductName();
            String status = filter.getStatus();
            Integer partnerId = filter.getPartnerId();
            String hql = "select count(*) from Product ";

            if (productName != null) {
                hql += "where lower(name) like :productName ";
            }

            if (status != null) {
                if (productName == null) {
                    hql += "where status = :status ";
                } else {
                    hql += "and status = :status ";
                }
            }

            if (partnerId != null && partnerId > 0) {
                if (productName == null && status == null) {
                    hql += "where partner.id = :partnerId ";
                } else {
                    hql += "and partner.id = :partnerId ";
                }
            }

            TypedQuery<Long> query = session .createQuery(hql, Long.class);

            if (productName != null) {
                query.setParameter("productName", "%" + productName.toLowerCase() + "%");
            }

            if (status != null) {
                query.setParameter("status", status);
            }

            if (partnerId != null && partnerId > 0) {
                query.setParameter("partnerId", partnerId);
            }

            return query.getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}