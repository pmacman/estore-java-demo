package integration;

import com.estore.dao.CartDao;
import com.estore.entity.Cart;
import com.estore.entity.CartDetail;
import com.estore.entity.Customer;
import com.estore.entity.Product;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CartDaoTest {
    @Test
    public void cartCrud() {
        // Create
        Cart cartToCreate = create();
        assertNotNull(cartToCreate);

        // Read
        Cart createdCart = getById(cartToCreate.getId());
        assertEquals(cartToCreate.getId(), createdCart.getId());
        createdCart = getByCustomerId(1);
        assertEquals(cartToCreate.getId(), createdCart.getId());

        // Update
        List<CartDetail> cartDetails = createdCart.getCartDetails();
        cartDetails.get(0).setQuantity(2);
        Cart updatedCart = update(createdCart);
        assertEquals(2, updatedCart.getCartDetails().get(0).getQuantity());

        // Read
        updatedCart = getById(updatedCart.getId());
        assertEquals(createdCart.getCartDetails().get(0).getQuantity(), updatedCart.getCartDetails().get(0).getQuantity());

        // Delete
        delete(createdCart);
        Cart remainingCart = getById(createdCart.getId());
        assertNull(remainingCart);
    }

    private Cart getById(int cartId)
    {
        CartDao dao = new CartDao();
        return dao.get(Cart.class, cartId);
    }

    private Cart getByCustomerId(int customerId)
    {
        CartDao dao = new CartDao();
        return dao.getByCustomer(customerId);
    }

    private Cart create() {
        Cart cart = new Cart();
        cart.setId(1);
        Customer customer = new Customer();
        customer.setId(1);
        Product product = new Product();
        product.setId(1);
        CartDetail cartDetail = new CartDetail(product, 1);
        List<CartDetail> cartDetails = new ArrayList<>();
        cartDetails.add(cartDetail);
        Cart cartToSave = new Cart(customer, cartDetails);
        CartDao dao = new CartDao();
        return dao.create(cartToSave);
    }

    private Cart update(Cart cart)
    {
        CartDao dao = new CartDao();
        return dao.update(cart);
    }

    private void delete(Cart cart)
    {
        CartDao dao = new CartDao();
        dao.delete(cart);
    }
}
