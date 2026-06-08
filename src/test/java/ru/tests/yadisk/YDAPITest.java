package ru.tests.yadisk;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class YDAPITest {
    private YDClient client;
    private static final String TOKEN_ENV_VAR = "YANDEX_DISK_TOKEN";
    private String token;

    @BeforeAll
    void setupToken() {
        token = System.getenv(TOKEN_ENV_VAR);
        if (token == null || token.isBlank()) {
            System.err.println("Переменная окружения " + TOKEN_ENV_VAR + " не задана. Тесты будут пропущены.");
        } else {
            client = new YDClient(token);
        }
    }

    private String generateUniquePath() {
        return "/test_folder_" + System.currentTimeMillis();
    }

    @Test
    @DisplayName("PUT: создание новой папки")
    void testCreateFolder() throws IOException, InterruptedException {
        assumeTrue(client != null, "Токен не задан, тест пропущен");
        String folderPath = generateUniquePath();

        HttpResponse<String> response = client.createFolder(folderPath);
        assertEquals(201, response.statusCode(), "Папка не создана (ожидался статус 201)");

        // Проверяем, что папка действительно появилась
        HttpResponse<String> getResponse = client.getFolderInfo(folderPath);
        assertEquals(200, getResponse.statusCode(), "Созданная папка не найдена");

        // Очистка
        client.deleteFolder(folderPath, true);
    }

    @Test
    @DisplayName("GET: получение информации о существующей папке")
    void testGetFolderInfo() throws IOException, InterruptedException {
        assumeTrue(client != null, "Токен не задан, тест пропущен");
        String folderPath = generateUniquePath();

        // Сначала создаём папку
        client.createFolder(folderPath);
        // Затем получаем информацию
        HttpResponse<String> response = client.getFolderInfo(folderPath);
        assertEquals(200, response.statusCode(), "Не удалось получить информацию о папке");

        // Проверяем содержимое ответа (например, поле "path")
        String body = response.body();
        assertTrue(body.contains(folderPath), "Ответ не содержит путь к папке");

        // Очистка
        client.deleteFolder(folderPath, true);
    }

    @Test
    @DisplayName("POST: публикация папки (создание публичной ссылки)")
    void testPublishFolder() throws IOException, InterruptedException {
        assumeTrue(client != null, "Токен не задан, тест пропущен");
        String folderPath = generateUniquePath();

        // Создаём папку
        client.createFolder(folderPath);
        // Публикуем
        HttpResponse<String> publishResponse = client.publishFolder(folderPath);
        assertEquals(200, publishResponse.statusCode(), "Не удалось опубликовать папку");

        String publicUrl = client.getPublicUrlFromResponse(publishResponse.body());
        assertNotNull(publicUrl, "Публичный URL не получен");
        assertTrue(publicUrl.startsWith("https://"), "URL имеет неверный формат");

        // Проверяем, что папка действительно опубликована (GET /resources?path=... поле "public_url")
        HttpResponse<String> infoResponse = client.getFolderInfo(folderPath);
        String infoBody = infoResponse.body();
        assertTrue(infoBody.contains("public_url"), "В информации о папке нет поля public_url");

        // Очистка
        client.deleteFolder(folderPath, true);
    }

    @Test
    @DisplayName("DELETE: удаление папки")
    void testDeleteFolder() throws IOException, InterruptedException {
        assumeTrue(client != null, "Токен не задан, тест пропущен");
        String folderPath = generateUniquePath();

        // Создаём папку
        client.createFolder(folderPath);
        // Удаляем навсегда
        HttpResponse<String> deleteResponse = client.deleteFolder(folderPath, true);
        assertEquals(204, deleteResponse.statusCode(), "Папка не удалена (ожидался статус 204)");

        // Проверяем, что папка больше не существует
        HttpResponse<String> getResponse = client.getFolderInfo(folderPath);
        assertEquals(404, getResponse.statusCode(), "Папка всё ещё существует после удаления");
    }
}