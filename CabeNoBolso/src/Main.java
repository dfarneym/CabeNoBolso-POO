import controller.PlanejamentoController;
import model.Planejamento;
import persistence.ArquivoPersistencia;

public class Main {

    public static void main(String[] args) {

        Planejamento planejamento = new Planejamento();

        ArquivoPersistencia persistencia =
                new ArquivoPersistencia("planejamento.dat");

        PlanejamentoController controller =
                new PlanejamentoController(
                        planejamento, persistencia);

        System.out.println(
                "Cabe no Bolso: aplicação inicializada.");
    }
}
