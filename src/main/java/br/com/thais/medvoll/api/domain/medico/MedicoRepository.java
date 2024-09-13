package br.com.thais.medvoll.api.domain.medico;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MedicoRepository extends JpaRepository<Medico, Long>{

	Page<Medico> findAllByAtivoTrue(Pageable paginacao);

	@Query(value = """
	        SELECT * FROM medicos m
	        WHERE m.ativo = true
	        AND m.especialidade = :especialidade
	        AND m.id NOT IN (
	            SELECT c.medico_id FROM consultas c
	            WHERE c.data = :data
	        )
	        """, nativeQuery = true)
	Page<Medico> escolherMedicoLivreNaData(Especialidade especialidade, LocalDateTime data, Pageable pageable);

    @Query("""
            select m.ativo
            from Medico m
            where
            m.id = :id
            """)
    Boolean findAtivoById(Long id);

}
