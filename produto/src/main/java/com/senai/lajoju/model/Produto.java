package com.senai.lajoju.model;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "servicos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Getter(AccessLevel.NONE)
	private UUID id;

	@Column(name = "nome", nullable = false, length = 120)
	private String nome;

	@Column(name = "descricao", nullable = false, length = 1000)
	private String descricao;

	@Column(name = "tempo_medio_minutos", nullable = false)
	private int tempoMedioMinutos;

	@Column(name = "preco", nullable = false, precision = 10, scale = 2)
	private BigDecimal preco;

	public Produto(String nome, String descricao, int tempoMedioMinutos, BigDecimal preco) {
		this.nome = nome;
		this.descricao = descricao;
		this.tempoMedioMinutos = tempoMedioMinutos;
		this.preco = preco;
	}

	public void update(String nome, String descricao, int tempoMedioMinutos, BigDecimal preco) {
		this.nome = nome;
		this.descricao = descricao;
		this.tempoMedioMinutos = tempoMedioMinutos;
		this.preco = preco;
	}

	public UUID getIdentificador() {
		return id;
	}

}
