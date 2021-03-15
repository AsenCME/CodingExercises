package pgdp.robot;

import java.util.ArrayList;
import java.util.List;

public class TerrainSensor extends Sensor<Character> {

    @Override
    public Character getData() {
        return this.owner.getWorld().getTerrain(this.owner.getPosition());
    }

    public static void main(String[] args) {
        Robot panicPenguin = new Robot("Panic!", 0, 0.5);

        // create memory
        Memory<Character> terrain = panicPenguin.createMemory(new Memory<>("terrain", ' '));

        // create and attach sensors
        panicPenguin.attachSensor(new TerrainSensor().setProcessor(terrain::set));

        // program the robot
        panicPenguin.setProgram(robot -> {
            List<Command> commands = new ArrayList<>();

            commands.add(r -> r.say(terrain.get().toString()));
            commands.add(r -> r.turnBy(Math.toRadians(5)));
            commands.add(r -> r.go(0.1));

            return commands;
        });

        World world = new World("""
                #######
                #  0  #
                #Don't#
                #PANIC#
                #     #
                #######""");

        panicPenguin.spawnInWorld(world, '0');
        world.run();
    }

}
