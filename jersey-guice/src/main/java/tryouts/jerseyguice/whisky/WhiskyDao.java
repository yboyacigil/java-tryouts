package tryouts.jerseyguice.whisky;

import java.util.List;
import java.util.Optional;

public interface WhiskyDao {

    List<Whisky> readAll();

    Optional<Whisky> read(String id);

    Optional<Whisky> create(Whisky whisky);

    Optional<Whisky> update(Whisky whisky);

    Optional<Whisky> delete(String id);
}
