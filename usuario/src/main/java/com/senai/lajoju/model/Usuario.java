package com.senai.lajoju.model;

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
@Table(name = "usuarios")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Getter(AccessLevel.NONE)
	private UUID id;

	@Column(nullable = false, unique = true, length = 320)
	private String email;

	@Column(name = "nome", nullable = false, length = 120)
	private String nome;

	@Column(name = "hash_senha", nullable = false, length = 100)
	private String hashSenha;

	@Column(name = "flg_funcionario", nullable = false)
	private boolean flgFuncionario;

	public Usuario(String nome, String email, String hashSenha, boolean flgFuncionario) {
		this.nome = nome;
		this.email = email;
		this.hashSenha = hashSenha;
		this.flgFuncionario = flgFuncionario;
	}

	public void update(String nome, String email, String hashSenha, boolean flgFuncionario) {
		this.nome = nome;
		this.email = email;
		this.hashSenha = hashSenha;
		this.flgFuncionario = flgFuncionario;
	}

	public UUID getIdentificador() {
		return id;
	}

}
