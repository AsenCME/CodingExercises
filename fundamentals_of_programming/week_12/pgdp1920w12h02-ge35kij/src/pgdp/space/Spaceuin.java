package pgdp.space;

import java.util.Stack;

public class Spaceuin extends Thread {

    private Beacon currentBeacon;
    private Beacon destinationBeacon;
    private FlightRecorder recorder;
    private Stack<Beacon> visitedBeacons;
    private Stack<Beacon> doNotVisitBeacons;

    boolean shouldTerminate = false;

    public Spaceuin(Beacon _start, Beacon _destination, FlightRecorder flightRecorder) {
        currentBeacon = _start;
        destinationBeacon = _destination;
        recorder = flightRecorder;
        visitedBeacons = new Stack<Beacon>();
        doNotVisitBeacons = new Stack<Beacon>();
        visitedBeacons.add(_start);
    }

    @Override
    public void start() {
        // Record inital arrival at starting beacon
        recorder.recordArrival(currentBeacon);

        while (currentBeacon != destinationBeacon && !shouldTerminate) {

            // #region Check interrupted
            shouldTerminate = Thread.interrupted();
            if (shouldTerminate)
                break;
            // #endregion

            // There are no connections from this beacon
            // Go back
            if (currentBeacon.connections().isEmpty()) {
                goBackOneBeacon();
                continue;
            }

            // Iterate over all connections
            boolean hasTravelled = false;
            for (BeaconConnection connection : currentBeacon.connections()) {

                // Get next beacon
                Beacon nextBeacon = connection.beacon();

                // If Pingu hasn't visited the connection
                // And the connection isn't one that would put him in a deadlock
                // -> Connect
                if (!visitedBeacons.contains(nextBeacon) && !doNotVisitBeacons.contains(nextBeacon)) {

                    // Connection is of type NORMAL
                    if (connection.type() == ConnectionType.NORMAL) {

                        // Add connection to visited
                        visitedBeacons.add(nextBeacon);

                        // #region Check interrupted
                        shouldTerminate = Thread.interrupted();
                        if (shouldTerminate)
                            break;
                        // #endregion

                        // Record the connection
                        recorder.recordDeparture(currentBeacon);
                        recorder.recordArrival(nextBeacon);

                        // Change current beacon
                        currentBeacon = nextBeacon;
                        hasTravelled = true;

                        // Go again
                        break;

                    } else {

                        // #region Check interrupted
                        shouldTerminate = Thread.interrupted();
                        if (shouldTerminate)
                            break;
                        // #endregion

                        // Connection is of type WORMHOLE
                        // Synchronize on the connecting beacon
                        synchronized (nextBeacon) {
                            // Make and start scopy
                            Spaceuin copy = new Spaceuin(nextBeacon, destinationBeacon, recorder.createCopy());
                            copy.start();
                        }

                    }

                }

            }

            // Has gone through all beacons and has not travelled
            // Go back
            if (!hasTravelled && !shouldTerminate)
                goBackOneBeacon();

        }

        // If thread is not interrupted
        // Tell the story
        // Interrupt the other threads
        if (!shouldTerminate) {
            recorder.tellStory();
            this.getThreadGroup().interrupt();
        }
    }

    private void goBackOneBeacon() {
        if (visitedBeacons.isEmpty())
            return;

        doNotVisitBeacons.add(visitedBeacons.pop());

        if (visitedBeacons.isEmpty())
            return;

        Beacon previousBeacon = visitedBeacons.peek();
        recorder.recordDeparture(currentBeacon);
        recorder.recordArrival(previousBeacon);
        currentBeacon = previousBeacon;
    }

    @Override
    public String toString() {
        return "Pingu#" + getId();
    }
}