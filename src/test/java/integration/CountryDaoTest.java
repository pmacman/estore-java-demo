package integration;

import com.estore.dao.CountryDao;
import com.estore.entity.Country;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CountryDaoTest {
    @Test
    public void getAll()
    {
        CountryDao dao = new CountryDao();
        List<Country> countries = dao.getAll(Country.class);
        assertTrue(countries.size() > 0);
    }

    @Test
    public void getCountryByIso2Code()
    {
        String iso2UnitedStates = "US";
        CountryDao dao = new CountryDao();
        Country country = dao.getByIso2Code(iso2UnitedStates);
        assertEquals(iso2UnitedStates, country.getIso2());
    }
}