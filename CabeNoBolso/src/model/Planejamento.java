package model;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.io.Serializable;

public class Planejamento implements Serializable{
    private static final long serialVersionUID = 1L;
    private final List<Participante> participantes;
    private final List<Despesa> despesas;

    public Planejamento() {
        this.participantes = new ArrayList<>();
        this.despesas = new ArrayList<>();
    }

    public void adicionarParticipante(Participante participante) {

        if (participante == null) {
            throw new IllegalArgumentException(
                    "O participante deve ser informado.");
        }
        for (Participante cadastrado : participantes) {

            if (cadastrado.getCodigo() == participante.getCodigo()) {
                throw new IllegalArgumentException(
                        "Já existe um participante com esse código.");
            }
        }

        this.participantes.add(participante);
    }

    public List<Participante> getParticipantes() {
        return new ArrayList<>(participantes);
    }

  
    // Retorna uma cópia para proteger a estrutura da lista interna.
    public List<Despesa> getDespesas() {
         return new ArrayList<>(despesas);
    }

    // Procura um participante cadastrado pelo código.
    public Participante buscarParticipante(int codigo) {

    for (Participante participante : participantes) {

        if (participante.getCodigo() == codigo) {
            return participante;
        }
    }

    throw new IllegalArgumentException(
        "Participante não encontrado para o código " + codigo
    );
}

    // Confere se todos os envolvidos pertencem ao planejamento.
    private void validarVinculos(List<Participante> envolvidos) {

        if (envolvidos == null || envolvidos.isEmpty()) {
          throw new IllegalArgumentException(
            "A despesa deve possuir pelo menos um participante.");
    }

    List<Integer> codigos = new ArrayList<>();

    for (Participante envolvido : envolvidos) {

        if (envolvido == null) {
            throw new IllegalArgumentException("Participante inválido.");
        }

        if (codigos.contains(envolvido.getCodigo())) {
            throw new IllegalArgumentException("Participante repetido na despesa.");
        }

        boolean cadastrado = false;

        for (Participante participante : participantes) {

            // Confere se é o próprio objeto cadastrado.
            if (participante == envolvido) {
                cadastrado = true;
                break;
            }
        }

        if (!cadastrado) {
            throw new IllegalArgumentException(
                "O participante de código " + envolvido.getCodigo()
                    + " não pertence ao planejamento.");
        }

        codigos.add(envolvido.getCodigo());
    }
}

// Adiciona a despesa após conferir seus vínculos.
public void adicionarDespesa(Despesa despesa) {

    if (despesa == null) {
        throw new IllegalArgumentException
            ("A despesa deve ser informada.");
    }

    validarVinculos(despesa.getParticipantes());

    despesas.add(despesa);
}

// Edita uma despesa cadastrada, seguindo a assinatura da UML.
public void atualizarDespesa(
        Despesa despesa,
        String descricao,
        BigDecimal valor,
        List<Participante> participantes) {

    if (despesa == null || !despesas.contains(despesa)) {
        throw new IllegalArgumentException
            ("A despesa não pertence ao planejamento.");
    }

    // Primeiro verifica os vínculos dos novos participantes.
    validarVinculos(participantes);

    // editar() valida descrição, valor e seleção antes de alterar.
    despesa.editar(descricao, valor, participantes);
}

// Exclui uma despesa cadastrada.
public void removerDespesa(Despesa despesa) {

    if (despesa == null || !despesas.contains(despesa)) {
        throw new IllegalArgumentException
        ("A despesa não pertence ao planejamento.");
    }

    despesas.remove(despesa);
}

// Confere se a pessoa consultada é um dos objetos cadastrados.
private void validarParticipanteCadastrado(Participante participante) {

    if (participante == null) {
        throw new IllegalArgumentException(
            "O participante deve ser informado."
        );
    }

    for (Participante cadastrado : participantes) {
        if (cadastrado == participante) {
            return; // Encerra a validação quando encontra o objeto.
        }
    }

    throw new IllegalArgumentException(
        "O participante não pertence ao planejamento."
    );
}

// Soma as contribuições da pessoa em todas as despesas.
public BigDecimal calcularTotalParticipante(Participante participante) {

    validarParticipanteCadastrado(participante);

    BigDecimal total = new BigDecimal("0.00");

    for (Despesa despesa : despesas) {
        total = total.add(
            despesa.calcularValorParticipante(participante)
        );
    }

    return total;
}

// Soma o custo total de cada despesa cadastrada.
public BigDecimal calcularTotalGeral() {

    BigDecimal total = new BigDecimal("0.00");

    for (Despesa despesa : despesas) {
        total = total.add(despesa.calcularTotal());
    }

    return total;
}

// Considera dentro do orçamento quem gastar até o limite disponível.
public boolean estaDentroDoOrcamento(Participante participante) {

    BigDecimal total = calcularTotalParticipante(participante);

    return total.compareTo(participante.getOrcamento()) <= 0;
}

// Retorna o excesso. Para gastos dentro do limite, retorna zero.
public BigDecimal calcularExcedente(Participante participante) {

    BigDecimal total = calcularTotalParticipante(participante);

    BigDecimal excedente = total.subtract(
        participante.getOrcamento()
    );

    if (excedente.compareTo(BigDecimal.ZERO) > 0) {
        return excedente;
    }

    return new BigDecimal("0.00");
}

// Remove um participante pelo código, caso não tenha despesas vinculadas.
public void removerParticipante(int codigo) {

    // Localiza o objeto cadastrado.
    // Se não encontrar, buscarParticipante() lança uma exceção.
    Participante participante = buscarParticipante(codigo);

    // Confere todas as despesas antes de remover.
    for (Despesa despesa : despesas) {

        if (despesa.participa(participante)) {
            throw new IllegalArgumentException(
                "Não é possível remover um participante vinculado a despesas."
            );
        }
    }

    // Remove somente após confirmar que não existem vínculos.
    participantes.remove(participante);
}

// Atualiza o nome e o orçamento, mantendo o código e os vínculos.
public void atualizarParticipante(
        int codigo, String nome, BigDecimal orcamento) {

    // Obtém o próprio objeto cadastrado no planejamento.
    Participante participante = buscarParticipante(codigo);

    // Valida todos os novos dados antes de modificar o objeto.
    if (nome == null || nome.isBlank()) {
        throw new IllegalArgumentException("Nome inválido.");
    }

    if (orcamento == null || orcamento.signum() < 0) {
        throw new IllegalArgumentException("Orçamento inválido.");
    }

    // Aplica as alterações no mesmo objeto utilizado pelas despesas.
    participante.setNome(nome);
    participante.setOrcamento(orcamento);
}
}
