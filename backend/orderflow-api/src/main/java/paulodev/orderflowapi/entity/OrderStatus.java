package paulodev.orderflowapi.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("pending"),
    PROCESSING("processing"),
    COMPLETED("completed"),
    CANCELED("canceled");

    private final String name;

    OrderStatus(String name) {
        this.name = name;
    }
}
