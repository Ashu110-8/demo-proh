package com.springrest.fnt.springrestfnt.service;

import java.math.BigDecimal;
import java.net.http.HttpRequest;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

@Service
public class Servicelayer {
	
private final RestTemplate restTemplate;
private String tkn;

@Autowired
public Servicelayer(RestTemplate restTemplate) {
	this.restTemplate=restTemplate;
} 


////
double distinmeters; 
public String getelidlist(double radius, double lati, double longi) {
	
	System.out.println("given radius and location" +radius+""+lati+""+longi);
	String url = "https://cetin-poc.fcp.fntcloud.de/app/command/axis/api/rest/entity/site/query";
	String body ="{\r\n"
			+ "  \"restrictions\": {\r\n"
			+ "    \r\n"
			+ "  },\r\n"
			+ "  \"returnAttributes\": []\r\n"
			+ "}";
	ResponseEntity<String> sqr = sitequeryAPI(url,body);
	//System.out.println("All element list"+sqr);
	String s1 = sqr.getBody();
	JSONObject root = new JSONObject(s1);
	JSONArray ja_data = root.getJSONArray("returnData");
	int length = ja_data .length();
	String elidlist ="";
	//String returnData = (String) root.get("returnData");
	//System.out.print(String.format("Returndata is %s",returnData));
	 //ArrayList<String> Element_id = new ArrayList<>();
	    // Map <String,String>geomap = new HashMap<String, String>();
	    for(int j=0; j<length; j++){
	        JSONObject json = ja_data.getJSONObject(j);
	        //System.out.println(json.getString("elid"));
	        //Element_id.add();
	        
	        String elid= json.getString("elid");
	        ResponseEntity<String> geoquery = sitequeryAPI("https://cetin-poc.fcp.fntcloud.de/app/command/axis/api/rest/entity/site/"+elid+"/QueryGeolocation",body);
	        String s2 = geoquery.getBody();
	    	JSONObject root1 = new JSONObject(s2);
	    	JSONArray ja_data1 = root1.getJSONArray("returnData");
	    	//System.out.println(ja_data1+"length of array"+ja_data1.length());
	    	//System.out.println(ja_data1.get(0));
	    	
	    	JSONObject tagObject = (JSONObject) ja_data1.get(0);
	    	//System.out.println(tagObject.get("coordX"));
	    	//System.out.println(tagObject.get("coordY"));
	    	//Double X2 = (Double) tagObject.get("coordX");
	    	BigDecimal bd = (BigDecimal) tagObject.get("coordX"); // the value you get
	    	double X2 = bd.doubleValue();
	    	BigDecimal bd1 = (BigDecimal) tagObject.get("coordY");
	    	double Y2 = bd1.doubleValue();
	    	//Double Y2 = (Double) tagObject.get("coordY");
	    	//Double X1 = (Double)lati;
	    	//double Y1 = (double)longi;
	    	
	    	//System.out.println(d);
	    	//System.out.println("input lati"+lati);
	    	//BigDecimal X1 = longi;
	    	//BigDecimal Y1 = lati;
	    	//BigDecimal dis=Math.sqrt((X2-X1)*(X2-X1) + (Y2-Y1)*(Y2-Y1));
	    	DistanceOfTwoCoord coord = new DistanceOfTwoCoord();
	    	//double distinmeters = coord.getDistanceFromLatLonInKm(X1, Y1, X2, Y2);
	    	//double distinmeters = coord.getDistanceFromLatLonInKm(52.5748401640,13.3954305794,52.5739273296,13.3972598459)*1000;
	    	distinmeters = coord.getDistanceFromLatLonInKm(lati,longi,Y2,X2)*1000;
	    	
	    	if (radius >= distinmeters) {
	    		elidlist = elidlist+elid+",";
	    	}
	    	System.out.println("Distnace in meters"+distinmeters);
	    	System.out.println("Distnace in meters"+lati+","+longi+","+Y2+","+X2);
	    	
	    }     
	    return elidlist;
	
	
	
	
}
private void ignoreCertificates() {
    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

		@Override
		public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}
    } };

    try {
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (Exception e) {
    	}
    }
///

public Iphone consumeAPI() {
	return restTemplate.getForObject("http://dummyjson.com/products/1", Iphone.class);
}

