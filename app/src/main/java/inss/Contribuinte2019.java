package inss;

import java.time.LocalDate;

public class Contribuinte2019 extends Contribuinte {
    static int IDADE_MINIMA_HOMEM = 65;
    static int IDADE_MINIMA_MULHER = 62;
    static int TEMPO_MINIMO_CONTRIBUICAO_HOMEM = 20;
    static int TEMPO_MINIMO_CONTRIBUICAO_MULHER = 15;
    static int PONTOS_MINIMOS_HOMEM = 105;
    static int PONTOS_MINIMOS_MULHER = 100;

    /**
     * Constructs a Contribuinte2019 object with the specified date of birth and gender.
     *
     * @param dataNascimento the date of birth of the contributor
     * @param genero the gender of the contributor
     */
    public Contribuinte2019(LocalDate dataNascimento, String genero) {
        super(dataNascimento, genero);
    }

    /**
     * Calculates the remaining time until retirement.
     * 
     * This method determines the minimum retirement age based on the gender of the contributor.
     * It then calculates the remaining years until the contributor reaches the minimum retirement age.
     * Additionally, it calculates the minimum points required for retirement based on the gender of the contributor.
     * The method returns the maximum value between the remaining years until the minimum retirement age 
     * and the difference between the minimum points required and the sum of the contributor's age and years of contribution.
     * 
     * @return the remaining time until retirement in years.
     */
    @Override
    public int calcularTempoRestanteAposentadoria() {
        int idadeMinima = "Homem".equalsIgnoreCase(getGenero()) ? IDADE_MINIMA_HOMEM : IDADE_MINIMA_MULHER;
        int anosRestantesIdade = idadeMinima - getIdade();
        int pontosMinimos = "Homem".equalsIgnoreCase(getGenero()) ? PONTOS_MINIMOS_HOMEM : PONTOS_MINIMOS_MULHER;
        return Math.max(anosRestantesIdade, pontosMinimos-(calcularAnosContribuicao()+getIdade()));
    }

    /**
     * Calculates the previdenciary factor based on the contribution time and the minimum contribution time.
     * The factor is calculated using the formula: 0.6 + 0.02 * (contributionTime - minimumContributionTime).
     *
     * @return the calculated previdenciary factor.
     */
    private double calcularFatorPrevidenciario() {
        double tempoContribuicao = calcularAnosContribuicao();
        double tempoMinimoContribuicao = "Homem".equalsIgnoreCase(getGenero()) ? TEMPO_MINIMO_CONTRIBUICAO_HOMEM : TEMPO_MINIMO_CONTRIBUICAO_MULHER;
        return 0.6 + 0.02*(tempoContribuicao-tempoMinimoContribuicao);
    }

    /**
     * Calculates the average contributions for the year 2019.
     * 
     * This method calculates the average contributions by first determining the total contribution time
     * and then using that value to compute the average contributions.
     * 
     * @return the average contributions as a double value
     */
    private double calcularMediaContribuicoes() {
        return calcularMediaContribuicoes(calcularTempoContribuicaoTotal());
    }

    /**
     * Calculates the retirement value for the contributor.
     * This method computes the average salary based on contributions and applies
     * the previdenciary factor to determine the final retirement amount.
     *
     * @return the calculated retirement value
     */
    @Override
    public double calcularValorAposentadoria() {
        double salarioMedio = calcularMediaContribuicoes();
        double fatorPrevidenciario = calcularFatorPrevidenciario();
        return salarioMedio * fatorPrevidenciario;
    }
}
