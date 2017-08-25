package tryouts.vertx.restjdbc;

public class Whisky {

    private static final Integer NO_ID = -1;

    private Integer id;

    private String name;

    private String origin;

    public Whisky() {
        this(NO_ID, null, null);
    }

    public Whisky(String name, String origin) {
        this(NO_ID, name, origin);
    }

    public Whisky(Integer id, String name, String origin) {
        this.id = id;
        this.name = name;
        this.origin = origin;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return "Whisky{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", origin='" + origin + '\'' +
                '}';
    }
}
