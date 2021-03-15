package pgdp.robot;

public class FwdSensor extends Sensor<Character> {

    public Position getFwdPosition() {
        var pos = this.owner.getPosition();
        var dir = Math.toDegrees(this.owner.getDirection());
        if (dir == 0) // east
            return new Position(pos.x + 1, pos.y);
        else if (dir == 90) // south
            return new Position(pos.x, pos.y + 1);
        else if (dir == 180) // west
            return new Position(pos.x - 1, pos.y);
        else if (dir == 270) // north
            return new Position(pos.x, pos.y - 1);
        return new Position(pos);
    }

    @Override
    public Character getData() {
        return this.owner.getWorld().getTerrain(this.getFwdPosition());
    }
}
