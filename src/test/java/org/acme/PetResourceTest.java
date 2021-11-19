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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.allOf;

@QuarkusTest
public class PetResourceTest {

    public void checkEquality(Map<String, Object> expected, JsonPath actual){
        for(String path : expected.keySet()){
            Assertions.assertEquals(expected.get(path), actual.get(path));
        }
    }

    @Test
    public void getAllPetsTest() {
        Response response = given().when().get("/v1/pets");
        Assertions.assertEquals(200, response.statusCode());
        System.out.println(response.jsonPath().prettyPrint());
    }

	@Test
    public void getPetFoundTest() {
        Response response = given().when().get("/v1/pets/1");
        Assertions.assertEquals(200, response.statusCode());
        System.out.println(response.jsonPath().prettyPrint());
    }

    @Test
    public void getPetNotFoundTest() {
        Response response = given().when().get("/v1/pets/100");
        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    public void addNewPetTest() {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("petId",50);
        jsonAsMap.put("petType", "Dog");
        jsonAsMap.put("petName", "Sudda");
        jsonAsMap.put("petAge", 10);

        Response response = given().contentType("application/json").body(jsonAsMap).when().post("/v1/pets");
        Assertions.assertEquals(200, response.statusCode());
        MatcherAssert.assertThat("application/json", equalTo(response.contentType()));
        JsonPath jsonPath = response.jsonPath();
        checkEquality(jsonAsMap, jsonPath);
        System.out.println(response.jsonPath().prettyPrint());
    }

    @Test
    public void addExistingPetTest() {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("petId",1);
        jsonAsMap.put("petType", "Dog");
        jsonAsMap.put("petName", "Boola");
        jsonAsMap.put("petAge", 3);

        Response response = given().contentType("application/json").body(jsonAsMap).when().post("/v1/pets");
        Assertions.assertEquals(500, response.statusCode());
    }

    @Test
    public void updateExistingPetTest() {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("petId",2);
        jsonAsMap.put("petType", "Cat");
        jsonAsMap.put("petName", "Kitty");
        jsonAsMap.put("petAge", 10);

        Response response = given().contentType("application/json").body(jsonAsMap).when().put("/v1/pets");
        Assertions.assertEquals(200, response.statusCode());
        MatcherAssert.assertThat("application/json", equalTo(response.contentType()));
        JsonPath jsonPath = response.jsonPath();
        checkEquality(jsonAsMap, jsonPath);
        System.out.println(response.jsonPath().prettyPrint());
    }

    @Test
    public void updateNotExistingPetTest() {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("petId",100);
        jsonAsMap.put("petType", "Dog");
        jsonAsMap.put("petName", "Boola");
        jsonAsMap.put("petAge", 3);

        Response response = given().contentType("application/json").body(jsonAsMap).when().put("/v1/pets");
        Assertions.assertEquals(500, response.statusCode());
    }

    @Test
    public void deleteExistingPetTest() {
        Response response = given().when().delete("/v1/pets/3");
        Assertions.assertEquals(200, response.statusCode());
        MatcherAssert.assertThat("application/json", equalTo(response.contentType()));
        System.out.println(response.jsonPath().prettyPrint());
    }

    @Test
    public void deleteNotExistingPetTest() {
        Response response = given().when().delete("/v1/pets/100");
        Assertions.assertEquals(500, response.statusCode());
    }

    @Test
    public void getValidPetByName(){
        Response response = given().when().get("/v1/pets/query/byName?petName=Boola");
        Assertions.assertEquals(200, response.statusCode());
        System.out.println(response.jsonPath().prettyPrint());
    }

    @Test
    public void getEmptyPetByName(){
        Response response = given().when().get("/v1/pets/query/byName?petName=");
        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    public void getInvalidPetByName(){
        Response response = given().when().get("/v1/pets/query/byName?petName=ABC");
        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    public void getValidPetByAge(){
        Response response = given().when().get("/v1/pets/query/byAge?petAge=3");
        Assertions.assertEquals(200, response.statusCode());
        System.out.println(response.jsonPath().prettyPrint());
    }

    @Test
    public void getPetByInvalidAge(){
        Response response = given().when().get("/v1/pets/query/byAge?petAge=-4");
        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    public void getInvalidPetByAge(){
        Response response = given().when().get("/v1/pets/query/byAge?petAge=1000");
        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    public void getValidPetByType(){
        Response response = given().when().get("/v1/pets/query/byType?petType=Dog");
        Assertions.assertEquals(200, response.statusCode());
        System.out.println(response.jsonPath().prettyPrint());
    }

    @Test
    public void getEmptyPetByType(){
        Response response = given().when().get("/v1/pets/query/byType?petType=");
        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    public void getInvalidPetByType(){
        Response response = given().when().get("/v1/pets/query/byType?petType=ABC");
        Assertions.assertEquals(404, response.statusCode());
    }
}