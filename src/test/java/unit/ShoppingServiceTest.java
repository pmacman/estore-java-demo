package unit;

import com.estore.controller.api.exception.ProductOutOfStockException;
import com.estore.dao.*;
import com.estore.dto.CartDto;
import com.estore.entity.*;
import com.estore.model.CartItem;
import com.estore.service.ShoppingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Unit tests for shopping service. These should not call the database. Use Mockito when needed.
 */
@RunWith(MockitoJUnitRunner.class)
public class ShoppingServiceTest {
	@Mock private CartDao mockCartDao;
    @Mock private CartDetailDao mockCartDetailDao;
	@Mock private CustomerDao mockCustomerDao;
    @Mock private OrderDao mockOrderDao;
    @Mock private ProductDao mockProductDao;
    @InjectMocks private ShoppingService service;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

	@Test
    public void addItemToCart_ItemInExistingCart_IncrementQuantity() throws Exception {
        // Arrange
        Cart fakeCart = getFakeCartWithOneProduct(100);
        Product fakeProduct = new Product();
        fakeProduct.setQuantity(100);
        when(mockCustomerDao.get(any(), anyInt())).thenReturn(new Customer());
        when(mockProductDao.get(any(), anyInt())).thenReturn(fakeProduct);
        when(mockCartDao.getByCustomer(anyInt())).thenReturn(fakeCart);
        when(mockCartDao.update(any())).thenReturn(fakeCart);
        CartItem cartItem = new CartItem(1, 1, 1);

        // Act - add duplicate item to cart (same product ID #1)
        service = new ShoppingService(mockCartDao, mockCartDetailDao, mockCustomerDao, mockOrderDao, mockProductDao);
        CartDto result = service.upsertItemToCart(cartItem);

        // Assert - product quantity should be incremented instead of adding additional item to cart
        List<CartDetail> cartDetailResults = result.getCart().getCartDetails();
        assertEquals(1, cartDetailResults.size());
        assertEquals(2, cartDetailResults.get(0).getQuantity());
    }

    @Test
    public void addItemToCart_ItemNotInExistingCart_AddItem() throws Exception {
        // Arrange
        Cart fakeCart = getFakeCartWithOneProduct(100);
        Product fakeProduct = new Product();
        fakeProduct.setQuantity(100);
        when(mockCustomerDao.get(any(), anyInt())).thenReturn(new Customer());
        when(mockProductDao.get(any(), anyInt())).thenReturn(fakeProduct);
        when(mockCartDao.getByCustomer(anyInt())).thenReturn(fakeCart);
        when(mockCartDao.update(any())).thenReturn(fakeCart);
        CartItem cartItem = new CartItem(1, 2, 2);

        // Act - add different item to cart (different product ID #2)
        service = new ShoppingService(mockCartDao, mockCartDetailDao, mockCustomerDao, mockOrderDao, mockProductDao);
        CartDto result = service.upsertItemToCart(cartItem);

        // Assert - product should be added to cart
        List<CartDetail> cartDetailResults = result.getCart().getCartDetails();
        assertEquals(2, cartDetailResults.size());
        assertEquals(1, cartDetailResults.get(0).getQuantity());
        assertEquals(2, cartDetailResults.get(1).getQuantity());
    }

    @Test
    public void addItemToCart_AddingOutOfStockItemToExistingCart_DoesNotAddItem() throws Exception {
        // Arrange
        Cart fakeCart = getFakeCartWithOneProduct(1);
        Product fakeProduct = new Product();
        fakeProduct.setQuantity(100);
        when(mockCustomerDao.get(any(), anyInt())).thenReturn(new Customer());
        when(mockProductDao.get(any(), anyInt())).thenReturn(fakeProduct);
        when(mockCartDao.getByCustomer(anyInt())).thenReturn(fakeCart);
        when(mockCartDao.update(any())).thenReturn(fakeCart);
        CartItem cartItem = new CartItem(1, 1, 100);

        // Act - add out of stock item to cart
        service = new ShoppingService(mockCartDao, mockCartDetailDao, mockCustomerDao, mockOrderDao, mockProductDao);
        CartDto result = service.upsertItemToCart(cartItem);

        // Assert - out of stock item should not be added to cart
        List<CartDetail> cartDetailResults = result.getCart().getCartDetails();
        assertEquals(1, cartDetailResults.size());
        assertEquals(1, cartDetailResults.get(0).getQuantity());
    }

    @Test(expected = ProductOutOfStockException.class)
    public void addItemToCart_AddingOutOfStockItemToNewCart_DoesNotAddItem() throws Exception {
        // Arrange
        when(mockCustomerDao.get(any(), anyInt())).thenReturn(new Customer());
        Product product = new Product();
        product.setQuantity(1);
        when(mockProductDao.get(any(), anyInt())).thenReturn(product);
        when(mockCartDao.getByCustomer(anyInt())).thenReturn(null);
        //when(mockCartDao.create(any())).thenAnswer(invocation -> invocation.getArgument(0, Cart.class));
        CartItem cartItem = new CartItem(1, 1, 100);

        // Act - add out of stock item to cart
        service = new ShoppingService(mockCartDao, mockCartDetailDao, mockCustomerDao, mockOrderDao, mockProductDao);
        CartDto result = service.upsertItemToCart(cartItem);

        // Assert ProductOutOfStockException thrown
    }

    @Test
    public void addItemToCart_AddingInStockItemToNewCart_AddsItem() throws Exception {
        // Arrange
        when(mockCustomerDao.get(any(), anyInt())).thenReturn(new Customer());
        Product product = new Product();
        product.setQuantity(100);
        when(mockProductDao.get(any(), anyInt())).thenReturn(product);
        when(mockCartDao.getByCustomer(anyInt())).thenReturn(null);
        when(mockCartDao.create(any())).thenAnswer(invocation -> invocation.getArgument(0, Cart.class));
        CartItem cartItem = new CartItem(1, 1, 1);

        // Act - add in stock item to cart
        service = new ShoppingService(mockCartDao, mockCartDetailDao, mockCustomerDao, mockOrderDao, mockProductDao);
        CartDto result = service.upsertItemToCart(cartItem);

        // Assert - in stock item should be added to cart
        List<CartDetail> cartDetailResults = result.getCart().getCartDetails();
        assertEquals(1, cartDetailResults.size());
    }

    private Cart getFakeCartWithOneProduct(int productQuantity) {
        Customer customer = new Customer();
        customer.setId(1);
        Partner partner = new Partner();
        partner.setId(1);
        Product product = new Product();
        product.setId(1);
        product.setQuantity(productQuantity);
        product.setPartner(partner);
        product.setQuantity(100);
        CartDetail cartDetail = new CartDetail(product, 1);
        cartDetail.setId(1);
        List<CartDetail> cartDetails = new ArrayList<>();
        cartDetails.add(cartDetail);
        Cart cart = new Cart(customer, cartDetails);
        cart.setId(1);
        return cart;
    }
}