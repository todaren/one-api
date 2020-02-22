import core.Configuration;
import core.httpclient.HttpClientFactory;
import core.utils.JsonManager;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.UnirestInstance;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.UUID;


@RunWith(JUnit4.class)
public class CategoryTests {

    @Before
    public void setUp() {
        Configuration.setUp();
    }

    /**
     * Add category Test. Let's do next steps:
     *  * create new category
     *  * find this category by id
     *
     *  If this category is found and name is the same then test is passed.
     */
    @Test
    public void addCategoryTest() {
        UUID id = UUID.randomUUID();
        String name = "Category 1";

        JSONObject category = JsonManager.createCategory(id.toString(), name);
        UnirestInstance httpClient = HttpClientFactory.createHttpClient();

        HttpResponse<JsonNode> postResponse = httpClient
                .post(Configuration.configurationProperties.get("API_PATH") + "categories")
                .body(category)
                .asJson();
        assertThat(postResponse.isSuccess(), is(true));
        assertThat(postResponse.getBody().getObject().get("id"), is(id.toString()));
        assertThat(postResponse.getBody().getObject().get("name"), is(name));

        HttpResponse<JsonNode> getResponse = httpClient
                .get(Configuration.configurationProperties.get("API_PATH") + "categories/" + id)
                .asJson();
        assertThat(getResponse.isSuccess(), is(true));
        assertThat(getResponse.getBody().getObject().get("id"), is(id.toString()));
        assertThat(getResponse.getBody().getObject().get("name"), is(name));

        httpClient.shutDown();
    }

    /**
     * Update category Test. Let's do next steps:
     *  * create new category
     *  * modify category name
     *  * update name of this category
     *  * find this category by id
     *
     *  If this category have updated name then test is passed.
     */
    @Test
    public void updateCategoryTest() {
        UUID id = UUID.randomUUID();
        String name = "Category 1";
        String updatedName = "Category 2";

        JSONObject category = JsonManager.createCategory(id.toString(), name);
        UnirestInstance httpClient = HttpClientFactory.createHttpClient();

        HttpResponse<JsonNode> response = httpClient
                .post(Configuration.configurationProperties.get("API_PATH") + "categories")
                .body(category)
                .asJson();
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getBody().getObject().get("id"), is(id.toString()));
        assertThat(response.getBody().getObject().get("name"), is(name));

        JSONObject updatedCategory = JsonManager.updateCategoryName(category, updatedName);

        HttpResponse<JsonNode> patchResponse = httpClient
                .patch(Configuration.configurationProperties.get("API_PATH") + "categories/" + id)
                .body(updatedCategory)
                .asJson();
        assertThat(patchResponse.isSuccess(), is(true));
        assertThat(patchResponse.getBody().getObject().get("id"), is(id.toString()));
        assertThat(patchResponse.getBody().getObject().get("name"), is(updatedName));

        HttpResponse<JsonNode> getResponse = httpClient
                .get(Configuration.configurationProperties.get("API_PATH") + "categories/" + id)
                .asJson();
        assertThat(getResponse.isSuccess(), is(true));
        assertThat(getResponse.getBody().getObject().get("id"), is(id.toString()));
        assertThat(getResponse.getBody().getObject().get("name"), is(updatedName));

        httpClient.shutDown();
    }

    /**
     * Delete category Test. Let's do next steps:
     *  * create new category
     *  * delete this category by id
     *  * try to find this category by id
     *
     *  If this category is not found and response code is 404 then test is passed.
     */
    @Test
    public void deleteCategoryTest() {
        UUID id = UUID.randomUUID();
        String name = "Category 1";

        JSONObject category = JsonManager.createCategory(id.toString(), name);
        UnirestInstance httpClient = HttpClientFactory.createHttpClient();

        HttpResponse<JsonNode> response = httpClient
                .post(Configuration.configurationProperties.get("API_PATH") + "categories")
                .body(category)
                .asJson();
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getBody().getObject().get("id"), is(id.toString()));
        assertThat(response.getBody().getObject().get("name"), is(name));

        HttpResponse<JsonNode> deleteResponse = httpClient
                .delete(Configuration.configurationProperties.get("API_PATH") + "categories/" + id)
                .asJson();
        assertThat(deleteResponse.isSuccess(), is(true));
        assertThat(deleteResponse.getBody().getObject().get("id"), is(id.toString()));
        assertThat(deleteResponse.getBody().getObject().get("name"), is(name));

        HttpResponse<JsonNode> getResponse = httpClient
                .get(Configuration.configurationProperties.get("API_PATH") + "categories/" + id)
                .asJson();
        assertThat(getResponse.isSuccess(), is(false));
        assertThat(getResponse.getBody().getObject().get("code"), is(404));
        assertThat(getResponse.getBody().getObject().get("name"), is("NotFound"));

        httpClient.shutDown();
    }

    /**
     * New category should can be found with all categories request. Let's do next steps:
     *  * create new category
     *  * request all category and get quantity of all categories
     *  * request all category request with filter which allows to get last category in the list
     *
     *  If last category is our added category then test is passed.
     */
    @Test
    public void addingNewCategoryInListOfCategories() {
        UUID id = UUID.randomUUID();
        String name = "Category 1";
        int limit = 1;
        int skip;

        JSONObject category = JsonManager.createCategory(id.toString(), name);
        UnirestInstance httpClient = HttpClientFactory.createHttpClient();

        HttpResponse<JsonNode> response = httpClient
                .post(Configuration.configurationProperties.get("API_PATH") + "categories")
                .body(category)
                .asJson();
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getBody().getObject().get("id"), is(id.toString()));
        assertThat(response.getBody().getObject().get("name"), is(name));

        HttpResponse<JsonNode> getResponse = httpClient
                .get(Configuration.configurationProperties.get("API_PATH") + "categories/")
                .asJson();

        skip = Integer.parseInt(getResponse.getBody().getObject().get("total").toString()) - 1;
        getResponse = httpClient
                .get(Configuration.configurationProperties.get("API_PATH") + "categories?$limit=" + limit + "&$skip=" + skip)
                .asJson();
        JSONArray dataArray = getResponse.getBody().getObject().getJSONArray("data");
        JSONObject categoryJson = dataArray.getJSONObject(0);
        assertThat(getResponse.isSuccess(), is(true));
        assertThat(categoryJson.get("id"), is(id.toString()));
        assertThat(categoryJson.get("name"), is(name));

        httpClient.shutDown();
    }
}
