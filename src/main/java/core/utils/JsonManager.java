package core.utils;

import kong.unirest.json.JSONObject;

/**
 * JsonManager can manage Json's that need for tests.
 */
public class JsonManager {

    /**
     * Method create new category Json with given parameters
     * @param id Category id
     * @param name Category Name
     * @return new JSONObject
     */
    public static JSONObject createCategory(String id, String name) {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", name);
        return json;
    }

    /**
     * Method replace name in category. Let's assume that id cannot be changed.
     * @param category JSONObject category
     * @param newName Name that should be set
     * @return updated JSONObject
     */
    public static JSONObject updateCategoryName(JSONObject category, String newName) {
        category.remove("name");
        category.put("name", newName);
        return category;
    }
}
