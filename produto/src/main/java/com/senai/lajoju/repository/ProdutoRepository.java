package com.senai.lajoju.repository;

import java.util.UUID;

import com.senai.lajoju.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {
}
