package com.omniteam.backofisbackend.jms;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class EmailSubscriberMQ {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JavaMailSender emailSender;

    @JmsListener(destination = "EmailQueue")
    public void receive(TextMessage messageObject)
    {
        try {

            EmailMessage emailMessage = objectMapper.readValue(messageObject.getText(),EmailMessage.class);
            System.out.println(emailMessage);
            MimeMessage emailMimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(emailMimeMessage, true);

            helper.setFrom(emailMessage.getFrom());
            helper.setTo(emailMessage.getTo());
            helper.setSubject("Order Report");
            helper.setText(csvToHtmlTable(emailMessage.getMessage()),true);

            FileSystemResource file
                    = new FileSystemResource(new File("target/test-outputs/orders.txt"));
            helper.addAttachment("OrderReport.csv", file);

            emailSender.send(emailMimeMessage);
            messageObject.acknowledge();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        catch (MailException ex)
        {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {

        }
    }





    public static String csvToHtmlTable(String str) {
        str = handleNewLine(str);
        String[] lines = str.split("\n");

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {

            List<String> rowData = getTableData(lines[i]);

            if (checkIfEmptyRow(rowData)) {
                continue;
            }

            result.append("<tr>");
            for (String td : rowData) {
                result.append(String.format("<td>%s</td>\n", td));
            }
            result.append("</tr>");
        }

        return String.format("<table cellspacing='0' border='1'>\n%s</table>",
                result.toString());
    }

    private static String handleNewLine(String str) {

        StringBuilder buffer = new StringBuilder();
        char[] chars = str.toCharArray();

        boolean inquote = false;

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '"') {
                inquote = !inquote;
            }

            if (chars[i] == '\n' && inquote) {
                buffer.append("<br>");
            } else {
                buffer.append(chars[i]);
            }
        }

        return buffer.toString();
    }

    private static List<String> getTableData(String str) {
        List<String> data = new ArrayList<String>();

        boolean inquote = false;
        StringBuilder buffer = new StringBuilder();
        char[] chars = str.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '"') {
                inquote = !inquote;
                continue;
            }

            if (chars[i] == ',') {
                if (inquote) {
                    buffer.append(chars[i]);
                } else {
                    data.add(buffer.toString());
                    buffer.delete(0, buffer.length());
                }
            } else {
                buffer.append(chars[i]);
            }

        }

        data.add(buffer.toString().trim());

        return data;
    }

    private static boolean checkIfEmptyRow(List<String> rowData) {

        for (String td : rowData) {
            if (!td.isEmpty()) {
                return false;
            }
        }

        return true;
    }









}
