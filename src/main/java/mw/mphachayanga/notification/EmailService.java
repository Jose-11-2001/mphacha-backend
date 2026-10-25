package mw.mphachayanga.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async // Sends in background so it doesn't slow down the request
    public void sendOrderConfirmation(String toEmail, Long orderId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@mphacha.mw");
        message.setTo(toEmail);
        message.setSubject("Mphacha-Yanga Order Confirmation #" + orderId);
        message.setText("Thank you for your order! We are processing it now.");
        mailSender.send(message);
    }

    @Async
    public void sendPaymentSuccess(String toEmail, Long orderId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@mphacha.mw");
        message.setTo(toEmail);
        message.setSubject("Payment Received - Mphacha-Yanga Order #" + orderId);
        message.setText("Your payment was successful. Your order is confirmed!");
        mailSender.send(message);
    }
}