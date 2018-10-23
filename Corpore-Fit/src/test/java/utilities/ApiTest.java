package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class ApiTest {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		URL url = new URL(
				"https://maps.googleapis.com/maps/api/distancematrix/json?origins=Corredera+Moron+de+la+frontera&destinations=france&key=AIzaSyA0ldlMQPJcOllaG902itsqprRM-JXH9Wc");

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setRequestMethod("GET");

		InputStream is = connection.getInputStream();
		StringBuilder sb = new StringBuilder();

		String line;
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		
		JSONObject json = new JSONObject(sb.toString());
		JSONArray rows = (JSONArray) json.get("rows");
		JSONObject elements = rows.getJSONObject(0);
		JSONArray elements2 = (JSONArray) elements.get("elements");
		JSONObject elements3 = elements2.getJSONObject(0);
		JSONObject distance = (JSONObject) elements3.get("distance");
		System.out.println(json);

	}

}
