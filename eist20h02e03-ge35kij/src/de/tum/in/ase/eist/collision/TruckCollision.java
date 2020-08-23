package de.tum.in.ase.eist.collision;

import de.tum.in.ase.eist.car.Car;
import de.tum.in.ase.eist.car.Truck;

public class TruckCollision extends Collision {
    public TruckCollision(Car car1, Car car2) {
        super(car1, car2);
    }

    @Override
    public Car evaluateLoser() {
        Car loser = super.evaluateLoser();

        if (loser instanceof Truck) {
            ((Truck) loser).getHit();
            if (((Truck) loser).getLives() == 0)
                return loser;
            return null;
        }

        return loser;
    }
}