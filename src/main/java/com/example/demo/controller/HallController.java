package com.example.demo.controller;

import com.example.demo.entity.Hall;
import com.example.demo.repository.HallRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/halls") // Все маршруты начинаются с /halls
public class HallController {

    @Autowired
    private HallRepository hallRepository; // Репозиторий для работы с залами

    /**
     * Отображает список всех залов
     * @param model Модель для передачи данных
     * @return имя шаблона списка залов
     */
    @GetMapping
    public String listHalls(Model model) {
        // Получаем все залы, отсортированные по номеру
        List<Hall> halls = hallRepository.findAllByOrderByHallNumberAsc();
        model.addAttribute("halls", halls);
        return "halls/list";
    }

    /**
     * Отображает форму для добавления нового зала
     * @param model Модель для передачи формы
     * @return имя шаблона формы создания
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("hall", new Hall()); // Пустой объект для формы
        return "halls/create";
    }

    /**
     * Обрабатывает создание нового зала
     * @param hall Данные зала из формы
     * @param result Результаты валидации
     * @return перенаправление на список залов
     */
    @PostMapping
    public String createHall(@Valid @ModelAttribute Hall hall,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "halls/create"; // Возвращаем форму с ошибками
        }
        hallRepository.save(hall); // Сохраняем зал в БД
        return "redirect:/halls";
    }

    /**
     * Отображает форму для редактирования существующего зала
     * @param id ID зала для редактирования
     * @param model Модель для передачи данных
     * @return имя шаблона формы редактирования
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        // Находим зал по ID
        Hall hall = hallRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Зал с ID " + id + " не найден"));

        model.addAttribute("hall", hall);
        return "halls/edit";
    }

    /**
     * Обрабатывает обновление зала
     * @param id ID зала для обновления
     * @param hall Обновленные данные зала
     * @param result Результаты валидации
     * @return перенаправление на список залов
     */
    @PostMapping("/update/{id}")
    public String updateHall(@PathVariable("id") Long id,
                             @Valid @ModelAttribute Hall hall,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "halls/edit";
        }

        hall.setHallId(id); // Устанавливаем ID из URL
        hallRepository.save(hall); // Сохраняем изменения
        return "redirect:/halls";
    }

    /**
     * Удаляет зал по ID
     * @param id ID зала для удаления
     * @return перенаправление на список залов
     */
    @GetMapping("/delete/{id}")
    public String deleteHall(@PathVariable("id") Long id) {
        Hall hall = hallRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Зал с ID " + id + " не найден"));

        // Проверяем, есть ли связанные сеансы
        if (hall.getSessions() != null && !hall.getSessions().isEmpty()) {
            throw new IllegalStateException("Нельзя удалить зал, у которого есть сеансы.");
        }

        hallRepository.delete(hall);
        return "redirect:/halls";
    }

    /**
     * Отображает детальную информацию о зале
     * @param id ID зала
     * @param model Модель для передачи данных
     * @return имя шаблона просмотра зала
     */
    @GetMapping("/{id}")
    public String viewHall(@PathVariable("id") Long id, Model model) {
        Hall hall = hallRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Зал с ID " + id + " не найден"));

        model.addAttribute("hall", hall);
        return "halls/view";
    }
}
