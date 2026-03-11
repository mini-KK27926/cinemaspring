package com.example.demo.controller;

import com.example.demo.repository.FilmRepository;
import com.example.demo.repository.SessionRepository;
import com.example.demo.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;

@Controller
public class MainController {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SaleRepository saleRepository;

    @GetMapping("/")
    public String home(Model model) {
        long filmCount = filmRepository.count();
        long sessionCount = sessionRepository.count();
        long saleCount = saleRepository.count();

        BigDecimal totalRevenue = BigDecimal.ZERO;
        var sales = saleRepository.findAll();
        for (var sale : sales) {
            if (sale.getPaymentAmount() != null) {
                totalRevenue = totalRevenue.add(sale.getPaymentAmount());
            }
        }

        model.addAttribute("filmCount", filmCount);
        model.addAttribute("sessionCount", sessionCount);
        model.addAttribute("saleCount", saleCount);
        model.addAttribute("totalRevenue", totalRevenue);

        return "index";
    }
}