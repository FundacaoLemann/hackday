package br.com.caelum.lemann;

import java.io.PrintStream;

import org.json.JSONException;
import org.json.JSONObject;

public class Diagnosis {

	private String student;
	private long start_time;
	private String diagnosis_level;

	public Diagnosis(JSONObject obj) {
		try {
			if (obj.has("diagnosis_level"))
				this.diagnosis_level = obj.getString("diagnosis_level");
			if (obj.has("start_time"))
				this.start_time = obj.getLong("start_time");
			if (obj.has("student"))
				this.student = obj.getJSONObject("student").getString("objectId");
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public void printMysql(PrintStream out) {
		out.print("insert into Diagnosis (level, start_time, student) values (");
		out.print("'" + diagnosis_level + "',");
		out.print(start_time + ", ");
		out.print("'" + student + "');");
		out.println();
	}
}
