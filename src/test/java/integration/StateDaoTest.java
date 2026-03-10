package integration;

import com.estore.dao.StateDao;
import com.estore.entity.State;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StateDaoTest {
    @Test
    public void getByCountry()
    {
        String iso2UnitedStates = "US";
        StateDao dao = new StateDao();
        List<State> states = dao.getByCountry(iso2UnitedStates);
        List<State> nonUsStates = states.stream().filter(s -> !s.getCountryIso2().equals("US")).collect(Collectors.toList());
        assertTrue(states.size() > 0);
        assertEquals(0, nonUsStates.size());
    }
}