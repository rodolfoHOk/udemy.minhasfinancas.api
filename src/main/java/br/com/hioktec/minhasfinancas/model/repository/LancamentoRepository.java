package br.com.hioktec.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.hioktec.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
	
}
