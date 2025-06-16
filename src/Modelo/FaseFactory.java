package Modelo;

public class FaseFactory {
    public static Fase criarFase(int numeroFase) {
        Fase fase = switch (numeroFase) {
            case 1 -> new Fase1();
            case 2 -> new Fase2();
            case 3 -> new Fase3();
            case 4 -> new Fase4();
            case 5 -> new Fase5();
            default -> {
                System.out.println("⚠️ Fase " + numeroFase + " não definida. Carregando fase padrão.");
                yield new Fase1();
            }
        };

        fase.carregarFase();
        return fase;
    }
}
