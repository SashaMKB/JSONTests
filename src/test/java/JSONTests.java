import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSONTests {
    // P.S: Для корректной работы некторых тестов необходимо изменить сам json- файл, который лежит в этой же директории


    // Загрузка JSON-файла
    private JSONObject loadJsonFile() throws IOException, JSONException {
        String content = new String(Files.readAllBytes(Paths.get("src/test/java/result.json")));
        return new JSONObject(content);
    }

    // positive test
    @Test
    public void testDetectivesInRange() throws IOException, JSONException {
        JSONObject jsonObject = loadJsonFile();
        JSONArray detectivesArray = jsonObject.getJSONArray("detectives");
        assertTrue("Detectives length is out of range", detectivesArray.length() > 0 && detectivesArray.length() < 3);
    }

    // negative test
    @Test
    public void testDetectivesIsOutOfRange() throws IOException, JSONException {
        JSONObject jsonObject = loadJsonFile();
        JSONArray detectivesArray = jsonObject.getJSONArray("detectives");
        assertTrue("Detectives length is in range", detectivesArray.length() < 0 || detectivesArray.length() > 3);
    }

    //positive test
    @Test
    public void testDetectiveMainIdInRange() throws IOException, JSONException {
        JSONObject jsonObject = loadJsonFile();
        JSONArray detectivesArray = jsonObject.getJSONArray("detectives");
        for (int i = 0; i < detectivesArray.length(); i++) {
            JSONObject detective = detectivesArray.getJSONObject(i);
            int mainId = detective.getInt("MainId");
            assertTrue("MainId is out of range", mainId >= 0 && mainId <= 10);
        }
    }

    //negative test
    @Test
    public void testDetectiveMainIdOutOfRange() throws IOException, JSONException {
        JSONObject jsonObject = loadJsonFile();
        JSONArray detectivesArray = jsonObject.getJSONArray("detectives");
        for (int i = 0; i < detectivesArray.length(); i++) {
            JSONObject detective = detectivesArray.getJSONObject(i);
            int mainId = detective.getInt("MainId");
            assertTrue("MainId is in range", mainId < 0 || mainId > 10);
        }
    }

    //positive
    @Test
    public void testCategoryIDValuesValid() throws IOException, JSONException {
        JSONObject jsonObject = loadJsonFile();
        JSONArray detectivesArray = jsonObject.getJSONArray("detectives");
        for (int i = 0; i < detectivesArray.length(); i++) {
            JSONObject detective = detectivesArray.getJSONObject(i);
            JSONArray categoriesArray = detective.getJSONArray("categories");
            for (int j = 0; j < categoriesArray.length(); j++) {
                JSONObject category = categoriesArray.getJSONObject(j);
                int categoryID = category.getInt("CategoryID");
                assertTrue("Invalid CategoryID value", categoryID == 1 || categoryID == 2);
            }
        }
    }

    // negative
    @Test
    public void testCategoryIDValuesInvalid() throws IOException, JSONException {
        JSONObject jsonObject = loadJsonFile();
        JSONArray detectivesArray = jsonObject.getJSONArray("detectives");
        for (int i = 0; i < detectivesArray.length(); i++) {
            JSONObject detective = detectivesArray.getJSONObject(i);
            JSONArray categoriesArray = detective.getJSONArray("categories");
            for (int j = 0; j < categoriesArray.length(); j++) {
                JSONObject category = categoriesArray.getJSONObject(j);
                int categoryID = category.getInt("CategoryID");
                assertTrue("Valid CategoryID value", categoryID != 1 && categoryID != 2);
            }
        }
    }
    // positive test
    @Test
    public void testExtraArrayPresence() throws IOException, JSONException {
        JSONObject jsonObject = loadJsonFile();
        JSONArray detectivesArray = jsonObject.getJSONArray("detectives");
        for (int i = 0; i < detectivesArray.length(); i++) {
            JSONObject detective = detectivesArray.getJSONObject(i);
            JSONArray categoriesArray = detective.getJSONArray("categories");
            for (int j = 0; j < categoriesArray.length(); j++) {
                JSONObject category = categoriesArray.getJSONObject(j);
                int categoryID = category.getInt("CategoryID");
                if (categoryID == 1) {
                    JSONArray extraArray = category.getJSONObject("extra").getJSONArray("extraArray");
                    assertTrue("ExtraArray should have at least one element for CategoryID=1", !extraArray.isEmpty());
                }
            }
        }
    }

    @Test
    public void testSuccessValue() throws IOException, JSONException {
        JSONObject jsonObject = loadJsonFile();
        JSONArray detectivesArray = jsonObject.getJSONArray("detectives");
        boolean success = jsonObject.getBoolean("success");
        boolean sherlockPresent = false;
        for (int i = 0; i < detectivesArray.length(); i++) {
            JSONObject detective = detectivesArray.getJSONObject(i);
            String firstName = detective.getString("firstName");
            if (firstName.equals("Sherlock")) { // < - может выпасть исключение (не JSONException)
                sherlockPresent = true;
                break;
            }
        }
        if (sherlockPresent) {
            assertTrue("Success value should be true if Sherlock is present", success);
        } else {
            assertFalse("Success value should be false if Sherlock is not present", success);
        }
    }
}
