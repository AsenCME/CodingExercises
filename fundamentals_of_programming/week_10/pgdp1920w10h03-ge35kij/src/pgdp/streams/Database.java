package pgdp.streams;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Database {
    private static Path baseDataDirectory = Paths.get("data");
    public static Database instance = new Database();

    public static void setBaseDataDirectory(Path baseDataDirectory) {
        Database.baseDataDirectory = baseDataDirectory;
    }

    public static Stream<Customer> processInputFileCustomer() {
        try {
            return Files.lines(Paths.get(baseDataDirectory.toString() + "/customer.tbl")).map(s -> {
                String[] args = s.split("[|]");
                int custKey = Integer.parseInt(args[0]);
                char[] varChar = args[1].toCharArray();
                int nationKey = Integer.parseInt(args[3]);
                char[] phone = args[4].toCharArray();
                float acctbal = Float.parseFloat(args[5]);
                String mktSegment = args[6];
                char[] comment = args[7].toCharArray();
                return new Customer(custKey, varChar, nationKey, phone, acctbal, mktSegment, comment);
            });
        } catch (Exception e) {
            return null;
        }
    }

    public static Stream<LineItem> processInputFileLineItem() {
        try {
            return Files.lines(Paths.get(baseDataDirectory.toString() + "/lineitem.tbl")).map(s -> {
                String[] args = s.split("[|]");
                int orderKey = Integer.parseInt(args[0]);
                int partKey = Integer.parseInt(args[1]);
                int suppKey = Integer.parseInt(args[2]);
                int lineItem = Integer.parseInt(args[3]);
                int quantity = Integer.parseInt(args[4]) * 100;
                float extendedPrice = Float.parseFloat(args[5]);
                float discount = Float.parseFloat(args[6]);
                float tax = Float.parseFloat(args[7]);
                char returnFlag = args[8].charAt(0);
                char lineStatus = args[9].charAt(0);
                LocalDate shipDate = LocalDate.parse(args[10]);
                LocalDate commitDate = LocalDate.parse(args[11]);
                LocalDate receiptDate = LocalDate.parse(args[12]);
                char[] shipInstruct = args[13].toCharArray();
                char[] shipMode = args[14].toCharArray();
                char[] comment = args[15].toCharArray();
                return new LineItem(orderKey, partKey, suppKey, lineItem, quantity, extendedPrice, discount, tax,
                        returnFlag, lineStatus, shipDate, commitDate, receiptDate, shipInstruct, shipMode, comment);
            });
        } catch (Exception e) {
            return null;
        }
    }

    public static Stream<Order> processInputFileOrders() {
        try {
            return Files.lines(Paths.get(baseDataDirectory.toString() + "/orders.tbl")).map(s -> {
                String[] args = s.split("[|]");
                int orderKey = Integer.parseInt(args[0]);
                int custKey = Integer.parseInt(args[1]);
                char orderStatus = args[2].charAt(0);
                float totalPrice = Float.parseFloat(args[3]);
                LocalDate orderDate = LocalDate.parse(args[4]);
                char[] orderPriority = args[5].toCharArray();
                char[] clerk = args[6].toCharArray();
                int shippingPriority = Integer.parseInt(args[7]);
                char[] comment = args[8].toCharArray();
                return new Order(orderKey, custKey, orderStatus, totalPrice, orderDate, orderPriority, clerk,
                        shippingPriority, comment);
            });
        } catch (Exception e) {
            return null;
        }
    }

    private final Stream<Customer> customers$;
    private final Stream<Order> orders$;
    private final Stream<LineItem> lineItems$;

    public Database() {
        customers$ = processInputFileCustomer();
        orders$ = processInputFileOrders();
        lineItems$ = processInputFileLineItem();
    }

    private Map<Integer, String> createCustomersMap() {
        Map<Integer, String> customers = new HashMap<Integer, String>();
        customers$.forEach(c -> {
            if (!customers.containsKey(c.custKey))
                customers.put(c.custKey, c.mktsegment);
        });
        return customers;
    }

    private Map<Integer, String> createOrdersMap() {
        Map<Integer, String> customersMap = createCustomersMap();
        Map<Integer, String> orders = new HashMap<Integer, String>();
        orders$.forEach(order -> {
            if (!orders.containsKey(order.orderKey))
                orders.put(order.orderKey, customersMap.get(order.custKey));
        });
        return orders;
    }

    public long getAverageQuantityPerMarketSegment(String segment) {
        Map<Integer, String> map = createOrdersMap();
        return lineItems$.filter(item -> map.get(item.orderKey).equals(segment))
                .collect(Collectors.averagingLong(item -> item.quantity)).longValue();
    }
}
