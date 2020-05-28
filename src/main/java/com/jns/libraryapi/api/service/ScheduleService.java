package com.jns.libraryapi.api.service;

import com.jns.libraryapi.api.model.entity.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    @Value("${application.mail.lateLoans.message}")
    private String message;

    private static final String CRON_LATE_LOANS = "0 0 0 1/1 * ?";

    private final LoanService loanService;

    private final EmailService emailService;

    @Scheduled(cron = CRON_LATE_LOANS)
    public void sendMailToLateLeans() {
        List<Loan> allLateLoans = loanService.getAllLateLoans();

        final List<String> emails = allLateLoans.stream()
                .map(loan -> loan.getCustumerEmail())
                .collect(Collectors.toList());

        emailService.sendMails(message, emails);
    }
}
