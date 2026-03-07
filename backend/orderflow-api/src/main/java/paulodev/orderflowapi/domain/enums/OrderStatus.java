package paulodev.orderflowapi.domain.enums;

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
