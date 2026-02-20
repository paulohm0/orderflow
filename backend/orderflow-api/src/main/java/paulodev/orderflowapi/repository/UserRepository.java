package paulodev.orderflowapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import paulodev.orderflowapi.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/*
 adiciono apenas os métodos específicos como no caso do findByUsername, que eu tenho uma
 pesquisa específica por um atributo, sempre usando a assinatura igual ao métod0
 original do JPA para que ele possa fazer a relação correta
*/

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<UserDetails> findByUsername(String username);

}

