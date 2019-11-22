package airports;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

//mockMvc get- and poost methods
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author Alexey Smolyaninov
 */

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AircraftControllerTest {
    
    @Autowired
    MockMvc mockMvc;
    
    @Autowired
    AircraftController aircraftController;
    
    @Autowired
    AircraftRepository aircraftRepository;
    
    @Test
    public void pathAircraftsIs200AndModelHasCorrectAttributes() throws Exception{
        mockMvc.perform(get("/aircrafts")).andExpect(status().isOk());
        
        mockMvc.perform(get("/aircrafts")).andExpect(model().attributeExists("aircrafts", "airports"));
    }
    
    @Test
    public void createAircraft() throws Exception{
        aircraftRepository.deleteAll();
        final String plane = "HA-LOL";
        mockMvc.perform(post("/aircrafts?name=" + plane)).andExpect(status().is3xxRedirection());
        
        long amountOfAircrafts = aircraftRepository.findAll().stream().filter(aircraft -> aircraft.getName().equals(plane)).count();
        assertTrue(amountOfAircrafts == 1);
    }
    
    @Test
    public void afterCreateAircraftItShouldBeShownInTheView() throws Exception{
        aircraftRepository.deleteAll();
        final String plane = "XP-55";
        
        mockMvc.perform(post("/aircrafts?name=" + plane)).andExpect(status().is3xxRedirection());
        
        MvcResult res = mockMvc.perform(get("/aircrafts")).andExpect(status().isOk()).andReturn();
        
        List<Aircraft> aircrafts = (List) res.getModelAndView().getModel().get("aircrafts");
        
        long amountOfAircrafts = aircrafts
                .stream()
                .filter(ac -> ac.getName().equals(plane))
                .count();
        assertTrue(amountOfAircrafts == 1);
    }
}
