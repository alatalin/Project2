package utils;

import com.github.javafaker.Faker;

import java.util.Locale;

public class TestData {

    Faker faker = new Faker(new Locale("en-GB"));

    public String randomUserName = faker.name().firstName() + ' ' + faker.name().lastName();
    public String randomPassword = faker.internet().password(10,15);

    public static final String DEFAULT_USERNAME = "Mark6";
    public static final String DEFAULT_PASSWORD = "Password123!()";
    public static final String WRONG_PASSWORD = "Password";


}
