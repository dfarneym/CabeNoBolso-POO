package model;

// Permite que os objetos Participante sejam serializados,
// ou seja, salvos e recuperados em arquivos.
import java.io.Serializable;

// Utilizado para trabalhar com valores monetários,
// evitando problemas de precisão do tipo double.
import java.math.BigDecimal;

/**
 * Representa uma pessoa cadastrada no planejamento de despesas.
 *
 * Cada participante possui um código identificador, um nome
 * e um orçamento disponível para o planejamento.
 *
 * Esta classe pertence à camada Model da arquitetura MVC.
 *
 * @author Daniel
 */
public class Participante implements Serializable {

    // Identificador da versão de serialização da classe.
    // Ajuda a controlar a compatibilidade dos objetos salvos.
    private static final long serialVersionUID = 1L;

    // Código identificador do participante.
    // O modificador final impede sua alteração após a criação.
    private final int codigo;

    // Nome do participante.
    private String nome;

    // Valor disponível para os gastos do participante.
    private BigDecimal orcamento;

    /**
     * Constrói um participante com seus dados iniciais.
     *
     * @param codigo código identificador do participante
     * @param nome nome do participante
     * @param orcamento orçamento disponível, maior ou igual a zero
     *
     * @throws IllegalArgumentException se o nome for inválido
     *         ou o orçamento for nulo ou negativo
     */
    public Participante(int codigo, String nome, BigDecimal orcamento) {

        // Impede nomes nulos, vazios ou compostos apenas por espaços.
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido");
        }

        // signum() retorna -1 para valores negativos, 0 para zero
        // e 1 para valores positivos.
        // Assim, orçamento zero é permitido, mas negativo não.
        if (orcamento == null || orcamento.signum() < 0) {
            throw new IllegalArgumentException("Orçamento inválido.");
        }

        // Após validar os dados, inicializa os atributos do objeto.
        this.codigo = codigo;
        this.nome = nome;
        this.orcamento = orcamento;
    }

    /**
     * Consulta o código identificador do participante.
     *
     * @return código do participante
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * Consulta o nome do participante.
     *
     * @return nome do participante
     */
    public String getNome() {
        return nome;
    }

    /**
     * Consulta o orçamento disponível para o planejamento.
     *
     * @return orçamento do participante
     */
    public BigDecimal getOrcamento() {
        return orcamento;
    }
}
