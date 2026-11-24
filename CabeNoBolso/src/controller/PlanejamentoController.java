package controller;

import java.math.BigDecimal;
import model.Participante;
import model.Planejamento;
import persistence.Persistencia;
import java.util.List;
import model.Despesa;

/**
 * Controlador responsável por intermediar as operações
 * entre a interface gráfica e o planejamento de despesas.
 *
 * Utiliza as classes da camada Model e os serviços
 * de persistência, mantendo a separação de responsabilidades
 * da arquitetura MVC.
 */

public class PlanejamentoController {

	private Planejamento planejamento;
	private final Persistencia persistencia;

	public PlanejamentoController(
			Planejamento planejamento,
			Persistencia persistencia) {

			if (planejamento == null || persistencia == null) {
				throw new IllegalArgumentException(
					"Planejamento e persistência são obrigatórios."
				);
			}

			this.planejamento = planejamento;
			this.persistencia = persistencia;
		}

	/**
	 * Solicita o cadastro de um participante no planejamento.
	 *
	 * @param codigo identificador do participante
	 * @param nome nome do participante
	 * @param orcamento orçamento disponível
	 */
	public void cadastrarParticipante(int codigo, String nome, BigDecimal orcamento) {

		Participante participante =	new Participante(codigo, nome, orcamento);

		planejamento.adicionarParticipante(participante);
   }
	/**
	 * Retorna os participantes cadastrados no planejamento.
	 *
	 * @return lista de participantes cadastrados
	 */
	public List<Participante> listarParticipantes() {
		return planejamento.getParticipantes();
	}
	/**
	 * Atualiza o nome e o orçamento de um participante cadastrado.
	 *
	 * @param codigo código do participante
	 * @param nome novo nome
	 * @param orcamento novo orçamento
	 */
	public void atualizarParticipante(
			int codigo, String nome, BigDecimal orcamento) {

		planejamento.atualizarParticipante(codigo, nome, orcamento);
	}
	/**
	* Adiciona uma despesa ao planejamento.
	*
	* @param despesa despesa a ser cadastrada
	*/
	public void adicionarDespesa(Despesa despesa) {
		planejamento.adicionarDespesa(despesa);
	}
	/**
	* Calcula o valor total das despesas do planejamento.
	*
	* @return total geral das despesas
	*/
	public BigDecimal calcularTotalGeral() {
		return planejamento.calcularTotalGeral();
	}
	/**
	 * Calcula o gasto total de um participante.
	 *
	 * @param participante participante cadastrado
	 * @return total das despesas do participante
	 */
	public BigDecimal calcularTotalParticipante(
			Participante participante) {

		return planejamento.calcularTotalParticipante(participante);
	}
		/**
	 * Retorna as despesas cadastradas no planejamento.
	 *
	 * @return lista de despesas cadastradas
	 */
	public List<Despesa> listarDespesas() {
		return planejamento.getDespesas();
	}
	/**
	 * Verifica se o participante está dentro do orçamento.
	 */
	public boolean estaDentroDoOrcamento(Participante participante) {
		return planejamento.estaDentroDoOrcamento(participante);
	}

	/**
	 * Calcula o valor que ultrapassou o orçamento.
	 */
	public BigDecimal calcularExcedente(Participante participante) {
		return planejamento.calcularExcedente(participante);
	}
	/**
	 * Salva o planejamento atual em arquivo.
	 */
	public void salvarPlanejamento() {
		persistencia.salvar(planejamento);
	}
	/**
	 * Carrega um planejamento salvo e substitui
	 * o planejamento atualmente utilizado pelo Controller.
	 */
	public void carregarPlanejamento() {
		Planejamento carregado = persistencia.carregar();

		if (carregado == null) {
			throw new IllegalStateException(
				"Não foi possível carregar o planejamento."
			);
		}

		this.planejamento = carregado;
	}
	/**
	 * Atualiza uma despesa cadastrada no planejamento.
	 *
	 * @param despesa despesa que será alterada
	 * @param descricao nova descrição
	 * @param valor novo valor
	 * @param participantes participantes selecionados
	 */
	public void atualizarDespesa(
			Despesa despesa,
			String descricao,
			BigDecimal valor,
			List<Participante> participantes) {

		planejamento.atualizarDespesa(
				despesa, descricao, valor, participantes);
	}
	/**
	 * Remove uma despesa cadastrada no planejamento.
	 *
	 * @param despesa despesa que será removida
	 */
	public void removerDespesa(Despesa despesa) {
		planejamento.removerDespesa(despesa);
	}
	/**
	 * Remove um participante do planejamento.
	 *
	 * @param codigo código do participante
	 */
	public void removerParticipante(int codigo) {
		planejamento.removerParticipante(codigo);
	}

}