public ResponseEntity<String> consumeAPIToken() {
	String url = "https://cetin-poc-keycloak.fcp.fntcloud.de/auth/realms/FNT-Application/protocol/openid-connect/token";
	
	HttpHeaders headers = new HttpHeaders();
	//headers.setContentType(MediaType.APPLICATION_JSON);
	//headers.ACCEPT_ENCODING();
	
	MultiValueMap<String,String> map = new LinkedMultiValueMap<String,String>();
	map.add("grant_type", "password");
	map.add("username", "wipro1");
	map.add("password", "FnTwip@321");
	map.add("client_id", "sso-gateway");
	
	HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<MultiValueMap<String,String>>(map,headers);
	ignoreCertificates();
	ResponseEntity<String> token = restTemplate.postForEntity(url, request, String.class, map);
	 String s1 = token.getBody();
	JSONObject root = new JSONObject(s1);
	tkn = (String) root.get("access_token");
	System.out.print(String.format("Bearer %s",tkn));
	
return token;
		
}
//Mandatory service
public String consumeAPIMandator() {
	String url = "https://cetin-poc.fcp.fntcloud.de/app/command/axis/api/rest/entity/login/changeMandator";
	JSONObject mandatorBody = new JSONObject();
	mandatorBody.put("manId", "1001");
	mandatorBody.put("userGroupName", "admin_1001|G");
	
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	
	String new_tkn = String.format("Bearer %s",tkn);
	headers.add("Authorization", new_tkn);
	
	//System.out.print(tkn);
	HttpEntity<String> request = 
		      new HttpEntity<String>(mandatorBody.toString(), headers);
	String mandator = restTemplate.postForObject(url, request, String.class);
	return mandator;
	
}

public ResponseEntity<String> sitequeryAPI(String url,String body) {
String site_url=url;
String site_body = body;
//JSONObject sitequeryBody = new JSONObject();
//List<Object> sList = new ArrayList<Object>();
//ReturnAttribute returnAttribute [] ;
//sitequeryBody.put("restrictions",sList);
//sitequeryBody.put("returnAttributes",returnAttribute);

HttpHeaders headers = new HttpHeaders();
headers.setContentType(MediaType.APPLICATION_JSON);

String new_tkn = String.format("Bearer %s",tkn);
headers.add("Authorization", new_tkn);

HttpEntity<String> request = 
new HttpEntity<String>(site_body, headers);
//ResponseEntity<String> sqr = restTemplate.postForObject(site_url, request, String.class);
ResponseEntity<String> sqr = restTemplate.postForEntity(site_url, request, String.class);
//JSONObject root = new JSONObject(sqr);
//System.out.print(sqr);
return sqr;
}

public ResponseEntity<String> sitequeryradius(double radius, double lati,double longi) {
	String elidlist = getelidlist(radius,lati,longi);
	System.out.println(elidlist);
	elidlist = elidlist.substring(0,elidlist.length()-1);
	elidlist = elidlist+'"';
	System.out.println(elidlist);
	
	String site_url="https://cetin-poc.fcp.fntcloud.de/app/command/axis/api/rest/entity/site/query";
	String body = "{\n  \"restrictions\": {\n    \"elidList\": {\n      \"value\": \""
			+ elidlist+""
					+ ",\n      \"operator\": \"=\"\n    }\n  },\n  \"returnAttributes\": []\n}";
	//System.out.print(body);
	
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);

	String new_tkn = String.format("Bearer %s",tkn);
	headers.add("Authorization", new_tkn);

	HttpEntity<String> request = 
	new HttpEntity<String>(body, headers);
	ResponseEntity<String> sqr = restTemplate.postForEntity(site_url, request, String.class);
	//System.out.println(sqr.getBody());
	String body1 = "{\"status\":{\"errorCode\":0,\"subErrorCode\":null,\"message\":null,\"success\":true},"
			+ "\"returnData\""
			+ ":[";
			
	String b="";
	//sqr = ResponseEntity.ok(body1);
	
	JSONObject root1 = new JSONObject(sqr.getBody());
	JSONArray ja_data1 = root1.getJSONArray("returnData");
	for (int i=0; i<ja_data1.length();i++)
	{
		
		System.out.println(ja_data1.getJSONObject(i));
		
		String name = ja_data1.getJSONObject(i).getString("name");
		String cat = ja_data1.getJSONObject(i).getString("category");
		String crb = ja_data1.getJSONObject(i).getString("cRestBandwidth");
		//String crb = new String();
		//if (ja_data1.getJSONObject(i).isNull("cRestBandwidth")){
			//String crb = crb+"";
			//} else {
		
			//}
		b = b + "{\"RestBandwidth\":\""
				+ crb+"\",\"name\":\""
				+ name+"\",\"category\":\""
				+ cat+
				"\"},";
		
	}
	b = b.substring(0,b.length()-1);
	String last_body = body1+b+"]}";
	System.out.println(last_body);
	sqr = ResponseEntity.ok(last_body);
	return sqr;

	
	}

