package homework;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Service for adding and taking {@link Customer} in reverse order.
 *
 * @author Vadim_Kraynov
 */
public class CustomerReverseOrder {
    private final Deque<Customer> stack = new ArrayDeque<>();

    public void add(Customer customer) {
        stack.push(customer);
    }

    public Customer take() {
        return stack.pop();
    }
}
