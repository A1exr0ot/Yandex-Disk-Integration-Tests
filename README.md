Идентификатор приложения. Используйте его в запросах для получения OAuth-токена | ClientID:      30c1619d79514e11973fd5b71f4ac908
Секретный ключ, которым будет подписан jwt-токен с информацией о пользователе   | Client secret: 68b6c25d33614a3b96777d1c669faf95

https://oauth.yandex.ru/authorize?response_type=token&client_id=30c1619d79514e11973fd5b71f4ac908

Token: y0__wgBEOHP4PEIGN2WQyC1z87pFzDkz-DxCJY_z3K5LD3waOfIGcLYVPLaP3W0
Token: y0__wgBEOHP4PEIGN2WQyD1l93pFzDkz-DxCJZi5p77OK6P5uTbGiuPUBCR51_g



# Автотесты для REST API Яндекс.Диска

Проект содержит набор автоматических тестов для проверки базовых операций с папками на Яндекс.Диске через официальное REST API.

**API**: [https://cloud-api.yandex.net/v1/disk](https://cloud-api.yandex.net/v1/disk)  
**Документация**: [Яндекс.Диск REST API](https://yandex.ru/dev/disk/api/concepts/about-docpage/)  
**Полигон для тестирования**: [https://yandex.ru/dev/disk/poligon/](https://yandex.ru/dev/disk/poligon/)

---

## 🎯 Что проверяется

Проект покрывает четыре основных HTTP-метода:

| Метод | Эндпоинт | Описание | Тест |
|-------|----------|----------|------|
| **PUT** | `/resources` | Создание новой папки | `testCreateFolder()` |
| **GET** | `/resources` | Получение информации о папке | `testGetFolderInfo()` |
| **POST** | `/resources/publish` | Публикация папки (создание публичной ссылки) | `testPublishFolder()` |
| **DELETE** | `/resources` | Безвозвратное удаление папки | `testDeleteFolder()` |

Каждый тест создаёт временную папку с уникальным именем, выполняет действие и удаляет папку после проверки.

---

## 📊 Отчётность (Allure)

Для визуализации результатов тестирования в проекте настроен **Allure Framework**. Пример отчёта:
![Allure Report Overview](images/allure-overview.png)
*(В отчёте отображаются пройденные тесты, время выполнения, тренды, категории, окружение и исполнитель – Maven.)*
После запуска `mvn clean test` отчёт автоматически генерируется в директории `target/allure-results`. Для просмотра выполните: ```bash mvn allure:serve

---

## 🛠 Стек технологий

Java 11+ (встроенный java.net.http.HttpClient)
JUnit 5 (Jupiter)
Jackson Databind – парсинг JSON
Allure – отчёты
Maven – сборка и зависимости

---
		
## ⚙️ Установка и настройка

1. Клонирование репозитория
bash git clone https://github.com/A1exr0ot/yandex-disk-api-tests.git
cd yandex-disk-api-tests

2. Получение OAuth-токена Яндекс.Диска
Перейдите на страницу разработчика OAuth.
Нажмите «Создать приложение».
Укажите имя и выберите доступ к Яндекс.Диску (галочка Яндекс.Диск REST API).
После создания нажмите «Показать токен» и скопируйте access_token.

3. Настройка переменной окружения
Требование: переменная YANDEX_DISK_TOKEN должна быть доступна в системе и в IntelliJ IDEA.

Системная переменная ОС
Windows(cmd): setx YANDEX_DISK_TOKEN "ваш_токен"
(после этого перезапустите OS)

Linux/macOS(bash): export YANDEX_DISK_TOKEN="ваш_токен"
(добавьте в ~/.bashrc или ~/.zshrc для постоянного использования)

Настройка в IntelliJ IDEA
Откройте проект.
Run → Edit Configurations.
Выберите конфигурацию JUnit (или создайте новую).
В поле Environment variables нажмите ... и добавьте: YANDEX_DISK_TOKEN=ваш_токен
Примените изменения.

Проверка:
echo %YANDEX_DISK_TOKEN%    # Windows (cmd)
echo $YANDEX_DISK_TOKEN     # Linux/macOS (bash)

4. Запуск тестов
Через Maven (терминал): mvn clean test
Через IDEA: Нажмите зелёную кнопку рядом с классом YandexDiskApiTest или используйте Ctrl+Shift+F10.

Пример успешного вывода:
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS

---

## 🧪 Детали тестов
Тест	Действие	Проверки
testCreateFolder()	PUT /resources	Статус 201, папка существует
testGetFolderInfo()	GET /resources	Статус 200, тело содержит путь
testPublishFolder()	POST /resources/publish	Статус 200, получен href (публичная ссылка)
testDeleteFolder()	DELETE /resources	Статус 204, повторный GET → 404
Каждый тест генерирует уникальное имя папки (System.currentTimeMillis()) и удаляет её после выполнения.

---

## 🧹 Очистка

Тесты удаляют созданные папки с параметром permanently=true. В случае падения до вызова deleteFolder() папка может остаться – при необходимости используйте @AfterEach.

---

## ❓ Возможные проблемы

Проблема	Решение
Тесты пропущены (skipped):	Не задана YANDEX_DISK_TOKEN – проверьте окружение
401 Unauthorized:	        Неверный токен – получите новый
423 Locked:     	        Подождите или используйте другое имя
507 Insufficient Storage:	Освободите место на Диске

---

## 🔄 Идеи для расширения

Негативные тесты (неверный токен, пустое имя, удаление несуществующей папки)
Загрузка файлов (PUT /resources/upload)
Параметризованные тесты
Allure-шаги (@Step) и вложения

---

## 📄 Лицензия
Проект предоставлен для демонстрации навыков автоматизации тестирования.
Автор: [Кораблев Александр]
GitHub: https://github.com/A1exr0ot

Идентификатор приложения. Используйте его в запросах для получения OAuth-токена | ClientID:      30c1619d79514e11973fd5b71f4ac908

Секретный ключ, которым будет подписан jwt-токен с информацией о пользователе   | Client secret: 68b6c25d33614a3b96777d1c669faf95

https://oauth.yandex.ru/authorize?response_type=token&client_id=30c1619d79514e11973fd5b71f4ac908

Token: y0__wgBEOHP4PEIGN2WQyC1z87pFzDkz-DxCJY_z3K5LD3waOfIGcLYVPLaP3W0

Token: y0__wgBEOHP4PEIGN2WQyD1l93pFzDkz-DxCJZi5p77OK6P5uTbGiuPUBCR51_g
