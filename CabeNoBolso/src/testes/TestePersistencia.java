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
        // Teste de carregamento de arquivo inexistente.
        ArquivoPersistencia arquivoInexistente =
                new ArquivoPersistencia("arquivo-que-nao-existe.dat");

        try {
        arquivoInexistente.carregar();

        throw new AssertionError(
                "Era esperado um erro para arquivo inexistente."
        );

        } catch (IllegalStateException e) {

        System.out.println(
                "TESTE APROVADO: arquivo inexistente tratado."
        );
        }
        // Testa o carregamento de um arquivo corrompido.
        try {
        java.nio.file.Files.writeString(
                java.nio.file.Path.of("teste-corrompido.dat"),
                "Conteudo invalido para serializacao"
        );

        ArquivoPersistencia arquivoCorrompido =
                new ArquivoPersistencia("teste-corrompido.dat");

        try {
                arquivoCorrompido.carregar();

                throw new AssertionError(
                        "Era esperado um erro para arquivo corrompido."
                );

        } catch (java.io.UncheckedIOException e) {
                System.out.println(
                        "TESTE APROVADO: arquivo corrompido tratado."
                );
        }

        } catch (java.io.IOException e) {
        throw new AssertionError(
                "Nao foi possivel preparar o arquivo de teste.", e
        );
      }
      // Verifica se uma tentativa inválida de salvar preserva os dados.
        ArquivoPersistencia persistenciaSegura =
                new ArquivoPersistencia("teste-planejamento.dat");

        // O arquivo já contém o planejamento válido salvo no primeiro teste.
        try {
        persistenciaSegura.salvar(null);

        throw new AssertionError(
                "Era esperado um erro ao tentar salvar null."
        );

        } catch (IllegalArgumentException e) {

        Planejamento preservado = persistenciaSegura.carregar();

        if (preservado.getParticipantes().size() != 1
                || !preservado.getParticipantes().get(0)
                        .getNome().equals("Ana")) {

                throw new AssertionError(
                "O planejamento anterior não foi preservado."
                );
        }

        System.out.println(
                "TESTE APROVADO: arquivo anterior preservado."
        );
      }
    }

}