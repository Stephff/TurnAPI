package testing;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import java.io.BufferedReader;


public class TurnAPITestValue {

	
		public static void main(String[] args) throws IOException, JSONException {
			new TurnAPITestValue().POST();
		
		}
		
		private void POST() throws JSONException, IOException {
			//Constant
			String auth = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1Mzg2NzY2MzYsImF1ZCI6IlF2MzlDaUlnR0FkNzl2N0hzenlVYXBWWWNhczRkUT09IiwiaXNzIjoiIiwiaWF0IjoxNTA3MTQwNjM2LCJzdWIiOiJyVDcyQnpVemF1MWRuREFqajdNUTVwWlg2ZkJKZkNHQ2R5eWxxVXU4N2RXX01lblpZTzY5SWh2LWFkUGk4NEZVIn0.7qbYfnpFjbfdgTykK6-L8O9J3IAxBIAypvoOzXRGnKA";
			
			
			JSONObject json = new JSONObject();
			
			//Headers
			json.put("phone_number", "3125551212");  //Change phone number  
			json.put("first_name", "Samwise"); //Change first name
			json.put("last_name", "Gamgee");  //Change last name

		
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
			    
			    //Status code
			    int code = httpResp.getStatusLine().getStatusCode();
			    System.out.println("Response: " + code);
			    
			    //Read the lines
			    if(code == HttpURLConnection.HTTP_OK) {
			    	InputStream inputstream = httpResp.getEntity().getContent();
			    	BufferedReader rd = new BufferedReader(new InputStreamReader(inputstream));
					String line;
					StringBuffer resp = new StringBuffer();
					
					while((line = rd.readLine()) != null) { //You can request a specific parameter to display
						resp.append(line);
						
						}
					
					
					//1) Validate a value inside a parameter -- Example: Looking date_of_birth
					JSONObject nodeRoot = new JSONObject(resp.toString());
				    String db = nodeRoot.getString("date_of_birth");
				    
				    
				    //Assert values and print results
				    Assert.assertEquals(db, "1960-12-20");
				    System.out.println("Expected: 1960-12-20 Actual: " + db);
					
										
					//2)Validate a value inside a parameter - An object inside another object -- Example: Looking for the  Zip code for the 2nd address
					JSONObject nodeRt = new JSONObject(resp.toString());
				    JSONObject nodeCh = nodeRt.getJSONObject("checks"); //Object
				    JSONArray addr = nodeCh.getJSONArray("addresses"); //Object inside the 1st object
				    JSONObject addr1 = addr.getJSONObject(1); //Location in the array of the object you are looking for
				    String addr2 = addr1.getString("zip");
				    
				    //Compare expected vs actual
				    Assert.assertEquals(addr2, "60607"); 
				    System.out.println("Expected: Zip:60607 Actual: Zip: " + addr2);
				    
				    rd.close();
							
				
				}
				else  {			
					InputStream inputstream = httpResp.getEntity().getContent();
			    	BufferedReader rd = new BufferedReader(new InputStreamReader(inputstream));
					String line;
					StringBuffer resp = new StringBuffer();
					
					while((line = rd.readLine()) != null) {
						resp.append(line);
						}
											
					rd.close();
					System.out.println(resp.toString());
				}
		
			} catch (Exception ex) {
				 ex.printStackTrace();
				 
			} finally {
			    httpClient.close();
			}
		}

}
