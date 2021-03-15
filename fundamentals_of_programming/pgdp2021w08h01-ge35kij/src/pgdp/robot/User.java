package pgdp.robot;

import java.util.ArrayList;

public class User {

    public static Robot makeMazeRunner() {
        Robot mazeRunner = new Robot("MazeRunner", 0, 1);

        // current square sensor
        var terrain = mazeRunner.createMemory(new Memory<Character>("current", ' '));
        var terrainSensor = new TerrainSensor().setProcessor(terrain::set);
        mazeRunner.attachSensor(terrainSensor);

        // fwd sensor
        var fwd = mazeRunner.createMemory(new Memory<Character>("fwd", ' '));
        var fwdSensor = new FwdSensor().setProcessor(fwd::set);
        mazeRunner.attachSensor(fwdSensor);

        // right sensor
        var right = mazeRunner.createMemory(new Memory<Character>("right", ' '));
        var rightSensor = new RightHandSensor().setProcessor(right::set);
        mazeRunner.attachSensor(rightSensor);

        // discovered end mem
        var foundEnd = mazeRunner.createMemory(new Memory<Boolean>("completed", false));

        mazeRunner.setProgram(robot -> {
            var cmds = new ArrayList<Command>();
            cmds.add(r -> {
                if (foundEnd.get())
                    return true;

                if (terrain.get() == '$') {
                    foundEnd.set(true);
                    r.say("Found end! Yay!");
                    return true;
                }

                var dir = Math.toDegrees(r.getDirection());
                var rightSquare = right.get();
                var fwdSquare = fwd.get();

                // no wall to the right
                if (rightSquare != '#') {
                    var turnSuccess = false;
                    if (dir == 0)
                        turnSuccess = r.turnTo(Math.toRadians(90));
                    else if (dir == 90)
                        turnSuccess = r.turnTo(Math.toRadians(180));
                    else if (dir == 180)
                        turnSuccess = r.turnTo(Math.toRadians(270));
                    else if (dir == 270)
                        turnSuccess = r.turnTo(0);
                    if (turnSuccess)
                        return r.go(1);
                }

                // wall on the right
                // no wall fwd
                if (fwdSquare != '#')
                    return r.go(1);
                // wall fwd - turn left
                else {
                    if (dir == 0)
                        return r.turnTo(Math.toRadians(270));
                    else if (dir == 90)
                        return r.turnTo(0);
                    else if (dir == 180)
                        return r.turnTo(Math.toRadians(90));
                    else if (dir == 270)
                        return r.turnTo(Math.toRadians(180));
                }
                return false;
            });
            return cmds;
        });
        return mazeRunner;

    }

    public static Robot makeLineFollower() {
        Robot lineFollower = new Robot("LineFollower", 0, 1);
        Memory<Character> terrain = lineFollower.createMemory(new Memory<>("terrain", ' '));
        var terrainSensor = new TerrainSensor().setProcessor(terrain::set);
        lineFollower.attachSensor(terrainSensor);
        lineFollower.setProgram(robot -> {
            var cmds = new ArrayList<Command>();
            cmds.add(r -> {
                switch (terrain.get()) {
                    case '>':
                        if (r.turnTo(0))
                            return r.go(1);
                    case '<':
                        if (r.turnTo(Math.toRadians(180)))
                            return r.go(1);
                    case '^':
                        if (r.turnTo(Math.toRadians(270)))
                            return r.go(1);
                    case 'v':
                        if (r.turnTo(Math.toRadians(90)))
                            return r.go(1);
                    default:
                        return r.go(1);
                }
            });
            return cmds;
        });
        return lineFollower;
    }

    public static void main(String[] args) {
        // run the simulation
        String map = "##############\n" + //
                "#  0   #   ##  $\n" + //
                "#  #  ##   #   #\n" + //
                "####   # ### ###\n" + //
                "#      #       #\n" + //
                "#  #      ##   #\n" + //
                "################";
        // String map = "################\n" + //
        // "#v<# #\n" + //
        // "#v^# #v<< # #\n" + //
        // "#v^<<<<<<0^ # #\n" + //
        // "#v # >>>^# #\n" + //
        // "#v### ^# ##\n" + //
        // "#>>>>>>^ #\n" + //
        // "################";
        // String map = "################\n" + //
        // "# #0 1 #\n" + //
        // "# # ## # #\n" + //
        // "# ### #T # #\n" + //
        // "# 3# a ### W #\n" + //
        // "# ### # 2 ##\n" + //
        // "# # #\n" + //
        // "###########$$$##";

        World world = new World(map);
        var robo = makeMazeRunner();
        robo.spawnInWorld(world, '0');
        world.run();
    }
}
