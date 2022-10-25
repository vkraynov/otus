package homework;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Service for adding and sorting {@link Customer}.
 *
 * @author Vadim_Kraynov
 */
public class CustomerService {
    private final TreeMap<Customer, String> orderedMap = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> smallest = orderedMap.firstEntry();

        return Objects.nonNull(smallest) ? Map.entry(new Customer(smallest.getKey()), smallest.getValue()) : null;
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> higherEntry = orderedMap.higherEntry(customer);

        return Objects.nonNull(higherEntry) ? Map.entry(new Customer(higherEntry.getKey()), higherEntry.getValue()) : null;
    }

    public void add(Customer customer, String data) {
        orderedMap.put(customer, data);
    }
}
