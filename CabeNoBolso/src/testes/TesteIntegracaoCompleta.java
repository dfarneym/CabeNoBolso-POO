package testes;

import java.math.BigDecimal;
import java.util.List;

import model.Participante;
import model.Planejamento;
import model.DespesaFixa;
import model.DespesaPorPessoa;
import persistence.ArquivoPersistencia;

public class TesteIntegracaoCompleta {

    private static void verificarValor(
            String descricao,
            BigDecimal obtido,
            String esperado) {

        if (obtido.compareTo(new BigDecimal(esperado)) != 0) {
            throw new AssertionError(
                descricao + ": esperado R$ " + esperado
                + ", obtido R$ " + obtido
            );
        }
    }

    public static void main(String[] args) {

        Planejamento planejamento = new Planejamento();

        Participante ana = new Participante(
                1, "Ana", new BigDecimal("70.00"));

        Participante bruno = new Participante(
                2, "Bruno", new BigDecimal("60.00"));

        Participante carla = new Participante(
                3, "Carla", new BigDecimal("70.00"));

        planejamento.adicionarParticipante(ana);
        planejamento.adicionarParticipante(bruno);
        planejamento.adicionarParticipante(carla);

        List<Participante> todos = List.of(ana, bruno, carla);
        List<Participante> anaECarla = List.of(ana, carla);

        planejamento.adicionarDespesa(
            new DespesaFixa(
                "Transporte",
                new BigDecimal("90.00"),
                todos
            )
        );

        planejamento.adicionarDespesa(
            new DespesaPorPessoa(
                "Almoço",
                new BigDecimal("20.00"),
                todos
            )
        );

        planejamento.adicionarDespesa(
            new DespesaFixa(
                "Recreação",
                new BigDecimal("30.00"),
                anaECarla
            )
        );

        planejamento.adicionarDespesa(
            new DespesaPorPessoa(
                "Sobremesa",
                new BigDecimal("10.00"),
                todos
            )
        );

        verificarValor("Total de Ana",
                planejamento.calcularTotalParticipante(ana),
                "75.00");

        verificarValor("Total de Bruno",
                planejamento.calcularTotalParticipante(bruno),
                "60.00");

        verificarValor("Total de Carla",
                planejamento.calcularTotalParticipante(carla),
                "75.00");

        verificarValor("Total geral",
                planejamento.calcularTotalGeral(),
                "210.00");

        System.out.println(
            "TESTE APROVADO: cálculos antes do salvamento."
        );

        // Salva e recupera o planejamento.
        ArquivoPersistencia persistencia =
                new ArquivoPersistencia("teste-integracao.dat");

        persistencia.salvar(planejamento);

        Planejamento recuperado = persistencia.carregar();

        // Utiliza os participantes do objeto recuperado.
        List<Participante> participantes =
                recuperado.getParticipantes();

        if (participantes.size() != 3) {
            throw new AssertionError(
                "Quantidade de participantes incorreta."
            );
        }

        verificarValor("Ana após carregar",
                recuperado.calcularTotalParticipante(
                    participantes.get(0)),
                "75.00");

        verificarValor("Bruno após carregar",
                recuperado.calcularTotalParticipante(
                    participantes.get(1)),
                "60.00");

        verificarValor("Carla após carregar",
                recuperado.calcularTotalParticipante(
                    participantes.get(2)),
                "75.00");

        verificarValor("Total geral após carregar",
                recuperado.calcularTotalGeral(),
                "210.00");

        System.out.println(
            "TESTE APROVADO: cálculos preservados após carregar."
        );
    }
}