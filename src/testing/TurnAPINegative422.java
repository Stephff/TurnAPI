package testing;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

//import java.net.HttpURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;


//Testing for status code 422 --  Missing required parameter 
public class TurnAPINegative422 { 

	
		public static void main(String[] args) throws IOException, JSONException {
			new TurnAPINegative422().POST();
		
		}
		
		private void POST() throws JSONException, IOException {
			//Constant
			String auth = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1Mzg2NzY2MzYsImF1ZCI6IlF2MzlDaUlnR0FkNzl2N0hzenlVYXBWWWNhczRkUT09IiwiaXNzIjoiIiwiaWF0IjoxNTA3MTQwNjM2LCJzdWIiOiJyVDcyQnpVemF1MWRuREFqajdNUTVwWlg2ZkJKZkNHQ2R5eWxxVXU4N2RXX01lblpZTzY5SWh2LWFkUGk4NEZVIn0.7qbYfnpFjbfdgTykK6-L8O9J3IAxBIAypvoOzXRGnKA";
			
			
			JSONObject json = new JSONObject();
			
			//Headers
			json.put("phone_number", "3125551212");  //Change phone number  
			json.put("first_name", "Samwise"); //Change first name
			//json.put("last_name", "Gamgee");  Removing required parameter to show error 422
		
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();

			//Authenticate and connect to the API	
			try {
			    HttpPost request = new HttpPost("https://api.turn-stage.io/v1/person/search"); 
			    StringEntity params = new StringEntity(json.toString());
			    request.addHeader("authorization", "Bearer " + auth);
			    request.addHeader("content-type", "application/json; ; charset=utf-8");
			    request.setEntity(params);
			    httpClient.execute(request);
			    
			    HttpResponse httpResp = httpClient.execute(request);
			    int code = httpResp.getStatusLine().getStatusCode();
			    System.out.println("Response: " + code);
			    
			    //Print message of status code
			    if(code ==  422) {			
					InputStream inputstream = httpResp.getEntity().getContent();
			    	BufferedReader rd = new BufferedReader(new InputStreamReader(inputstream));
					String line;
					StringBuffer resp = new StringBuffer();
					
					while((line = rd.readLine()) != null) {
						resp.append(line);
					}
					
					rd.close();
					
					//Print results
					System.out.println(resp.toString());
				}
		
			} catch (Exception ex) {
				 ex.printStackTrace();
				 
			} finally {
			    httpClient.close();
			}
		}

}