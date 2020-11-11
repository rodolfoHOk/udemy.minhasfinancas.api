package br.com.hioktec.minhasfinancas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.hioktec.minhasfinancas.model.entity.Autoridade;
import br.com.hioktec.minhasfinancas.model.enums.AutoridadeNome;

@Repository
public interface AutoridadeRepository extends JpaRepository<Autoridade, Long> {
	
	Optional<Autoridade> findByNome(AutoridadeNome nome);
	
}
