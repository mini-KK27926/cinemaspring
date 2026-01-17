package com.example.demo.controller;

import com.example.demo.entity.Cashier;
import com.example.demo.repository.CashierRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/cashiers") // Все маршруты начинаются с /cashiers
public class CashierController {

    @Autowired
    private CashierRepository cashierRepository; // Репозиторий для работы с кассирами

    /**
     * Отображает список всех кассиров с возможностью фильтрации по активности
     * @param model Модель для передачи данных
     * @param active Флаг активности для фильтрации (необязательный параметр)
     * @return имя шаблона списка кассиров
     */
    @GetMapping
    public String listCashiers(Model model,
                               @RequestParam(required = false) Boolean active) {
        List<Cashier> cashiers;

        // Фильтрация по активности
        if (active != null) {
            if (active) {
                cashiers = cashierRepository.findByIsActiveTrue(); // Только активные
            } else {
                cashiers = cashierRepository.findAll(); // Все кассиры
            }
            model.addAttribute("selectedActive", active);
        } else {
            // Без фильтрации - все кассиры отсортированные по ФИО
            cashiers = cashierRepository.findAllByOrderByFullNameAsc();
        }

        model.addAttribute("cashiers", cashiers);
        return "cashiers/list";
    }

    /**
     * Отображает форму для добавления нового кассира
     * @param model Модель для передачи формы
     * @return имя шаблона формы создания
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("cashier", new Cashier()); // Пустой объект для формы
        return "cashiers/create";
    }

    /**
     * Обрабатывает создание нового кассира
     * @param cashier Данные кассира из формы
     * @param result Результаты валидации
     * @return перенаправление на список кассиров
     */
    @PostMapping
    public String createCashier(@Valid @ModelAttribute Cashier cashier,
                                BindingResult result) {
        if (result.hasErrors()) {
            return "cashiers/create";
        }

        // Устанавливаем активность по умолчанию, если не указана
        if (cashier.getIsActive() == null) {
            cashier.setIsActive(true);
        }

        cashierRepository.save(cashier); // Сохраняем кассира
        return "redirect:/cashiers";
    }

    /**
     * Отображает форму для редактирования существующего кассира
     * @param id ID кассира для редактирования
     * @param model Модель для передачи данных
     * @return имя шаблона формы редактирования
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Cashier cashier = cashierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Кассир с ID " + id + " не найден"));

        model.addAttribute("cashier", cashier);
        return "cashiers/edit";
    }

    /**
     * Обрабатывает обновление кассира
     * @param id ID кассира для обновления
     * @param cashier Обновленные данные кассира
     * @param result Результаты валидации
     * @return перенаправление на список кассиров
     */
    @PostMapping("/update/{id}")
    public String updateCashier(@PathVariable("id") Long id,
                                @Valid @ModelAttribute Cashier cashier,
                                BindingResult result) {
        if (result.hasErrors()) {
            return "cashiers/edit";
        }

        cashier.setCashierId(id); // Устанавливаем ID из URL
        cashierRepository.save(cashier); // Сохраняем изменения
        return "redirect:/cashiers";
    }

    /**
     * Удаляет кассира по ID
     * @param id ID кассира для удаления
     * @return перенаправление на список кассиров
     */
    @GetMapping("/delete/{id}")
    public String deleteCashier(@PathVariable("id") Long id) {
        Cashier cashier = cashierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Кассир с ID " + id + " не найден"));

        // Проверяем, есть ли связанные продажи
        if (cashier.getSales() != null && !cashier.getSales().isEmpty()) {
            throw new IllegalStateException("Нельзя удалить кассира, у которого есть продажи.");
        }

        cashierRepository.delete(cashier);
        return "redirect:/cashiers";
    }

    /**
     * Отображает детальную информацию о кассире
     * @param id ID кассира
     * @param model Модель для передачи данных
     * @return имя шаблона просмотра кассира
     */
    @GetMapping("/{id}")
    public String viewCashier(@PathVariable("id") Long id, Model model) {
        Cashier cashier = cashierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Кассир с ID " + id + " не найден"));

        model.addAttribute("cashier", cashier);
        return "cashiers/view";
    }
}
