package com.senai.lajoju.repository;

import java.util.UUID;

import com.senai.lajoju.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioServico extends JpaRepository<Servico, UUID> {
}
