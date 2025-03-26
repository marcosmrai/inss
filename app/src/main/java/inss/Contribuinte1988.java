package inss;
import java.time.LocalDate;


/**
 * A classe Contribuinte representa um contribuinte do INSS com informações
 * sobre data de nascimento, gênero e contribuições mensais.
 */
public class Contribuinte1988 extends Contribuinte {
    /**
     * Construtor da classe Contribuinte.
     *
     * @param dataNascimento A data de nascimento do contribuinte.
     * @param genero O gênero do contribuinte, deve ser "Homem" ou "Mulher".
     * @throws IllegalArgumentException Se o gênero não for "Homem" ou "Mulher".
     */
    public Contribuinte1988(LocalDate dataNascimento, String genero) {
        super(dataNascimento, genero);
    }

    /**
     * Calcula o tempo restante até a aposentadoria com base na idade e gênero do contribuinte.
     *
     * @return O número de anos restantes até a aposentadoria.
     */
    @Override
    public int calcularTempoRestanteAposentadoria() {
        int idadeAposentadoria = getGenero().equals("Homem") ? 50 : 45;
        return Math.max(0, idadeAposentadoria - getIdade());
    }

    /**
     * Calcula o valor da aposentadoria com base na média das últimas 36 contribuições.
     *
     * @return O valor da aposentadoria.
     */
    @Override
    public double calcularValorAposentadoria() {
        return calcularMediaContribuicoes(36);
    }
}
