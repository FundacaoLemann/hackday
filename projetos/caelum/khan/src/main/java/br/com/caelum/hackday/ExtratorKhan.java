package br.com.caelum.hackday;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class ExtratorKhan {

	public static void main(String[] args) throws IOException {
		File csvData = new File("khan.csv");
		CSVParser parser = CSVParser.parse(csvData, StandardCharsets.UTF_8, CSVFormat.RFC4180);
		
		PrintStream out = new PrintStream("khan.sql");
		
		int registro = 1;
		for (CSVRecord csvRecord : parser) {
			
			if(registro++ == 1) continue;
			
			out.print("insert into khan (");
			out.print("rede,escola,turma,login,aluno,com_dificuldade,precisa_praticar,praticado,nivel1,nivel2,dominado,pontos,exerciseminutes,videominutes,totalminutes,semana");
			out.print(") values (");
			
			out.print("'" + limpaTexto(csvRecord.get(0)) + "',");
			out.print("'" + limpaTexto(csvRecord.get(1)) + "',");
			out.print("'" + limpaTexto(csvRecord.get(2).replace("'", "")) + "',");
			out.print("'" + limpaTexto(csvRecord.get(3)) + "',");
			out.print("'" + limpaTexto(csvRecord.get(4)) + "',");
			out.print("'" + limpaNumero(csvRecord.get(5)) + "',");
			out.print("'" + limpaNumero(csvRecord.get(6)) + "',");
			out.print("'" + limpaNumero(csvRecord.get(7)) + "',");
			out.print("'" + limpaNumero(csvRecord.get(8)) + "',");
			out.print("'" + limpaNumero(csvRecord.get(9)) + "',");
			out.print("'" + limpaNumero(csvRecord.get(10)) + "',");
			out.print("'" + limpaNumero(csvRecord.get(11)) + "',");
			out.print("'" + limpaNumero(csvRecord.get(12)) + "',");
			out.print("'" + limpaNumero(csvRecord.get(13)) + "',");
			out.print("'" + limpaNumero(csvRecord.get(14)) + "',");
			out.print("'" + csvRecord.get(15) + "')");
			out.print(";");
			
			out.println();
		}
		
		out.close();
	}

	private static String limpaTexto(String texto) {
		return texto.replace("'", "''");
	}

	private static String limpaNumero(String numero) {
		if(numero == null || numero.isEmpty()) return "0";
		return numero;
	}
}
