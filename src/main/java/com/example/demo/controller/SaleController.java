package com.example.demo.controller;

import com.example.demo.dto.CreateSaleDto;
import com.example.demo.entity.Sale;
import com.example.demo.service.SaleService;
import com.example.demo.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private SessionService sessionService;

    @GetMapping
    public String listSales(Model model) {
        List<Sale> sales = saleService.findAllSales();

        BigDecimal totalRevenue = sales.stream()
                .map(Sale::getPaymentAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("sales", sales);
        model.addAttribute("salesCount", sales.size());
        model.addAttribute("totalRevenue", totalRevenue);

        return "sales/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("sale", new CreateSaleDto());
        model.addAttribute("sessions", sessionService.findAll());
        return "sales/create";
    }

    @PostMapping
    public String saveSale(@ModelAttribute("sale") CreateSaleDto saleDto) {
        saleService.saveSale(saleDto);
        return "redirect:/sales";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("sale", saleService.getDtoById(id));
        model.addAttribute("sessions", sessionService.findAll());
        model.addAttribute("saleId", id);
        return "sales/edit";
    }

    @PostMapping("/update/{id}")
    public String updateSale(@PathVariable Long id, @ModelAttribute("sale") CreateSaleDto saleDto) {
        saleService.updateSale(id, saleDto);
        return "redirect:/sales";
    }
    @GetMapping("/{id}/delete")
    public String confirmDelete(@PathVariable Long id, Model model) {
        Sale sale = saleService.findById(id);
        model.addAttribute("sale", sale);
        return "sales/delete";
    }

    @PostMapping("/{id}/delete")
    public String deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
        return "redirect:/sales";
    }
}