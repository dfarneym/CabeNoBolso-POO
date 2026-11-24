package model;

import java.math.BigDecimal;
import java.util.List;

public class TestePlanejamento {

    public static void main(String[] args) {

        Planejamento planejamento = new Planejamento();

        // TESTE 1 — Planejamento inicialmente sem participantes.
        verificar(
            "Lista inicialmente vazia",
            planejamento.getParticipantes().isEmpty()
        );

        // TESTE 2 — Cadastro de Ana.
        Participante ana = new Participante(
            1, "Ana", new BigDecimal("70.00")
        );

        planejamento.adicionarParticipante(ana);

        verificar(
            "Cadastro de Ana",
            planejamento.getParticipantes().size() == 1
                && planejamento.getParticipantes().get(0) == ana
        );

        // TESTE 3 — Rejeição de código duplicado.
        Participante duplicado = new Participante(
            1, "Bruno", new BigDecimal("60.00")
        );

        boolean duplicadoRejeitado = false;

        try {
            planejamento.adicionarParticipante(duplicado);
        } catch (IllegalArgumentException erro) {
            duplicadoRejeitado = true;
            System.out.println("Mensagem recebida: " + erro.getMessage());
        }

        verificar(
            "Código duplicado rejeitado e cadastro preservado",
            duplicadoRejeitado
                && planejamento.getParticipantes().size() == 1
                && planejamento.getParticipantes().get(0) == ana
        );

        // TESTE 4 — Rejeição de participante nulo.
        boolean nuloRejeitado = false;

        try {
            planejamento.adicionarParticipante(null);
        } catch (IllegalArgumentException erro) {
            nuloRejeitado = true;
            System.out.println("Mensagem recebida: " + erro.getMessage());
        }

        verificar(
            "Participante nulo rejeitado e cadastro preservado",
            nuloRejeitado
                && planejamento.getParticipantes().size() == 1
                && planejamento.getParticipantes().get(0) == ana
        );

        // TESTE 5 — Cadastro de Bruno com código diferente.
        Participante bruno = new Participante(
            2, "Bruno", new BigDecimal("60.00")
        );

        planejamento.adicionarParticipante(bruno);

        verificar(
            "Cadastro de Bruno",
            planejamento.getParticipantes().size() == 2
                && planejamento.getParticipantes().get(1) == bruno
        );

        // TESTE 6 — Alterar a cópia não modifica a lista interna.
        List<Participante> copia = planejamento.getParticipantes();

        copia.clear();

        verificar(
            "Lista interna de participantes protegida",
            copia.isEmpty()
                && planejamento.getParticipantes().size() == 2
                && planejamento.getParticipantes().get(0) == ana
                && planejamento.getParticipantes().get(1) == bruno
        );

        // Cadastra Carla para montar o caso completo.
        Participante carla = new Participante(
            3, "Carla", new BigDecimal("70.00")
        );

        planejamento.adicionarParticipante(carla);

        List<Participante> todos = List.of(ana, bruno, carla);

        // Transporte: R$ 30,00 para cada participante.
        Despesa transporte = new DespesaFixa(
            "Transporte", new BigDecimal("90.00"), todos
        );

        // Almoço: R$ 20,00 para cada participante.
        Despesa almoco = new DespesaPorPessoa(
            "Almoço", new BigDecimal("20.00"), todos
        );

        // Atividade: R$ 15,00 para Ana e R$ 15,00 para Carla.
        Despesa atividade = new DespesaFixa(
            "Atividade recreativa",
            new BigDecimal("30.00"),
            List.of(ana, carla)
        );

        // Despesa adicional: R$ 10,00 para cada participante.
        Despesa adicional = new DespesaPorPessoa(
            "Despesa adicional", new BigDecimal("10.00"), todos
        );

        planejamento.adicionarDespesa(transporte);
        planejamento.adicionarDespesa(almoco);
        planejamento.adicionarDespesa(atividade);
        planejamento.adicionarDespesa(adicional);

        // TESTE 7 — Caso completo: Ana 75, Bruno 60, Carla 75.
        conferirTotais(
            "Caso completo", planejamento, ana, bruno, carla,
            "75.00", "60.00", "75.00", "210.00"
        );

        // TESTE 8 — Excedentes e igualdade de orçamento.
        verificar(
            "Ana excede R$ 5,00",
            !planejamento.estaDentroDoOrcamento(ana)
                && igual(planejamento.calcularExcedente(ana), "5.00")
        );

        verificar(
            "Bruno está exatamente no orçamento",
            planejamento.estaDentroDoOrcamento(bruno)
                && igual(planejamento.calcularExcedente(bruno), "0.00")
        );

        verificar(
            "Carla excede R$ 5,00",
            !planejamento.estaDentroDoOrcamento(carla)
                && igual(planejamento.calcularExcedente(carla), "5.00")
        );

        // TESTE 9 — Proteção da lista interna de despesas.
        List<Despesa> copiaDespesas = planejamento.getDespesas();

        copiaDespesas.clear();

        verificar(
            "Lista interna de despesas protegida",
            copiaDespesas.isEmpty()
                && planejamento.getDespesas().size() == 4
        );

        // TESTE 10 — Atualiza o transporte de R$ 90 para R$ 120.
        // Cada participante passa a gastar R$ 10 a mais.
        planejamento.atualizarDespesa(
            transporte,
            "Transporte atualizado",
            new BigDecimal("120.00"),
            todos
        );

        verificar(
            "Descrição do transporte atualizada",
            transporte.getDescricao().equals("Transporte atualizado")
        );

        conferirTotais(
            "Após atualizar transporte",
            planejamento, ana, bruno, carla,
            "85.00", "70.00", "85.00", "240.00"
        );

        // TESTE 11 — Edição inválida deve preservar a despesa.
        boolean despesaInvalidaRejeitada = false;

        try {
            planejamento.atualizarDespesa(
                transporte,
                "Nome que não deve ser aplicado",
                new BigDecimal("-1.00"),
                todos
            );
        } catch (IllegalArgumentException erro) {
            despesaInvalidaRejeitada = true;
            System.out.println("Mensagem recebida: " + erro.getMessage());
        }

        verificar(
            "Edição inválida preserva a despesa",
            despesaInvalidaRejeitada
                && transporte.getDescricao().equals("Transporte atualizado")
                && igual(transporte.getValor(), "120.00")
                && transporte.getParticipantes().equals(todos)
        );

        conferirTotais(
            "Após rejeitar edição da despesa",
            planejamento, ana, bruno, carla,
            "85.00", "70.00", "85.00", "240.00"
        );

        // Restaura o transporte ao valor original.
        planejamento.atualizarDespesa(
            transporte, "Transporte", new BigDecimal("90.00"), todos
        );

        conferirTotais(
            "Transporte restaurado",
            planejamento, ana, bruno, carla,
            "75.00", "60.00", "75.00", "210.00"
        );

        // TESTE 12 — Remove a atividade recreativa.
        planejamento.removerDespesa(atividade);

        verificar(
            "Atividade removida",
            planejamento.getDespesas().size() == 3
                && !planejamento.getDespesas().contains(atividade)
        );

        conferirTotais(
            "Após remover atividade",
            planejamento, ana, bruno, carla,
            "60.00", "60.00", "60.00", "180.00"
        );

        verificar(
            "Orçamento atualizado após remover despesa",
            planejamento.estaDentroDoOrcamento(ana)
                && igual(planejamento.calcularExcedente(ana), "0.00")
        );

        // TESTE 13 — Rejeita remover participante vinculado.
        boolean remocaoRejeitada = false;

        try {
            planejamento.removerParticipante(2);
        } catch (IllegalArgumentException erro) {
            remocaoRejeitada = true;
            System.out.println("Mensagem recebida: " + erro.getMessage());
        }

        verificar(
            "Remoção de Bruno rejeitada e cadastro preservado",
            remocaoRejeitada
                && planejamento.getParticipantes().size() == 3
                && planejamento.buscarParticipante(2) == bruno
        );

        conferirTotais(
            "Após rejeitar remoção de Bruno",
            planejamento, ana, bruno, carla,
            "60.00", "60.00", "60.00", "180.00"
        );

        // TESTE 14 — Permite remover participante sem despesas.
        Participante diego = new Participante(
            4, "Diego", new BigDecimal("50.00")
        );

        planejamento.adicionarParticipante(diego);
        planejamento.removerParticipante(4);

        verificar(
            "Participante sem despesas removido",
            planejamento.getParticipantes().size() == 3
                && !planejamento.getParticipantes().contains(diego)
        );

        // Recoloca a atividade: Ana volta a gastar R$ 75,00.
        planejamento.adicionarDespesa(atividade);

        // TESTE 15 — Atualiza nome e orçamento de Ana.
        planejamento.atualizarParticipante(
            1, "Ana Maria", new BigDecimal("80.00")
        );

        verificar(
            "Nome e orçamento atualizados no mesmo objeto",
            planejamento.buscarParticipante(1) == ana
                && ana.getNome().equals("Ana Maria")
                && igual(ana.getOrcamento(), "80.00")
                && transporte.getParticipantes().get(0) == ana
        );

        conferirTotais(
            "Após atualizar participante",
            planejamento, ana, bruno, carla,
            "75.00", "60.00", "75.00", "210.00"
        );

        verificar(
            "Ana dentro do novo orçamento",
            planejamento.estaDentroDoOrcamento(ana)
                && igual(planejamento.calcularExcedente(ana), "0.00")
        );

        // TESTE 16 — Rejeita nomes nulo, vazio e apenas espaços.
        String[] nomesInvalidos = {null, "", "   "};

        for (String nomeInvalido : nomesInvalidos) {

            boolean nomeRejeitado = false;

            try {
                // O orçamento diferente detecta alteração parcial.
                planejamento.atualizarParticipante(
                    1, nomeInvalido, new BigDecimal("100.00")
                );
            } catch (IllegalArgumentException erro) {
                nomeRejeitado = true;
                System.out.println(
                    "Mensagem recebida: " + erro.getMessage()
                );
            }

            verificar("Nome inválido rejeitado", nomeRejeitado);
            conferirDadosDeAna(planejamento, ana);
        }

        // TESTE 17 — Rejeita orçamento nulo e negativo.
        BigDecimal[] orcamentosInvalidos = {
            null, new BigDecimal("-1.00")
        };

        for (BigDecimal orcamentoInvalido : orcamentosInvalidos) {

            boolean orcamentoRejeitado = false;

            try {
                // O nome diferente detecta alteração parcial.
                planejamento.atualizarParticipante(
                    1, "Nome que não deve ser aplicado",
                    orcamentoInvalido
                );
            } catch (IllegalArgumentException erro) {
                orcamentoRejeitado = true;
                System.out.println(
                    "Mensagem recebida: " + erro.getMessage()
                );
            }

            verificar("Orçamento inválido rejeitado", orcamentoRejeitado);
            conferirDadosDeAna(planejamento, ana);
        }

        // TESTE 18 — Orçamento igual ao gasto.
        planejamento.atualizarParticipante(
            1, "Ana Maria", new BigDecimal("75.00")
        );

        verificar(
            "Igualdade considerada dentro do orçamento",
            igual(ana.getOrcamento(), "75.00")
                && planejamento.estaDentroDoOrcamento(ana)
                && igual(planejamento.calcularExcedente(ana), "0.00")
        );

        // TESTE 19 — Orçamento zero é permitido.
        planejamento.atualizarParticipante(
            1, "Ana Maria", new BigDecimal("0.00")
        );

        verificar(
            "Orçamento zero aceito e excedente calculado",
            igual(ana.getOrcamento(), "0.00")
                && !planejamento.estaDentroDoOrcamento(ana)
                && igual(planejamento.calcularExcedente(ana), "75.00")
        );

        // Deixa Ana com orçamento de R$ 80,00 ao final.
        planejamento.atualizarParticipante(
            1, "Ana Maria", new BigDecimal("80.00")
        );

        conferirTotais(
            "Conferência final",
            planejamento, ana, bruno, carla,
            "75.00", "60.00", "75.00", "210.00"
        );

        System.out.println("Todos os cenários de teste passaram.");

    } // Fecha o main.

