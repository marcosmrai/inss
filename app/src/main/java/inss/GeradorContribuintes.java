package inss;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Random;

/**
 * The GeradorContribuintes class is responsible for generating random contributors
 * and simulating their career over time.
 */
public class GeradorContribuintes {
    private static final Random random = new Random();

    /**
     * Gera um novo contribuinte com dados aleatórios.
     *
     * @return Um objeto Contribuinte com data de nascimento, gênero e data de início de trabalho gerados aleatoriamente.
     * @throws IOException Se ocorrer um erro de I/O durante a geração do contribuinte.
     * @throws URISyntaxException Se ocorrer um erro de URI durante a geração do contribuinte.
     */
    public static Contribuinte gerarContribuinte() throws IOException, URISyntaxException {
        int anoNascimento = 1950 + random.nextInt(61); // Entre 1950 e 2010
        int mesNascimento = 1 + random.nextInt(12);
        int diaNascimento = 1 + random.nextInt(28); // Para simplificar, consideramos até 28 dias
        LocalDate dataNascimento = LocalDate.of(anoNascimento, mesNascimento, diaNascimento);

        String genero = random.nextBoolean() ? "Homem" : "Mulher";
        
        int idadeInicioTrabalho = 16 + random.nextInt(10); // Entre 16 e 25 anos
        LocalDate dataInicioTrabalho = dataNascimento.plusYears(idadeInicioTrabalho);

        Contribuinte contribuinte = gerarContribuinteAleatorio(dataNascimento, genero, dataInicioTrabalho);
        simularCarreira(contribuinte, dataInicioTrabalho);
        

        return contribuinte;
    }

    /**
     * Generates a random contributor based on the provided birth date, gender, and work start date.
     *
     * @param dataNascimento the birth date of the contributor
     * @param genero the gender of the contributor
     * @param dataInicioTrabalho the start date of the contributor's work
     * @return a Contribuinte object based on the work start date
     * @throws IOException if an I/O error occurs
     * @throws URISyntaxException if a URI syntax error occurs
     */
    private static Contribuinte gerarContribuinteAleatorio(LocalDate dataNascimento, String genero, LocalDate dataInicioTrabalho) throws IOException, URISyntaxException {
        int anoInicio = dataInicioTrabalho.getYear();
        if (anoInicio <= 1998) {
            return new Contribuinte1988(dataNascimento, genero);
        } else if (anoInicio <= 2019) {
            return new Contribuinte1998(dataNascimento, genero);
        } else {
            return new Contribuinte2019(dataNascimento, genero);
        }
        
    }

    /**
     * Simulates the career of a contributor over time, generating monthly salary contributions.
     *
     * @param contribuinte The contributor whose career is being simulated.
     * @param dataInicio The start date of the career simulation.
     */
    private static void simularCarreira(Contribuinte contribuinte, LocalDate dataInicio) {
        LocalDate dataAtual = dataInicio;
        LocalDate dataFim = LocalDate.of(2024, Month.DECEMBER, 31);
        boolean empregado = true;
        double salario = 1000 + random.nextDouble() * 9000; // Salário entre 1000 e 10000
        double desvioPadrao = 500; // Define o desvio padrão para o salário

        while (dataAtual.isBefore(dataFim)) {
            if (empregado) {
                // Simula salário mensal
                // System.out.println("Data: " + dataAtual + " - Salário: " + salario);

                // Cria uma nova contribuição mensal e adiciona ao contribuinte
                PagamentoMensal contribuicao = new PagamentoMensal(dataAtual, salario, contribuinte);
                contribuinte.adicionarContribuicao(contribuicao);

                // Verifica se perde o emprego
                if (random.nextDouble() < 0.005) {
                    empregado = false;
                    // System.out.println("Perdeu o emprego em " + dataAtual);
                }
                // Verifica se muda de emprego ou ganha aumento
                if (random.nextDouble() < 0.01) {
                    salario = salario + Math.abs(random.nextGaussian() * desvioPadrao);
                    // System.out.println("Mudou de emprego em " + dataAtual + " - Novo salário base: " + salario);
                }
            } else {
                // Verifica se consegue um novo emprego
                if (random.nextDouble() < 0.05) {
                    empregado = true;
                    salario = salario + random.nextGaussian() * desvioPadrao;
                    // System.out.println("Conseguiu um novo emprego em " + dataAtual);
                }
            }

            // Avança um mês
            dataAtual = dataAtual.plusMonths(1);
        }
    }
}
