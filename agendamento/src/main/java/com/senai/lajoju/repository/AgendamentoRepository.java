package com.senai.lajoju.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.senai.lajoju.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoRepository extends JpaRepository<Agendamento, UUID> {

	boolean existsByFuncionarioIdAndDataAndHoraInicioLessThanAndHoraFimGreaterThan(
			UUID funcionarioId, LocalDate data, LocalTime horaFim, LocalTime horaInicio);

	boolean existsByFuncionarioIdAndDataAndHoraInicioLessThanAndHoraFimGreaterThanAndIdNot(
			UUID funcionarioId, LocalDate data, LocalTime horaFim, LocalTime horaInicio, UUID id);
}
