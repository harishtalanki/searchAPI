package com.iapps.itunestest;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URLEncoder;

import static com.jayway.restassured.RestAssured.given;
/**
 * Created by HARISHA.TALANKI on 10/22/2016.
 */
public class ItunesSearchAPITest {
    String baseURL;

    @BeforeClass
    public void beforeClassSetup(){
        RestAssured.defaultParser = Parser.JSON;
        this.baseURL = "https://itunes.apple.com/search";
    }

    @Test
    public void testiTunesSearchAPIWithOnlyTerm(){
        Response r = given().queryParam("term", "jack+johnson").
                get(this.baseURL).
                then().extract().response();
        Assert.assertEquals(200, r.getStatusCode());
        Assert.assertNotNull(r.getBody(),"Response is empty");
    }

    @Test
    public void testiTunesSearchAPIWithTermSpecailCharacter(){
        Response r = given().queryParam("term", "*").
                get(this.baseURL).
                then().extract().response();

        //System.out.println(r.getBody().asString());
        Assert.assertEquals(200, r.getStatusCode());
        Assert.assertNotNull(r.getBody(),"Response is empty");
        Assert.assertEquals(true, r.jsonPath().getLong("resultCount") >0 );
    }

    @Test
    public void testiTunesSearchAPIWithTermEmpty(){
        Response r = given().queryParam("term", "").
                get(this.baseURL).
                then().extract().response();

        //System.out.println(r.getBody().asString());
        Assert.assertEquals(200, r.getStatusCode());
        Assert.assertNotNull(r.getBody(),"Response is empty");
        Assert.assertEquals(0, r.path("resultCount"));
    }

    @Test
    public void testiTunesSearchAPIWithTermContainsSpecialCharacter(){
        Response r = given().queryParam("term", URLEncoder.encode("&")).
                get(this.baseURL).
                then().extract().response();

        //System.out.println(r.getBody().asString());
        Assert.assertEquals(200, r.getStatusCode());
        Assert.assertNotNull(r.getBody(),"Response is empty");
        Assert.assertEquals(true, r.jsonPath().getLong("resultCount") >0 );
    }

    @Test
    public void testiTunesSearchAPIWithTermContiningSpace(){
        Response r = given().queryParam("term", URLEncoder.encode("Katy Perry")).
                get(this.baseURL).
                then().extract().response();

        //System.out.println(r.getBody().asString());
        Assert.assertEquals(200, r.getStatusCode());
        Assert.assertNotNull(r.getBody(),"Response is empty");
        Assert.assertEquals(true, r.jsonPath().getLong("resultCount") >0 );
    }

    // Similarly we can have many tests with the combinations of all the other three parameters

    @Test
    public void testiTunesSearchAPITermAndCountry(){
        Response r = given().queryParam("term", URLEncoder.encode("Shreya Goshal"),"country","IN").
                get(this.baseURL).
                then().extract().response();

        //System.out.println(r.getBody().asString());
        Assert.assertEquals(200, r.getStatusCode());
        Assert.assertNotNull(r.getBody(),"Response is empty");
        Assert.assertEquals(true, r.jsonPath().getLong("resultCount") >0 );
    }

    @Test
    public void testiTunesSearchAPITermAndInvalidCountry_shouldConsiderUSAsDefault(){
        Response r = given().queryParam("term", URLEncoder.encode("Katy Perry"),"country","ZZ").
                get(this.baseURL).
                then().extract().response();

        //System.out.println(r.getBody().asString());
        Assert.assertEquals(200, r.getStatusCode());
        Assert.assertNotNull(r.getBody(),"Response is empty");
        Assert.assertEquals(true, r.jsonPath().getLong("resultCount") >0 );
    }

    @Test
    public void testiTunesSearchAPITermAndBlankCountryCode(){
        Response r = given().queryParam("term", URLEncoder.encode("Katy Perry"),"country","").
                get(this.baseURL).
                then().extract().response();

        //System.out.println(r.getBody().asString());
        Assert.assertEquals(200, r.getStatusCode());
        Assert.assertNotNull(r.getBody(),"Response is empty");
        Assert.assertEquals(0, r.jsonPath().getLong("resultCount"));
    }

    @Test
    public void testiTunesSearchAPITermAndValidCountryWithValidMeidaParam(){
        Response r = given().queryParam("term", URLEncoder.encode("Katy Perry"),"country","US", "media", "all").
                get(this.baseURL).
                then().extract().response();

        //System.out.println(r.getBody().asString());
        Assert.assertEquals(200, r.getStatusCode());
        Assert.assertNotNull(r.getBody(),"Response is empty");
        Assert.assertEquals(true, r.jsonPath().getLong("resultCount") > 0);
    }

    @Test
    public void testiTunesSearchAPITermAndValidCountryWithInValidMeidaParam(){
        Response r = given().queryParam("term", URLEncoder.encode("Katy Perry"),"country","US", "media", "junk").
                get(this.baseURL).
                then().extract().response();

        //System.out.println(r.getBody().asString());
        Assert.assertEquals(200, r.getStatusCode());
        Assert.assertNotNull(r.getBody(),"Response is empty");
        Assert.assertEquals(true, r.jsonPath().getLong("resultCount") > 0);
    }
}
