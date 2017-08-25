package tryouts.akka.akkabot;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;

public class BotMaster extends AbstractActor {

    public BotMaster() {
        for (int i = 0; i < 10; i++) {
            final ActorRef child = getContext().actorOf(Props.create(AkkaBot.class));
            getContext().watch(child);
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(StartChildBots.class, this::onStartChildBots)
                .match(Terminated.class, this::onChildTerminated)
                .build();
    }

    private void onChildTerminated(Terminated terminated) {
        System.out.println(terminated.actor().path() + ": Child has stopped, starting a new one");
        final ActorRef child = getContext().actorOf(Props.create(AkkaBot.class));
        getContext().watch(child);
    }

    private void onStartChildBots(StartChildBots startChildBots) {
        final AkkaBot.Move move = new AkkaBot.Move(AkkaBot.Direction.FORWARD);
        for(ActorRef child: getContext().getChildren()) {
            System.out.println("Master started moving: " + child);
            child.tell(move, getSelf());
        }
    }

    public static class StartChildBots {};


}
