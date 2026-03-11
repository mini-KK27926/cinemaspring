package com.example.demo.service;

import com.example.demo.dto.CreateSaleDto;
import com.example.demo.entity.Sale;
import com.example.demo.entity.Session;
import com.example.demo.repository.SaleRepository;
import com.example.demo.repository.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final SessionRepository sessionRepository;

    public SaleService(SaleRepository saleRepository, SessionRepository sessionRepository) {
        this.saleRepository = saleRepository;
        this.sessionRepository = sessionRepository;
    }

    public List<Sale> findAllSales() {
        return saleRepository.findAll();
    }

    public Sale findById(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Продажа не найдена"));
    }

    public CreateSaleDto getDtoById(Long id) {
        Sale sale = findById(id);
        CreateSaleDto dto = new CreateSaleDto();
        dto.setSessionId(sale.getSession().getSessionId());
        dto.setTicketsCount(sale.getTicketsCount());
        return dto;
    }

    @Transactional
    public void saveSale(CreateSaleDto dto) {
        Session session = sessionRepository.findById(dto.getSessionId())
                .orElseThrow(() -> new RuntimeException("Сеанс не найден"));

        Sale sale = new Sale();
        sale.setSession(session);
        sale.setTicketsCount(dto.getTicketsCount());
        sale.setSaleDatetime(LocalDateTime.now());

        calculateAndSetAmount(sale, session, dto.getTicketsCount());

        saleRepository.save(sale);
    }

    @Transactional
    public void updateSale(Long id, CreateSaleDto dto) {
        Sale sale = findById(id);
        Session session = sessionRepository.findById(dto.getSessionId())
                .orElseThrow(() -> new RuntimeException("Сеанс не найден"));

        sale.setSession(session);
        sale.setTicketsCount(dto.getTicketsCount());

        calculateAndSetAmount(sale, session, dto.getTicketsCount());

        saleRepository.save(sale);
    }

    private void calculateAndSetAmount(Sale sale, Session session, Integer count) {
        if (session.getTicketPrice() != null && count != null) {
            BigDecimal total = session.getTicketPrice().multiply(BigDecimal.valueOf(count));
            sale.setPaymentAmount(total);
        }
    }

    public void deleteSale(Long id) {
        saleRepository.deleteById(id);
    }
}