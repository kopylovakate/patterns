package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class ReplayDeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }


    @Test
    @DisplayName("Should successful plan a meeting")
    public void shouldSuccessfulPlanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id='city'] .input__control").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        $("[data-test-id='date'] .input__control").setValue(firstMeetingDate);
        $("[data-test-id='name'] .input__control").setValue(validUser.getName());
        $("[data-test-id='phone'] .input__control").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldHave(Condition.exactText("Успешно!"), Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + firstMeetingDate),
                        Duration.ofSeconds(15));
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        $("[data-test-id='date'] .input__control").setValue(secondMeetingDate);
        $(".button").click();
        $("[data-test-id='replan-notification'] .notification__title")
                .shouldHave(Condition.exactText("Необходимо подтверждение"), Duration.ofSeconds(15));
        $("[data-test-id='replan-notification'] .button").click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldHave(Condition.exactText("Успешно!"), Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + secondMeetingDate),
                        Duration.ofSeconds(15));
    }
}