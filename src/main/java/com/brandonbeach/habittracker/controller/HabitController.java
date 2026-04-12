package com.brandonbeach.habittracker.controller;


import com.brandonbeach.habittracker.model.Habit;
import com.brandonbeach.habittracker.model.HabitFrequency;
import com.brandonbeach.habittracker.service.HabitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/habits")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
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
}