    // Compara valores monetários.
    // compareTo() considera 75.0 e 75.00 numericamente iguais.
    private static boolean igual(BigDecimal atual, String esperado) {
        return atual.compareTo(new BigDecimal(esperado)) == 0;
    }

    // Reúne as quatro verificações de totais para evitar repetição.
    private static void conferirTotais(
            String etapa,
            Planejamento planejamento,
            Participante ana,
            Participante bruno,
            Participante carla,
            String esperadoAna,
            String esperadoBruno,
            String esperadoCarla,
            String esperadoGeral) {

        verificar(
            etapa + " - Ana",
            igual(
                planejamento.calcularTotalParticipante(ana),
                esperadoAna
            )
        );

        verificar(
            etapa + " - Bruno",
            igual(
                planejamento.calcularTotalParticipante(bruno),
                esperadoBruno
            )
        );

        verificar(
            etapa + " - Carla",
            igual(
                planejamento.calcularTotalParticipante(carla),
                esperadoCarla
            )
        );

        verificar(
            etapa + " - Total geral",
            igual(planejamento.calcularTotalGeral(), esperadoGeral)
        );
    }

    // Confere se uma edição rejeitada preservou os dados anteriores.
    private static void conferirDadosDeAna(
            Planejamento planejamento, Participante ana) {

        verificar(
            "Dados anteriores de Ana preservados",
            planejamento.buscarParticipante(1) == ana
                && ana.getCodigo() == 1
                && ana.getNome().equals("Ana Maria")
                && igual(ana.getOrcamento(), "80.00")
                && igual(
                    planejamento.calcularTotalParticipante(ana),
                    "75.00"
                )
        );
    }

    // Exibe PASSOU ou interrompe o teste quando a condição é falsa.
    private static void verificar(String teste, boolean condicao) {

        if (!condicao) {
            throw new AssertionError("FALHOU - " + teste);
        }

        System.out.println("PASSOU - " + teste);
    }

} // Fecha a classe.