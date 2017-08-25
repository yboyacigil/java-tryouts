package tryouts.jerseyguice.whisky;

public class Whisky {

    private String id;

    private String name;

    private String origin;

    public Whisky() {
    }

    public Whisky(String name, String origin) {
        this.name = name;
        this.origin = origin;
        this.id = "";
    }

    public Whisky(String id, String name, String origin) {
        this.id = id;
        this.name = name;
        this.origin = origin;
    }

    public String getName() {
        return name;
    }

    public String getOrigin() {
        return origin;
    }

    public String getId() {
        return id;
    }

    public Whisky setName(String name) {
        this.name = name;
        return this;
    }

    public Whisky setOrigin(String origin) {
        this.origin = origin;
        return this;
    }

    public Whisky setId(String id) {
        this.id = id;
        return this;
    }
}
