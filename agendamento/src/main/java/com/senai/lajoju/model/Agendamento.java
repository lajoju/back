package com.senai.lajoju.model;

import java.time.LocalDate;
import java.time.LocalTime;
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
@Table(name = "agendamentos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Agendamento {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Getter(AccessLevel.NONE)
	private UUID id;

	@Column(name = "usuario_id", nullable = false)
	private UUID usuarioId;

	@Column(name = "funcionario_id", nullable = false)
	private UUID funcionarioId;

	@Column(name = "servico_id", nullable = false)
	private UUID produtoId;

	@Column(nullable = false)
	private LocalDate data;

	@Column(name = "hora_inicio", nullable = false)
	private LocalTime horaInicio;

	@Column(name = "hora_fim", nullable = false)
	private LocalTime horaFim;

	public Agendamento(
			UUID usuarioId,
			UUID funcionarioId,
			UUID produtoId,
			LocalDate data,
			LocalTime horaInicio,
			LocalTime horaFim) {
		this.usuarioId = usuarioId;
		this.funcionarioId = funcionarioId;
		this.produtoId = produtoId;
		this.data = data;
		this.horaInicio = horaInicio;
		this.horaFim = horaFim;
	}

	public void update(
			UUID usuarioId,
			UUID funcionarioId,
			UUID produtoId,
			LocalDate data,
			LocalTime horaInicio,
			LocalTime horaFim) {
		this.usuarioId = usuarioId;
		this.funcionarioId = funcionarioId;
		this.produtoId = produtoId;
		this.data = data;
		this.horaInicio = horaInicio;
		this.horaFim = horaFim;
	}

	public UUID getIdentificador() {
		return id;
	}

}
