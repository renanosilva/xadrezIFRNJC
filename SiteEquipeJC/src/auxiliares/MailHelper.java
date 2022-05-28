/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 05/07/2006
 */
package auxiliares;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Classe que cont�m m�todos para envio de emails.
 * 
 * @author Renan
 */
public class MailHelper {

	private static final String PASSWORD = "senha";
	public static final String EMAIL_SISTEMA = "email@gmail.com";

	public static void enviarEmail(String destino, String titulo,
			String mensagem) throws AddressException, MessagingException {
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				// Par�metros de conex�o com servidor Gmail
				Properties props = new Properties();
				props.put("mail.smtp.host", "smtp.gmail.com");
				props.put("mail.smtp.socketFactory.port", "465");
				props.put("mail.smtp.socketFactory.class",
						"javax.net.ssl.SSLSocketFactory");
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.port", "465");

				Session session = Session.getDefaultInstance(props,
						new javax.mail.Authenticator() {
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(EMAIL_SISTEMA,
										PASSWORD);
							}
						});

				// Ativa Debug para sess�o 
				session.setDebug(true);

				final Message message = new MimeMessage(session);
				
				try {
					message.setFrom(new InternetAddress(EMAIL_SISTEMA, "Cubo Social"));
					
					Address[] toUser = InternetAddress // Destinat�rio(s)
							.parse(destino);
					
					message.setRecipients(Message.RecipientType.TO, toUser);
					message.setSubject(titulo);// Assunto
					message.setContent(mensagem, "text/html; charset=UTF-8");
					
					Transport.send(message);
					System.out.println("Email enviado...");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
					return;
				} catch (MessagingException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		t.start();
		
	}

}