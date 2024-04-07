package org.icmss.icmssuserservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${MAIL_USERNAME}")
    private String senderMail;


    public void sendOtp(String email, String generatedOtp) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(senderMail, "Smart Ride Support");
        helper.setTo(email);

        String subject = "Here's your One Time Password (OTP) - Expire in 10 minutes!";

        String content = "<p>Hello </p>"
                + "<p>For security reason, you're required to use the following "
                + "One Time Password to register:</p>"
                + "<p><b>" + generatedOtp + "</b></p>"
                + "<br>"
                + "<p>Note: this OTP is set to expire in 10 minutes.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);
        mailSender.send(message);
    }
}
