package br.com.caelum.lemann;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImportaDados {

	public static void main(String[] args) throws IOException, JSONException {
		new ImportaDados().importaChallengeOutput(file("ChallengeOutput.json"));
		new ImportaDados().importaStudent(file("Student.json"));
		new ImportaDados().importaDiagnosis(file("DiagnosisLevelSnapshot.json"));
	}

	private static File file(String name) {
		return new File("projetos/caelum/gatopolis/dados/", name);
	}

	private void importaStudent(File file) throws IOException, JSONException {
		String content = Files.readAllLines(file.toPath()).stream().collect(Collectors.joining("\n"));
		JSONObject obj = new JSONObject(content);
		JSONArray results = obj.getJSONArray("results");
		
		List<Student> students = new ArrayList<Student>();
		for(int i = 0; i < results.length(); i++) {
			JSONObject result = results.getJSONObject(i);
			students.add(new Student(result));
		}
		
		PrintStream out = saida("students.txt");
		students.forEach(c -> c.printMysql(out));
		out.close();
		System.out.println(students.size() + " estudantes");
	}

	private PrintStream saida(String name) throws FileNotFoundException {
		return new PrintStream(file(name));
	}
	

	private void importaDiagnosis(File file) throws IOException, JSONException {
		String content = Files.readAllLines(file.toPath()).stream().collect(Collectors.joining("\n"));
		JSONObject obj = new JSONObject(content);
		JSONArray results = obj.getJSONArray("results");
		
		List<Diagnosis> diagnosis = new ArrayList<>();
		for(int i = 0; i < results.length(); i++) {
			JSONObject result = results.getJSONObject(i);
			diagnosis.add(new Diagnosis(result));
		}
		
		PrintStream out = saida("diagnosis.txt");
		diagnosis.forEach(c -> c.printMysql(out));
		out.close();
		System.out.println(diagnosis.size() + " dianósticos");
	}

	private void importaChallengeOutput(File file) throws IOException, JSONException {
		String content = Files.readAllLines(file.toPath()).stream().collect(Collectors.joining("\n"));
		JSONObject obj = new JSONObject(content);
		JSONArray results = obj.getJSONArray("results");
		List<ChallengeOutput > outputs = new ArrayList<>();
		for (int i = 0; i < results.length(); i++) {
			JSONObject result = results.getJSONObject(i);
			if(result.has("student")) {
				outputs.add(new ChallengeOutput(result));
			}
		}
		
		PrintStream out = saida("challenges.txt");
		outputs.forEach(c -> c.printMysql(out));
		out.close();
		System.out.println(outputs.size() + " challenges");
	}

}