//function to extract visibleId
String cust_elid = "";
String getVisibleId(String tx_name) {
String v_id = "";
String service_url ="https://cetin-poc.fcp.fntcloud.de/app/command/axis/api/rest/entity/serviceTelco/query";
String service_body = "{\n    \"restrictions\": {\n        \"elid\": {\n            \"operator\": \"=\",\n            \"value\": \""
		+tx_name+"\"\n        }\n    }\n}"; 

ResponseEntity<String> service_visible = sitequeryAPI(service_url,service_body);

JSONObject sroot = new JSONObject(service_visible.getBody());
JSONArray sja_data1 = sroot.getJSONArray("returnData");
for (int j=0; j<sja_data1.length();j++)
{
v_id = sja_data1.getJSONObject(j).getString("visibleId");
cust_elid = sja_data1.getJSONObject(j).getString("elid");
}
return v_id;
}
//function to extract customer name
String getCustomer(String cust_elid) {
String company_name = "";
String service_url ="https://cetin-poc.fcp.fntcloud.de/app/command/axis/api/rest/entity/serviceTelco/"
		+ cust_elid+ "/Organizations";
String service_body = "{\r\n  \"relationRestrictions\": {},\r\n  \"entityRestrictions\": {},\r\n  \"returnRelationAttributes\": [],\r\n  \"returnEntityAttributes\": []\r\n}"; 

ResponseEntity<String> service_cust = sitequeryAPI(service_url,service_body);

JSONObject sroot = new JSONObject(service_cust.getBody());
JSONArray sja_data1 = sroot.getJSONArray("returnData");
for (int j=0; j<sja_data1.length();j++)
{
	company_name = sja_data1.getJSONObject(j).getJSONObject("entity").getString("company");

}
return company_name;
}
public ResponseEntity<String> consumeNetworkElement(String ename) {
	//String ename ="7360-OLT-BER-01_NE";
	String site_url="https://cetin-poc.fcp.fntcloud.de/app/command/axis/api/rest/entity/networkElement/query";
	String body = "{\n    \"restrictions\": {\n        \"name\": {\n            \"operator\": \"=\",\n            \"value\": \""
			+ename+"\"\n        }\n    }\n}";
	//System.out.print(body);
	//HttpHeaders headers = new HttpHeaders();
	//headers.setContentType(MediaType.APPLICATION_JSON);

	//String new_tkn = String.format("Bearer %s",tkn);
	//headers.add("Authorization", new_tkn);

	//HttpEntity<String> request = new HttpEntity<String>(body, headers);
	//System.out.println(site_url);
	//System.out.println(request);
	//ResponseEntity<String> sqr = restTemplate.postForEntity(site_url, request, String.class);
	//return sqr;
	ResponseEntity<String> sqr = sitequeryAPI(site_url,body);
	JSONObject root = new JSONObject(sqr.getBody());
	JSONArray arr = (JSONArray) root.get("returnData");
	//JSONArray ja_data1;
	String elid ="";
	for (int i = 0; i < arr.length(); i++) {  
        
        // store each object in JSONObject  
        JSONObject explrObject = arr.getJSONObject(i);  
          
        // get field value from JSONObject using get() method
         elid = (String) explrObject.get("elid");
        //System.out.println(explrObject.get("elid"));
        
}
	System.out.println(elid);
	//ResponseEntity<String> logicalportsquery = sitequeryAPI("https://cetin-poc.fcp.fntcloud.de/app/command/axis/api/rest/entity/networkElement/"+elid+"/LogicalPorts");
	String site_url1="https://cetin-poc.fcp.fntcloud.de/app/command/axis/api/rest/entity/networkElement/"+elid+"/LogicalPorts";
	String body1="{\r\n"
			+ "  \"restrictions\": {\r\n"
			+ "    \r\n"
			+ "  },\r\n"
			+ "  \"returnAttributes\": []\r\n"
			+ "}";
	String body2 = "{\n    \"restrictions\": {\n        \"txServiceElid\": {\n            \"operator\": \"!like\",\n            \"value\": null\n        }\n    }\n}";
	
	ResponseEntity<String> logicalportsquery = sitequeryAPI(site_url1,body2);
	
	String newbody = "{\"status\":{\"errorCode\":0,\"subErrorCode\":null,\"message\":null,\"success\":true},"
			+ "\"returnData\""
			+ ":[";
			
	String b="";
	//sqr = ResponseEntity.ok(body1);
	
	JSONObject root1 = new JSONObject(logicalportsquery.getBody());
	JSONArray ja_data1 = root1.getJSONArray("returnData");
	for (int i=0; i<ja_data1.length();i++)
	{
		
		//System.out.println(ja_data1.getJSONObject(i));
		
		String name = ja_data1.getJSONObject(i).getString("portName");
		String tx_name = ja_data1.getJSONObject(i).getString("txServiceElid");
		//String cat = ja_data1.getJSONObject(i).getString("category");
		//String crb = ja_data1.getJSONObject(i).getString("cRestBandwidth");
		//String crb = new String();
		//if (ja_data1.getJSONObject(i).isNull("cRestBandwidth")){
			//String crb = crb+"";
			//} else {
		
			//}
		String vid=getVisibleId(tx_name);
		String cname= getCustomer(cust_elid);
		System.out.println(cname);
		b = b + "{\"NEName\":\""
				+ ename+"\",\"PortName\":\""
				+ name+"\",\"VisibleId\":\""
				+vid+"\",\"CustomerName\":\""
				+cname+
				"\"},";
		//System.out.println(tx_name);
		
		
	}
	b = b.substring(0,b.length()-1);
	String last_body = newbody+b+"]}";
	//System.out.println(last_body);
	logicalportsquery = ResponseEntity.ok(last_body);
	return logicalportsquery;
		
}
}




