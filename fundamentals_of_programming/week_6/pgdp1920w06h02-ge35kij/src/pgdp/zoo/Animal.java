package pgdp.zoo;

class Animal {
    private String name;
    private int foodCosts;

    public Animal(String _name, int _foodCosts) {
        name = _name;
        foodCosts = _foodCosts;
    }

    public int getFoodCosts() {
        return foodCosts;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return String.format("(name: %s, foodCosts: %d)", name, foodCosts);
    }
}