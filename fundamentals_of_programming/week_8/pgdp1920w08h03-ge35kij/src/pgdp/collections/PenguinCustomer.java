package pgdp.collections;

/**
 * PenguinCustomer
 */
public class PenguinCustomer {

    private final String name;
    private int money;
    private Stack<FishyProduct> products;

    public PenguinCustomer(String _name, int _money) {
        if (_name == null || _name == "")
            ExceptionUtil.illegalArgument("Name cannot be empty of null");
        if (money < 0)
            ExceptionUtil.illegalArgument("Money cannot be less then 0");
        name = _name;
        money = _money;
        products = new LinkedStack<FishyProduct>();
    }

    public void addProductToBasket(FishyProduct x) {
        products.push(x);
    }

    public void placeAllProductsOnBand(Queue<FishyProduct> band) {
        StackConnector<FishyProduct> a = new StackConnector<FishyProduct>(products);
        QueueConnector<FishyProduct> b = new QueueConnector<FishyProduct>(band);
        DataStructureLink<FishyProduct> link = new DataStructureLink<FishyProduct>(a, b);
        link.moveAllFromAToB();
    }

    public void takeAllProductsFromBand(Queue<FishyProduct> band) {
        QueueConnector<FishyProduct> a = new QueueConnector<FishyProduct>(band);
        StackConnector<FishyProduct> b = new StackConnector<FishyProduct>(products);

        DataStructureLink<FishyProduct> link = new DataStructureLink<FishyProduct>(a, b);
        link.moveAllFromAToB();
    }

    public void pay(int bill) {
        if (bill < 0)
            ExceptionUtil.illegalArgument("Bill cannot be less than 0.");
        if (money - bill < 0)
            ExceptionUtil.unsupportedOperation("If this penguin were to pay it would go broke");
        money -= bill;
    }

    public void goToCheckout(PenguinSupermarket market) {
        market.getCheckoutWithSmallestQueue().getQueue().enqueue(this);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the money
     */
    public int getMoney() {
        return money;
    }

    /**
     * @return the products
     */
    public Stack<FishyProduct> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return String.format("Name: %s | Money: %d | Basket: %s", name, money, products.toString());
    }
}