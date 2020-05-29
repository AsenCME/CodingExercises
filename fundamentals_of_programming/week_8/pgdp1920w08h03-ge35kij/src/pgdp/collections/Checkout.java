package pgdp.collections;

/**
 * Checkout
 */
public class Checkout {

    private Queue<PenguinCustomer> queue;
    private Queue<FishyProduct> bandBefore;
    private Queue<FishyProduct> bandAfter;

    public static void main(String[] args) {
        PenguinCustomer cust = new PenguinCustomer("Tux", 7);
        FishyProduct fish = new FishyProduct("Fish", 12);
        cust.addProductToBasket(fish);

        Checkout c = new Checkout();
        c.queue.enqueue(cust);

        String s = c.toString();
    }

    public Checkout() {
        queue = new LinkedQueue<PenguinCustomer>();
        bandBefore = new LinkedQueue<FishyProduct>();
        bandAfter = new LinkedQueue<FishyProduct>();
    }

    /**
     * @return the customers
     */
    public Queue<PenguinCustomer> getQueue() {
        return queue;
    }

    /**
     * @return the bandBefore
     */
    public Queue<FishyProduct> getBandBeforeCashier() {
        return bandBefore;
    }

    /**
     * @return the bandAfter
     */
    public Queue<FishyProduct> getBandAfterCashier() {
        return bandAfter;
    }

    public int queueLength() {
        return queue.size();
    }

    public void serveNextCustomer() {
        PenguinCustomer cust = queue.dequeue();
        if (cust == null)
            return;
        cust.placeAllProductsOnBand(bandBefore);
        int bill = 0;
        while (bandBefore.size() > 0) {
            FishyProduct product = bandBefore.dequeue();
            bill += product.getPrice();
            bandAfter.enqueue(product);
        }
        cust.takeAllProductsFromBand(bandAfter);
        cust.pay(bill);
    }

    @Override
    public String toString() {
        return String.format("Customers: %s | Band before cashier: %s | Band after cashier: %s", queue.toString(),
                bandBefore.toString(), bandAfter.toString());
    }
}