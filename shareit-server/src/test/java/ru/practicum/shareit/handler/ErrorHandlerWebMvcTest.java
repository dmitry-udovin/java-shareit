package ru.practicum.shareit.handler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ErrorHandlerProbeController.class)
@Import(ErrorHandler.class)
class ErrorHandlerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void userNotFound_returns404() throws Exception {
        mockMvc.perform(get("/__probe-errors/user-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }

    @Test
    void itemNotFound_returns404() throws Exception {
        mockMvc.perform(get("/__probe-errors/item-not-found"))
                .andExpect(status().isNotFound());
    }

    @Test
    void requestNotFound_returns404() throws Exception {
        mockMvc.perform(get("/__probe-errors/request-not-found"))
                .andExpect(status().isNotFound());
    }

    @Test
    void notOwner_returns403() throws Exception {
        mockMvc.perform(get("/__probe-errors/not-owner"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("FORBIDDEN"));
    }

    @Test
    void notWhoBooked_returns403() throws Exception {
        mockMvc.perform(get("/__probe-errors/not-who-booked"))
                .andExpect(status().isForbidden());
    }

    @Test
    void missingBooking_returns403() throws Exception {
        mockMvc.perform(get("/__probe-errors/missing-booking"))
                .andExpect(status().isForbidden());
    }

    @Test
    void ownerNotExists_returns403() throws Exception {
        mockMvc.perform(get("/__probe-errors/owner-not-exists"))
                .andExpect(status().isForbidden());
    }

    @Test
    void accessDenied_returns403() throws Exception {
        mockMvc.perform(get("/__probe-errors/access-denied"))
                .andExpect(status().isForbidden());
    }

    @Test
    void commentValidation_returns400() throws Exception {
        mockMvc.perform(get("/__probe-errors/comment-validation"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"));
    }

    @Test
    void illegalArgument_returns400() throws Exception {
        mockMvc.perform(get("/__probe-errors/illegal-arg"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void emailConflict_returns409() throws Exception {
        mockMvc.perform(get("/__probe-errors/email-conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"));
    }

    @Test
    void cannotCreateBooking_returns400() throws Exception {
        mockMvc.perform(get("/__probe-errors/cannot-book"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void genericThrowable_returns500() throws Exception {
        mockMvc.perform(get("/__probe-errors/throwable"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("INTERNAL_ERROR"));
    }
}
