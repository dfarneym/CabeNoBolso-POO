package persistence;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UncheckedIOException;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import model.Planejamento;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.AtomicMoveNotSupportedException;

/**
 * Responsável por armazenar e recuperar
 * os dados do planejamento em arquivo.
 *
 * Implementará a interface Persistencia
 * por meio da serialização de objetos Java.
 */
public class ArquivoPersistencia implements Persistencia{

    private final File arquivo;

    public ArquivoPersistencia(String caminhoArquivo) {
        if (caminhoArquivo == null || caminhoArquivo.isBlank()) {
            throw new IllegalArgumentException(
                "O caminho do arquivo não pode ser vazio."
            );
        }

        this.arquivo = new File(caminhoArquivo);
    }
    /**
     * Salva o planejamento no arquivo configurado.
     *
     * @param planejamento planejamento que será salvo
     */
    @Override
    public void salvar(Planejamento planejamento) {
        gravarObjeto(planejamento);
    }

    /**
     * Recupera o planejamento armazenado no arquivo.
     *
     * @return planejamento recuperado
     * @throws IllegalStateException se o arquivo não contiver
     *         um objeto do tipo Planejamento
     */
    @Override
    public Planejamento carregar() {

        Object objeto = lerObjeto();

        if (!(objeto instanceof Planejamento)) {
            throw new IllegalStateException(
                "O arquivo não contém um planejamento válido."
            );
        }

        return (Planejamento) objeto;
    }
    /**
 * Grava um objeto serializável utilizando um arquivo temporário.
 *
 * O arquivo anterior só é substituído após a gravação
 * completa do novo objeto.
 *
 * @param objeto objeto que será armazenado
 * @throws IllegalArgumentException se o objeto for nulo
 * @throws UncheckedIOException se ocorrer uma falha na gravação
 */
private void gravarObjeto(Serializable objeto) {

    if (objeto == null) {
        throw new IllegalArgumentException(
            "O objeto não pode ser nulo."
        );
    }

    Path destino = arquivo.toPath().toAbsolutePath();
    Path temporario = null;

    try {
        // Cria o temporário na mesma pasta do arquivo definitivo.
        temporario = Files.createTempFile(
            destino.getParent(),
            "planejamento-",
            ".tmp"
        );

        // Primeiro grava o objeto no arquivo temporário.
        try (ObjectOutputStream saida = new ObjectOutputStream(
                new FileOutputStream(temporario.toFile()))) {

            saida.writeObject(objeto);
        }

        // Substitui o arquivo original somente após gravar tudo.
        Files.move(
            temporario,
            destino,
            StandardCopyOption.ATOMIC_MOVE,
            StandardCopyOption.REPLACE_EXISTING
        );

    } catch (AtomicMoveNotSupportedException e) {
        throw new UncheckedIOException(
            "O sistema de arquivos não oferece substituição atômica.",
            e
        );

    } catch (IOException e) {
        throw new UncheckedIOException(
            "Erro ao salvar o planejamento.",
            e
        );

    } finally {
        // Remove o temporário caso alguma etapa tenha falhado.
        if (temporario != null) {
            try {
                Files.deleteIfExists(temporario);
            } catch (IOException e) {
                // Não substitui a exceção original, se houver.
            }
        }
    }
}
    /**
     * Recupera um objeto anteriormente salvo no arquivo.
     *
     * @return objeto recuperado do arquivo
     * @throws IllegalStateException se o arquivo não existir
     *         ou contiver dados incompatíveis
     * @throws UncheckedIOException se ocorrer uma falha de leitura
     */
    private Object lerObjeto() {

        if (!arquivo.exists()) {
            throw new IllegalStateException(
                "Não existe um planejamento salvo."
            );
        }

        try (ObjectInputStream entrada = new ObjectInputStream(
                new FileInputStream(arquivo))) {

            return entrada.readObject();

        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                "O arquivo contém dados incompatíveis.", e
            );

        } catch (IOException e) {
            throw new UncheckedIOException(
                "Erro ao carregar o arquivo.", e
            );
        }
    }
}