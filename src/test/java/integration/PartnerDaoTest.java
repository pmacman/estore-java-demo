package integration;

import com.estore.dao.PartnerDao;
import com.estore.entity.Country;
import com.estore.entity.Partner;
import com.estore.entity.PartnerContact;
import com.estore.entity.State;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PartnerDaoTest {
    @Test
    public void partnerCrud()
    {
        // Create
        Partner partnerToCreate = create();
        assertNotNull(partnerToCreate);

        // Read
        Partner createdPartner = getById(partnerToCreate.getId());
        assertEquals(partnerToCreate.getId(), createdPartner.getId());
        assertEquals(partnerToCreate.getPartnerContacts().size(), createdPartner.getPartnerContacts().size());

        // Update
        createdPartner.setCompanyName("updated");
        Partner updatedPartner = update(createdPartner);
        assertEquals(createdPartner.getCompanyName(), updatedPartner.getCompanyName());

        // Read
        updatedPartner = getById(updatedPartner.getId());
        assertEquals(createdPartner.getCompanyName(), updatedPartner.getCompanyName());

        // Deactivate
        Partner deactivatedPartner = deactivate(updatedPartner);
        assertFalse(deactivatedPartner.getActive());

        // Read
        deactivatedPartner = getById(deactivatedPartner.getId());
        assertFalse(deactivatedPartner.getActive());

        // Delete
        delete(deactivatedPartner);
        Partner remainingPartner = getById(deactivatedPartner.getId());
        assertNull(remainingPartner);
    }

    private Partner getById(int partnerId)
    {
        PartnerDao dao = new PartnerDao();
        return dao.get(Partner.class, partnerId);
    }

    private Partner create()
    {
        State state = new State(17, "IL", "Illinois", "US");
        Country country = new Country("US", "United States");
        List<PartnerContact> partnerContacts = new ArrayList<>();
        partnerContacts.add(new PartnerContact("123"));
        Partner partnerToSave = new Partner("Partner Unit Test", "description",
                "123 Elm St", "Ste 101", "Chicago", state, "60611", country,
                "555-555-5555", "companytest@email.com", true, partnerContacts);
        PartnerDao dao = new PartnerDao();
        return dao.create(partnerToSave);
    }

    private Partner update(Partner partner)
    {
        PartnerDao dao = new PartnerDao();
        return dao.update(partner);
    }

    private Partner deactivate(Partner partner)
    {
        PartnerDao dao = new PartnerDao();
        return dao.deactivate(partner);
    }

    private void delete(Partner partner)
    {
        PartnerDao dao = new PartnerDao();
        dao.delete(partner);
    }
}