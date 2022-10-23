package homework;

import java.util.Comparator;
import java.util.Map;
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

        return Map.entry(new Customer(smallest.getKey()), smallest.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        for (Map.Entry<Customer, String> entry : orderedMap.entrySet()) {
            if (entry.getKey().getScores() > customer.getScores()) {
                return Map.entry(new Customer(entry.getKey()), entry.getValue());
            }
        }
        return null;
    }

    public void add(Customer customer, String data) {
        orderedMap.put(customer, data);
    }
}
