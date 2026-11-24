package persistence;

import model.Planejamento;

/**
 * Define o contrato para salvar e recuperar
 * um planejamento de despesas.
 *
 * A implementação concreta utilizará
 * a serialização de objetos Java.
 */
public interface Persistencia {

    /**
     * Salva o planejamento.
     *
     * @param planejamento planejamento que será armazenado
     */
    void salvar(Planejamento planejamento);

    /**
     * Recupera um planejamento anteriormente salvo.
     *
     * @return planejamento recuperado
     */
    Planejamento carregar();
}