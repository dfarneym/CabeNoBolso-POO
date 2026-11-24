package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public abstract class Despesa implements Serializable {
    private static final long serialVersionUID = 1L;

    private String descricao;
    private BigDecimal valor;
    private List<Participante> participantes;

    public Despesa(String descricao, BigDecimal valor,
                   List<Participante> participantes) {
        editar(descricao, valor, participantes);
    }

    private static void validarDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "A descrição não pode ser vazia.");
        }
    }

    private static BigDecimal validarValor(BigDecimal valor) {
        if (valor == null) {
            throw new IllegalArgumentException(
                    "Informe o valor da despesa.");
        }

        BigDecimal valorArredondado =
                valor.setScale(2, RoundingMode.HALF_UP);

        if (valorArredondado.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "O valor deve ser maior que zero.");
        }

        return valorArredondado;
    }

    private static void validarParticipantes(
            List<Participante> participantes) {

        if (participantes == null || participantes.isEmpty()) {
            throw new IllegalArgumentException(
                    "Selecione pelo menos um participante.");
        }

        List<Integer> codigos = new ArrayList<>();

        for (Participante p : participantes) {
            if (p == null) {
                throw new IllegalArgumentException(
                        "Participante inválido.");
            }

            if (codigos.contains(p.getCodigo())) {
                throw new IllegalArgumentException(
                        "Participante repetido.");
            }

            codigos.add(p.getCodigo());
        }
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public List<Participante> getParticipantes() {
        return new ArrayList<>(participantes);
    }

    public void setDescricao(String descricao) {
        validarDescricao(descricao);
        this.descricao = descricao.trim();
    }

    public void setValor(BigDecimal valor) {
        this.valor = validarValor(valor);
    }

    public void setParticipantes(
            List<Participante> participantes) {
        validarParticipantes(participantes);
        this.participantes = new ArrayList<>(participantes);
    }

    public final void editar(String descricao, BigDecimal valor,
                             List<Participante> participantes) {

        validarDescricao(descricao);
        BigDecimal novoValor = validarValor(valor);
        validarParticipantes(participantes);

        this.descricao = descricao.trim();
        this.valor = novoValor;
        this.participantes = new ArrayList<>(participantes);
    }

    public boolean participa(Participante participante) {
        if (participante == null) {
            return false;
        }

        for (Participante p : participantes) {
            if (p.getCodigo() == participante.getCodigo()) {
                return true;
            }
        }

        return false;
    }

    public abstract BigDecimal calcularTotal();

    public abstract BigDecimal calcularValorParticipante(
            Participante participante);
}
