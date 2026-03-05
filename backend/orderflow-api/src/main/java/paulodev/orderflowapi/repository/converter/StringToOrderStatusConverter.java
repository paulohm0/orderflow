package paulodev.orderflowapi.repository.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import paulodev.orderflowapi.entity.OrderStatus;
import paulodev.orderflowapi.exception.InvalidOperationException;

@Component
public class StringToOrderStatusConverter implements Converter<String, OrderStatus> {

    @Override
    public OrderStatus convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        try {
            return OrderStatus.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidOperationException("o status inserido é inválido");
        }
    }
}
