package testes;

import model.Despesa;
import model.DespesaFixa;
import model.DespesaPorPessoa;
import model.Participante;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class TesteValidacaoDespesas {

    public static void main(String[] args) {

        Participante ana = new Participante(
                1, "Ana", new BigDecimal("70.00"));

        Participante bruno = new Participante(
                2, "Bruno", new BigDecimal("60.00"));

        Participante carla = new Participante(
                3, "Carla", new BigDecimal("70.00"));

        Participante daniel = new Participante(
                4, "Daniel", new BigDecimal("80.00"));

        List<Participante> participantes =
                Arrays.asList(ana, bruno, carla);

        Despesa despesa = new DespesaFixa(
                "Transporte",
                new BigDecimal("100.00"),
                participantes);

        // TESTE 1 - Rateio de centavos
        System.out.println("TESTE 1 - Rateio de R$ 100,00");
        System.out.println("Ana: R$ " +
                despesa.calcularValorParticipante(ana));
        System.out.println("Bruno: R$ " +
                despesa.calcularValorParticipante(bruno));
        System.out.println("Carla: R$ " +
                despesa.calcularValorParticipante(carla));
        System.out.println("Total: R$ " +
                despesa.calcularTotal());

        // TESTE 2 - Participantes fora de ordem
        System.out.println("\nTESTE 2 - Fora de ordem");

        despesa.setParticipantes(
                Arrays.asList(carla, bruno, ana));

        System.out.println("Ana: R$ " +
                despesa.calcularValorParticipante(ana));
        System.out.println("Bruno: R$ " +
                despesa.calcularValorParticipante(bruno));
        System.out.println("Carla: R$ " +
                despesa.calcularValorParticipante(carla));

        // TESTE 3 - Pessoa nao envolvida
        System.out.println("\nTESTE 3 - Nao participante");
        System.out.println("Daniel: R$ " +
                despesa.calcularValorParticipante(daniel));

        // TESTE 4 - Entradas invalidas
        System.out.println("\nTESTE 4 - Entradas invalidas");

        try {
            despesa.setValor(new BigDecimal("-10.00"));
            System.out.println("FALHA: valor negativo aceito.");
        } catch (IllegalArgumentException e) {
            System.out.println("Valor negativo rejeitado.");
        }

        try {
            despesa.setDescricao("");
            System.out.println("FALHA: descricao vazia aceita.");
        } catch (IllegalArgumentException e) {
            System.out.println("Descricao vazia rejeitada.");
        }

        try {
            despesa.setParticipantes(new ArrayList<>());
            System.out.println("FALHA: lista vazia aceita.");
        } catch (IllegalArgumentException e) {
            System.out.println("Lista vazia rejeitada.");
        }

        try {
            despesa.setParticipantes(Arrays.asList(ana, ana));
            System.out.println("FALHA: participante repetido aceito.");
        } catch (IllegalArgumentException e) {
            System.out.println("Participante repetido rejeitado.");
        }

        // TESTE 5 - Preservacao dos dados
        System.out.println("\nTESTE 5 - Edicao invalida");

        String descricaoAnterior = despesa.getDescricao();
        BigDecimal valorAnterior = despesa.getValor();
        List<Participante> listaAnterior =
                despesa.getParticipantes();

        try {
            despesa.editar(
                    "Nova descricao",
                    new BigDecimal("50.00"),
                    new ArrayList<>());

            System.out.println("FALHA: edicao invalida aceita.");
        } catch (IllegalArgumentException e) {
            System.out.println("Edicao rejeitada.");
        }

        System.out.println("Descricao atual: " +
                despesa.getDescricao());
        System.out.println("Valor atual: R$ " +
                despesa.getValor());
        System.out.println("Quantidade de participantes: " +
                despesa.getParticipantes().size());

        boolean dadosPreservados =
                descricaoAnterior.equals(despesa.getDescricao())
                        && valorAnterior.equals(despesa.getValor())
                        && listaAnterior.equals(despesa.getParticipantes());

        System.out.println("Dados preservados: " +
                dadosPreservados);

        // TESTE 6 - Casas decimais
        System.out.println("\nTESTE 6 - Casas decimais");

        despesa.setValor(new BigDecimal("10.125"));
        System.out.println("10.125 -> " + despesa.getValor());

        despesa.setValor(new BigDecimal("10.124"));
        System.out.println("10.124 -> " + despesa.getValor());

        try {
            despesa.setValor(new BigDecimal("0.001"));
            System.out.println("FALHA: valor 0.001 aceito.");
        } catch (IllegalArgumentException e) {
            System.out.println("Valor 0.001 rejeitado.");
        }

        System.out.println("Valor preservado: R$ " +
                despesa.getValor());

        // TESTE 7 - Despesa por pessoa
        System.out.println("\nTESTE 7 - Despesa por pessoa");

        Despesa almoco = new DespesaPorPessoa(
                "Almoco",
                new BigDecimal("20.00"),
                participantes);

        System.out.println("Ana: R$ " +
                almoco.calcularValorParticipante(ana));
        System.out.println("Daniel: R$ " +
                almoco.calcularValorParticipante(daniel));
        System.out.println("Total: R$ " +
                almoco.calcularTotal());

        System.out.println("\nFim dos testes.");
    }
}
