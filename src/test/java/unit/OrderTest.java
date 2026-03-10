package unit;

import com.estore.entity.Order;
import com.estore.entity.OrderDetail;
import com.estore.entity.Product;
import com.estore.infrastructure.EstoreConstants;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderTest {
    @Test
    public void calculateBaseTotal() {
        // Arrange
        Product product = new Product();
        product.setPrice(10);
        OrderDetail orderDetail1 = new OrderDetail(product, 1);
        OrderDetail orderDetail2 = new OrderDetail(product, 2);
        List<OrderDetail> orderDetails = new ArrayList<>();
        orderDetails.add(orderDetail1);
        orderDetails.add(orderDetail2);
        Order order = new Order();
        order.setOrderDetails(orderDetails);

        // Act
        double result = order.calculateBaseTotal();

        // Assert
        assertEquals(30, result, 0);
    }

    @Test
    public void calculateTax() {
        // Arrange
        Product product = new Product();
        product.setPrice(10);
        OrderDetail orderDetail1 = new OrderDetail(product, 1);
        OrderDetail orderDetail2 = new OrderDetail(product, 2);
        List<OrderDetail> orderDetails = new ArrayList<>();
        orderDetails.add(orderDetail1);
        orderDetails.add(orderDetail2);
        Order order = new Order();
        order.setOrderDetails(orderDetails);

        // Act
        double result = order.calculateTax();

        // Assert
        assertEquals(3, result, 0);
    }

    @Test
    public void calculateShippingCost() {
        // Arrange
        Product product = new Product();
        product.setPrice(10);
        Order order = new Order();
        order.setShippingMethod(EstoreConstants.ShippingMethod.STANDARD.name());

        // Act
        double result = order.calculateShippingCost();

        // Assert
        assertEquals(5, result, 0);
    }

    @Test
    public void calculateTotal() {
        // Arrange
        Product product = new Product();
        product.setPrice(10);
        OrderDetail orderDetail1 = new OrderDetail(product, 1);
        OrderDetail orderDetail2 = new OrderDetail(product, 2);
        List<OrderDetail> orderDetails = new ArrayList<>();
        orderDetails.add(orderDetail1);
        orderDetails.add(orderDetail2);
        Order order = new Order();
        order.setOrderDetails(orderDetails);
        order.setShippingMethod(EstoreConstants.ShippingMethod.PRIORITY.name());

        // Act
        double result = order.calculateTotal();

        // Assert
        assertEquals(43, result, 0);
    }
}