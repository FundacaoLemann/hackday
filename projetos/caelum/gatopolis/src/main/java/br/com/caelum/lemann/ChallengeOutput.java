package br.com.caelum.lemann;

import java.io.PrintStream;
import java.time.LocalDateTime;

import org.json.JSONException;
import org.json.JSONObject;

public class ChallengeOutput {

	private JSONObject result;
	private ChallengeOutputStudent student;
	private LocalDateTime createdAt;
	private String createdAtString;
	private String asserts;
	private String assertPercentage;

	public ChallengeOutput(JSONObject result) throws JSONException {
//		System.out.println(result);
		this.result = result;
		this.createdAtString = limpaData(result);
		this.createdAt = LocalDateTime.parse(limpaData(result));
		this.student = new ChallengeOutputStudent(result.getJSONObject("student"));
		this.asserts = result.getString("asserts");
		this.assertPercentage = result.getString("asserts_percentage");
	}

	private String limpaData(JSONObject result) throws JSONException {
		String data = result.getString("createdAt");
		return data.replaceAll("Z", "");
	}
	
	public void printMysql(PrintStream out) {
		out.println("insert into ChallengeOutput (student, createdAt, asserts, asserts_percentage) values ('" + student.getId() + "', '" + createdAtString + "', " + asserts + ", " + assertPercentage + ");\n ");
	}

}
