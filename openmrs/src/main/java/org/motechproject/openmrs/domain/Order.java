package org.motechproject.openmrs.domain;

import java.util.Objects;

/**
 * Represents a single order. An order represents the instructions for patient in which order and how long
 * should one take a medications.
 */
public class Order {

    private String uuid;

    public String getUuid () {
        return uuid;
    }

    public void setUuid (String uuid) {
        this.uuid = uuid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Order)) {
            return false;
        }

        Order other = (Order) o;

        return Objects.equals(uuid, other.uuid);
    }
}
