
package view;

import model.Participante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class TelaPrincipal extends JFrame {

    // ===== PARTICIPANTES =====

    private JTextField campoCodigo;
    private JTextField campoNome;
    private JTextField campoOrcamento;

    private JButton botaoCadastrar;
    private JButton botaoAlterar;

    private JTable tabelaParticipantes;
    private DefaultTableModel modeloParticipantes;

    // ===== DESPESAS =====

    private JTextField campoDescricao;
    private JTextField campoValor;
    private JComboBox<String> campoTipo;

    // Agora trabalhamos com objetos Participante
    private JList<Participante> listaParticipantes;
    private DefaultListModel<Participante> modeloListaParticipantes;

    private JButton botaoAdicionarDespesa;
    private JButton botaoEditarDespesa;
    private JButton botaoExcluirDespesa;

    private JTable tabelaDespesas;
    private DefaultTableModel modeloDespesas;

    // ===== RESUMO =====

    private JLabel textoTotalGeral;

    private JTable tabelaResumo;
    private DefaultTableModel modeloResumo;

    // ===== SALVAR E CARREGAR =====

    private JButton botaoSalvar;
    private JButton botaoCarregar;

    // ===== CONSTRUTOR =====

    public TelaPrincipal() {

        setTitle("Cabe no Bolso");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        JTabbedPane abas = new JTabbedPane();

        abas.addTab(
                "Participantes",
                criarPainelParticipantes()
        );

        abas.addTab(
                "Despesas",
                criarPainelDespesas()
        );

        abas.addTab(
                "Resumo",
                criarPainelResumo()
        );

        add(abas, BorderLayout.CENTER);

        // Botões principais
        botaoSalvar = new JButton("Salvar planejamento");
        botaoCarregar = new JButton("Carregar planejamento");

        JPanel painelBotoesPrincipais = new JPanel();

        painelBotoesPrincipais.add(botaoSalvar);
        painelBotoesPrincipais.add(botaoCarregar);

        add(painelBotoesPrincipais, BorderLayout.SOUTH);
    }

    // ===== ABA PARTICIPANTES =====

    private JPanel criarPainelParticipantes() {

        JPanel painel = new JPanel(
                new BorderLayout(10, 10)
        );

        JPanel formulario = new JPanel(
                new GridLayout(3, 2, 10, 10)
        );

        campoCodigo = new JTextField();
        campoNome = new JTextField();
        campoOrcamento = new JTextField();

        formulario.add(new JLabel("Código:"));
        formulario.add(campoCodigo);

        formulario.add(new JLabel("Nome:"));
        formulario.add(campoNome);

        formulario.add(new JLabel("Orçamento (R$):"));
        formulario.add(campoOrcamento);

        // Botões
        botaoCadastrar = new JButton("Cadastrar");
        botaoAlterar = new JButton("Alterar");

        JPanel painelBotoes = new JPanel();

        painelBotoes.add(botaoCadastrar);
        painelBotoes.add(botaoAlterar);

        // Tabela
        String[] colunas = {
                "Código",
                "Nome",
                "Orçamento (R$)"
        };

        modeloParticipantes = criarModeloTabela(colunas);

        tabelaParticipantes = new JTable(modeloParticipantes);

        tabelaParticipantes.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane rolagem =
                new JScrollPane(tabelaParticipantes);

        painel.add(formulario, BorderLayout.NORTH);
        painel.add(rolagem, BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);

        return painel;
    }

    // ===== ABA DESPESAS =====

    private JPanel criarPainelDespesas() {

        JPanel painel = new JPanel(
                new BorderLayout(10, 10)
        );

        // Formulário
        JPanel formulario = new JPanel(
                new GridLayout(3, 2, 10, 10)
        );

        campoDescricao = new JTextField();
        campoValor = new JTextField();

        campoTipo = new JComboBox<>(
                new String[]{"Fixa", "Por pessoa"}
        );

        formulario.add(new JLabel("Descrição:"));
        formulario.add(campoDescricao);

        formulario.add(new JLabel("Valor (R$):"));
        formulario.add(campoValor);

        formulario.add(new JLabel("Tipo:"));
        formulario.add(campoTipo);

        // ===== LISTA DE PARTICIPANTES =====

        modeloListaParticipantes = new DefaultListModel<>();

        listaParticipantes = new JList<>(
                modeloListaParticipantes
        );

        listaParticipantes.setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
        );

        // Mostra código e nome sem alterar o objeto
        listaParticipantes.setCellRenderer(
                new DefaultListCellRenderer() {

                    @Override
                    public Component getListCellRendererComponent(
                            JList<?> lista,
                            Object valor,
                            int indice,
                            boolean selecionado,
                            boolean comFoco) {

                        JLabel rotulo = (JLabel)
                                super.getListCellRendererComponent(
                                        lista,
                                        valor,
                                        indice,
                                        selecionado,
                                        comFoco
                                );

                        if (valor instanceof Participante) {

                            Participante participante =
                                    (Participante) valor;

                            rotulo.setText(
                                    participante.getCodigo()
                                            + " - "
                                            + participante.getNome()
                            );
                        }

                        return rotulo;
                    }
                }
        );

        JScrollPane rolagemParticipantes =
                new JScrollPane(listaParticipantes);

        JPanel painelSelecao = new JPanel(
                new BorderLayout(5, 5)
        );

        painelSelecao.add(
                new JLabel("Participantes envolvidos:"),
                BorderLayout.NORTH
        );

        painelSelecao.add(
                rolagemParticipantes,
                BorderLayout.CENTER
        );

        // ===== TABELA DE DESPESAS =====

        String[] colunasDespesas = {
                "Descrição",
                "Tipo",
                "Valor (R$)",
                "Participantes"
        };

        modeloDespesas = criarModeloTabela(colunasDespesas);

        tabelaDespesas = new JTable(modeloDespesas);

        tabelaDespesas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane rolagemDespesas =
                new JScrollPane(tabelaDespesas);

        JPanel painelCentral = new JPanel(
                new GridLayout(2, 1, 5, 5)
        );

        painelCentral.add(painelSelecao);
        painelCentral.add(rolagemDespesas);

        // Botões
        botaoAdicionarDespesa = new JButton("Adicionar");
        botaoEditarDespesa = new JButton("Editar");
        botaoExcluirDespesa = new JButton("Excluir");

        JPanel painelBotoes = new JPanel();

        painelBotoes.add(botaoAdicionarDespesa);
        painelBotoes.add(botaoEditarDespesa);
        painelBotoes.add(botaoExcluirDespesa);

        painel.add(formulario, BorderLayout.NORTH);
        painel.add(painelCentral, BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);

        return painel;
    }

    // ===== ABA RESUMO =====

    private JPanel criarPainelResumo() {

        JPanel painel = new JPanel(
                new BorderLayout(10, 10)
        );

        JPanel painelTotal = new JPanel();

        JLabel tituloTotal = new JLabel(
                "Total geral das despesas:"
        );

        textoTotalGeral = new JLabel("R$ 0,00");

        textoTotalGeral.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        painelTotal.add(tituloTotal);
        painelTotal.add(textoTotalGeral);

        String[] colunasResumo = {
                "Participante",
                "Total (R$)",
                "Orçamento (R$)",
                "Situação"
        };

        modeloResumo = criarModeloTabela(colunasResumo);

        tabelaResumo = new JTable(modeloResumo);

        JScrollPane rolagem =
                new JScrollPane(tabelaResumo);

        painel.add(painelTotal, BorderLayout.NORTH);
        painel.add(rolagem, BorderLayout.CENTER);

        return painel;
    }

    // ===== MODELO DE TABELA =====

    private DefaultTableModel criarModeloTabela(
            String[] colunas) {

        return new DefaultTableModel(colunas, 0) {

            @Override
            public boolean isCellEditable(
                    int linha,
                    int coluna) {

                return false;
            }
        };
    }

    // ===== CONSULTAR CAMPOS =====

    public String getCodigoDigitado() {
        return campoCodigo.getText().trim();
    }

    public String getNomeDigitado() {
        return campoNome.getText().trim();
    }

    public String getOrcamentoDigitado() {
        return campoOrcamento.getText().trim();
    }

    public String getDescricaoDigitada() {
        return campoDescricao.getText().trim();
    }

    public String getValorDigitado() {
        return campoValor.getText().trim();
    }

    public String getTipoSelecionado() {
        return (String) campoTipo.getSelectedItem();
    }

    // NOVO: retorna os objetos Participante
    public List<Participante> getParticipantesSelecionados() {

        return listaParticipantes.getSelectedValuesList();
    }

    // ===== LINHAS SELECIONADAS =====

    public int getLinhaParticipanteSelecionada() {

        int linha = tabelaParticipantes.getSelectedRow();

        if (linha == -1) {
            return -1;
        }

        return tabelaParticipantes.convertRowIndexToModel(
                linha
        );
    }

    public int getLinhaDespesaSelecionada() {

        int linha = tabelaDespesas.getSelectedRow();

        if (linha == -1) {
            return -1;
        }

        return tabelaDespesas.convertRowIndexToModel(
                linha
        );
    }

    // ===== PREENCHER CAMPOS =====

    public void preencherCamposParticipante(
            String codigo,
            String nome,
            String orcamento) {

        campoCodigo.setText(codigo);
        campoNome.setText(nome);
        campoOrcamento.setText(orcamento);
    }

    public void preencherCamposDespesa(
            String descricao,
            String valor,
            String tipo) {

        campoDescricao.setText(descricao);
        campoValor.setText(valor);
        campoTipo.setSelectedItem(tipo);
    }

    // ===== LIMPAR CAMPOS =====

    public void limparCamposParticipante() {

        campoCodigo.setText("");
        campoNome.setText("");
        campoOrcamento.setText("");
    }

    public void limparCamposDespesa() {

        campoDescricao.setText("");
        campoValor.setText("");
        campoTipo.setSelectedIndex(0);

        listaParticipantes.clearSelection();
    }

    // ===== ATUALIZAR LISTA DE PARTICIPANTES =====

    // Recebe os objetos originais do planejamento
    public void atualizarListaParticipantes(
            List<Participante> participantes) {

        modeloListaParticipantes.clear();

        for (Participante participante : participantes) {

            modeloListaParticipantes.addElement(
                    participante
            );
        }
    }

    // Seleciona na lista as pessoas de uma despesa
    public void selecionarParticipantes(
            List<Participante> participantes) {

        listaParticipantes.clearSelection();

        for (int i = 0;
             i < modeloListaParticipantes.size();
             i++) {

            Participante atual =
                    modeloListaParticipantes.getElementAt(i);

            for (Participante participante : participantes) {

                if (atual == participante) {

                    listaParticipantes.addSelectionInterval(
                            i, i
                    );

                    break;
                }
            }
        }
    }

    // ===== ATUALIZAR TABELAS =====

    public void limparTabelaParticipantes() {
        modeloParticipantes.setRowCount(0);
    }

    public void adicionarLinhaParticipante(
            Object codigo,
            Object nome,
            Object orcamento) {

        modeloParticipantes.addRow(
                new Object[]{
                        codigo,
                        nome,
                        orcamento
                }
        );
    }

    public void limparTabelaDespesas() {
        modeloDespesas.setRowCount(0);
    }

    public void adicionarLinhaDespesa(
            Object descricao,
            Object tipo,
            Object valor,
            Object participantes) {

        modeloDespesas.addRow(
                new Object[]{
                        descricao,
                        tipo,
                        valor,
                        participantes
                }
        );
    }

    public void limparTabelaResumo() {
        modeloResumo.setRowCount(0);
    }

    public void adicionarLinhaResumo(
            Object participante,
            Object total,
            Object orcamento,
            Object situacao) {

        modeloResumo.addRow(
                new Object[]{
                        participante,
                        total,
                        orcamento,
                        situacao
                }
        );
    }

    public void atualizarTotalGeral(String total) {
        textoTotalGeral.setText(total);
    }

    // ===== AÇÕES DOS BOTÕES =====

    public void aoCadastrar(ActionListener acao) {
        botaoCadastrar.addActionListener(acao);
    }

    public void aoAlterar(ActionListener acao) {
        botaoAlterar.addActionListener(acao);
    }

    public void aoAdicionarDespesa(ActionListener acao) {
        botaoAdicionarDespesa.addActionListener(acao);
    }

    public void aoEditarDespesa(ActionListener acao) {
        botaoEditarDespesa.addActionListener(acao);
    }

    public void aoExcluirDespesa(ActionListener acao) {
        botaoExcluirDespesa.addActionListener(acao);
    }

    public void aoSalvar(ActionListener acao) {
        botaoSalvar.addActionListener(acao);
    }

    public void aoCarregar(ActionListener acao) {
        botaoCarregar.addActionListener(acao);
    }

    // ===== MENSAGENS =====

    public void mostrarMensagem(String mensagem) {

        JOptionPane.showMessageDialog(
                this,
                mensagem
        );
    }

    public void mostrarErro(String mensagem) {

        JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Erro",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // ===== INICIAR O PROGRAMA =====

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            TelaPrincipal janela = new TelaPrincipal();
            janela.setVisible(true);

        });
    }
}



