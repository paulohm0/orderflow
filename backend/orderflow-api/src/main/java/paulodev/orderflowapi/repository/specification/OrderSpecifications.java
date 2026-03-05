package paulodev.orderflowapi.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import paulodev.orderflowapi.entity.Order;
import paulodev.orderflowapi.entity.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class OrderSpecifications {

    public static Specification<Order> filterByStatusOrDescription(OrderStatus status, String description) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> conditions = new ArrayList<>();

            // busca pelo campo de status
            if (status != null) {
                conditions.add(criteriaBuilder.equal(root.get("status"), status));
            }

            // busca pelo campo de description
            if (description != null && !description.isBlank()) {
                conditions.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")),
                        "%" + description.toLowerCase() + "%"));
            }

            // se os  dois campos estiverem vazios, retorna todos
            if (conditions.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.or(conditions.toArray(new Predicate[0]));
        };
    }
}