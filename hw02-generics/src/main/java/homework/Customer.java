package homework;

import java.util.Objects;

public class Customer implements Comparable<Customer>, Cloneable {
    private final long id;
    private String name;
    private long scores;

    // todo: 1. в этом классе надо исправить ошибки

    public Customer(long id, String name, long scores) {
        this.id = id;
        this.name = name;
        this.scores = scores;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getScores() {
        return scores;
    }

    public void setScores(long scores) {
        this.scores = scores;
    }

    @Override
    public String toString() {
        return "Customer{" + "id=" + id + ", name='" + name + '\'' + ", scores=" + scores + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        else if (o instanceof Customer customer) {
            return Objects.equals(this.id, customer.id);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(id);
        result = 31 * result << 4;
        return result;
    }

    @Override
    public int compareTo(Customer customer) {
        return Integer.compare((int) this.scores, (int) customer.scores);
    }

    @Override
    public Customer clone() {
        try {
            return (Customer) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
