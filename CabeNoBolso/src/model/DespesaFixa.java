package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class DespesaFixa extends Despesa {

    private static final long serialVersionUID = 1L;

    public DespesaFixa(String descricao, BigDecimal valor,
                       List<Participante> participantes) {
        super(descricao, valor, participantes);
    }

    @Override
    public BigDecimal calcularTotal() {
        return getValor();
    }

    @Override
    public BigDecimal calcularValorParticipante(
            Participante participante) {

        if (!participa(participante)) {
            return new BigDecimal("0.00");
        }

        List<Participante> participantes = getParticipantes();

        BigDecimal quantidade =
                BigDecimal.valueOf(participantes.size());

        BigDecimal parte = getValor().divide(
                quantidade, 2, RoundingMode.DOWN);

        BigDecimal sobra =
                getValor().subtract(parte.multiply(quantidade));

        int centavosRestantes =
                sobra.multiply(new BigDecimal("100")).intValueExact();

        int posicao = 0;

        for (Participante p : participantes) {
            if (p.getCodigo() < participante.getCodigo()) {
                posicao++;
            }
        }

        if (posicao < centavosRestantes) {
            parte = parte.add(new BigDecimal("0.01"));
        }

        return parte;
    }
}
