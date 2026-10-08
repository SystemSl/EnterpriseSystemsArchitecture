package com.example.EnterpriseSystemsArchitecture.jms;

import com.example.EnterpriseSystemsArchitecture.config.JmsConfig;
import com.example.EnterpriseSystemsArchitecture.dto.EntityChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);

    @Value("${app.mail.recipient:admin@example.com}")
    private String recipientEmail;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @JmsListener(destination = JmsConfig.ENTITY_EVENTS_TOPIC)
    public void onMessage(EntityChangeEvent event) {
        log.info("[JMS NotificationListener] Получено событие для анализа: {}", event);

        if (event == null || event.getActionType() == null) {
            return;
        }

        String subject = "Уведомление о событии в системе: " + event.getActionType() + " " + event.getEntityName();
        String text = String.format("Здравствуйте!\n\nВ системе зафиксировано изменение:\n" +
                        "Сущность: %s (ID: %d)\n" +
                        "Тип операции: %s\n" +
                        "Подробности: %s\n" +
                        "Время: %s\n",
                event.getEntityName(), event.getEntityId(), event.getActionType(), event.getDetails(), event.getTimestamp());

        sendEmail(subject, text);
    }

    private void sendEmail(String subject, String text) {
        if (mailSender != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(recipientEmail);
                message.setSubject(subject);
                message.setText(text);
                mailSender.send(message);
                log.info("[JMS NotificationListener] Письмо успешно отправлено на {}", recipientEmail);
            } catch (Exception e) {
                log.warn("[JMS NotificationListener] Не удалось отправить письмо через SMTP ({}). Выводим в лог:\n{}\n{}",
                        e.getMessage(), subject, text);
            }
        } else {
            log.info("[JMS NotificationListener] [Имитация Email на {}]\nТЕМА: {}\nТЕКСТ:\n{}",
                    recipientEmail, subject, text);
        }
    }
}
