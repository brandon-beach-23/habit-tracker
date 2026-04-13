package com.brandonbeach.habittracker.controller;


import com.brandonbeach.habittracker.model.Habit;
import com.brandonbeach.habittracker.model.HabitFrequency;
import com.brandonbeach.habittracker.service.HabitCompletionService;
import com.brandonbeach.habittracker.service.HabitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/habits")
public class HabitController {

    private final HabitService habitService;
    private final HabitCompletionService habitCompletionService;

    public HabitController(HabitService habitService, HabitCompletionService habitCompletionService) {
        this.habitService = habitService;
        this.habitCompletionService = habitCompletionService;
    }

    @GetMapping
    public String habits(Model model) {
        model.addAttribute("habits", habitService.getAllActiveHabits());
        return "habits";
    }

    @GetMapping("/new")
    public String showCreateHabitForm(Model model) {
        model.addAttribute("habit", new Habit());
        model.addAttribute("frequencies", HabitFrequency.values());
        return "createHabit";
    }

    @PostMapping
    public String createHabit(@ModelAttribute Habit habit) {
    habitService.createHabit(habit);
    return "redirect:/habits";
    }

    @PostMapping("/{id}/deactivate")
    public String deactivateHabit(@PathVariable Long id) {
        habitService.deactivateHabit(id);
        return "redirect:/habits";
    }

    @PostMapping("/{id}/complete")
    public String completeHabitForToday(@PathVariable Long id) {
        habitCompletionService.completeHabitForToday(id);
        return "redirect:/habits";
    }

    @GetMapping("/{id}")
    public String habitDetails(@PathVariable Long id, Model model) {
        Habit habit = habitService.getHabitById(id)
                .orElseThrow(() -> new IllegalArgumentException("Habit not found for id: " + id));
        int streak = habitCompletionService.calculateStreak(id);
        model.addAttribute("habit", habit);
        model.addAttribute("streak", streak);
        return "habitDetail";
    }
}
