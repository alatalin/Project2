# Проект по автоматизации тестовых сценариев API для демо-магазина [Demoqa](https://demoqa.com)
<p align="center">
<img title="Demoqa" src="media/logo/Toolsqa-logo.png">

## :pushpin: Содержание:

- [Использованный стек технологий](#computer-использованный-стек-технологий)
- [Примеры автоматизированных кейсов](#примеры-автоматизированных-кейсов)
- [Запуск тестов из терминала](#arrow_forward-запуск-тестов-из-терминала)
- [Сборка в Jenkins](#сборка-в-jenkins)
- [Пример Allure-отчета](#пример-allure-отчета)
- [Уведомления в Telegram с использованием бота](#уведомления-в-telegram-с-использованием-бота)

## :computer: Использованный стек технологий

<p align="center">
<a href="https://www.jetbrains.com/idea/"><img width="6%" title="IntelliJ IDEA" src="media/logo/Intelij_IDEA.svg"></a>
<a href="https://www.java.com/"><img width="6%" title="Java" src="media/logo/Java.svg"></a>
<a href="https://selenide.org/"><img width="6%" title="Selenide" src="media/logo/Selenide.svg"></a>
<a href="https://aerokube.com/selenoid/"><img width="6%" title="Selenoid" src="media/logo/Selenoid.svg"></a>
<a href="https://github.com/allure-framework"><img width="6%" title="Allure Report" src="media/logo/Allure_Report.svg"></a>
<a href="https://qameta.io/"><img width="5%" title="Allure TestOps" src="media/logo/AllureTestOps.svg"></a>
<a href="https://gradle.org/"><img width="6%" title="Gradle" src="media/logo/Gradle.svg"></a>
<a href="https://junit.org/junit5/"><img width="6%" title="JUnit5" src="media/logo/JUnit5.svg"></a>
<a href="https://github.com/"><img width="6%" title="GitHub" src="media/logo/GitHub.svg"></a>
<a href="https://www.jenkins.io/"><img width="6%" title="Jenkins" src="media/logo/Jenkins.svg"></a>
<a href="https://web.telegram.org/"><img width="6%" title="Telegram" src="media/logo/Telegram.svg"></a>
<a href="https://www.atlassian.com/software/jira"><img width="5%" title="Jira" src="media/logo/Jira.svg"></a>
</p>

- В данном проекте автотесты написаны на языке <code>Java</code> с использованием фреймворка для тестирования Selenide.
- В качестве сборщика был использован - <code>Gradle</code>.
- Использованы фреймворки <code>JUnit 5</code> и [Selenide](https://selenide.org/).
- Для удаленного запуска реализована джоба в <code>Jenkins</code> с формированием Allure-отчета и отправкой результатов в <code>Telegram</code> при помощи бота.
- Осуществлена интеграция с <code>Allure TestOps</code> и <code>Jira</code>

Содержание Allure-отчета:
* Шаги теста;
* Page Source;
* Логи браузерной консоли;


### Примеры автоматизированных кейсов

- [x] Поиск по артикулу на главной странице
- [x] Поиск по тексту на главной странице
- [x] Поиск по VIN незарегистрированным/неавторизованным пользователем на главной странице
- [x] Поиск по бренду производителя в каталоге
- [x] Поиск по бренду автомобиля незарегистрированным/неавторизованным пользователем в каталоге
- [x] Поиск по гос. номеру автомобиля незарегистрированным/неавторизованным пользователем на главной странице
- [x] Добавление товара в корзину незарегистрированным/неавторизованным пользователем
- [x] Удаление товара из корзины незарегистрированным/неавторизованным пользователем

## :arrow_forward: Запуск автотестов

### Запуск тестов из терминала
```
gradle clean avtopasker_test
```
При выполнении команды, данные тесты запустятся удаленно в <code>Selenoid</code>.

### Запуск тестов на удаленном браузере
```
gradle clean avtopasker_test -DremoteHost="selenoid.autotests.cloud" -DbrowserSize="1920х1080" -Dbrowser="chrome" -DbrowserVersion="128.0"
```
При необходимости также можно переопределить параметры запуска

```
clean
-DremoteHost=${REMOTE}
-DbrowserSize=${BROWSER_SIZE}
-Dbrowser=${BROWSER}
-DbrowserVersion=${BROWSER_VERSION}"
```

### Параметры сборки

* <code>BROWSER</code> – браузер, в котором будут выполняться тесты. По-умолчанию - <code>chrome</code>.
* <code>BROWSER_VERSION</code> – версия браузера, в которой будут выполняться тесты. По-умолчанию - <code>128.0</code>.
* <code>BROWSER_SIZE</code> – размер окна браузера, в котором будут выполняться тесты. По-умолчанию - <code>1920x1080</code>.
* <code>REMOTE</code> – адрес удаленного сервера, на котором будут запускаться тесты.

## <img src="media/logo/Jenkins.svg" title="Jenkins" width="4%"/> [Сборка в Jenkins](https://jenkins.autotests.cloud/job/QuickProjectAvtopasker)
<p align="center">
<img title="Jenkins Build" src="media/screens/JenkinsBuild.png">
</p>

## <img src="media/logo/Allure_Report.svg" title="Allure Report" width="4%"/> [Пример Allure-отчета](https://jenkins.autotests.cloud/job/QuickProjectAvtopasker/5/allure/#)
### Overview

<p align="center">
<img title="Allure Overview" src="media/screens/allureReport.png">
</p>

### Результат выполнения теста

<p align="center">
<img title="Test Results in Alure" src="media/screens/ResultTest.png">
</p>

## <img src="media/logo/AllureTestOps.svg" title="Allure TestOps" width="4%"/> [Интеграция с Allure TestOps](https://allure.autotests.cloud/project/4696/test-cases?treeId=0)

Выполнена интеграция сборки <code>Jenkins</code> с <code>Allure TestOps</code>.
Результат выполнения автотестов отображается в <code>Allure TestOps</code>
На Dashboard в <code>Allure TestOps</code> отображена статистика пройденных тестов.

<p align="center">
<img title="Allure TestOps DashBoard" src="media/screens/allureAutotestCloud.png">
</p>

## <img src="media/logo/Jira.svg" title="Jira" width="4%"/> [Интеграция с Jira](https://jira.autotests.cloud/browse/HOMEWORK-1431)

Реализована интеграция <code>Allure TestOps</code> с <code>Jira</code>, в тикете отображается информация, какие тест-кейсы были написаны в рамках задачи и результат их прогона.

<p align="center">
<img title="Jira Task" src="media/screens/jiraTask.png">
</p>

## <img width="4%" style="vertical-align:middle" title="Telegram" src="media/logo/Telegram.svg"> Уведомления в Telegram с использованием бота

После завершения сборки, бот созданный в <code>Telegram</code>, автоматически обрабатывает и отправляет сообщение с результатом.

<p align="center">
<img width="70%" title="Telegram Notifications" src="media/screens/notification.png">
</p>

## Видео примера запуска тестов в Selenoid

К каждому тесту в отчете прилагается видео прогона.
<p align="center">
  <img title="Selenoid Video" src="media/screens/video.gif">
</p>