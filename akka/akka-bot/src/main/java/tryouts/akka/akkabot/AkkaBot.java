package tryouts.akka.akkabot;

import akka.actor.AbstractActor;

import java.util.Optional;
import java.util.Random;

public class AkkaBot extends AbstractActor {

    private Optional<Direction> direction = Optional.empty();
    private boolean moving = false;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Move.class, this::onMove)
                .match(Stop.class, this::onStop)
                .build();
    }

    private void onStop(Stop stop) {
        moving = false;
        System.out.println(getSelf().path() + ": I stopped moving");
    }

    private void onMove(Move move) {
        moving = true;
        direction = Optional.of(move.direction);
        System.out.println(getSelf().path() + ": I'm now moving " + direction.get());

        final Random random = new Random();
        final int nextInt = random.nextInt();
        if ((nextInt % 2) == 0) {
            getContext().stop(getSelf());
        }
    }

    public enum Direction {FORWARD, BACKWARD, LEFT, RIGHT};

    public static class Move {
        public Direction direction;

        public Move(Direction direction) {
            this.direction = direction;
        }
    }

    public static class Stop {}

    public static class GetRobotState {}

    public static class RobotState {
        public final Direction direction;
        public final boolean moving;

        public RobotState(Direction direction, boolean moving) {
            this.direction = direction;
            this.moving = moving;
        }
    }
}
