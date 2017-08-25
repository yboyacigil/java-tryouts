package tryouts.akka.helloworld;

import akka.actor.ActorSystem;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {

        final ActorSystem system = ActorSystem.create();

        System.out.println("Press any key to terminate...");
        System.in.read();

        System.out.println("Shutting down actor system...");
        system.terminate();
    }
}
