# Автотесты REST API Яндекс.Диска

Проект содержит набор автоматических тестов для проверки базовых операций с папками на Яндекс.Диске через REST API.

**Тестовое задание**: Необходимо разработать пример проекта автотестов для сервиса Яндекс.Диска [https://yandex.ru/dev/disk/rest/](https://yandex.ru/dev/disk/rest/)

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

- Для визуализации результатов тестирования в проекте настроен **Allure Framework**.
- После запуска `mvn clean test` отчёт автоматически генерируется в директории `target/allure-results`. Для просмотра выполните: `mvn allure:serve`.

---

## 🛠 Стек технологий

- Java 11+ (встроенный java.net.http.HttpClient)
- JUnit 5 (Jupiter)
- Jackson Databind – парсинг JSON
- Allure – отчёты
- Maven – сборка и зависимости

---
		
## ⚙️ Установка и настройка

1. Клонирование репозитория:
   - bash git clone https://github.com/A1exr0ot/YD-APITests
   - cd yandex-disk-api-tests

2. Получение OAuth-токена Яндекс.Диска:
   - Перейдите на страницу разработчика OAuth https://oauth.yandex.ru/client/30c1619d79514e11973fd5b71f4ac908.
   - В управление приложением -> Настройки запрашиваем права на доступ к диску.
   - Нажмите «Создать приложение».
   - Открыть пустую вкладку и в адресной строке ввести url https://oauth.yandex.ru/authorize?response_type=token&client_id=30c1619d79514e11973fd5b71f4ac908.
   - Откроется страница на которой будет отображен токен, его нужно скопировать и сохранить.
   - Создать в ОС переменную окружения, Win+R, cmd, ввести команду setx YANDEX_DISK_TOKEN "ваш_токен".

3. Настройка переменной окружения:
   - Требование: переменная YANDEX_DISK_TOKEN должна быть доступна в OС или заданна в IntelliJ IDEA (Run -> Debug Configuration).
   - Системная переменная ОС Windows(cmd): setx YANDEX_DISK_TOKEN "ваш_токен" (после этого перезапустите OS)
   - Linux/macOS(bash): export YANDEX_DISK_TOKEN="ваш_токен" (добавьте в ~/.bashrc или ~/.zshrc для постоянного использования)

4. Настройка в IntelliJ IDEA:
   - Откройте проект.
   - Run → Edit Configurations.
   - Выберите конфигурацию JUnit (или создайте новую).
   - В поле Environment variables нажмите ... и добавьте: YANDEX_DISK_TOKEN=ваш_токен.
   - Примените изменения.

5. Проверка:
   - echo %YANDEX_DISK_TOKEN%    # Windows (cmd)
   - echo $YANDEX_DISK_TOKEN     # Linux/macOS (bash)

6. Запуск тестов:
   - Через Maven (терминал): mvn clean test
   - Через IDEA: Нажмите зелёную кнопку рядом с классом YDAPITest или используйте Ctrl+Shift+F10.

7. Пример успешного вывода:
   - [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
   - [INFO] BUILD SUCCESS

---

## 🧪 Детали тестов

| Тест | Действие | Проверки |
|------|----------|----------|
| `testCreateFolder()` | `PUT /resources` | Статус 201, папка существует |
| `testGetFolderInfo()` | `GET /resources` | Статус 200, тело содержит путь |
| `testPublishFolder()` | `POST /resources/publish` | Статус 200, получен `href` (публичная ссылка) |
| `testDeleteFolder()` | `DELETE /resources` | Статус 204, повторный `GET` → 404 |

Каждый тест генерирует уникальное имя папки (например, `/test_folder_1700000000000`) и удаляет её после выполнения.

---

## 🧹 Очистка

Тесты удаляют созданные папки с параметром permanently=true. В случае падения до вызова deleteFolder() папка может остаться – при необходимости используйте @AfterEach.

---

## ❓ Возможные проблемы

| Проблема | Решение |
|----------|---------|
| Тесты пропущены (`skipped`) | Не задана `YANDEX_DISK_TOKEN` – проверьте окружение |
| `401 Unauthorized` | Неверный токен – получите новый |
| `423 Locked` | Подождите или используйте другое имя папки |
| `507 Insufficient Storage` | Освободите место на Диске |

---

## 🔄 Идеи для расширения

- Негативные тесты (неверный токен, пустое имя папки, удаление несуществующей папки)
- Загрузка файлов (`PUT /resources/upload`)
- Параметризованные тесты
- Allure-шаги (`@Step`) и вложения

---

## 📄 Лицензия
Проект предоставлен для демонстрации навыков автоматизации тестирования.
**Автор**: Александр Кораблев [https://github.com/A1exr0ot](https://github.com/A1exr0ot)
