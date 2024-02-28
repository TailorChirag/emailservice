package chiragtailor.tech.emailservice.consumers;

import chiragtailor.tech.emailservice.dtos.SendEmailEventDto;
import chiragtailor.tech.emailservice.utils.EmailUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Service
public class SendEamilToConsumer {

    private ObjectMapper objectMapper;

    public SendEamilToConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "sendEmail", groupId = "emailService")
    public void sendEmailToConsumer(String message) throws JsonProcessingException {

        SendEmailEventDto dto = objectMapper.readValue(message, SendEmailEventDto.class);

        String to = dto.getTo();
        String body = dto.getBody();
        String from = dto.getFrom();
        String subject = dto.getSubject();

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("studentpractising@gmail.com" , "qovcpxbuvpfgqwdw");
            }
        };

        Session session = Session.getDefaultInstance(props, auth);
        System.out.println("Session created");
        EmailUtil.sendEmail(session, to, subject, body);
    }
}
