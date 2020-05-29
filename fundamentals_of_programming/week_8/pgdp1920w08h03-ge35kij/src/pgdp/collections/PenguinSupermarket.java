package pgdp.collections;

/**
 * PenguinSupermarket
 */
public class PenguinSupermarket {

    private Checkout[] checkouts;

    public PenguinSupermarket(int checkoutsNum) {
        if (checkoutsNum <= 0)
            ExceptionUtil.illegalArgument("Checkouts cannot be less than 1.");
        checkouts = new Checkout[checkoutsNum];
        for (int i = 0; i < checkouts.length; i++)
            checkouts[i] = new Checkout();
    }

    public Checkout getCheckoutWithSmallestQueue() {
        Checkout smallest = checkouts[0];
        int min = smallest.queueLength();
        for (int i = 1; i < checkouts.length; i++) {
            Checkout other = checkouts[i];
            int len = other.queueLength();
            if (len < min) {
                smallest = other;
                min = len;
            }
        }
        return smallest;
    }

    public void closeCheckout(int index) {
        int oldLen = checkouts.length;
        if (index >= oldLen || index < 0)
            ExceptionUtil.illegalArgument("Checkout index out fo range");
        int newLen = oldLen - 1;
        if (newLen <= 0)
            ExceptionUtil.unsupportedOperation("Cannot close all checkouts");

        Checkout closedCheckout = checkouts[index];

        Checkout[] newCheckouts = new Checkout[newLen];
        for (int i = 0, j = 0; i < oldLen; i++) {
            if (i == index)
                continue;
            newCheckouts[j] = checkouts[i];
            j++;
        }
        checkouts = newCheckouts;
        QueueConnector<PenguinCustomer> queueOnClosedLink = new QueueConnector<PenguinCustomer>(
                closedCheckout.getQueue());
        StackConnector<PenguinCustomer> customerOrderLink = new StackConnector<PenguinCustomer>(
                new LinkedStack<PenguinCustomer>());
        DataStructureLink<PenguinCustomer> link = new DataStructureLink<PenguinCustomer>(queueOnClosedLink,
                customerOrderLink);
        link.moveAllFromAToB();

        while (customerOrderLink.hasNextElement()) {
            PenguinCustomer nextCustInLine = customerOrderLink.removeNextElement();
            nextCustInLine.goToCheckout(this);
        }
    }

    public void serveCustomers() {
        for (Checkout checkout : checkouts)
            checkout.serveNextCustomer();
    }

    /**
     * @return the checkouts
     */
    public Checkout[] getCheckouts() {
        return checkouts;
    }

}