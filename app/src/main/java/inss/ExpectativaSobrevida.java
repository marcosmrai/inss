package inss;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExpectativaSobrevida {

    private static ExpectativaSobrevida instanciaHomem;
    private static ExpectativaSobrevida instanciaMulher;

    private final Map<Integer, Double> expectativaVidaHomens;
    private final Map<Integer, Double> expectativaVidaMulheres;
    private final String genero;

    private ExpectativaSobrevida(String genero) throws IOException, URISyntaxException {
        this.genero = genero;
        this.expectativaVidaHomens = carregarExpectativaVida("https://ftp.ibge.gov.br/Tabuas_Completas_de_Mortalidade/Tabuas_Completas_de_Mortalidade_2023/xlsx/homens.xlsx");
        this.expectativaVidaMulheres = carregarExpectativaVida("https://ftp.ibge.gov.br/Tabuas_Completas_de_Mortalidade/Tabuas_Completas_de_Mortalidade_2023/xlsx/mulheres.xlsx");
    }

    public static ExpectativaSobrevida getInstance(String genero) throws IOException, URISyntaxException {
        if ("Homem".equalsIgnoreCase(genero)) {
            if (instanciaHomem == null) {
                instanciaHomem = new ExpectativaSobrevida(genero);
            }
            return instanciaHomem;
        } else if ("Mulher".equalsIgnoreCase(genero)) {
            if (instanciaMulher == null) {
                instanciaMulher = new ExpectativaSobrevida(genero);
            }
            return instanciaMulher;
        }
        throw new IllegalArgumentException("Gênero inválido: " + genero);
    }

    @Override
    public String toString() {
        return "ExpectativaSobrevida{" +
                ", genero='" + genero + '\'' +
                '}';
    }

    public double obterExpectativaSobrevida(int idade) {
        if ("M".equalsIgnoreCase(genero)) {
            Double expectativa = expectativaVidaHomens.get(idade);
            if (expectativa != null) {
                return expectativa;
            }
        } else if ("F".equalsIgnoreCase(genero)) {
            Double expectativa = expectativaVidaMulheres.get(idade);
            if (expectativa != null) {
                return expectativa;
            }
        }
        // Tratar caso a idade/gênero não seja encontrada (pode retornar um valor padrão ou lançar uma exceção)
        System.err.println("Idade ou gênero não encontrados: idade=" + idade + ", gênero=" + genero);
        return -1; // Retorna -1 como valor padrão em caso de erro
    }

    private Map<Integer, Double> carregarExpectativaVida(String url) throws IOException, URISyntaxException {
        File arquivoExcel = baixarArquivoExcel(url);
        Map<Integer, Double> dadosExpectativa = lerDadosExpectativa(arquivoExcel);
        arquivoExcel.delete(); // Apaga o arquivo temporário
        return dadosExpectativa;
    }

    private File baixarArquivoExcel(String url) throws IOException, URISyntaxException {
        URL urlArquivo = new URI(url).toURL();
        File arquivoTemporario = File.createTempFile("expectativa_temp", ".xlsx");
        try (InputStream entrada = urlArquivo.openStream();
             FileOutputStream saida = new FileOutputStream(arquivoTemporario)) {
            byte[] buffer = new byte[1024];
            int bytesLidos;
            while ((bytesLidos = entrada.read(buffer)) != -1) {
                saida.write(buffer, 0, bytesLidos);
            }
        }
        return arquivoTemporario;
    }

    private Map<Integer, Double> lerDadosExpectativa(File arquivo) throws IOException {
        Map<Integer, Double> dados = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(arquivo);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int linhaIndice = 4; linhaIndice <= sheet.getLastRowNum(); linhaIndice++) {
                Row linha = sheet.getRow(linhaIndice);
                if (linha != null) {
                    try {
                        Cell celulaIdade = linha.getCell(0);
                        Cell celulaExpectativa = linha.getCell(6);

                        if (celulaIdade != null && celulaExpectativa != null) {
                            if (celulaIdade.getCellType() == CellType.NUMERIC && celulaExpectativa.getCellType() == CellType.NUMERIC) {
                                int idade = (int) celulaIdade.getNumericCellValue();
                                double expectativa = celulaExpectativa.getNumericCellValue();
                                dados.put(idade, expectativa);
                            } else {
                                //pass
                            }
                        }
                    } catch (NumberFormatException e) {
                        //pass
                    }
                }
            }
        }
        return dados;
    }

    public static void main(String[] args) {
        try {
            ExpectativaSobrevida expectativaHomem = ExpectativaSobrevida.getInstance("M");
            System.out.println("Expectativa de sobrevida aos 35 anos (Homem): " + expectativaHomem.obterExpectativaSobrevida(35));

            ExpectativaSobrevida expectativaMulher = ExpectativaSobrevida.getInstance("F");
            System.out.println("Expectativa de sobrevida aos 40 anos (Mulher): " + expectativaMulher.obterExpectativaSobrevida(40));

        } catch (IOException | URISyntaxException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
}
