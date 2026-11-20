package model;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class Planejamento {
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

}
