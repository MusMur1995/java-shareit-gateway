package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestController.class)
@Import(ErrorHandler.class)
class ErrorHandlerTest {

    @Autowired
    private MockMvc mvc;


    @Test
    void handleNotFoundException() throws Exception {
        mvc.perform(get("/tests/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Объект не найден"))
                .andExpect(jsonPath("$.message").value("Не обнаружено"));
    }

    @Test
    void handleNotOwnerException() throws Exception {
        mvc.perform(get("/tests/not-owner"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Доступ запрещен"))
                .andExpect(jsonPath("$.message").value("Не является владельцем"));
    }

    @Test
    void handleDuplicateException() throws Exception {
        mvc.perform(get("/tests/duplicate"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Конфликт данных"))
                .andExpect(jsonPath("$.message").value("Уже существует"));
    }

    @Test
    void handleValidationException() throws Exception {
        mvc.perform(get("/tests/validation"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Ошибка валидации"))
                .andExpect(jsonPath("$.message").value("Ошибка валидации"));
    }

    @Test
    void handleIllegalStateException() throws Exception {
        mvc.perform(get("/tests/illegal"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Вещь недоступна для бронирования"))
                .andExpect(jsonPath("$.message").value("Нельзя забронировать"));
    }

    @Test
    void handleThrowable() throws Exception {
        mvc.perform(get("/tests/any-error"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Ошибка на сервере"))
                .andExpect(jsonPath("$.message").value("Что-то пошло не так"));
    }

    @Test
    void handleUnavailableItemException() throws Exception {
        mvc.perform(get("/tests/unavailable-item"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Вещь недоступна для бронирования"))
                .andExpect(jsonPath("$.message").value("Вещь временно недоступна"));
    }
}