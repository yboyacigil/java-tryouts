package tryouts.jerseyguice.whisky;

import com.google.inject.AbstractModule;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class WhiskyModule extends AbstractModule {

    @Override
    protected void configure() {
        WhiskyDao whiskyDao = provideMemoryWhiskyDao();
        Whisky bowmore = new Whisky("Bowmore 15 Years Laimrig", "Scotland, Islay");
        whiskyDao.create(bowmore);
        Whisky talisker = new Whisky("Talisker 57° North", "Scotland, Island");
        whiskyDao.create(talisker);

        bind(WhiskyDao.class).toInstance(whiskyDao);
    }

    private WhiskyDao provideMemoryWhiskyDao() {
        return new WhiskyDao() {

            private final AtomicInteger idProvider = new AtomicInteger();
            private Map<String, Whisky> whiskyMap = new HashMap<>();

            @Override
            public List<Whisky> readAll() {
                return new ArrayList<>(whiskyMap.values());
            }

            @Override
            public Optional<Whisky> read(String id) {
                return Optional.ofNullable(whiskyMap.get(id));
            }

            @Override
            public Optional<Whisky> create(Whisky whisky) {
                if (whisky == null) {
                    return Optional.empty();
                }
                whisky.setId(String.valueOf(idProvider.getAndIncrement()));
                whiskyMap.put(whisky.getId(), whisky);

                return Optional.of(whisky);
            }

            @Override
            public Optional<Whisky> update(Whisky whisky) {
                if (whisky == null || !whiskyMap.containsKey(whisky.getId())) {
                    return Optional.empty();
                }

                whiskyMap.put(whisky.getId(), whisky);
                return Optional.of(whisky);
            }

            @Override
            public Optional<Whisky> delete(String id) {
                return Optional.ofNullable(whiskyMap.remove(id));
            }
        };
    }
}
