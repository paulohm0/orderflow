package paulodev.orderflowapi.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import paulodev.orderflowapi.entity.Order;
import paulodev.orderflowapi.entity.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.criteria.Predicate;
import paulodev.orderflowapi.entity.User;
import paulodev.orderflowapi.exception.ResourceNotFoundException;
import paulodev.orderflowapi.repository.UserRepository;

public class OrderSpecifications {

    public static Specification<Order> filterByStatusOrDescription(UUID authenticatedUserId, OrderStatus status, String description) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> conditions = new ArrayList<>();

            conditions.add(criteriaBuilder.equal(root.get("user").get("id"), authenticatedUserId));

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