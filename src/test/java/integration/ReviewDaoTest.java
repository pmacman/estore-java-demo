package integration;

import com.estore.dao.ReviewDao;
import com.estore.entity.Customer;
import com.estore.entity.Product;
import com.estore.entity.Review;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReviewDaoTest {
    @Test
    public void reviewCrud() {
        // Create
        Review reviewToCreate = create();
        assertNotNull(reviewToCreate);

        // Read
        Review createdReview = getById(reviewToCreate.getId());
        assertEquals(reviewToCreate.getId(), createdReview.getId());

        // Update
        createdReview.setReview("updated");
        Review updatedReview = update(createdReview);
        assertEquals(createdReview.getReview(), updatedReview.getReview());

        // Read
        updatedReview = getById(updatedReview.getId());
        assertEquals(createdReview.getReview(), updatedReview.getReview());

        // Delete
        delete(updatedReview);
        Review remainingReview = getById(updatedReview.getId());
        assertNull(remainingReview);
    }

    private Review getById(int reviewId)
    {
        ReviewDao dao = new ReviewDao();
        return dao.get(Review.class, reviewId);
    }

    private Review create() {
        Customer customer = new Customer();
        customer.setId(1);
        Product product = new Product();
        product.setId(1);
        Review reviewToSave = new Review(customer, product, "review");
        ReviewDao dao = new ReviewDao();
        return dao.create(reviewToSave);
    }

    private Review update(Review review)
    {
        ReviewDao dao = new ReviewDao();
        return dao.update(review);
    }

    private void delete(Review review)
    {
        ReviewDao dao = new ReviewDao();
        dao.delete(review);
    }
}