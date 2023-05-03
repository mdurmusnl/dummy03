package gmibank_api;

import base_urls.GmiBankBaseUrl;
import com.google.gson.Gson;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;
import pojos.Country;
import pojos.States;
import utils.ObjectMapperUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

public class Post_Country_Revision extends GmiBankBaseUrl {
/*
the task exists in the Post_Country class
 */

    @Test
    public void postCountryRevision(){
        //set the url
        spec.pathParams("first","api","second","tp-countries");
        //set th expected data
          States state1=new States(0,"My Country");
          States state2=new States(1,"Your State");
        States state3=new States(2,"Her State");
        List<States> statesList=new ArrayList<>();
        statesList.add(state1);
        statesList.add(state2);
        statesList.add(state3);
        Country expectedData=new Country("My Country",statesList);
//send the request get the response
        Response response=given(spec).body(expectedData).post("{first}/{second}");
        response.prettyPrint();

        //do assertion
        //1st way of validation  with hamcrest matcher
          response.then().
                  statusCode(201).
                  body("name",equalTo(expectedData.getName()),
                          "states.id[0]",equalTo(expectedData.getStates().get(0).getId()),
                          "states.name",equalTo(expectedData.getStates().get(0).getName()),
                          "states.id[1]",equalTo(expectedData.getStates().get(1).getId()),
                          "states.name[1]",equalTo(expectedData.getStates().get(1).getName()),
                          "states.id[2]",equalTo(expectedData.getStates().get(2).getId()),
                          "states.name[2]",equalTo(expectedData.getStates().get(2).getName()) );

//2nd validation    response with jsonpath
        JsonPath jsonPath=response.jsonPath();
        assertEquals(201,response.statusCode());
        assertEquals(expectedData.getName(),jsonPath.getString("name"));
        assertEquals(expectedData.getStates().get(0).getName(),jsonPath.getList("states.id").get(0));
        assertEquals(expectedData.getStates().get(0).getName(),jsonPath.getList("states.name").get(0));
        assertEquals(expectedData.getStates().get(1).getId(),jsonPath.getList("states.id").get(1) );
        assertEquals(expectedData.getStates().get(1).getName(),jsonPath.getList("states.name").get(1) );
        assertEquals(expectedData.getStates().get(2).getId(),jsonPath.getList("states.id").get(2) );
        assertEquals(expectedData.getStates().get(2).getName(),jsonPath.getList("states.name").get(2) );

        //3rd way of validation  response with a Map
         Map<String ,Object> actualDataMap= response.as(HashMap.class);
        assertEquals(201,response.statusCode());
        assertEquals(expectedData.getName(),actualDataMap.get("name"));
        assertEquals(expectedData.getStates().get(0).getId(),  ( (Map) ( (List<Object>)actualDataMap.get("states") ).get(0) ).get("id")  );
        assertEquals(expectedData.getStates().get(0).getName(),  ( (Map) ( (List<Object>)actualDataMap.get("states") ).get(0) ).get("name")  );
        assertEquals(expectedData.getStates().get(1).getId(), ((Map)((List<Object>)actualDataMap.get("states")).get(1)).get("id"));
        assertEquals(expectedData.getStates().get(1).getName(), ((Map)((List<Object>)actualDataMap.get("states")).get(1)).get("name"));
        assertEquals(expectedData.getStates().get(2).getId(), ((Map)((List<Object>)actualDataMap.get("states")).get(2)).get("id"));
        assertEquals(expectedData.getStates().get(2).getName(), ((Map)((List<Object>)actualDataMap.get("states")).get(2)).get("name"));

        //4th validation   response with pojo        nice way    fasterxml   .
           //if we ignore id in the response then we can see requst body and response body are same
           // so we can use Country pojo class to create actualData
        Country actualDataPojo=response.as(Country.class);
        assertEquals(expectedData.getName(),actualDataPojo.getName());
        assertEquals(expectedData.getStates().get(0).getId(),actualDataPojo.getStates().get(0).getId());
        assertEquals(expectedData.getStates().get(0).getName(),actualDataPojo.getStates().get(0).getName());
        assertEquals(expectedData.getStates().get(1).getId(),actualDataPojo.getStates().get(1).getId());
        assertEquals(expectedData.getStates().get(1).getName(),actualDataPojo.getStates().get(1).getName());
        assertEquals(expectedData.getStates().get(2).getId(),actualDataPojo.getStates().get(2).getId());
        assertEquals(expectedData.getStates().get(2).getName(),actualDataPojo.getStates().get(2).getName());

        //5th validation   response withObjectMapper      it requires  codehouse
        Country actualDataObjeMapper=ObjectMapperUtils.convertJsonToObject(response.asString(),Country.class);
 assertEquals(expectedData.getName(),actualDataObjeMapper.getName());
 assertEquals(expectedData.getStates().get(0).getId(),actualDataObjeMapper.getStates().get(0).getId());
 assertEquals(expectedData.getStates().get(0).getName(),actualDataObjeMapper.getStates().get(0).getName());
 assertEquals(expectedData.getStates().get(1).getId(),actualDataObjeMapper.getStates().get(1).getId());
        assertEquals(expectedData.getStates().get(1).getName(),actualDataPojo.getStates().get(1).getName());
        assertEquals(expectedData.getStates().get(2).getName(),actualDataPojo.getStates().get(2).getName());
        assertEquals(expectedData.getStates().get(2).getName(),actualDataPojo.getStates().get(2).getName());

        //6th validation    Gson   google product  BETTER THAN oBJECTmAPPER  DOESNT THROW EXCEPTION
     Country actualDataGson=new Gson().fromJson(response.asString(),Country.class);
  assertEquals(expectedData.getName(),actualDataGson.getName());
  assertEquals(expectedData.getStates().get(0).getId(),actualDataGson.getStates().get(0).getId());
  assertEquals(expectedData.getStates().get(0).getName(),actualDataGson.getStates().get(0).getName());
        assertEquals(expectedData.getStates().get(1).getId(),actualDataGson.getStates().get(1).getId());
        assertEquals(expectedData.getStates().get(1).getName(),actualDataGson.getStates().get(1).getName());
        assertEquals(expectedData.getStates().get(2).getId(),actualDataGson.getStates().get(2).getId());
        assertEquals(expectedData.getStates().get(2).getName(),actualDataGson.getStates().get(2).getName());




    }
}
