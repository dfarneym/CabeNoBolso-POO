package controller;
import java.math.BigDecimal;
import model.Participante;

/**
 * Controlador responsável por intermediar as operações
 * entre a interface gráfica e o planejamento de despesas.
 *
 * Utiliza as classes da camada Model e os serviços
 * de persistência, mantendo a separação de responsabilidades
 * da arquitetura MVC.
 */

public class PlanejamentoController {
	
	/**
	 * Solicita o cadastro de um participante no planejamento.
	 *
	 * @param codigo identificador do participante
	 * @param nome nome do participante
	 * @param orcamento orçamento disponível
	 */
	public void cadastrarParticipante(int codigo, String nome, BigDecimal orcamento) {
		// Implementação após integração com Planejamento.
		Participante participante = new Participante(codigo, nome, orcamento);
	}
	
	/**
	 * Inicializa o controlador com suas dependências.
	 *
	 * @param planejamento objeto responsável por gerenciar
	 *                     participantes e despesas
	 * @param persistencia serviço responsável por salvar
	 *                     e carregar o planejamento
	 *
	 * O construtor será implementado após a integração
	 * das classes Planejamento e Persistencia.
	 */
	
	/*
	 * Construtor planejado para integração futura:
	 *
	 * public PlanejamentoController(
	 *         Planejamento planejamento,
	 *         Persistencia persistencia) {
	 *
	 *     this.planejamento = planejamento;
	 *     this.persistencia = persistencia;
	 * }
	 */
	

}
