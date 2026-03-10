package integration;

import com.estore.dao.CustomerDao;
import com.estore.entity.Country;
import com.estore.entity.Customer;
import com.estore.entity.State;
import org.junit.Test;

import static org.junit.Assert.*;

public class CustomerDaoTest {
    @Test
    public void customerCrud() {
        // Create
        Customer customerToCreate = create();
        assertNotNull(customerToCreate);

        // Read
        Customer createdCustomer = getById(customerToCreate.getId());
        assertEquals(customerToCreate.getId(), createdCustomer.getId());

        // Update
        createdCustomer.setFirstName("updated");
        Customer updatedCustomer = update(createdCustomer);
        assertEquals(createdCustomer.getFirstName(), updatedCustomer.getFirstName());

        // Read
        updatedCustomer = getById(updatedCustomer.getId());
        assertEquals(createdCustomer.getFirstName(), updatedCustomer.getFirstName());

        // Deactivate
        Customer deactivatedCustomer = deactivate(updatedCustomer);
        assertFalse(updatedCustomer.getActive());

        // Read
        deactivatedCustomer = getById(deactivatedCustomer.getId());
        assertFalse(deactivatedCustomer.getActive());

        // Delete
        delete(deactivatedCustomer);
        Customer remainingCustomer = getById(deactivatedCustomer.getId());
        assertNull(remainingCustomer);
    }

    private Customer getById(int customerId)
    {
        CustomerDao dao = new CustomerDao();
        return dao.get(Customer.class, customerId);
    }

    private Customer create() {
        State state = new State(17, "IL", "Illinois", "US");
        Country country = new Country("US", "United States");
        Customer customerToSave = new Customer("123", "first", "last",
                "street1", "street2","city", state, "60611", country, "555-555-5555",
                "company@email.com", true);
        CustomerDao dao = new CustomerDao();
        return dao.create(customerToSave);
    }

    private Customer update(Customer customer)
    {
        CustomerDao dao = new CustomerDao();
        return dao.update(customer);
    }

    private Customer deactivate(Customer customer)
    {
        CustomerDao dao = new CustomerDao();
        return dao.deactivate(customer);
    }

    private void delete(Customer customer)
    {
        CustomerDao dao = new CustomerDao();
        dao.delete(customer);
    }
}