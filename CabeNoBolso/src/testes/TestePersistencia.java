package testes;

import java.math.BigDecimal;

import model.Participante;
import model.Planejamento;
import persistence.ArquivoPersistencia;

public class TestePersistencia {

    public static void main(String[] args) {

        Planejamento planejamento = new Planejamento();

        Participante participante = new Participante(
                1, "Ana", new BigDecimal("70.00")
        );

        planejamento.adicionarParticipante(participante);

        ArquivoPersistencia persistencia =
                new ArquivoPersistencia("teste-planejamento.dat");

        persistencia.salvar(planejamento);

        Planejamento recuperado = persistencia.carregar();

        if (recuperado.getParticipantes().size() != 1) {
            throw new AssertionError(
                    "Quantidade de participantes incorreta."
            );
        }

        Participante ana = recuperado.getParticipantes().get(0);

        if (!ana.getNome().equals("Ana")
                || ana.getOrcamento().compareTo(
                        new BigDecimal("70.00")) != 0) {

            throw new AssertionError(
                    "Os dados recuperados estão incorretos."
            );
        }

        System.out.println(
                "TESTE APROVADO: planejamento salvo e carregado."
        );
    }
}