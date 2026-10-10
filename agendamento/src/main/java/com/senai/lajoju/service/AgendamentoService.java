package com.senai.lajoju.service;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import com.senai.lajoju.dto.AgendamentoRequest;
import com.senai.lajoju.dto.AgendamentoResponse;
import com.senai.lajoju.dto.ProdutoRemotoResponse;
import com.senai.lajoju.dto.UsuarioRemotoResponse;
import com.senai.lajoju.mapper.AgendamentoMapper;
import com.senai.lajoju.model.Agendamento;
import com.senai.lajoju.repository.AgendamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

	private final AgendamentoRepository appointments;
	private final AgendamentoMapper mapper;
	private final ClienteUsuarioService userClient;
	private final ClienteProdutoService productClient;

	@Transactional(readOnly = true)
	public List<AgendamentoResponse> findAll() {
		return appointments.findAll().stream().map(mapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public AgendamentoResponse findById(UUID id) {
		return mapper.toResponse(findAppointment(id));
	}

	@Transactional
	public AgendamentoResponse create(AgendamentoRequest request, String bearerToken, String authenticatedEmail) {
		Agendamento appointment = toAppointment(request, bearerToken, authenticatedEmail);
		if (appointments.existsByFuncionarioIdAndDataAndHoraInicioLessThanAndHoraFimGreaterThan(
				appointment.getFuncionarioId(), appointment.getData(),
				appointment.getHoraFim(), appointment.getHoraInicio())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "O funcionário já possui um agendamento nesse período.");
		}
		return mapper.toResponse(appointments.saveAndFlush(appointment));
	}

	@Transactional
	public AgendamentoResponse update(
            UUID id, AgendamentoRequest request, String bearerToken, String authenticatedEmail) {
		Agendamento appointment = findAppointment(id);
		Agendamento updated = toAppointment(request, bearerToken, authenticatedEmail);
		if (appointments.existsByFuncionarioIdAndDataAndHoraInicioLessThanAndHoraFimGreaterThanAndIdNot(
				updated.getFuncionarioId(), updated.getData(), updated.getHoraFim(),
				updated.getHoraInicio(), id)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "O funcionário já possui um agendamento nesse período.");
		}
		appointment.update(
				updated.getUsuarioId(),
				updated.getFuncionarioId(),
				updated.getProdutoId(),
				updated.getData(),
				updated.getHoraInicio(),
				updated.getHoraFim());
		return mapper.toResponse(appointments.save(appointment));
	}

	@Transactional
	public void delete(UUID id) {
		appointments.delete(findAppointment(id));
	}

	private Agendamento toAppointment(
            AgendamentoRequest request, String bearerToken, String authenticatedEmail) {
		UsuarioRemotoResponse user = fetchUser(request.usuarioId(), bearerToken);
		if (authenticatedEmail == null || !user.email().equalsIgnoreCase(authenticatedEmail)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Só é permitido agendar para o usuário autenticado.");
		}

		UsuarioRemotoResponse employee = fetchUser(request.funcionarioId(), bearerToken);
		if (!employee.flgFuncionario()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O usuário selecionado não é funcionário.");
		}

		ProdutoRemotoResponse product = fetchProduct(request.produtoId(), bearerToken);
		LocalTime endTime;
		try {
			endTime = request.horaInicio().plusMinutes(product.tempoMedioMinutos());
		} catch (RuntimeException exception) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, "O horário calculado ultrapassa o fim do dia.", exception);
		}
		if (!endTime.isAfter(request.horaInicio())) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, "O horário calculado ultrapassa o fim do dia.");
		}
		return new Agendamento(
				request.usuarioId(), request.funcionarioId(), request.produtoId(),
				request.data(), request.horaInicio(), endTime);
	}

	private UsuarioRemotoResponse fetchUser(UUID id, String bearerToken) {
		try {
			UsuarioRemotoResponse user = userClient.getUser(id, bearerToken);
			if (user == null || user.email() == null) {
				throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Resposta inválida do serviço usuário.");
			}
			return user;
		} catch (HttpClientErrorException.NotFound exception) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário referenciado não existe.", exception);
		} catch (RestClientException exception) {
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Não foi possível consultar o serviço usuário.", exception);
		}
	}

	private ProdutoRemotoResponse fetchProduct(UUID id, String bearerToken) {
		try {
			ProdutoRemotoResponse product = productClient.getProduct(id, bearerToken);
			if (product == null || product.tempoMedioMinutos() <= 0) {
				throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Resposta inválida do serviço produto.");
			}
			return product;
		} catch (HttpClientErrorException.NotFound exception) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Serviço referenciado não existe.", exception);
		} catch (RestClientException exception) {
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Não foi possível consultar o serviço produto.", exception);
		}
	}

	private Agendamento findAppointment(UUID id) {
		return appointments.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado."));
	}
}
