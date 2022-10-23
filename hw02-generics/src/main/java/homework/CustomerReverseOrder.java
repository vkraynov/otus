package homework;

import java.util.LinkedList;

/**
 * Service for adding and taking {@link Customer} in reverse order.
 *
 * @author Vadim_Kraynov
 */
public class CustomerReverseOrder {
    private final LinkedList<Customer> linkedList = new LinkedList<>();

    public void add(Customer customer) {
        linkedList.add(customer);
    }

    public Customer take() {
        Customer last = linkedList.getLast();
        linkedList.remove(last);
        return last;
    }
}
