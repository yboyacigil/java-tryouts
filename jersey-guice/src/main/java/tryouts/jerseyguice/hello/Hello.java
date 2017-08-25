package tryouts.jerseyguice.hello;

public class Hello {

    private final String msg;

    public Hello(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
