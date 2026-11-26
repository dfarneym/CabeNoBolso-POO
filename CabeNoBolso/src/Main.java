import controller.PlanejamentoController;
import model.Planejamento;
import persistence.ArquivoPersistencia;
import view.TelaPrincipal;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            Planejamento planejamento = new Planejamento();

            ArquivoPersistencia persistencia =
                    new ArquivoPersistencia("planejamento.dat");

            PlanejamentoController controller =
                    new PlanejamentoController(planejamento, persistencia);

            TelaPrincipal tela = new TelaPrincipal(controller);
            tela.setVisible(true);
        });
    }
}