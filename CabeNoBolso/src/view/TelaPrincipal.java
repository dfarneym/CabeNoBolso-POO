
package view;

import controller.PlanejamentoController;
import model.Participante;
import model.Despesa;
import model.DespesaFixa;
import model.DespesaPorPessoa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TelaPrincipal extends JFrame {

    // ===== CONTROLLER =====

    private final PlanejamentoController controller;

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

    // ===== ARQUIVOS =====

    private JButton botaoSalvar;
    private JButton botaoCarregar;

    // ===== CONSTRUTOR =====

    public TelaPrincipal(PlanejamentoController controller) {

        if (controller == null) {
            throw new IllegalArgumentException(
                    "Controller não pode ser nulo."
            );
        }

        this.controller = controller;

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

        // Botões sempre visíveis
        botaoSalvar = new JButton("Salvar planejamento");
        botaoCarregar = new JButton("Carregar planejamento");

        JPanel painelArquivos = new JPanel();

        painelArquivos.add(botaoSalvar);
        painelArquivos.add(botaoCarregar);

        add(painelArquivos, BorderLayout.SOUTH);

        // Conecta todos os eventos
        conectarAcoesParticipantes();
        conectarAcoesDespesas();
        conectarAcoesArquivos();

        // Atualiza os dados ao abrir a janela
        atualizarTudoNaTela();
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

        botaoCadastrar = new JButton("Cadastrar");
        botaoAlterar = new JButton("Alterar");

        JPanel painelBotoes = new JPanel();

        painelBotoes.add(botaoCadastrar);
        painelBotoes.add(botaoAlterar);

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

        // ===== BOTÕES DE DESPESAS =====

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

    // ===== MÉTODOS AUXILIARES =====

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

    private String formatarMoeda(BigDecimal valor) {

        NumberFormat formato =
                NumberFormat.getCurrencyInstance(
                        Locale.of("pt", "BR")
                );

        return formato.format(valor);
    }

    private BigDecimal converterValor(String texto) {

        String valor = texto.trim().replace(",", ".");

        return new BigDecimal(valor);
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

    public List<Participante> getParticipantesSelecionados() {
        return listaParticipantes.getSelectedValuesList();
    }

    // ===== LINHAS SELECIONADAS =====

    public int getLinhaParticipanteSelecionada() {

        int linha = tabelaParticipantes.getSelectedRow();

        if (linha == -1) {
            return -1;
        }

        return tabelaParticipantes.convertRowIndexToModel(linha);
    }

    public int getLinhaDespesaSelecionada() {

        int linha = tabelaDespesas.getSelectedRow();

        if (linha == -1) {
            return -1;
        }

        return tabelaDespesas.convertRowIndexToModel(linha);
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

        tabelaParticipantes.clearSelection();
    }

    public void limparCamposDespesa() {

        tabelaDespesas.clearSelection();

        campoDescricao.setText("");
        campoValor.setText("");
        campoTipo.setSelectedIndex(0);
        campoTipo.setEnabled(true);

        listaParticipantes.clearSelection();
    }

    // ===== LISTA DE PARTICIPANTES =====

    public void atualizarListaParticipantes(
            List<Participante> participantes) {

        modeloListaParticipantes.clear();

        for (Participante participante : participantes) {

            modeloListaParticipantes.addElement(participante);
        }
    }

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

                    listaParticipantes.addSelectionInterval(i, i);
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
                new Object[]{codigo, nome, orcamento}
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

        JOptionPane.showMessageDialog(this, mensagem);
    }

    public void mostrarErro(String mensagem) {

        JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Erro",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // ===== EVENTOS DE PARTICIPANTES =====

    private void conectarAcoesParticipantes() {

        aoCadastrar(evento -> cadastrarParticipante());

        aoAlterar(evento -> alterarParticipante());

        tabelaParticipantes.getSelectionModel()
                .addListSelectionListener(evento -> {

                    if (!evento.getValueIsAdjusting()) {
                        preencherParticipanteSelecionado();
                    }
                });
    }

    // ===== CADASTRAR PARTICIPANTE =====

    private void cadastrarParticipante() {

        try {

            int codigo = Integer.parseInt(
                    getCodigoDigitado()
            );

            String nome = getNomeDigitado();

            BigDecimal orcamento = converterValor(
                    getOrcamentoDigitado()
            );

            controller.cadastrarParticipante(
                    codigo,
                    nome,
                    orcamento
            );

            atualizarParticipantesNaTela();
            atualizarResumoNaTela();
            limparCamposParticipante();

            mostrarMensagem(
                    "Participante cadastrado com sucesso!"
            );

        } catch (IllegalArgumentException erro) {

            mostrarErro(
                    "Não foi possível cadastrar: "
                            + erro.getMessage()
            );
        }
    }

    // ===== ALTERAR PARTICIPANTE =====

    private void alterarParticipante() {

        try {

            int linha = getLinhaParticipanteSelecionada();

            if (linha == -1) {

                mostrarErro(
                        "Selecione um participante na tabela."
                );

                return;
            }

            int codigo = Integer.parseInt(
                    modeloParticipantes.getValueAt(
                            linha, 0
                    ).toString()
            );

            String nome = getNomeDigitado();

            BigDecimal orcamento = converterValor(
                    getOrcamentoDigitado()
            );

            controller.atualizarParticipante(
                    codigo,
                    nome,
                    orcamento
            );

            atualizarParticipantesNaTela();
            atualizarResumoNaTela();
            limparCamposParticipante();

            mostrarMensagem(
                    "Participante atualizado com sucesso!"
            );

        } catch (IllegalArgumentException erro) {

            mostrarErro(
                    "Não foi possível alterar: "
                            + erro.getMessage()
            );
        }
    }

    // ===== PREENCHER PARTICIPANTE SELECIONADO =====

    private void preencherParticipanteSelecionado() {

        int linha = getLinhaParticipanteSelecionada();

        if (linha == -1) {
            return;
        }

        int codigo = Integer.parseInt(
                modeloParticipantes.getValueAt(
                        linha, 0
                ).toString()
        );

        for (Participante participante :
                controller.listarParticipantes()) {

            if (participante.getCodigo() == codigo) {

                preencherCamposParticipante(
                        String.valueOf(participante.getCodigo()),
                        participante.getNome(),
                        participante.getOrcamento().toPlainString()
                );

                return;
            }
        }
    }

    // ===== ATUALIZAR PARTICIPANTES =====

    public void atualizarParticipantesNaTela() {

        List<Participante> participantes =
                controller.listarParticipantes();

        limparTabelaParticipantes();

        for (Participante participante : participantes) {

            adicionarLinhaParticipante(
                    participante.getCodigo(),
                    participante.getNome(),
                    formatarMoeda(participante.getOrcamento())
            );
        }

        atualizarListaParticipantes(participantes);
    }

    // ===== EVENTOS DE DESPESAS =====

    private void conectarAcoesDespesas() {

        aoAdicionarDespesa(evento -> adicionarDespesa());
        aoEditarDespesa(evento -> editarDespesa());
        aoExcluirDespesa(evento -> excluirDespesa());

        tabelaDespesas.getSelectionModel()
                .addListSelectionListener(evento -> {

                    if (!evento.getValueIsAdjusting()) {
                        preencherDespesaSelecionada();
                    }
                });
    }

    // ===== ADICIONAR DESPESA =====

    private void adicionarDespesa() {

        try {

            if (getLinhaDespesaSelecionada() != -1) {

                mostrarErro(
                        "Finalize a edição ou desmarque a "
                                + "despesa antes de adicionar outra."
                );

                return;
            }

            String descricao = getDescricaoDigitada();

            BigDecimal valor = converterValor(
                    getValorDigitado()
            );

            List<Participante> participantes =
                    getParticipantesSelecionados();

            if (participantes.isEmpty()) {

                mostrarErro(
                        "Selecione pelo menos um participante."
                );

                return;
            }

            Despesa despesa;

            if ("Fixa".equals(getTipoSelecionado())) {

                despesa = new DespesaFixa(
                        descricao,
                        valor,
                        participantes
                );

            } else {

                despesa = new DespesaPorPessoa(
                        descricao,
                        valor,
                        participantes
                );
            }

            controller.adicionarDespesa(despesa);

            atualizarDespesasNaTela();
            atualizarResumoNaTela();
            limparCamposDespesa();

            mostrarMensagem(
                    "Despesa adicionada com sucesso!"
            );

        } catch (IllegalArgumentException erro) {

            mostrarErro(
                    "Não foi possível adicionar a despesa: "
                            + erro.getMessage()
            );
        }
    }

    // ===== EDITAR DESPESA =====

    private void editarDespesa() {

        try {

            int linha = getLinhaDespesaSelecionada();

            if (linha == -1) {

                mostrarErro(
                        "Selecione uma despesa na tabela "
                                + "para editar."
                );

                return;
            }

            List<Despesa> despesas =
                    controller.listarDespesas();

            Despesa despesa = despesas.get(linha);

            String descricao = getDescricaoDigitada();

            BigDecimal valor = converterValor(
                    getValorDigitado()
            );

            List<Participante> participantes =
                    getParticipantesSelecionados();

            if (participantes.isEmpty()) {

                mostrarErro(
                        "Selecione pelo menos um participante."
                );

                return;
            }

            controller.atualizarDespesa(
                    despesa,
                    descricao,
                    valor,
                    participantes
            );

            atualizarDespesasNaTela();
            atualizarResumoNaTela();
            limparCamposDespesa();

            mostrarMensagem(
                    "Despesa atualizada com sucesso!"
            );

        } catch (IllegalArgumentException erro) {

            mostrarErro(
                    "Não foi possível editar a despesa: "
                            + erro.getMessage()
            );

        } catch (IndexOutOfBoundsException erro) {

            mostrarErro(
                    "A despesa selecionada não foi encontrada."
            );
        }
    }

    // ===== EXCLUIR DESPESA =====

    private void excluirDespesa() {

        try {

            int linha = getLinhaDespesaSelecionada();

            if (linha == -1) {

                mostrarErro(
                        "Selecione uma despesa na tabela "
                                + "para excluir."
                );

                return;
            }

            List<Despesa> despesas =
                    controller.listarDespesas();

            Despesa despesa = despesas.get(linha);

            int resposta = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente excluir a despesa "
                            + despesa.getDescricao() + "?",
                    "Confirmar exclusão",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (resposta != JOptionPane.YES_OPTION) {
                return;
            }

            controller.removerDespesa(despesa);

            atualizarDespesasNaTela();
            atualizarResumoNaTela();
            limparCamposDespesa();

            mostrarMensagem(
                    "Despesa excluída com sucesso!"
            );

        } catch (IllegalArgumentException erro) {

            mostrarErro(
                    "Não foi possível excluir a despesa: "
                            + erro.getMessage()
            );

        } catch (IndexOutOfBoundsException erro) {

            mostrarErro(
                    "A despesa selecionada não foi encontrada."
            );
        }
    }

    // ===== PREENCHER DESPESA SELECIONADA =====

    private void preencherDespesaSelecionada() {

        int linha = getLinhaDespesaSelecionada();

        if (linha == -1) {

            campoTipo.setEnabled(true);
            return;
        }

        List<Despesa> despesas =
                controller.listarDespesas();

        if (linha >= despesas.size()) {
            return;
        }

        Despesa despesa = despesas.get(linha);

        String tipo;

        if (despesa instanceof DespesaFixa) {
            tipo = "Fixa";
        } else {
            tipo = "Por pessoa";
        }

        preencherCamposDespesa(
                despesa.getDescricao(),
                despesa.getValor().toPlainString(),
                tipo
        );

        campoTipo.setEnabled(false);

        selecionarParticipantes(
                despesa.getParticipantes()
        );
    }

    // ===== ATUALIZAR DESPESAS =====

    public void atualizarDespesasNaTela() {

        List<Despesa> despesas =
                controller.listarDespesas();

        limparTabelaDespesas();

        for (Despesa despesa : despesas) {

            String tipo;

            if (despesa instanceof DespesaFixa) {
                tipo = "Fixa";
            } else {
                tipo = "Por pessoa";
            }

            StringBuilder nomes = new StringBuilder();

            for (Participante participante :
                    despesa.getParticipantes()) {

                if (nomes.length() > 0) {
                    nomes.append(", ");
                }

                nomes.append(participante.getNome());
            }

            adicionarLinhaDespesa(
                    despesa.getDescricao(),
                    tipo,
                    formatarMoeda(despesa.getValor()),
                    nomes.toString()
            );
        }
    }

    // ===== ATUALIZAR RESUMO =====

    public void atualizarResumoNaTela() {

        BigDecimal totalGeral =
                controller.calcularTotalGeral();

        atualizarTotalGeral(
                formatarMoeda(totalGeral)
        );

        limparTabelaResumo();

        List<Participante> participantes =
                controller.listarParticipantes();

        for (Participante participante : participantes) {

            BigDecimal totalParticipante =
                    controller.calcularTotalParticipante(
                            participante
                    );

            BigDecimal orcamento =
                    participante.getOrcamento();

            String situacao;

            if (controller.estaDentroDoOrcamento(
                    participante)) {

                situacao = "Dentro do orçamento";

            } else {

                BigDecimal excedente =
                        controller.calcularExcedente(
                                participante
                        );

                situacao = "Excedeu "
                        + formatarMoeda(excedente);
            }

            adicionarLinhaResumo(
                    participante.getNome(),
                    formatarMoeda(totalParticipante),
                    formatarMoeda(orcamento),
                    situacao
            );
        }
    }

    // ===== NOVO: ATUALIZAR TODAS AS ABAS =====

    public void atualizarTudoNaTela() {

        atualizarParticipantesNaTela();
        atualizarDespesasNaTela();
        atualizarResumoNaTela();
    }

    // ===== NOVO: EVENTOS DE ARQUIVOS =====

    private void conectarAcoesArquivos() {

        aoSalvar(evento -> salvarPlanejamento());

        aoCarregar(evento -> carregarPlanejamento());
    }

    // ===== NOVO: SALVAR PLANEJAMENTO =====

    private void salvarPlanejamento() {

        try {

            // O Controller utiliza a persistência
            // configurada pela classe Main.
            controller.salvarPlanejamento();

            mostrarMensagem(
                    "Planejamento salvo com sucesso!"
            );

        } catch (RuntimeException erro) {

            mostrarErro(
                    "Não foi possível salvar o planejamento: "
                            + erro.getMessage()
            );
        }
    }

    // ===== NOVO: CARREGAR PLANEJAMENTO =====

    private void carregarPlanejamento() {

        // Aviso para evitar perder alterações não salvas
        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Carregar um planejamento salvo substituirá "
                        + "os dados atuais.\nDeseja continuar?",
                "Confirmar carregamento",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            // Recupera os dados salvos pelo Controller
            controller.carregarPlanejamento();

            // Limpa os formulários da tela
            limparCamposParticipante();
            limparCamposDespesa();

            // Exibe os dados que acabaram de ser carregados
            atualizarTudoNaTela();

            mostrarMensagem(
                    "Planejamento carregado com sucesso!"
            );

        } catch (RuntimeException erro) {

            mostrarErro(
                    "Não foi possível carregar o planejamento: "
                            + erro.getMessage()
            );
        }
    }
}
