package com.loan24.basic.audit.service;

import com.loan24.basic.audit.domain.AccessAmount;
import com.loan24.basic.audit.domain.AccessLog;
import com.loan24.basic.audit.repository.AccessAmountRepository;
import com.loan24.basic.audit.repository.AccessLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AccessAmountService {

    @Autowired
    private AccessAmountRepository repository;
    @Autowired
    private AccessLogRepository logRepository;

    @Async
    @Transactional
    public void updateAmount(final HttpServletRequest request) {
        if (request.getHeader("X-Forwarded-For") == null) {
            return;
        }
        final var remoteAddress = request.getHeader("X-Forwarded-For").split(" ")[1];
        final var accessLog = logRepository.findByRemoteAddress(remoteAddress);
        if (accessLog.isEmpty()) {
            final var al = new AccessLog().setRemoteAddress(remoteAddress).setAccessAt(LocalDateTime.now());
            logRepository.save(al);
        } else {
            final var al = accessLog.get();
            if (Duration.between(al.getAccessAt(), LocalDateTime.now()).abs().toMinutes() < 10) {
                return;
            } else {
                al.setAccessAt(LocalDateTime.now());
                logRepository.save(al);
            }
        }
        final var accessAmount = repository.findByYearAndMonth(LocalDate.now().getYear(), LocalDate.now().getMonth());
        accessAmount.ifPresentOrElse(
                amount -> {
                    amount.setAmount(amount.getAmount() + 1);
                    repository.save(amount);
                },
                () -> {
                    final var amount = new AccessAmount()
                            .setAmount(1)
                            .setYear(LocalDate.now().getYear())
                            .setMonth(LocalDate.now().getMonth());
                    repository.save(amount);
                }
        );
    }
}
