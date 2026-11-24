package testes;

import java.math.BigDecimal;

import controller.PlanejamentoController;
import model.Participante;
import model.Planejamento;
import persistence.ArquivoPersistencia;
import java.util.List;

import model.Despesa;
import model.DespesaFixa;

public class TesteController {

    public static void main(String[] args) {

        Planejamento planejamento = new Planejamento();

        ArquivoPersistencia persistencia =
                new ArquivoPersistencia("teste-controller.dat");

        PlanejamentoController controller =
                new PlanejamentoController(
                        planejamento, persistencia);

        controller.cadastrarParticipante(
                1, "Ana", new BigDecimal("70.00"));

        if (planejamento.getParticipantes().size() != 1) {
            throw new AssertionError(
                    "O participante não foi cadastrado.");
        }

        System.out.println(
                "TESTE APROVADO: cadastro pelo Controller.");

        // Verifica se o Controller retorna o participante cadastrado.
        if (controller.listarParticipantes().size() != 1) {
            throw new AssertionError(
                    "A consulta não retornou o participante.");
        }

        if (!controller.listarParticipantes().get(0).getNome().equals("Ana")) {
            throw new AssertionError(
                    "O nome do participante está incorreto.");
        }

        System.out.println(
                "TESTE APROVADO: consulta de participantes pelo Controller.");

        // Atualiza o nome e o orçamento de Ana.
        controller.atualizarParticipante(
                1, "Ana Maria", new BigDecimal("80.00"));

        // Consulta novamente o participante.
        Participante participanteAtualizado =
                controller.listarParticipantes().get(0);

        // Verifica o novo nome.
        if (!participanteAtualizado.getNome().equals("Ana Maria")) {
            throw new AssertionError(
                    "O nome do participante não foi atualizado.");
        }

        // Verifica o novo orçamento.
        if (participanteAtualizado.getOrcamento()
                .compareTo(new BigDecimal("80.00")) != 0) {
            throw new AssertionError(
                    "O orçamento não foi atualizado.");
        }

        System.out.println(
                "TESTE APROVADO: atualização de participante pelo Controller.");

        // Cria uma despesa fixa de R$ 90,00 para Ana.
        DespesaFixa transporte = new DespesaFixa(
                "Transporte",
                new BigDecimal("90.00"),
                List.of(participanteAtualizado)
        );

        // Cadastra a despesa por meio do Controller.
        controller.adicionarDespesa(transporte);

        // Verifica se o gasto de Ana foi registrado.
        BigDecimal totalAna =
                planejamento.calcularTotalParticipante(participanteAtualizado);

        if (totalAna.compareTo(new BigDecimal("90.00")) != 0) {
            throw new AssertionError(
                    "O total da despesa está incorreto: " + totalAna);
        }

        System.out.println(
                "TESTE APROVADO: cadastro de despesa pelo Controller.");

        // Verifica o total geral por meio do Controller.
        BigDecimal totalGeral = controller.calcularTotalGeral();

        if (totalGeral.compareTo(new BigDecimal("90.00")) != 0) {
            throw new AssertionError(
                    "Total geral incorreto: " + totalGeral);
        }

        System.out.println(
                "TESTE APROVADO: total geral pelo Controller.");

        // Consulta o gasto individual de Ana pelo Controller.
        BigDecimal gastoAna =
        controller.calcularTotalParticipante(participanteAtualizado);

        if (gastoAna.compareTo(new BigDecimal("90.00")) != 0) {
            throw new AssertionError(
                    "Gasto individual incorreto: " + gastoAna);
        }

        System.out.println(
                "TESTE APROVADO: total individual pelo Controller.");

        // Verifica se a despesa cadastrada aparece na consulta.
        if (controller.listarDespesas().size() != 1) {
            throw new AssertionError(
                    "A consulta não retornou a despesa cadastrada.");
        }

        if (!controller.listarDespesas().get(0)
                .getDescricao().equals("Transporte")) {
            throw new AssertionError(
                    "A descrição da despesa está incorreta.");
        }

        System.out.println(
                "TESTE APROVADO: consulta de despesas pelo Controller.");

        // Ana possui orçamento atualizado de R$ 80,00
        // e uma despesa de R$ 90,00.
        if (controller.estaDentroDoOrcamento(participanteAtualizado)) {
            throw new AssertionError(
                    "Ana deveria estar acima do orçamento.");
        }

        BigDecimal excedente =
                controller.calcularExcedente(participanteAtualizado);

        if (excedente.compareTo(new BigDecimal("10.00")) != 0) {
            throw new AssertionError(
                    "Excedente incorreto: " + excedente);
        }

        System.out.println(
                "TESTE APROVADO: orçamento excedido pelo Controller.");
        // Salva o planejamento por meio do Controller.
        controller.salvarPlanejamento();

        // Verifica se o arquivo foi criado.
        if (!new java.io.File("teste-controller.dat").isFile()) {
            throw new AssertionError(
                    "O arquivo do planejamento não foi criado.");
        }

        System.out.println(
                "TESTE APROVADO: salvamento pelo Controller.");
        // Altera o planejamento atual após o salvamento.
        controller.cadastrarParticipante(
                2, "Bruno", new BigDecimal("60.00"));

        // Confirma que agora existem dois participantes.
        if (controller.listarParticipantes().size() != 2) {
            throw new AssertionError(
                    "Bruno não foi cadastrado antes do carregamento.");
        }

        // Recupera o planejamento salvo anteriormente.
        controller.carregarPlanejamento();

        // O arquivo continha apenas Ana.
        if (controller.listarParticipantes().size() != 1) {
            throw new AssertionError(
                    "O planejamento salvo não foi recuperado corretamente.");
        }

        // Confere os dados e o cálculo recuperados.
        if (!controller.listarParticipantes().get(0)
                .getNome().equals("Ana Maria")) {
            throw new AssertionError(
                    "O nome salvo não foi recuperado.");
        }

        if (controller.calcularTotalGeral()
                .compareTo(new BigDecimal("90.00")) != 0) {
            throw new AssertionError(
                    "O total salvo não foi recuperado.");
        }

        System.out.println(
                "TESTE APROVADO: carregamento pelo Controller.");
        // Recupera a despesa do planejamento carregado.
        Despesa despesaCarregada =
                controller.listarDespesas().get(0);

        // Recupera Ana do planejamento carregado.
        Participante anaCarregada =
                controller.listarParticipantes().get(0);

        // Atualiza a despesa de R$ 90,00 para R$ 120,00.
        controller.atualizarDespesa(
                despesaCarregada,
                "Transporte atualizado",
                new BigDecimal("120.00"),
                List.of(anaCarregada)
        );

        // Confere a nova descrição.
        if (!despesaCarregada.getDescricao()
                .equals("Transporte atualizado")) {
            throw new AssertionError(
                    "A descrição da despesa não foi atualizada.");
        }

        // Confere o novo total.
        if (controller.calcularTotalGeral()
                .compareTo(new BigDecimal("120.00")) != 0) {
            throw new AssertionError(
                    "O total geral não foi atualizado.");
        }

        System.out.println(
                "TESTE APROVADO: atualização de despesa pelo Controller.");

        // Remove a despesa de transporte atualizada.
        controller.removerDespesa(despesaCarregada);

        // Confirma que não existem mais despesas.
        if (!controller.listarDespesas().isEmpty()) {
            throw new AssertionError(
                    "A despesa não foi removida.");
        }

        // Confirma que o total geral voltou a zero.
        if (controller.calcularTotalGeral()
                .compareTo(new BigDecimal("0.00")) != 0) {
            throw new AssertionError(
                    "O total geral deveria ser zero após a exclusão.");
        }

        System.out.println(
                "TESTE APROVADO: remoção de despesa pelo Controller.");
        // Agora que a despesa foi removida,
        // Ana pode ser excluída do planejamento.
        controller.removerParticipante(1);

        // Verifica se a lista ficou vazia.
        if (!controller.listarParticipantes().isEmpty()) {
            throw new AssertionError(
                    "O participante não foi removido.");
        }

        System.out.println(
                "TESTE APROVADO: remoção de participante pelo Controller.");

    }

}