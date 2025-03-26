package inss;
import java.time.LocalDate;


/**
 * A classe PagamentoMensal representa um pagamento mensal realizado a um contribuinte.
 * Ela contém informações sobre a data do pagamento, o salário e o contribuinte.
 */
public class PagamentoMensal {
    /**
     * Constrói um novo PagamentoMensal com a data, salário e contribuinte especificados.
     *
     * @param data a data do pagamento
     * @param salario o valor do salário
     * @param contribuinte o contribuinte que recebe o pagamento
     */
    private final LocalDate data;
    private final double salario;
    private final Contribuinte contribuinte;

    
    public PagamentoMensal(LocalDate data, double salario, Contribuinte contribuinte) {
        this.data = data;
        this.salario = salario;
        this.contribuinte = contribuinte;
    }

    /**
     * Retorna a data do pagamento.
     *
     * @return a data do pagamento
     */
    public LocalDate getData() {
        return data;
    }

    /**
     * Retorna o valor do salário.
     *
     * @return o valor do salário
     */
    public double getSalario() {
        return salario;
    }

    /**
     * Retorna o contribuinte que recebe o pagamento.
     *
     * @return o contribuinte que recebe o pagamento
     */
    public Contribuinte getContribuinte() {
        return contribuinte;
    }
}
