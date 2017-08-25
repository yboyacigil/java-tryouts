package tryouts.akka.akkabot;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Application {

    public static void main(String[] args) {

        ActorSystem system = ActorSystem.create();

        final ActorRef botMaster = system.actorOf(Props.create(BotMaster.class), "botMaster");

        botMaster.tell(
                new BotMaster.StartChildBots(),
                ActorRef.noSender()
        );
    }
}
