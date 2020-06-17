package com.naver.board.task;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import com.naver.board.domain.MailVO;

@Component
public class SendMail {

   @Autowired
   private JavaMailSenderImpl mailSender;

   @Value("${sendfile}")
   private String sendfile;

   public void sendMail(MailVO vo) {
      MimeMessagePreparator mp = new MimeMessagePreparator() {

         @Override
         public void prepare(MimeMessage mimeMessage) throws Exception {
            
            //�ι�° ���� true �� ��Ƽ ��Ʈ �޽����� ����ϰڴٴ� �ǹ�
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            
            helper.setFrom(vo.getFrom());
            helper.setTo(vo.getTo());
            helper.setSubject(vo.getSubject());
            
            //1.���ڷθ� �����ϴ� ��� 
            //helper.setText(vo.getContent(), true); //�ι�° ���ڴ� html �� ����ϰڴٴ� ��
            
            //2.�̹����� �����ؼ� ������ ��� 
            String content = "<img src='cid:Home'>" + vo.getContent();
            helper.setText(content, true);
            
            FileSystemResource file = new FileSystemResource(new File(sendfile));
            
            helper.addInline("Home", file);
            
            //3. ������ ÷���ؼ� ������ ��� 
            FileSystemResource file1 = new FileSystemResource(new File(sendfile));
            
            //ù��° ���� : ÷�ε� ������ �̸� 
            //�ι�° ���� : ÷������ 
            helper.addAttachment("�����մϴ�.jpg", file1);
         }
      };
      
      mailSender.send(mp); //���� �����մϴ�. 
      System.out.println("���� �����߽��ϴ�. ");
      
   }
}