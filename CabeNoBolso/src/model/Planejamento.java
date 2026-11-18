package model;

import java.util.ArrayList;
import java.util.List;

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
}
