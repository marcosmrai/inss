package inss;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;


/**
 * A classe Contribuinte representa um contribuinte do INSS com informações
 * sobre data de nascimento, gênero e contribuições mensais.
 */
public abstract class Contribuinte {
    private LocalDate dataNascimento;
    private String genero;
    private List<PagamentoMensal> contribuicoes;

    /**
     * Construtor da classe Contribuinte.
     *
     * @param dataNascimento A data de nascimento do contribuinte.
     * @param genero O gênero do contribuinte, deve ser "Homem" ou "Mulher".
     * @throws IllegalArgumentException Se o gênero não for "Homem" ou "Mulher".
     */
    public Contribuinte(LocalDate dataNascimento, String genero) {
        if (!genero.equals("Homem") && !genero.equals("Mulher")) {
            throw new IllegalArgumentException("Gênero deve ser 'Homem' ou 'Mulher'");
        }
        this.dataNascimento = dataNascimento;
        this.genero = genero;
        this.contribuicoes = new ArrayList<>();
    }

    /**
     * Obtém o gênero do contribuinte.
     *
     * @return O gênero do contribuinte.
     */
    public String getGenero() {
        return genero;
    }

    /**
     * Calcula a idade atual do contribuinte.
     *
     * @return A idade do contribuinte em anos.
     */
    public int getIdade() {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    /**
     * Adiciona uma contribuição mensal à lista de contribuições do contribuinte.
     *
     * @param contribuicao A contribuição mensal a ser adicionada.
     */
    public void adicionarContribuicao(PagamentoMensal contribuicao) {
        contribuicoes.add(contribuicao);
    }

    /**
     * Calcula o tempo total de contribuição em meses.
     *
     * @return O número total de meses de contribuição.
     */
    public int calcularTempoContribuicaoTotal() {
        return contribuicoes.size();
    }

    /**
     * Calcula o número de anos entre a primeira e a última contribuição.
     *
     * @return O número de anos de contribuição.
     */
    public int calcularAnosContribuicao() {
        if (contribuicoes.isEmpty()) {
            return 0;
        }
        LocalDate primeiraContribuicao = contribuicoes.get(0).getData();
        LocalDate ultimaContribuicao = contribuicoes.get(contribuicoes.size() - 1).getData();
        return Period.between(primeiraContribuicao, ultimaContribuicao).getYears();
    }

    /**
     * Calcula a média das últimas n contribuições.
     *
     * @param n O número de contribuições a serem consideradas para o cálculo da média.
     * @return A média das últimas n contribuições.
     */
    protected double calcularMediaContribuicoes(int n) {
        if (n <= 0 || n > contribuicoes.size()) {
            throw new IllegalArgumentException("Número inválido de contribuições");
        }
        return contribuicoes.stream()
                .skip(contribuicoes.size() - n)
                .mapToDouble(PagamentoMensal::getSalario)
                .average()
                .orElse(0.0);
    }

    /**
     * Calcula o tempo restante até a aposentadoria com base na idade e gênero do contribuinte.
     *
     * @return O número de anos restantes até a aposentadoria.
     */
    public abstract int calcularTempoRestanteAposentadoria();

    /**
     * Calcula o valor da aposentadoria com base na média das últimas 36 contribuições.
     *
     * @return O valor da aposentadoria.
     */
    public abstract double calcularValorAposentadoria();

    /**
     * Retorna uma representação em string do objeto Contribuinte.
     *
     * @return Uma string representando o objeto Contribuinte.
     */
    @Override
    public String toString() {
        return "Contribuinte{" +
                "classe='" + this.getClass() + '\'' +
                "dataNascimento=" + dataNascimento +
                ", genero='" + genero + '\'' +
                '}';
    }
}
