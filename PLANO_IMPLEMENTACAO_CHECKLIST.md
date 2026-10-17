# Checklist de implementação — Lajoju

Este checklist transforma o levantamento dos requisitos em entregas priorizadas.
Preencha a coluna **Responsável** quando alguém assumir uma tarefa e atualize
**Status** durante o trabalho.

## Prioridades

- **P0 — Essencial:** controle de acesso e integridade de dados; resolver antes de
  disponibilizar o sistema a clientes.
- **P1 — Alta:** fluxos centrais de agendamento e conta.
- **P2 — Normal:** completar dados e políticas do salão que dependem de decisão
  operacional.

## Backlog priorizado

| Feito | Prioridade | Entrega / requisitos relacionados | Critério de conclusão | Depende de | Responsável | Status |
|---|---|---|---|---|---|---|
| [ ] | P0 | **Restringir acesso aos dados pessoais** — RF3, RF4; regras do usuário 4 e 5 | Cliente consulta e altera apenas o próprio perfil por endpoints autenticados; consulta de outros usuários e alteração do papel de funcionário são restritas a uma operação autorizada; testes cobrem acesso permitido e negado. | Definir quais operações ficam disponíveis a administradores, se houver esse papel. | A definir | Pendente |
| [ ] | P0 | **Definir ciclo de vida e cancelamento de agendamentos** — RF13; regras de agendamento 11 e 13 | Agendamento possui status; cancelar altera o status sem apagar o registro; cliente autenticado só cancela o próprio agendamento; transições inválidas são recusadas; testes cobrem cada caso. | A política de prazo mínimo (tarefa P2). | A definir | Pendente |
| [ ] | P0 | **Restringir listagem e consulta de agendamentos** — RF7, RF14; regra do usuário 4 | Cliente não consegue listar nem consultar dados de agendamentos de outras pessoas; consultas gerais ficam reservadas a perfil autorizado; testes cobrem isolamento por cliente. | Controle de acesso aos dados pessoais. | A definir | Pendente |
| [ ] | P1 | **Impedir agendamentos no passado** — regra de agendamento 8 | Criação e alteração recusam datas passadas e horários já passados no dia corrente, usando o fuso horário definido para o salão; testes cobrem limites de data e hora. | Definir fuso horário do salão. | A definir | Pendente |
| [ ] | P1 | **Implementar expediente e consulta de disponibilidade** — RF8, RF20; regra de agendamento 10 | API informa horários disponíveis por serviço, profissional e data; cálculo considera duração do serviço, expediente, pausas, folgas e agendamentos ativos; confirmação valida novamente a disponibilidade. | Definir expediente, pausas e folgas; serviço precisa ter duração. | A definir | Pendente |
| [ ] | P1 | **Evitar conflito de horários mesmo com requisições simultâneas** — regra de agendamento 9 | Dois agendamentos incompatíveis para o mesmo profissional e período não podem ser confirmados mesmo quando as requisições chegam ao mesmo tempo; cancelados não ocupam horário; teste de concorrência cobre o caso. | Status do agendamento e mecanismo de disponibilidade. | A definir | Pendente |
| [ ] | P1 | **Criar consultas do histórico e dos próximos agendamentos do cliente** — RF7, RF14 | Cliente autenticado consulta somente seus agendamentos anteriores e futuros, com status e ordenação por data/hora; testes validam filtros e isolamento. | Status do agendamento e controle de acesso. | A definir | Pendente |
| [ ] | P1 | **Desativar usuário sem apagar o histórico** — RF5 | Desativação preserva os dados e agendamentos; usuário desativado não consegue fazer login nem criar novos agendamentos; definir como tratar agendamentos futuros existentes e cobrir com testes. | Decidir o tratamento dos agendamentos futuros do usuário desativado. | A definir | Pendente |
| [ ] | P1 | **Adicionar logout** — RF2 | Comportamento de logout fica definido e implementado. Se o token for invalidado pelo servidor, todos os microsserviços rejeitam tokens revogados até o vencimento; testes comprovam logout e rejeição posterior do token. | Decidir entre logout apenas no cliente ou revogação efetiva do token; revogação exige solução compartilhada entre microsserviços. | A definir | Pendente |
| [ ] | P2 | **Definir e aplicar prazo mínimo de cancelamento** — regra de agendamento 12 | Salão decide se existe prazo e qual é. Se existir, regra configurável bloqueia cancelamentos fora do prazo; se não existir, política sem prazo fica documentada e testada. | Decisão operacional do salão. | A definir | Pendente |
| [ ] | P2 | **Cadastrar descrições reais para os serviços existentes** — RF16; regra de serviços 14 | Cada serviço oferecido tem descrição específica, revisada pelo salão, em vez do texto genérico de transição usado para registros antigos; listagem e detalhe exibem a descrição. | Salão fornecer ou aprovar os textos. | A definir | Pendente |

## Registro do que já foi implementado

- [x] **Campo de descrição no cadastro, edição, persistência e resposta da API**
  — RF16 e regra de serviços 14 parcialmente atendidos. A migration adiciona e
  torna a descrição obrigatória, preenchendo registros existentes com um texto
  provisório. Os novos cadastros e edições precisam enviar `description`.
- [x] **Testes do mapeamento da descrição** — criação, atualização e resposta.
- [x] **Exemplos de criação e edição atualizados na coleção Postman.**

## Observações para a equipe

- Marque `[x]` somente quando o critério de conclusão da linha estiver atendido
  e os testes correspondentes passarem.
- A descrição está implementada tecnicamente, mas os textos genéricos da
  migration não substituem descrições comerciais aprovadas para cada serviço.
- As prioridades indicam ordem sugerida; atualize-as se uma decisão de produto
  ou operação alterar a sequência.
