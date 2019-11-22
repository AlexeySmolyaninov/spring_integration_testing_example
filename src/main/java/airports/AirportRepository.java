package airports;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<Airport, Long> {

    public Airport getOneByIdentifier(String identifier);
}
