package pgdp.collections;

public class FishyProduct {
    private final String name;
    private final int price;

    public FishyProduct(String _name, int _price) {
        if (_name == null || _name == "")
            ExceptionUtil.illegalArgument("Name cannot be empty or null.");
        if (_price <= 0)
            ExceptionUtil.illegalArgument("Price must be greater than 0.");
        name = _name;
        price = _price;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the price
     */
    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("Name: %s | Price: %d", name, price);
    }
}