package model;

import java.math.BigDecimal;
import java.util.List;

public class DespesaPorPessoa extends Despesa {

    private static final long serialVersionUID = 1L;

    public DespesaPorPessoa(String descricao, BigDecimal valor,
                            List<Participante> participantes) {
        super(descricao, valor, participantes);
    }

    @Override
    public BigDecimal calcularTotal() {
        return getValor().multiply(
                BigDecimal.valueOf(getParticipantes().size()));
    }

    @Override
    public BigDecimal calcularValorParticipante(
            Participante participante) {

        if (participa(participante)) {
            return getValor();
        }

        return new BigDecimal("0.00");
    }
}

