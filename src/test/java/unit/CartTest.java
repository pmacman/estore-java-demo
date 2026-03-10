package unit;

import com.estore.entity.Cart;
import com.estore.entity.CartDetail;
import com.estore.entity.Product;
import com.estore.infrastructure.EstoreConstants;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CartTest {
    @Test
    public void calculateBaseTotal() {
        // Arrange
        Product product = new Product();
        product.setPrice(10);
        CartDetail cartDetail1 = new CartDetail(product, 1);
        CartDetail cartDetail2 = new CartDetail(product, 2);
        List<CartDetail> cartDetails = new ArrayList<>();
        cartDetails.add(cartDetail1);
        cartDetails.add(cartDetail2);
        Cart cart = new Cart();
        cart.setCartDetails(cartDetails);

        // Act
        double result = cart.calculateBaseTotal();

        // Assert
        assertEquals(30, result, 0);
    }

    @Test
    public void calculateTax() {
        // Arrange
        Product product = new Product();
        product.setPrice(10);
        CartDetail cartDetail1 = new CartDetail(product, 1);
        CartDetail cartDetail2 = new CartDetail(product, 2);
        List<CartDetail> cartDetails = new ArrayList<>();
        cartDetails.add(cartDetail1);
        cartDetails.add(cartDetail2);
        Cart cart = new Cart();
        cart.setCartDetails(cartDetails);

        // Act
        double result = cart.calculateTax();

        // Assert
        assertEquals(3, result, 0);
    }

    @Test
    public void calculateShippingCost() {
        // Arrange
        Product product = new Product();
        product.setPrice(10);
        Cart cart = new Cart();
        cart.setShippingMethod(EstoreConstants.ShippingMethod.STANDARD.name());

        // Act
        double result = cart.calculateShippingCost();

        // Assert
        assertEquals(5, result, 0);
    }

    @Test
    public void calculateTotal() {
        // Arrange
        Product product = new Product();
        product.setPrice(10);
        CartDetail cartDetail1 = new CartDetail(product, 1);
        CartDetail cartDetail2 = new CartDetail(product, 2);
        List<CartDetail> cartDetails = new ArrayList<>();
        cartDetails.add(cartDetail1);
        cartDetails.add(cartDetail2);
        Cart cart = new Cart();
        cart.setCartDetails(cartDetails);
        cart.setShippingMethod(EstoreConstants.ShippingMethod.PRIORITY.name());

        // Act
        double result = cart.calculateTotal();

        // Assert
        assertEquals(43, result, 0);
    }
}