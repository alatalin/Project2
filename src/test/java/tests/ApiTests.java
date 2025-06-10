package tests;


import io.qameta.allure.Owner;
import models.*;

import models.response.GenerateTokenResponseModel;
import models.response.LoginResponseModel;
import models.response.RegisterResponseErrorModel;
import models.response.RegisterResponseModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.TestData;

import java.time.format.DateTimeFormatter;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.ApiSpec.*;
import static specs.Endpoints.*;
import static utils.TestData.*;

@Tag("api")
@Owner("alatalin")
public class ApiTests extends TestBase {

    TestData testData = new TestData();

    @Test
    @DisplayName("Проверка успешной регистрации нового пользователя")
    @Tag("positive")
    void successfulRegisterTest() {
        RegisterBodyModel regData = new RegisterBodyModel();
        regData.setUserName(testData.randomUserName);
        regData.setPassword(DEFAULT_PASSWORD);

        RegisterResponseModel regResponse = step("Отправляем запрос на регистрацию", () ->
                given(requestSpec)
                        .body(regData)
                        .when()
                        .post(REGISTRATION)
                        .then()
                        .spec(getResponseSpecStatusCode(201))
                        .extract().as(RegisterResponseModel.class));

        step("Проверяем ответ", () -> {
            assertThat(regResponse.getUsername()).isEqualTo(testData.randomUserName);
            assertThat(regResponse.getUserID()).isNotNull();
        });
    }

    @Test
    @DisplayName("Проверка регистрации существующего пользователя")
    @Tag("negative")
    void unsuccessfulExistingUserRegisterTest() {
        RegisterBodyModel regData = new RegisterBodyModel();
        regData.setUserName(DEFAULT_USERNAME);
        regData.setPassword(DEFAULT_PASSWORD);

        RegisterResponseErrorModel regErrResponse = step("Отправляем запрос на регистрацию", () ->
                given(requestSpec)
                        .body(regData)
                        .when()
                        .post(REGISTRATION)
                        .then()
                        .spec(getResponseSpecStatusCode(406))
                        .extract().as(RegisterResponseErrorModel.class));

        step("Проверяем ответ", () -> {
            assertThat(regErrResponse.getCode()).isEqualTo(1204);
            assertThat(regErrResponse.getMessage()).isEqualTo("User exists!");
        });
    }

    @Test
    @DisplayName("Проверка регистрации пользователя c простым паролем")
    @Tag("negative")
    void unsuccessfulWrongPasswordUserRegisterTest() {
        RegisterBodyModel regData = new RegisterBodyModel();
        regData.setUserName(DEFAULT_USERNAME);
        regData.setPassword(WRONG_PASSWORD);

        RegisterResponseErrorModel regErrResponse = step("Отправляем запрос на регистрацию", () ->
                given(requestSpec)
                        .body(regData)
                        .when()
                        .post(REGISTRATION)
                        .then()
                        .spec(getResponseSpecStatusCode(400))
                        .extract().as(RegisterResponseErrorModel.class));

        step("Проверяем ответ", () -> {
            assertThat(regErrResponse.getCode()).isEqualTo(1300);
            assertThat(regErrResponse.getMessage()).isEqualTo("Passwords must have at least one non alphanumeric character, one digit ('0'-'9'), one uppercase ('A'-'Z'), one lowercase ('a'-'z'), one special character and Password must be eight characters or longer.");
        });
    }

    @Test
    @DisplayName("Проверка авторизации пользователя")
    @Tag("positive")
    void successfulLoginTest() {
        GenerateTokenBodyModel tokenData = new GenerateTokenBodyModel();
        tokenData.setUserName(DEFAULT_USERNAME);
        tokenData.setPassword(DEFAULT_PASSWORD);
        LoginBodyModel logData = new LoginBodyModel();
        logData.setUserName(DEFAULT_USERNAME);
        logData.setPassword(DEFAULT_PASSWORD);

        GenerateTokenResponseModel tokenResponse = step("Отправляем запрос генерации токена", () ->
                given(requestSpec)
                        .body(tokenData)
                        .when()
                        .post(GENERATE_TOKEN)
                        .then()
                        .spec(getResponseSpecStatusCode(200))
                        .extract().as(GenerateTokenResponseModel.class));

        step("Проверяем ответ", () -> {
            assertThat(tokenResponse.getExpires()).isNotNull();
            assertThat(tokenResponse.getResult()).isEqualTo("User authorized successfully.");
            assertThat(tokenResponse.getToken()).isNotNull();
            assertThat(tokenResponse.getStatus()).isEqualTo("Success");
        });

        LoginResponseModel logResponse = step("Отправляем запрос на авторизацию", () ->
                given(requestSpec)
                        .body(logData)
                        .when()
                        .post(LOGIN)
                        .then()
                        .spec(getResponseSpecStatusCode(200))
                        .extract().as(LoginResponseModel.class));

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        step("Проверяем ответ", () -> {
            assertThat(logResponse.getUserId()).isNotNull();
            assertThat(logResponse.getUsername()).isEqualTo(DEFAULT_USERNAME);
            assertThat(logResponse.getPassword()).isEqualTo(DEFAULT_PASSWORD);
            assertThat(logResponse.getToken()).isEqualTo(tokenResponse.getToken());
            assertThat(logResponse.getExpires().format(dateTimeFormatter)).isEqualTo(tokenResponse.getExpires().format(dateTimeFormatter));
            assertThat(logResponse.getCreated_date()).isNotNull();
            assertThat(logResponse.getIsActive()).isEqualTo(false);
        });
    }

