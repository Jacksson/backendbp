package co.jackson.microservices.customer.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PersonaRepository extends JpaRepository<PersonaEntity, Long> {

    @Transactional(readOnly = true)
    List<PersonaEntity> findByIdentificacion(String identificacion);
}
