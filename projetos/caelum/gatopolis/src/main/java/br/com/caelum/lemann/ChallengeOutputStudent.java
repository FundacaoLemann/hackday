package br.com.caelum.lemann;

import org.json.JSONException;
import org.json.JSONObject;

public class ChallengeOutputStudent {

	private JSONObject student;
	private String objectId;

	public ChallengeOutputStudent(JSONObject student) throws JSONException {
		this.student = student;
		this.objectId = this.student.getString("objectId");
	}

	public String getId() {
		return objectId;
	}
	

}
