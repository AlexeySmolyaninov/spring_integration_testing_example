package airports;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Assert;

/**
 *
 * @author Alexey Smolyaninov
 */

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class AirportServiceTest {
    
    @Autowired
    private AirportService airportService;
    
    @Autowired
    private AirportRepository airportRepository;
    
    
    @Test
    public void creatingAirport(){
        airportRepository.deleteAll();
        
        Airport airport = new Airport("HEL", "Helsinki", new ArrayList<>());
        airportService.create(airport.getIdentifier(), airport.getName());
        
        List<Airport> airports = airportRepository.findAll(Example.of(airport));
        
        Assert.assertTrue(airports.size() == 1);
    }
    
    @Test
    public void listAirport(){
        airportRepository.deleteAll();
        
        //creating 3 aiprots
        airportService.create("SPB", "Saint-Petersburg");
        airportService.create("HEL", "Helsinki");
        airportService.create("LA", "Los-Angeles");
        
        List<Airport> airports = airportService.list();
        
        List<Airport> spb = airports.stream()
            .filter(airport -> {
                return airport.getIdentifier().equals("SPB") && airport.getName().equals("Saint-Petersburg");
            }).collect(Collectors.toList());
        
        
        List<Airport> frankfurt = airports.stream()
            .filter(airport -> {
                return airport.getIdentifier().equals("FRK") && airport.getName().equals("Frankfurt");
            }).collect(Collectors.toList());
        
        Assert.assertTrue(airports.size() == 3);
        Assert.assertTrue(spb.size() == 1);
        Assert.assertTrue(frankfurt.isEmpty());
    }
    
    @Test
    public void createAirportWillNotCreateSameAirportsMoreThenOnce(){
        //deleting all airports
        airportRepository.deleteAll();
        
        Airport airport = new Airport("HEL", "Helsinki", new ArrayList<>());
        airportService.create(airport.getIdentifier(), airport.getName());
        airportService.create(airport.getIdentifier(), airport.getName());
        
        List<Airport> helsinkiAirport = airportService.list()
                .stream()
                .filter(ap -> {
                    return ap.getIdentifier().equals(airport.getIdentifier());
                })
                .collect(Collectors.toList());
        Assert.assertTrue(helsinkiAirport.size() == 1);
    }
}
