package integration;

import com.estore.dao.OrderDao;
import com.estore.entity.Customer;
import com.estore.entity.Order;
import com.estore.entity.OrderDetail;
import com.estore.entity.Product;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrderDaoTest {
    @Test
    public void orderDaoTest() {
        // Create
        Order orderToCreate = create();
        assertNotNull(orderToCreate);

        // Read
        Order createdOrder = getById(orderToCreate.getId());
        assertEquals(orderToCreate.getId(), createdOrder.getId());

        // Update
        createdOrder.setStatus("COMPLETE");
        Order updatedOrder = update(createdOrder);
        assertEquals(createdOrder.getStatus(), updatedOrder.getStatus());

        // Read
        updatedOrder = getById(updatedOrder.getId());
        assertEquals(createdOrder.getStatus(), updatedOrder.getStatus());

        // Delete
        delete(updatedOrder);
        Order remainingOrder = getById(updatedOrder.getId());
        assertNull(remainingOrder);
    }

    private Order getById(int orderId)
    {
        OrderDao dao = new OrderDao();
        return dao.get(Order.class, orderId);
    }

    private Order create() {
        Customer customer = new Customer();
        customer.setId(1);
        Product product = new Product();
        product.setId(1);
        product.setPrice(10);
        OrderDetail orderDetail = new OrderDetail(product, 1);
        List<OrderDetail> orderDetails = new ArrayList<>();
        orderDetails.add(orderDetail);
        Order orderToSave = new Order(customer, orderDetails, "STANDARD", "PENDING");
        OrderDao dao = new OrderDao();
        return dao.create(orderToSave);
    }

    private Order update(Order order)
    {
        OrderDao dao = new OrderDao();
        return dao.update(order);
    }

    private void delete(Order order)
    {
        OrderDao dao = new OrderDao();
        dao.delete(order);
    }
}