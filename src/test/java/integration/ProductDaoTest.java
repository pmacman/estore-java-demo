package integration;

import com.estore.dao.ProductDao;
import com.estore.entity.Partner;
import com.estore.entity.Product;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProductDaoTest {
    @Test
    public void productCrud() {
        // Create
        Product productToCreate = create();
        assertNotNull(productToCreate);

        // Read
        Product createdProduct = getById(productToCreate.getId());
        assertEquals(productToCreate.getId(), createdProduct.getId());

        // Update
        createdProduct.setName("updated");
        Product updatedProduct = update(createdProduct);
        assertEquals(createdProduct.getName(), updatedProduct.getName());

        // Read
        updatedProduct = getById(updatedProduct.getId());
        assertEquals(createdProduct.getName(), updatedProduct.getName());

        // Delete
        delete(updatedProduct);
        Product remainingProduct = getById(updatedProduct.getId());
        assertNull(remainingProduct);
    }

    private Product getById(int productId)
    {
        ProductDao dao = new ProductDao();
        return dao.get(Product.class, productId);
    }

    private Product create() {
        Partner partner = new Partner();
        partner.setId(1);
        Product productToSave = new Product(partner, "name", "description", 100, 1000, "ACTIVE");
        ProductDao dao = new ProductDao();
        return dao.create(productToSave);
    }

    private Product update(Product product)
    {
        ProductDao dao = new ProductDao();
        return dao.update(product);
    }

    private void delete(Product product)
    {
        ProductDao dao = new ProductDao();
        dao.delete(product);
    }
}