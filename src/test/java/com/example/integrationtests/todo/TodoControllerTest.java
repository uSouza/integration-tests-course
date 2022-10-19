package com.example.integrationtests.todo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
final class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TodoRepository todoRepository;

    private TodoItem parseResultToTodoItem(ResultActions resultActions) {
        try {
            final var contentAsString = resultActions.andReturn().getResponse().getContentAsString();
            return objectMapper.readValue(contentAsString, TodoItem.class);
        } catch (Exception exception) {
            throw new RuntimeException("Error trying to parse result to todo item: " + exception.getMessage());
        }
    }

    public byte[] parseObjToByteArray(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException("Error trying to parse obj to byte array: " + exception.getMessage());
        }
    }

    @Test
    void testCreate() throws Exception {
        final var request = new TodoItem("Integration Test", "Learning integration test.");
        final var resultActions = mockMvc.perform(post("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(parseObjToByteArray(request)))
            .andExpect(status().isCreated());
        final var todoItem = parseResultToTodoItem(resultActions);

        assertThat(todoItem.getId()).isGreaterThan(0);
        assertThat(todoItem.getName()).isEqualTo(request.getName());
    }

    @Test
    void testComplete() throws Exception {
        final var initialTodoItem = new TodoItem("Integration Test", "Learning integration test.");
        todoRepository.save(initialTodoItem);

        mockMvc.perform(patch(String.format("/todo/%d/complete",
                initialTodoItem.getId())).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        final var updatedTodoItem = todoRepository.findById(initialTodoItem.getId())
            .orElseThrow(TodoService.TodoItemNotFoundException::new);

        assertFalse(initialTodoItem.isDone());
        assertTrue(updatedTodoItem.isDone());
    }
}
