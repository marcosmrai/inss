package inss;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;

/**
 * A classe Contribuinte1998 representa um contribuinte do sistema de seguridade social
 * que começou a contribuir em 1998. Esta classe estende a classe Contribuinte e
 * inclui regras específicas para calcular a elegibilidade e os benefícios de aposentadoria
 * com base nas regulamentações vigentes em 1998.
 */
public class Contribuinte1998 extends Contribuinte {
    static int IDADE_MINIMA_APOSENTADORIA = 55; // Exemplo de idade mínima para aposentadoria
    static int TEMPO_MINIMO_CONTRIBUICAO = 25; // Exemplo de tempo mínimo de contribuição
    private final ExpectativaSobrevida expectativa;
    
    /**
     * Constructs a Contribuinte1998 object with the specified date of birth and gender.
     *
     * @param dataNascimento the date of birth of the contributor
     * @param genero the gender of the contributor
     * @throws IOException if an I/O error occurs while initializing ExpectativaSobrevida
     * @throws URISyntaxException if a URI syntax error occurs while initializing ExpectativaSobrevida
     * @throws RuntimeException if an error occurs while initializing ExpectativaSobrevida
     */
    public Contribuinte1998(LocalDate dataNascimento, String genero) throws IOException, URISyntaxException {
        super(dataNascimento, genero);
        try {
            expectativa = ExpectativaSobrevida.getInstance(genero);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Error initializing ExpectativaSobrevida", e);
        }
    }

    /**
     * Calculates the remaining time until retirement for a contributor.
     * 
     * This method considers both the minimum retirement age and the minimum contribution time,
     * taking into account a gender-based bonus for men.
     * 
     * @return the maximum of the remaining years based on age and contribution time.
     */
    @Override
    public int calcularTempoRestanteAposentadoria() {
        int bonusGenero = ("Homem".equalsIgnoreCase(getGenero()) ? 5 : 0);
        int anosRestantesIdade = IDADE_MINIMA_APOSENTADORIA + bonusGenero - getIdade();
        int anosRestantesContribuicao = TEMPO_MINIMO_CONTRIBUICAO + bonusGenero - calcularTempoContribuicaoTotal()/12;
        return Math.max(anosRestantesIdade, anosRestantesContribuicao);
    }

    /**
     * Calculates the previdenciary factor for the contributor.
     *
     * The previdenciary factor is calculated based on the contribution rate, 
     * the number of years of contribution, the contributor's age, and the 
     * life expectancy at the contributor's age.
     *
     * @return the calculated previdenciary factor
     */
    public double calcularFatorPrevidenciario() {
        double aliquotaContribuicao = 0.31;
        double tempoContribuicao = calcularAnosContribuicao();
        int idade = getIdade();
        return (tempoContribuicao * aliquotaContribuicao) / expectativa.obterExpectativaSobrevida(idade) * (1 + idade + tempoContribuicao * aliquotaContribuicao);
    }

    /**
     * Calculates the average contributions of the contributor.
     *
     * @return the average contributions as a double value.
     */
    private double calcularMediaContribuicoes(){
        return calcularMediaContribuicoes(calcularTempoContribuicaoTotal());
    }

    /**
     * Calculates the retirement amount for the contributor.
     *
     * This method calculates the average salary of contributions and the social security factor,
     * then returns the product of these two values as the retirement amount.
     *
     * @return the calculated retirement amount
     */
    @Override
    public double calcularValorAposentadoria() {
        double salarioMedio = calcularMediaContribuicoes();
        double fatorPrevidenciario = calcularFatorPrevidenciario();
        return salarioMedio * fatorPrevidenciario;
    }
}