    @Test
    @DisplayName("Проверка авторизации незарегистрированного пользователя")
    @Tag("negative")
    void unsuccessfulLoginTest() {
        GenerateTokenBodyModel tokenData = new GenerateTokenBodyModel();
        tokenData.setUserName(testData.randomUserName);
        tokenData.setPassword(testData.randomPassword);

        GenerateTokenResponseModel tokenResponse = step("Отправляем запрос генерации токена", () ->
                given(requestSpec)
                        .body(tokenData)
                        .when()
                        .post(GENERATE_TOKEN)
                        .then()
                        .spec(getResponseSpecStatusCode(200))
                        .extract().as(GenerateTokenResponseModel.class));

        step("Проверяем ответ", () -> {
            assertThat(tokenResponse.getExpires()).isNull();
            assertThat(tokenResponse.getResult()).isEqualTo("User authorization failed.");
            assertThat(tokenResponse.getToken()).isNull();
            assertThat(tokenResponse.getStatus()).isEqualTo("Failed");
        });
    }

    @Test
    @DisplayName("Проверка удаления существующего пользователя")
    @Tag("positive")
    void successfulDeleteUserTest() {
        RegisterBodyModel regData = new RegisterBodyModel();
        regData.setUserName(testData.randomUserName);
        regData.setPassword(DEFAULT_PASSWORD);

        RegisterResponseModel regResponse = step("Отправляем запрос на регистрацию", () ->
                given(requestSpec)
                        .body(regData)
                        .when()
                        .post(REGISTRATION)
                        .then()
                        .spec(getResponseSpecStatusCode(201))
                        .extract().as(RegisterResponseModel.class));

        step("Проверяем ответ", () -> {
            assertThat(regResponse.getUsername()).isEqualTo(testData.randomUserName);
            assertThat(regResponse.getUserID()).isNotNull();
        });

        GenerateTokenBodyModel tokenData = new GenerateTokenBodyModel();
        tokenData.setUserName(regResponse.getUsername());
        tokenData.setPassword(DEFAULT_PASSWORD);
        LoginBodyModel logData = new LoginBodyModel();
        logData.setUserName(regResponse.getUsername());
        logData.setPassword(DEFAULT_PASSWORD);

        GenerateTokenResponseModel tokenResponse = step("Отправляем запрос генерации токена", () ->
                given(requestSpec)
                        .body(tokenData)
                        .when()
                        .post(GENERATE_TOKEN)
                        .then()
                        .spec(getResponseSpecStatusCode(200))
                        .extract().as(GenerateTokenResponseModel.class));

        step("Проверяем ответ", () -> {
            assertThat(tokenResponse.getExpires()).isNotNull();
            assertThat(tokenResponse.getResult()).isEqualTo("User authorized successfully.");
            assertThat(tokenResponse.getToken()).isNotNull();
            assertThat(tokenResponse.getStatus()).isEqualTo("Success");
        });

        LoginResponseModel logResponse = step("Отправляем запрос на авторизацию", () ->
                given(requestSpec)
                        .body(logData)
                        .when()
                        .post(LOGIN)
                        .then()
                        .spec(getResponseSpecStatusCode(200))
                        .extract().as(LoginResponseModel.class));

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        step("Проверяем ответ", () -> {
            assertThat(logResponse.getUserId()).isNotNull();
            assertThat(logResponse.getUsername()).isEqualTo(regResponse.getUsername());
            assertThat(logResponse.getPassword()).isEqualTo(DEFAULT_PASSWORD);
            assertThat(logResponse.getToken()).isEqualTo(tokenResponse.getToken());
            assertThat(logResponse.getExpires().format(dateTimeFormatter)).isEqualTo(tokenResponse.getExpires().format(dateTimeFormatter));
            assertThat(logResponse.getCreated_date()).isNotNull();
            assertThat(logResponse.getIsActive()).isEqualTo(false);
        });

        step("Отправляем запрос на удаление", () ->
                given(requestSpec)
                        .header("Authorization", "Bearer " + tokenResponse.getToken())
                        .when()
                        .delete(DELETE_USER + logResponse.getUserId())
                        .then()
                        .spec(getResponseSpecStatusCode(204))
        );
    }
}

