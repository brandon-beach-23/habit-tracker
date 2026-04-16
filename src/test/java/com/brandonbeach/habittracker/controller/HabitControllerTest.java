package com.brandonbeach.habittracker.controller;

import com.brandonbeach.habittracker.model.Habit;
import com.brandonbeach.habittracker.model.HabitFrequency;
import com.brandonbeach.habittracker.service.HabitCompletionService;
import com.brandonbeach.habittracker.service.HabitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HabitController.class)
public class HabitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HabitService habitService;
    @MockitoBean
    private HabitCompletionService habitCompletionService;

    @Test
    public void getHabits_returnsHabitsView() throws Exception {
        Habit habit = new Habit("Coding", "Coding Practice", HabitFrequency.DAILY);
        habit.setId(1L);

        when(habitService.getAllActiveHabits()).thenReturn(List.of(habit));
        when(habitCompletionService.calculateStreak(any())).thenReturn(0);

        mockMvc.perform(get("/habits"))
                .andExpect(status().isOk())
                .andExpect(view().name("habits"))
                .andExpect(model().attributeExists("habits"))
                .andExpect(model().attributeExists("streaks"));
    }

    @Test
    void getNewHabitForm_returnsCreateHabitView() throws Exception {
        mockMvc.perform(get("/habits/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("createHabit"))
                .andExpect(model().attributeExists("habit"))
                .andExpect(model().attributeExists("frequencies"));
    }

    @Test
    void createHabit_redirectsToHabits_whenValidData() throws Exception {
        mockMvc.perform(post("/habits")
                        .param("name", "Coding Practice")
                        .param("description", "Daily coding")
                        .param("frequency", "DAILY"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/habits"));

        verify(habitService).createHabit(any(Habit.class));
    }

    @Test
    void createHabit_returnsFormWithErrors_whenNameIsBlank() throws Exception {
        mockMvc.perform(post("/habits")
                        .param("name", "")
                        .param("frequency", "DAILY"))
                .andExpect(status().isOk())
                .andExpect(view().name("createHabit"))
                .andExpect(model().attributeExists("frequencies"));
    }

    @Test
    void completeHabit_redirectsToHabits() throws Exception {
        mockMvc.perform(post("/habits/1/complete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/habits"));

        verify(habitCompletionService).completeHabitForToday(1L);
    }
}
