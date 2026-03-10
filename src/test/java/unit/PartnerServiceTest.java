package unit;

import com.estore.dao.PartnerDao;
import com.estore.dto.PartnerDto;
import com.estore.entity.Partner;
import com.estore.service.PartnerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PartnerServiceTest {
    @Mock private PartnerDao daoMock;
    @InjectMocks private PartnerService service;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * This is an example of how to mock the DAO methods using Mockito.
     * We are calling a service method while providing a mock of the DAO method so it does not call the database.
     * Reference: https://javacodehouse.com/blog/mockito-tutorial
     */
    @Test
    public void getPartnerById() throws Exception {
        when(daoMock.get(any(), anyInt())).thenReturn(new Partner());
        PartnerDto partner = service.getPartner(0);
        assertNotNull(partner);
    }
}