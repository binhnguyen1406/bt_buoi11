package framework.utils;

public class TestDataFactory {
    private TestDataFactory() {
    }

    public static UserData defaultCheckoutUser() {
        return JsonReader.read("testdata/users.json", UserData.class);
    }
}
