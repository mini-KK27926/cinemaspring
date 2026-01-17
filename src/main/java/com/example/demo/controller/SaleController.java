package com.example.demo.controller;

import com.example.demo.entity.Sale;
import com.example.demo.entity.Ticket;
import com.example.demo.entity.Cashier;
import com.example.demo.repository.SaleRepository;
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.CashierRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/sales") // Все маршруты начинаются с /sales
public class SaleController {

    @Autowired
    private SaleRepository saleRepository; // Репозиторий для работы с продажами
    @Autowired
    private TicketRepository ticketRepository; // Репозиторий для работы с билетами
    @Autowired
    private CashierRepository cashierRepository; // Репозиторий для работы с кассирами

    /**
     * Отображает список всех продаж
     * @param model Модель для передачи данных
     * @return имя шаблона списка продаж
     */
    @GetMapping
    public String listSales(Model model) {
        // Получаем все продажи, отсортированные по дате (новые сначала)
        List<Sale> sales = saleRepository.findAllByOrderBySaleDatetimeDesc();
        model.addAttribute("sales", sales);
        return "sales/list";
    }

    /**
     * Отображает форму для создания новой продажи
     * @param model Модель для передачи данных
     * @return имя шаблона формы создания
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("sale", new Sale()); // Пустой объект для формы

        // Получаем свободные билеты и активных кассиров для выпадающих списков
        List<Ticket> availableTickets = ticketRepository.findByStatus("свободен");
        List<Cashier> cashiers = cashierRepository.findByIsActiveTrue();

        model.addAttribute("tickets", availableTickets);
        model.addAttribute("cashiers", cashiers);
        model.addAttribute("paymentMethods", List.of("наличные", "карта", "онлайн"));

        return "sales/create";
    }

    /**
     * Обрабатывает создание новой продажи
     * @param sale Данные продажи из формы
     * @param result Результаты валидации
     * @param ticketId ID выбранного билета
     * @param cashierId ID выбранного кассира
     * @param model Модель для передачи данных
     * @return перенаправление на список продаж
     */
    @PostMapping
    public String createSale(@Valid @ModelAttribute Sale sale,
                             BindingResult result,
                             @RequestParam("ticketId") Long ticketId,
                             @RequestParam("cashierId") Long cashierId,
                             Model model) {

        if (result.hasErrors()) {
            // При ошибках возвращаем списки билетов и кассиров
            model.addAttribute("tickets", ticketRepository.findByStatus("свободен"));
            model.addAttribute("cashiers", cashierRepository.findByIsActiveTrue());
            model.addAttribute("paymentMethods", List.of("наличные", "карта", "онлайн"));
            return "sales/create";
        }

        // Находим билет по ID
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Билет не найден"));

        // Проверяем, свободен ли билет
        if (!"свободен".equals(ticket.getStatus())) {
            throw new IllegalStateException("Билет уже продан");
        }

        // Находим кассира по ID
        Cashier cashier = cashierRepository.findById(cashierId)
                .orElseThrow(() -> new IllegalArgumentException("Кассир не найден"));

        // Устанавливаем связи
        sale.setTicket(ticket);
        sale.setCashier(cashier);
        sale.setSaleDatetime(LocalDateTime.now()); // Устанавливаем текущее время

        saleRepository.save(sale); // Сохраняем продажу

        // Обновляем статус билета на "продан"
        ticket.setStatus("продан");
        ticketRepository.save(ticket);

        return "redirect:/sales";
    }

    /**
     * Отображает детальную информацию о продаже
     * @param id ID продажи
     * @param model Модель для передачи данных
     * @return имя шаблона просмотра продажи
     */
    @GetMapping("/{id}")
    public String viewSale(@PathVariable("id") Long id, Model model) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Продажа не найдена"));

        model.addAttribute("sale", sale);
        return "sales/view";
    }

    /**
     * Удаляет продажу по ID и освобождает билет
     * @param id ID продажи для удаления
     * @return перенаправление на список продаж
     */
    @GetMapping("/delete/{id}")
    public String deleteSale(@PathVariable("id") Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Продажа не найдена"));

        // Освобождаем билет при отмене продажи
        Ticket ticket = sale.getTicket();
        if (ticket != null) {
            ticket.setStatus("свободен");
            ticketRepository.save(ticket);
        }

        saleRepository.delete(sale); // Удаляем продажу
        return "redirect:/sales";
    }
}
