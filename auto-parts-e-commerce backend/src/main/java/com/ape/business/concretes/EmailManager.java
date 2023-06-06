package com.ape.business.concretes;

import com.ape.business.abstracts.EmailService;
import com.ape.utility.ErrorMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailManager implements EmailService {
    private final JavaMailSender mailSender;

    @Value("${management.gamingpromarket.app.mailAddress}")
    private String mailAddress;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Override
    public void send(String to, String email) {
        try {
            MimeMessage mimeMessage =  mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"UTF-8");
            helper.setText(email,true);
            helper.setTo(to);
            helper.setSubject("Welcome to the Gaming Pro Market");
            helper.setFrom(mailAddress);
            mailSender.send(mimeMessage);
        }catch (MessagingException e){
            logger.error(ErrorMessage.FAILED_TO_SEND_EMAIL_MESSAGE,e);
            throw new IllegalStateException(ErrorMessage.FAILED_TO_SEND_EMAIL_MESSAGE);
        }
    }

    public String buildRegisterEmail(String firstName, String link) {
        return null;
    }
}
