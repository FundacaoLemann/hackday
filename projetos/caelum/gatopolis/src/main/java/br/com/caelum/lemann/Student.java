package br.com.caelum.lemann;

import java.io.PrintStream;

import org.json.JSONException;
import org.json.JSONObject;

public class Student {

	private String gender;
	private String school;
	private String studentGroup;
	private long birthDate;
	private String objectId;

	public Student(JSONObject obj) {
		try {
			this.objectId = obj.getString("objectId");
			this.birthDate = obj.getLong("birth_date");
			this.gender = obj.getString("gender");
			
			if(obj.has("school")) 
				this.school = obj.getJSONObject("school").getString("objectId");
			
			if(obj.has("studentgroup"))
				this.studentGroup = obj.getJSONObject("studentgroup").getString("objectId");
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public Object getGender() {
		return gender;
	}

	public Object getSchool() {
		return school;
	}

	public Object getStudentGroup() {
		return studentGroup;
	}

	public long getBirthDate() {
		return birthDate;
	}

	public void printMysql(PrintStream out) {
		out.print("insert into Student (objectId, birthDate, gender, school, studentgroup) values (");
		out.print("'" + objectId + "',");
		out.print(birthDate + ", ");
		out.print("'" + gender + "',");
		out.print("'" + school + "'," );
		out.print("'" + studentGroup + "');");
		out.println();
	}
	
	
}
