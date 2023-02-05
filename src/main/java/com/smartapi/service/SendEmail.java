package com.smartapi.service;

import com.smartapi.Configs;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.Instant;

@Service
@Log4j2
public class SendEmail {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	Configs configs;

	public void sendMail(String message) {
		if (message != null && message.contains("Failed to")) {
			// reinit session repeated errors
			long epochNow = Instant.now().getEpochSecond();
			if (Math.abs(epochNow- configs.getReInitLastEpoch()) > 1800) {
				configs.setReInitLastEpoch(epochNow);
			} else {
				log.info("Skipping mail for failure, as recently sent same mail");
				return;
			}
		}

		log.info("Sending email");
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo("vijaykumarvijay886@gmail.com");

		msg.setSubject("Opt Trade, stop at max loss");
		msg.setText(message);
		msg.setFrom("vijaykumarvijay886@gmail.com");
		try {
			javaMailSender.send(msg);
			log.info("Email sent");
		} catch (Exception e) {
			log.error("Error in sending mail ", e);
		}
	}
}



