package tryouts.java.hiddenconstructor;

public class Bar {

    public void configure() {
        // Cannot access Foo constructor because it is package private
        // Foo foo = new Foo(123);
    }
}
