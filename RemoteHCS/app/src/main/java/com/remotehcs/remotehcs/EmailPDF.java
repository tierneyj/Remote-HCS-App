package com.remotehcs.remotehcs;

import java.io.FileOutputStream;
import java.util.Date;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Properties;
 
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.mail.PasswordAuthentication;


import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfContentByte;

public class EmailPDF {
     
    /**
     * Sends an email with a PDF attachment.
     */

    private static ContactInfo patient;

    private static Font[] fonts = { new Font(),
                                    new Font(Font.FontFamily.UNDEFINED, 16, Font.BOLD),
                                    new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD),
                                    new Font(Font.FontFamily.HELVETICA, 18),
                                    new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD),
    };

    public void email(ContactInfo patient) {
        String smtpHost = "mail.privateemail.com"; //replace this with a valid host
        int smtpPort = 465; //replace this with a valid port
                 
        String sender = "noreply@remotehcs.com"; //replace this with a valid sender email address
        String recipient = patient.getEmail(); //replace this with a valid recipient email address
        String content = "Attached is the summary from your recent visit."; //this will be the text of the email
        String subject = "Medical Summary"; //this will be the subject of the email
        final String username = "noreply@remotehcs.com";
        final String password = "seniordesign15";
         
        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");     
        Session session = Session.getInstance( properties,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
         
        ByteArrayOutputStream outputStream = null;
         
        try {           
            //construct the text body part
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(content);
             
            //now write the PDF content to the output stream
            outputStream = new ByteArrayOutputStream();
            writePdf(outputStream, patient);
            byte[] bytes = outputStream.toByteArray();
            //construct the pdf body part
            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
            pdfBodyPart.setFileName(patient.getFname() + " " + patient.getLname() + " Medical Summary.pdf");             
            //construct the mime multi part
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(textBodyPart);
            mimeMultipart.addBodyPart(pdfBodyPart);
             
            //create the sender/recipient addresses
            InternetAddress iaSender = new InternetAddress(sender, "Remote HCS");
            InternetAddress iaRecipient = new InternetAddress(recipient);
            //construct the mime message
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(iaSender);
            mimeMessage.setSubject(subject);
            mimeMessage.setRecipient(Message.RecipientType.TO, iaRecipient);
            mimeMessage.setContent(mimeMultipart);
            
            //send off the email
            Transport.send(mimeMessage);

        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            //clean off
            if(null != outputStream) {
                try { outputStream.close(); outputStream = null; }
                catch(Exception ex) { }
            }
        }
    }
     
    /**
     * Writes the content of a PDF file (using iText API)
     * to the {@link OutputStream}.
     * @param outputStream {@link OutputStream}.
     * @throws Exception
     */
    public void writePdf(OutputStream outputStream, ContactInfo patient) throws Exception {
        Document document = new Document();
        Rectangle rectangle = new Rectangle(612,792);
        document.setPageSize(rectangle);

        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
         
        document.open();

        Paragraph title = new Paragraph();
        title.setAlignment(Element.ALIGN_CENTER);
        title.setFont(fonts[4]);
        Paragraph paragraph = new Paragraph();


        //Name: "First M Last"
        title.add("Nanohealth Visit Summary");
        document.add(title);
        paragraph.add("\n");
        paragraph.add("\n");
        paragraph.add("Name: ");

        if (patient.getMname() == "") {
            paragraph.add(patient.getFname() + " " + patient.getLname());
        } else {
            paragraph.add(patient.getFname() + " " + patient.getMname() + ". " + patient.getLname());
        }

        paragraph.add("\n");
        paragraph.add("\n");

        //DOB: "XX/XX/XXXX"
        paragraph.add("DOB: ");
        paragraph.add(patient.getDob());
        paragraph.add("\n");
        paragraph.add("\n");

        //ExternalId: 
        paragraph.add("Social Security: ");
        paragraph.add(patient.getSs());
        paragraph.add("\n");
        paragraph.add("\n");

        //Gender:
        paragraph.add("Gender: ");
        paragraph.add(patient.getSex());
        paragraph.add("\n");
        paragraph.add("\n");

        //Social Security:
        paragraph.add("Social Security: ");
        paragraph.add(patient.getSs());
        paragraph.add("\n");
        paragraph.add("\n");

        //Marital Status:
        paragraph.add("Marital Status: ");
        paragraph.add(patient.getStatus());
        paragraph.add("\n");

        document.add(paragraph);            

        document.close();
    }

    public EmailPDF (ContactInfo newPatient) {
        patient = newPatient;
        email(patient);
    }
}