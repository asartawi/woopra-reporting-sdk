package com.woopra.java.sdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.woopra.java.sdk.entities.WoopraFunnelResponse;
import com.woopra.java.sdk.entities.WoopraProfileResponse;
import com.woopra.java.sdk.entities.WoopraReportResponse;

/**
 * 
 * @author abdulhafeth
 *
 */
public class WoopraFrontdoor {

	private String username;

	private String password;

	private String version;

	public WoopraFrontdoor(String username, String password) {
		this(username, password, "2.4");
	}

	public WoopraFrontdoor(String username, String password, String version) {
		this.setUsername(username);
		this.setPassword(password);
		this.setVersion(version);
	}

	@Autowired
	private WoopraQueryBuilder queryBuilder;

	public String sendRequest(String requesType, String requestBody) {
		HttpResponse httpResponse;
		try {
			String webPage = "https://www.woopra.com/rest/" + this.getVersion() + "/" + requesType;

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(webPage);
			httpPost.addHeader(BasicScheme.authenticate(
					new UsernamePasswordCredentials(this.getUsername(), this.getPassword()), "UTF-8", false));
			HttpEntity entity = new ByteArrayEntity(requestBody.getBytes());
			httpPost.setEntity(entity);

			httpResponse = httpClient.execute(httpPost);
			HttpEntity responseEntity = httpResponse.getEntity();
			InputStream is = responseEntity.getContent();
			BufferedReader buffReader = new BufferedReader(new InputStreamReader(is));
			StringBuffer response = new StringBuffer();

			String line = null;
			try {
				line = buffReader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (line != null) {
				response.append(line);
				response.append('\n');
				try {
					line = buffReader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				buffReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return response.toString();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Send report request
	 * @param requestBody
	 * @return
	 */
	public WoopraReportResponse sendReportRequest(String requestBody) {
		return parseReportResponse(sendRequest("report", requestBody));
	}

	/**
	 * send funnel request
	 * @param requestBody
	 * @return
	 */
	public WoopraFunnelResponse sendFunnelRequest(String requestBody) {
		return parseFunnelResponse(sendRequest("funnel", requestBody));
	}

	/**
	 * Send profile request
	 * @param userId
	 * @return
	 */
	public WoopraProfileResponse sendProfileRequest(Integer userId, String site) {

		if (userId == null) {
			return null;
		}

		JSONObject obj = new JSONObject();
		obj.put("website", site);
		obj.put("key", "id");
		obj.put("value", "" + userId);
		obj.put("date_format", "MM/dd/yyyy");
		return parseProfileResponse(sendRequest("profile", obj.toString()));
	}

	private WoopraReportResponse parseReportResponse(String woopraResponseString) {
		JSONObject response;
		try {
			response = new JSONObject(woopraResponseString);
		} catch (JSONException e) {
			e.printStackTrace();
			try {
				new JSONArray(woopraResponseString);
			} catch (JSONException e1) {
				e1.printStackTrace();
				return null;
			}
			throw new RuntimeException("Couldn't parse the response returned from woopra", e);
		}
		JSONObject total = (JSONObject) response.get("total");
		JSONArray cells = total.getJSONArray("cells");
		List<Long> columnTotals = new ArrayList<>();
		for (int i = 0; i < cells.length(); i++) {
			Long totalCount = Long.parseLong("" + cells.get(i));
			columnTotals.add(totalCount);
		}

		Long count = Long.parseLong("" + total.get("count"));

		JSONArray rows = (JSONArray) response.getJSONArray("rows");

		WoopraReportResponse woopraResponse = new WoopraReportResponse();
		woopraResponse.setCount(count);
		woopraResponse.setRows(rows);
		woopraResponse.setColumnsTotal(columnTotals);
		return woopraResponse;
	}

	private WoopraFunnelResponse parseFunnelResponse(String woopraResponseString) {
		JSONObject response;
		try {
			response = new JSONObject(woopraResponseString);
		} catch (JSONException e) {
			e.printStackTrace();
			try {
				new JSONArray(woopraResponseString);
			} catch (JSONException e1) {
				e1.printStackTrace();
				return null;
			}
			throw new RuntimeException("Couldn't parse the response returned from woopra", e);
		}
		JSONArray rows = (JSONArray) response.getJSONArray("items");
		WoopraFunnelResponse woopraResponse = new WoopraFunnelResponse();
		woopraResponse.setItems(rows);
		return woopraResponse;
	}

	private WoopraProfileResponse parseProfileResponse(String woopraResponseString) {
		JSONObject response;
		try {
			response = new JSONObject(woopraResponseString);
		} catch (JSONException e) {
			e.printStackTrace();
			try {
				new JSONArray(woopraResponseString);
			} catch (JSONException e1) {
				e1.printStackTrace();
				return null;
			}
			throw new RuntimeException("Couldn't parse the response returned from woopra", e);
		}
		WoopraProfileResponse woopraResponse = new WoopraProfileResponse();
		woopraResponse.setSummary((JSONObject) response.getJSONObject("summary"));
		woopraResponse.setCustomInfo((JSONObject) response.getJSONObject("custom"));
		return woopraResponse;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
