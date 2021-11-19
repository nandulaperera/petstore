package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@QuarkusTest
public class PetTypeResourceTest {

    public void checkEquality(Map<String, Object> expected, JsonPath actual){
        for(String path : expected.keySet()){
            Assertions.assertEquals(expected.get(path), actual.get(path));
        }
    }

    @Test
    public void getAllPetTypesTest() {
        Response response = given().when().get("/v1/petTypes");
        Assertions.assertEquals(200, response.statusCode());
        System.out.println(response.jsonPath().prettyPrint());
    }

	@Test
    public void getPetTypeFoundTest() {
        Response response = given().when().get("/v1/petTypes/1");
        Assertions.assertEquals(200, response.statusCode());
        System.out.println(response.jsonPath().prettyPrint());
    }

    @Test
    public void getPetTypeNotFoundTest() {
        Response response = given().when().get("/v1/petTypes/100");
        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    public void addNewPetTypeTest() {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("petTypeId",50);
        jsonAsMap.put("petType", "Horse");

        Response response = given().contentType("application/json").body(jsonAsMap).when().post("/v1/petTypes");
        Assertions.assertEquals(200, response.statusCode());
        MatcherAssert.assertThat("application/json", equalTo(response.contentType()));
        JsonPath jsonPath = response.jsonPath();
        checkEquality(jsonAsMap, jsonPath);
        System.out.println(response.jsonPath().prettyPrint());
    }

    @Test
    public void addExistingPetTypeTest() {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("petTypeId",1);
        jsonAsMap.put("petType", "Dog");

        Response response = given().contentType("application/json").body(jsonAsMap).when().post("/v1/petTypes");
        Assertions.assertEquals(500, response.statusCode());
    }

    @Test
    public void updateExistingPetTypeTest() {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("petTypeId",2);
        jsonAsMap.put("petType", "Kitty");

        Response response = given().contentType("application/json").body(jsonAsMap).when().put("/v1/petTypes");
        Assertions.assertEquals(200, response.statusCode());
        MatcherAssert.assertThat("application/json", equalTo(response.contentType()));
        JsonPath jsonPath = response.jsonPath();
        checkEquality(jsonAsMap, jsonPath);
        System.out.println(response.jsonPath().prettyPrint());
    }

    @Test
    public void updateNotExistingPetTypeTest() {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("petTypeId",100);
        jsonAsMap.put("petType", "Elephant");

        Response response = given().contentType("application/json").body(jsonAsMap).when().put("/v1/petTypes");
        Assertions.assertEquals(500, response.statusCode());
    }

    @Test
    public void deleteExistingPetTypeTest() {
        Response response = given().when().delete("/v1/petTypes/3");
        Assertions.assertEquals(200, response.statusCode());
        MatcherAssert.assertThat("application/json", equalTo(response.contentType()));
        System.out.println(response.jsonPath().prettyPrint());
    }

    @Test
    public void deleteNotExistingPetTypeTest() {
        Response response = given().when().delete("/v1/petTypes/100");
        Assertions.assertEquals(500, response.statusCode());
    }
}