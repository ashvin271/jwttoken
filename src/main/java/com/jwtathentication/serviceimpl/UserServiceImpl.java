package com.jwtathentication.serviceimpl;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.procedure.ParameterMisuseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwtathentication.bean.FarmarBean;
import com.jwtathentication.bean.Survey;
import com.jwtathentication.entity.User;
import com.jwtathentication.payload.LoginRequest;
import com.jwtathentication.repository.UserRepository;
import com.jwtathentication.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Value("${spring.report.key}")
	private String key;  
	
	private static final String TOKEN_URL = "https://www.test.transuniondecisioncentre.co.in/DC/TUCL/TU.DE.Pont/Token";
	private static final String DOCUMENT_URL = "https://www.test.transuniondecisioncentre.co.in/DC/TUCL/TU.DE.Pont/Applications";
	private static final String REPORT_URL = "https://www.test.transuniondecisioncentre.co.in/DC/TUcl/TU.DE.Pont/documents";
	private static final String SAVE_FARM_URL = "https://micro.satyukt.com/sat2credit/register/farms";
	private static final String PDF_REPORT_URL = "https://micro.satyukt.com/sat2credit/report";
	private static final String GET_FARMER_DATA_BY_FID = "https://fruits.karnataka.gov.in/FRUITSKCC/FRUITSData/GetFarmerDataByFID";

	@Autowired
	private UserRepository UserRepository;

	@Autowired
	private BCryptPasswordEncoder PasswordEncoder;

	@Override
	public User findUserByName(String username) {

		return UserRepository.getUserByUserName(username);
	}

	@Override
	public User saveUser(User user) {
		System.out.println("user details save call........."+user.getFirstName());
		user.setRole("USER_ROLE");
		user.setPassword(PasswordEncoder.encode(user.getPassword()));
		return UserRepository.save(user);
	}

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		System.out.println("get all user method call.....");
		return UserRepository.findAll();
	}

	@Override
	public String getToken(LoginRequest loginRequest) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

		map.add("grant_type",loginRequest.getGrant_type().toString());
		map.add("username", loginRequest.getUsername().toString());
		map.add("password", loginRequest.getPassword().toString());

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		//String data = restTemplate.postForEntity("https://www.test.transuniondecisioncentre.co.in/DC/TUCL/TU.DE.Pont/Token", request, String.class).getBody();

		Map<String, String> response = null;
		try {
			response = restTemplate.postForObject(TOKEN_URL, request, Map.class);
		}catch(BadCredentialsException e) {
			throw new BadCredentialsException(e.getMessage());
		}
		catch(NullPointerException e) {
			throw new NullPointerException(e.getMessage());
		}
		catch(HttpClientErrorException e) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getResponseBodyAsString());
		}
		catch(Exception e) {
			throw new Exception(e.getMessage());
		}
		if(!response.isEmpty()) {
			System.out.println("first api completed...........");
			long Id=0l;
			RestTemplate restTemplateTwo = new RestTemplate();
			String access_token = response.get("access_token");
			String token_type = response.get("token_type");
			HttpHeaders httpheaders = new HttpHeaders();
			httpheaders.setContentType(MediaType.APPLICATION_JSON);
			httpheaders.set("Authorization", token_type+" "+access_token);
			String jsonString = getJson();
			//JSONParser json = new JSONParser(jsonString.toString());

			HttpEntity<String> request2 = new HttpEntity<String>(jsonString, httpheaders);
			String responseTwo = null;
			try {
				responseTwo = restTemplateTwo.postForEntity(DOCUMENT_URL, request2, String.class).getBody();
			}
			catch (BeanCreationException e) {
				throw new BeanCreationException(e.getLocalizedMessage());
			}catch(NullPointerException e) {
				throw new NullPointerException(e.getLocalizedMessage());
			}
			catch(HttpClientErrorException e) {
				throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, e.getLocalizedMessage());
			}
			catch(Exception e) {
				throw new Exception(e.getLocalizedMessage());
			}
			Object object;
			JSONParser jsonParser = new JSONParser();
			try {
				object = jsonParser.parse(responseTwo.toString());
				JSONObject jsonObject = (JSONObject) object;
				//JSONArray jsonArray =  ((Object) jsonObject).getJSONArray("Fields");

				JSONObject fields = (JSONObject) jsonObject.get("Fields");
				JSONObject applicants = (JSONObject) fields.get("Applicants");
				JSONArray applicantList = (JSONArray) applicants.get("Applicant");
				Iterator itr = applicantList.iterator();
				while (itr.hasNext()) {
					Object slide = itr.next();
					JSONObject jsonObject2 = (JSONObject) slide;
					JSONObject services = (JSONObject) jsonObject2.get("Services");
					JSONArray serviceList = (JSONArray) services.get("Service");
					Iterator itr2 = serviceList.iterator();
					while (itr2.hasNext()) {
						Object slide2 = itr2.next();
						JSONObject jsonObject3 = (JSONObject) slide2;
						JSONObject operations = (JSONObject) jsonObject3.get("Operations");
						JSONArray operationList = (JSONArray) operations.get("Operation");
						if(operationList!=null && !operationList.isEmpty()) {
							JSONObject jsonObject4 = (JSONObject) operationList.get(0);
							JSONObject data = (JSONObject) jsonObject4.get("Data");
							JSONObject response1 = (JSONObject) data.get("Response");
							JSONObject rawResponse = (JSONObject) response1.get("RawResponse");
							JSONObject document = (JSONObject) rawResponse.get("Document");
							Id = (Long) document.get("Id");
							System.out.println(Id);
						}
					}

				}
			} catch (ParseException e) {
				throw new ParameterMisuseException(e.getLocalizedMessage());
			}

			if(Id>0) {
				System.out.println("Second api completed...........");
				RestTemplate restTemplate3 = new RestTemplate();
				HttpHeaders headers3 = new HttpHeaders();
				headers3.setContentType(MediaType.APPLICATION_JSON);
				headers3.set("Authorization", token_type+" "+access_token);
				HttpEntity<String> request3 = new HttpEntity<String>("parameters", headers3);
				String responseThree = restTemplate3.exchange(REPORT_URL+"/"+Id,HttpMethod.GET, request3,String.class).getBody();
				try {
					if(responseThree!=null) {
						System.out.println(responseThree);
						System.setProperty("java.awt.headless", "false");
						File file = new File("report.html");
						Files.write(file.toPath(), responseThree.getBytes());
						Desktop.getDesktop().browse(file.toURI());
					}
				} catch (IOException e) {
					throw new IOException(e.getLocalizedMessage());
				}
				catch(HttpClientErrorException e) {
					throw new HttpClientErrorException(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
				}
				catch (Exception e) {
					throw new Exception(e.getLocalizedMessage());
				}
			}
			System.out.println("Third api completed...........");
			return "success" ;
		}
		return null ;
	}


	public String getJson(){

		String s =	"{\r\n"
				+ "  \"RequestInfo\": {\r\n"
				+ "    \"SolutionSetName\": \"Go_NABARD_AGSS\",\r\n"
				+ "    \"ExecuteLatestVersion\": \"true\"\r\n"
				+ "  },\r\n"
				+ "  \"Fields\": {\r\n"
				+ "    \"Applicants\": {\r\n"
				+ "      \"Applicant\": {\r\n"
				+ "        \"ApplicantFirstName\": \"Rashid\",\r\n"
				+ "        \"ApplicantMiddleName\": \"\",\r\n"
				+ "        \"ApplicantLastName\": \"Jamdar\",\r\n"
				+ "        \"DateOfBirth\": \"28081988\",\r\n"
				+ "        \"Gender\": \"Male\",\r\n"
				+ "        \"EmailAddress\": \"\",\r\n"
				+ "        \"Identifiers\": {\r\n"
				+ "          \"Identifier\": [\r\n"
				+ "            {\r\n"
				+ "              \"IdNumber\": \"ANZPJ6190L\",\r\n"
				+ "              \"IdType\": \"01\"\r\n"
				+ "            }\r\n"
				+ "          ]\r\n"
				+ "        },\r\n"
				+ "        \"Telephones\": {\r\n"
				+ "          \"Telephone\": {\r\n"
				+ "            \"TelephoneNumber\": \"9004132095\",\r\n"
				+ "            \"TelephoneType\": \"01\",\r\n"
				+ "            \"TelephoneCountryCode\": \"91\"\r\n"
				+ "          }\r\n"
				+ "        },\r\n"
				+ "        \"Addresses\": {\r\n"
				+ "          \"Address\": {\r\n"
				+ "            \"AddressType\": \"02\",\r\n"
				+ "            \"AddressLine1\": \"Room NO.11/2, Gupta Sadan, Mohili Village\",\r\n"
				+ "            \"AddressLine2\": \"Sakinaka\",\r\n"
				+ "            \"AddressLine3\": \"\",\r\n"
				+ "            \"AddressLine4\": \"\",\r\n"
				+ "            \"AddressLine5\": \"\",\r\n"
				+ "            \"City\": \"Mumbai\",\r\n"
				+ "            \"PinCode\": \"400072\",\r\n"
				+ "            \"ResidenceType\": \"02\",\r\n"
				+ "            \"StateCode\": \"27\"\r\n"
				+ "          }\r\n"
				+ "        },\r\n"
				+ "        \"KeyPersonRelation\": \"test1\",\r\n"
				+ "        \"KeyPersonName\": \"test2\",\r\n"
				+ "        \"MemberRelationName1\": \"test3\",\r\n"
				+ "        \"Accounts\": {\r\n"
				+ "          \"Account\": {\r\n"
				+ "            \"AccountNumber\": \"test21\"\r\n"
				+ "          }\r\n"
				+ "        },\r\n"
				+ "        \"Services\": {\r\n"
				+ "          \"Service\": {\r\n"
				+ "            \"Id\": \"CORE\",\r\n"
				+ "            \"Operations\": {\r\n"
				+ "              \"Operation\": [\r\n"
				+ "                {\r\n"
				+ "                  \"Name\": \"ConsumerCIR\",\r\n"
				+ "                  \"Params\": {\r\n"
				+ "                    \"Param\": [\r\n"
				+ "                      {\r\n"
				+ "                        \"Name\": \"CibilBureauFlag\",\r\n"
				+ "                        \"Value\": \"false\"\r\n"
				+ "                      },\r\n"
				+ "                      {\r\n"
				+ "                        \"Name\": \"Amount\",\r\n"
				+ "                        \"Value\": \"400000\"\r\n"
				+ "                      },\r\n"
				+ "                      {\r\n"
				+ "                        \"Name\": \"Purpose\",\r\n"
				+ "                        \"Value\": \"36\"\r\n"
				+ "                      },\r\n"
				+ "                      {\r\n"
				+ "                        \"Name\": \"ScoreType\",\r\n"
				+ "                        \"Value\": \"08\"\r\n"
				+ "                      },\r\n"
				+ "                      {\r\n"
				+ "                        \"Name\": \"MemberCode\",\r\n"
				+ "                        \"Value\": \"FI60118888_UATC2CNPE\"\r\n"
				+ "                      },\r\n"
				+ "                      {\r\n"
				+ "                        \"Name\": \"Password\",\r\n"
				+ "                        \"Value\": \"dvn@qurhqgvi8Gpqcuize\"\r\n"
				+ "                      },\r\n"
				+ "                      {\r\n"
				+ "                        \"Name\": \"FormattedReport\",\r\n"
				+ "                        \"Value\": \"true\"\r\n"
				+ "                      },\r\n"
				+ "                      {\r\n"
				+ "                        \"Name\": \"GSTStateCode\",\r\n"
				+ "                        \"Value\": \"33\"\r\n"
				+ "                      }\r\n"
				+ "                    ]\r\n"
				+ "                  }\r\n"
				+ "                },\r\n"
				+ "                {\r\n"
				+ "                  \"Name\": \"IDV\",\r\n"
				+ "                  \"Params\": {\r\n"
				+ "                    \"Param\": [\r\n"
				+ "                      {\r\n"
				+ "                        \"Name\": \"IDVerificationFlag\",\r\n"
				+ "                        \"Value\": \"false\"\r\n"
				+ "                      },\r\n"
				+ "                      {\r\n"
				+ "                        \"Name\": \"ConsumerConsentForUIDAIAuthentication\",\r\n"
				+ "                        \"Value\": \"N\"\r\n"
				+ "                      },\r\n"
				+ "                      {\r\n"
				+ "                        \"Name\": \"GSTStateCode\",\r\n"
				+ "                        \"Value\": \"33\"\r\n"
				+ "                      }\r\n"
				+ "                    ]\r\n"
				+ "                  }\r\n"
				+ "                },\r\n"
				+ "                {\r\n"
				+ "                  \"Name\": \"NTC\",\r\n"
				+ "                  \"Params\": {\r\n"
				+ "                    \"Param\": [\r\n"
				+ "                      {\r\n"
				+ "                        \"Name\": \"DSTuNtcFlag\",\r\n"
				+ "                        \"Value\": \"true\"\r\n"
				+ "                      },\r\n"
				+ "                      {\r\n"
				+ "                        \"Name\": \"NTCProductType\",\r\n"
				+ "                        \"Value\": \"CC\"\r\n"
				+ "                      }\r\n"
				+ "                    ]\r\n"
				+ "                  }\r\n"
				+ "                },\r\n"
				+ "                {\r\n"
				+ "                  \"Name\": \"MFI\",\r\n"
				+ "                  \"Params\": {\r\n"
				+ "                    \"Param\": [\r\n"
				+ "                      {\r\n"
				+ "                        \"Name\": \"MFIBureauFlag\",\r\n"
				+ "                        \"Value\": \"false\"\r\n"
				+ "                      },\r\n"
				+ "                      {\r\n"
				+ "                        \"Name\": \"MFIEnquiryAmount\",\r\n"
				+ "                        \"Value\": \"400000\"\r\n"
				+ "                      },\r\n"
				+ "                      {\r\n"
				+ "                        \"Name\": \"MFILoanPurpose\",\r\n"
				+ "                        \"Value\": \"10\"\r\n"
				+ "                      },\r\n"
				+ "                      {\r\n"
				+ "                        \"Name\": \"MFICenterReferenceNo\",\r\n"
				+ "                        \"Value\": \"center\"\r\n"
				+ "                      },\r\n"
				+ "                      {\r\n"
				+ "                        \"Name\": \"MFIBranchReferenceNo\",\r\n"
				+ "                        \"Value\": \"branch\"\r\n"
				+ "                      },\r\n"
				+ "                      {\r\n"
				+ "                        \"Name\": \"GSTStateCode\",\r\n"
				+ "                        \"Value\": \"33\"\r\n"
				+ "                      }\r\n"
				+ "                    ]\r\n"
				+ "                  }\r\n"
				+ "                },\r\n"
				+ "                {\r\n"
				+ "                  \"Name\": \"FIWaiver\",\r\n"
				+ "                  \"Params\": {\r\n"
				+ "                    \"Param\": [\r\n"
				+ "                      {\r\n"
				+ "                        \"Name\": \"FIWaiverFlag\",\r\n"
				+ "                        \"Value\": \"true\"\r\n"
				+ "                      }\r\n"
				+ "                    ]\r\n"
				+ "                  }\r\n"
				+ "                }\r\n"
				+ "              ]\r\n"
				+ "            }\r\n"
				+ "          }\r\n"
				+ "        }\r\n"
				+ "      }\r\n"
				+ "    },\r\n"
				+ "    \"ApplicationData\": {\r\n"
				+ "      \"GSTStateCode\": \"27\",\r\n"
				+ "      \"Services\": {\r\n"
				+ "        \"Service\": {\r\n"
				+ "          \"Id\": \"CORE\",\r\n"
				+ "          \"Skip\": \"N\",\r\n"
				+ "          \"Consent\": \"true\"\r\n"
				+ "        }\r\n"
				+ "      }\r\n"
				+ "    },\r\n"
				+ "    \"TrailLevel\": \"5\"\r\n"
				+ "  }\r\n"
				+ "}\r\n"
				+ "";

		return s;
	}

	@Override
	public List<String> surveyDetails(Survey survey) throws Exception {
		List<String> reportList = new ArrayList<String>();
		List<String> reportList2 = new ArrayList<String>();
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> request = new HttpEntity<String>("parameters", headers);
		String response = null;
		long farmid=0l;
		if(survey.getCordinates()==null) {
			survey.setCordinates("");
		}
		try {
		response = restTemplate.exchange(SAVE_FARM_URL+"?key="+key+"&farm_name="+survey.getFarmName()+"&cordinates="+survey.getCordinates()+"&state="+survey.getState()+
				"&district="+survey.getDistrict()+"&taluk="+survey.getTaluk()+"&crop="+survey.getCrop()+"&survey_id="+survey.getSurveyId()+"&sowingdate="+survey.getSowingdate()+"&area="+survey.getArea()+
				"&unit="+survey.getUnit()+"&sub_survey_id="+survey.getSubSurveyId()+"&village="+survey.getVillage()+"&irrigationType="+survey.getIrrigationType(),HttpMethod.GET, request,String.class).getBody();
		
		System.out.println(response);
		Object object;
		
			JSONParser jsonParser = new JSONParser();
			object = jsonParser.parse(response.toString());
			JSONObject jsonObject = (JSONObject) object;
			String id = (String) jsonObject.get("farmid");
			if(id!=null) {
				farmid = Long.parseLong(id);
			}
			System.out.println("first time call......................"+farmid);
		    reportList = callingReportApi(farmid);
			if(reportList==null || reportList.isEmpty()) {
				try {
					Thread.sleep(900000);
					 System.out.println("after 15 minute......................"+farmid);
					 reportList = callingReportApi(farmid);
				} catch (InterruptedException e) {
					throw new InterruptedException(e.getLocalizedMessage());
				}
			}
			if(reportList==null || reportList.isEmpty()) {
				reportList2.addAll(reportList);
				reportList.isEmpty();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		catch(HttpClientErrorException e) {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
		}
		
		return reportList2;
	}

	public List<String> callingReportApi(long farmid){
		List<String> reportList = new ArrayList<String>();
		RestTemplate restTemplate = new RestTemplate();
		ObjectMapper mapper = new ObjectMapper();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		try {
		ResponseEntity<Map> response = null;
	    response = restTemplate.exchange(PDF_REPORT_URL+"?apikey="+key+"&farmid="+farmid,HttpMethod.GET, entity, Map.class);
	    if(response!=null && response.getBody()!=null && !response.getBody().isEmpty()) {
		Map<String,Object> hasMap= mapper.convertValue(response.getBody(),Map.class);
			System.out.println(hasMap);
			Map<String,Object> obj= (Map<String, Object>) hasMap.get("reports");
			System.out.println(obj.get("pdf"));
			reportList = (List<String>) obj.get("pdf");
		}
		}catch(HttpClientErrorException e) {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
		}

		//reportList=(hasMap.get("reports"));
		return reportList;
	}

	@Override
	public Map<String,Object> getSurveyReport(long farmid) throws Exception {
		Map<String,Object> object = new HashMap<>();
		object = callingReportApi1(farmid);
			if(object==null || object.isEmpty()) {
				try {
					Thread.sleep(900000);
					 System.out.println("after 15 minute......................");
					 object = callingReportApi1(farmid);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return object;
	}
	
	public Map<String,Object> callingReportApi1(long farmid){
		Map<String,Object> map = new HashMap<>();
		RestTemplate restTemplate = new RestTemplate();
		ObjectMapper mapper = new ObjectMapper();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		try {
		ResponseEntity<Map> response = null;
	    response = restTemplate.exchange(PDF_REPORT_URL+"?apikey="+key+"&farmid="+farmid,HttpMethod.GET, entity, Map.class);
	    if(response!=null && response.getBody()!=null && !response.getBody().isEmpty()) {
		Map<String,Object> hasMap= mapper.convertValue(response.getBody(),Map.class);
			return hasMap;
		}
		}catch(HttpClientErrorException e) {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
		}

		//reportList=(hasMap.get("reports"));
		return map;
	}

	@Override
	public List<Map<String,Object>> surveyDetails2(Survey survey) throws Exception {
		List<Map<String,Object>> objectList = new ArrayList<>();
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> request = new HttpEntity<String>("parameters", headers);
		String response = null;
		long farmid=0l;
		if(survey.getCordinates()==null) {
			survey.setCordinates("");
		}
		try {
		response = restTemplate.exchange(SAVE_FARM_URL+"?key="+key+"&farm_name="+survey.getFarmName()+"&cordinates="+survey.getCordinates()+"&state="+survey.getState()+
				"&district="+survey.getDistrict()+"&taluk="+survey.getTaluk()+"&crop="+survey.getCrop()+"&survey_id="+survey.getSurveyId()+"&sowingdate="+survey.getSowingdate()+"&area="+survey.getArea()+
				"&unit="+survey.getUnit()+"&sub_survey_id="+survey.getSubSurveyId()+"&village="+survey.getVillage()+"&irrigationType="+survey.getIrrigationType(),HttpMethod.GET, request,String.class).getBody();
		
		System.out.println(response);
		Object object;
		
			JSONParser jsonParser = new JSONParser();
			object = jsonParser.parse(response.toString());
			JSONObject jsonObject = (JSONObject) object;
			String id = (String) jsonObject.get("farmid");
			if(id!=null) {
				farmid = Long.parseLong(id);
			}
			Map<String,Object> mapObject = new HashMap<>();
			System.out.println("first time call......................");
			mapObject = callingReportApi1(farmid);
			if(mapObject==null || mapObject.isEmpty()) {
				try {
					Thread.sleep(900000);
					 System.out.println("after 15 minute......................");
					 mapObject = callingReportApi1(farmid);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(mapObject!=null && !mapObject.isEmpty()) {
				objectList.add(mapObject);
				mapObject=null;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		catch(HttpClientErrorException e) {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
		}
		
		return objectList;
	}

	@Override
	public Map<String,Object> getFarmerDataByFID(FarmarBean farmarBean) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

		map.add("UserName",farmarBean.getUserName().toString());
		map.add("UserPassword", farmarBean.getUserPassword().toString());
		map.add("FarmerRegNo", farmarBean.getFarmerRegNo().toString());

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		Map<String, Object> response = null;
		try {
			response = restTemplate.postForObject(GET_FARMER_DATA_BY_FID, request, Map.class);
			System.out.println(response);
		}catch(BadCredentialsException e) {
			throw new BadCredentialsException(e.getMessage());
		}catch(HttpClientErrorException e) {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
		}catch(Exception e) {
			throw new Exception(e.getLocalizedMessage());
		}
		return response;
	}
    
}
