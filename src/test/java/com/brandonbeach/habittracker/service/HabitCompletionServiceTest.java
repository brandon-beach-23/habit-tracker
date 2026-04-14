package com.brandonbeach.habittracker.service;

import com.brandonbeach.habittracker.model.Habit;
import com.brandonbeach.habittracker.model.HabitCompletion;
import com.brandonbeach.habittracker.model.HabitFrequency;
import com.brandonbeach.habittracker.repository.HabitCompletionRepository;
import com.brandonbeach.habittracker.repository.HabitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HabitCompletionServiceTest {

    @Mock
    private HabitCompletionRepository habitCompletionRepository;

    @Mock
    private HabitRepository habitRepository;

    @InjectMocks
    private HabitCompletionService habitCompletionService;

    @Test
    public void calculateStreak_returnZero_whenNoCompletions() {
        Habit habit = new Habit("Coding Practice", "Daily coding", HabitFrequency.DAILY);
        when(habitRepository.findById(1L)).thenReturn(Optional.of(habit));
        when(habitCompletionRepository.findByHabitOrderByCompletionDate(habit))
                .thenReturn(Collections.emptyList());

        int streak = habitCompletionService.calculateStreak(1L);

        assertEquals(0, streak);
    }

    @Test
    public void calculateStreak_returnSeven_whenCompletedForWeek() {
        LocalDate today = LocalDate.now();

        Habit habit = new Habit("Coding Practice", "Daily coding", HabitFrequency.DAILY);
        habit.setCreatedDate(today.minusDays(7));

        List<HabitCompletion> habitCompletions = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            habitCompletions.add(new HabitCompletion(habit, today.minusDays(i)));
        }

        when(habitRepository.findById(1L)).thenReturn(Optional.of(habit));
        when(habitCompletionRepository.findByHabitOrderByCompletionDate(habit))
                .thenReturn(habitCompletions);

        int streak = habitCompletionService.calculateStreak(1L);

        assertEquals(7, streak);
    }

    @Test
    public void calculateStreak_returnZero_whenStreakBroken() {
        LocalDate today = LocalDate.now();

        Habit habit = new Habit("Coding Practice", "Daily coding", HabitFrequency.DAILY);
        habit.setCreatedDate(today.minusDays(7));

        List<HabitCompletion> habitCompletions = new ArrayList<>();
        for (int i = 6; i >= 3; i--) {
            habitCompletions.add(new HabitCompletion(habit, today.minusDays(i)));
        }

        when(habitRepository.findById(1L)).thenReturn(Optional.of(habit));
        when(habitCompletionRepository.findByHabitOrderByCompletionDate(habit))
                .thenReturn(habitCompletions);

        int streak = habitCompletionService.calculateStreak(1L);

        assertEquals(0, streak);
    }

    @Test
    public void completeHabitForToday_returnsTrue_whenNoCompletionToday() {
        LocalDate today = LocalDate.now();
        Habit habit = new Habit("Coding Practice", "Daily Coding", HabitFrequency.DAILY);

        when(habitRepository.findById(1L)).thenReturn(Optional.of(habit));
        when(habitCompletionRepository.findByHabitAndCompletionDate(habit, today)).thenReturn(Optional.empty());

        boolean alreadyCompleted = habitCompletionService.completeHabitForToday(1L);

        assertTrue(alreadyCompleted);
        verify(habitCompletionRepository).save(any(HabitCompletion.class));

    }

    @Test
    public void completeHabitForToday_returnsFalse_whenAlreadyCompletedToday() {
        LocalDate today = LocalDate.now();
        Habit habit = new Habit("Coding Practice", "Daily Coding", HabitFrequency.DAILY);

        when(habitRepository.findById(1L)).thenReturn(Optional.of(habit));
        when(habitCompletionRepository.findByHabitAndCompletionDate(habit, today))
                .thenReturn(Optional.of(new HabitCompletion(habit, today)));

        boolean alreadyCompleted = habitCompletionService.completeHabitForToday(1L);

        assertFalse(alreadyCompleted);
        verify(habitCompletionRepository, never()).save(any(HabitCompletion.class));
    }

}
