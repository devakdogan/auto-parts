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

@Service
@RequiredArgsConstructor
public class EmailManager implements EmailService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String mailAddress;
    private static final Logger logger = LoggerFactory.getLogger(EmailManager.class);

    @Override
    public void send(String to, String email) {
        try {
            MimeMessage mimeMessage =  mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"UTF-8");
            helper.setText(email,true);
            helper.setTo(to);
            helper.setSubject("Welcome to the Auto Parts");
            helper.setFrom(mailAddress);
            mailSender.send(mimeMessage);
        }catch (MessagingException e){
            logger.error(ErrorMessage.FAILED_TO_SEND_EMAIL_MESSAGE,e);
            throw new IllegalStateException(ErrorMessage.FAILED_TO_SEND_EMAIL_MESSAGE);
        }
    }

    public String buildRegisterEmail(String firstName, String link) {
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "  <head>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "    <meta name=\"x-apple-disable-message-reformatting\" />\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "    <meta name=\"color-scheme\" content=\"light dark\" />\n" +
                "    <meta name=\"supported-color-schemes\" content=\"light dark\" />\n" +
                "    <title></title>\n" +
                "    <style type=\"text/css\" rel=\"stylesheet\" media=\"all\">\n" +
                "      /* Base ------------------------------ */\n" +
                "\n" +
                "      @import url(\"https://fonts.googleapis.com/css?family=Nunito+Sans:400,700&display=swap\");\n" +
                "      body {\n" +
                "        width: 100% !important;\n" +
                "        height: 100%;\n" +
                "        margin: 0;\n" +
                "        -webkit-text-size-adjust: none;\n" +
                "      }\n" +
                "\n" +
                "      a {\n" +
                "        color: #e63946 ;\n" +
                "      }\n" +
                "\n" +
                "      a img {\n" +
                "        border: none;\n" +
                "      }\n" +
                "\n" +
                "      td {\n" +
                "        word-break: break-word;\n" +
                "      }\n" +
                "\n" +
                "      .preheader {\n" +
                "        display: none !important;\n" +
                "        visibility: hidden;\n" +
                "        mso-hide: all;\n" +
                "        font-size: 1px;\n" +
                "        line-height: 1px;\n" +
                "        max-height: 0;\n" +
                "        max-width: 0;\n" +
                "        opacity: 0;\n" +
                "        overflow: hidden;\n" +
                "      }\n" +
                "      /* Type ------------------------------ */\n" +
                "\n" +
                "      body,\n" +
                "      td,\n" +
                "      th {\n" +
                "        font-family: \"Nunito Sans\", Helvetica, Arial, sans-serif;\n" +
                "      }\n" +
                "\n" +
                "      h1 {\n" +
                "        margin-top: 0;\n" +
                "        color: #333333;\n" +
                "        font-size: 22px;\n" +
                "        font-weight: bold;\n" +
                "        text-align: left;\n" +
                "      }\n" +
                "\n" +
                "      h2 {\n" +
                "        margin-top: 0;\n" +
                "        color: #333333;\n" +
                "        font-size: 16px;\n" +
                "        font-weight: bold;\n" +
                "        text-align: left;\n" +
                "      }\n" +
                "\n" +
                "      h3 {\n" +
                "        margin-top: 0;\n" +
                "        color: #333333;\n" +
                "        font-size: 14px;\n" +
                "        font-weight: bold;\n" +
                "        text-align: left;\n" +
                "      }\n" +
                "\n" +
                "      td,\n" +
                "      th {\n" +
                "        font-size: 16px;\n" +
                "      }\n" +
                "\n" +
                "      p,\n" +
                "      ul,\n" +
                "      ol,\n" +
                "      blockquote {\n" +
                "        margin: 0.4em 0 1.1875em;\n" +
                "        font-size: 16px;\n" +
                "        line-height: 1.625;\n" +
                "      }\n" +
                "\n" +
                "      p.sub {\n" +
                "        font-size: 13px;\n" +
                "      }\n" +
                "      /* Utilities ------------------------------ */\n" +
                "\n" +
                "      .align-right {\n" +
                "        text-align: right;\n" +
                "      }\n" +
                "\n" +
                "      .align-left {\n" +
                "        text-align: left;\n" +
                "      }\n" +
                "\n" +
                "      .align-center {\n" +
                "        text-align: center;\n" +
                "      }\n" +
                "\n" +
                "      .u-margin-bottom-none {\n" +
                "        margin-bottom: 0;\n" +
                "      }\n" +
                "      /* Buttons ------------------------------ */\n" +
                "\n" +
                "      .button {\n" +
                "        background-color: rgb(29, 53, 87);\n" +
                "        border-top: 10px solid rgb(29, 53, 87);\n" +
                "        border-right: 18px solid rgb(29, 53, 87);\n" +
                "        border-bottom: 10px solid rgb(29, 53, 87);\n" +
                "        border-left: 18px solid rgb(29, 53, 87);\n" +
                "        display: inline-block;\n" +
                "        color: #fff;\n" +
                "        text-decoration: none;\n" +
                "        border-radius: 3px;\n" +
                "        box-shadow: 0 2px 3px rgba(0, 0, 0, 0.16);\n" +
                "        -webkit-text-size-adjust: none;\n" +
                "        box-sizing: border-box;\n" +
                "      }\n" +
                "\n" +
                "      .button--green {\n" +
                "        background-color: #e63946;\n" +
                "        border-top: 10px solid #e63946;\n" +
                "        border-right: 18px solid #e63946;\n" +
                "        border-bottom: 10px solid #e63946;\n" +
                "        border-left: 18px solid #e63946;\n" +
                "      }\n" +
                "\n" +
                "      .button--red {\n" +
                "        background-color: #ff6136;\n" +
                "        border-top: 10px solid #ff6136;\n" +
                "        border-right: 18px solid #ff6136;\n" +
                "        border-bottom: 10px solid #ff6136;\n" +
                "        border-left: 18px solid #ff6136;\n" +
                "      }\n" +
                "\n" +
                "      @media only screen and (max-width: 500px) {\n" +
                "        .button {\n" +
                "          width: 100% !important;\n" +
                "          text-align: center !important;\n" +
                "        }\n" +
                "      }\n" +
                "      /* Attribute list ------------------------------ */\n" +
                "\n" +
                "      .attributes {\n" +
                "        margin: 0 0 21px;\n" +
                "      }\n" +
                "\n" +
                "      .attributes_content {\n" +
                "        background-color: #f4f4f7;\n" +
                "        padding: 16px;\n" +
                "      }\n" +
                "\n" +
                "      .attributes_item {\n" +
                "        padding: 0;\n" +
                "      }\n" +
                "      /* Related Items ------------------------------ */\n" +
                "\n" +
                "      .related {\n" +
                "        width: 100%;\n" +
                "        margin: 0;\n" +
                "        padding: 25px 0 0 0;\n" +
                "        -premailer-width: 100%;\n" +
                "        -premailer-cellpadding: 0;\n" +
                "        -premailer-cellspacing: 0;\n" +
                "      }\n" +
                "\n" +
                "      .related_item {\n" +
                "        padding: 10px 0;\n" +
                "        color: #cbcccf;\n" +
                "        font-size: 15px;\n" +
                "        line-height: 18px;\n" +
                "      }\n" +
                "\n" +
                "      .related_item-title {\n" +
                "        display: block;\n" +
                "        margin: 0.5em 0 0;\n" +
                "      }\n" +
                "\n" +
                "      .related_item-thumb {\n" +
                "        display: block;\n" +
                "        padding-bottom: 10px;\n" +
                "      }\n" +
                "\n" +
                "      .related_heading {\n" +
                "        border-top: 1px solid #cbcccf;\n" +
                "        text-align: center;\n" +
                "        padding: 25px 0 10px;\n" +
                "      }\n" +
                "      /* Discount Code ------------------------------ */\n" +
                "\n" +
                "      .discount {\n" +
                "        width: 100%;\n" +
                "        margin: 0;\n" +
                "        padding: 24px;\n" +
                "        -premailer-width: 100%;\n" +
                "        -premailer-cellpadding: 0;\n" +
                "        -premailer-cellspacing: 0;\n" +
                "        background-color: #f4f4f7;\n" +
                "        border: 2px dashed #cbcccf;\n" +
                "      }\n" +
                "\n" +
                "      .discount_heading {\n" +
                "        text-align: center;\n" +
                "      }\n" +
                "\n" +
                "      .discount_body {\n" +
                "        text-align: center;\n" +
                "        font-size: 15px;\n" +
                "      }\n" +
                "      /* Social Icons ------------------------------ */\n" +
                "\n" +
                "      .social {\n" +
                "        width: auto;\n" +
                "      }\n" +
                "\n" +
                "      .social td {\n" +
                "        padding: 0;\n" +
                "        width: auto;\n" +
                "      }\n" +
                "\n" +
                "      .social_icon {\n" +
                "        height: 20px;\n" +
                "        margin: 0 8px 10px 8px;\n" +
                "        padding: 0;\n" +
                "      }\n" +
                "      /* Data table ------------------------------ */\n" +
                "\n" +
                "      .purchase {\n" +
                "        width: 100%;\n" +
                "        margin: 0;\n" +
                "        padding: 35px 0;\n" +
                "        -premailer-width: 100%;\n" +
                "        -premailer-cellpadding: 0;\n" +
                "        -premailer-cellspacing: 0;\n" +
                "      }\n" +
                "\n" +
                "      .purchase_content {\n" +
                "        width: 100%;\n" +
                "        margin: 0;\n" +
                "        padding: 25px 0 0 0;\n" +
                "        -premailer-width: 100%;\n" +
                "        -premailer-cellpadding: 0;\n" +
                "        -premailer-cellspacing: 0;\n" +
                "      }\n" +
                "\n" +
                "      .purchase_item {\n" +
                "        padding: 10px 0;\n" +
                "        color: #51545e;\n" +
                "        font-size: 15px;\n" +
                "        line-height: 18px;\n" +
                "      }\n" +
                "\n" +
                "      .purchase_heading {\n" +
                "        padding-bottom: 8px;\n" +
                "        border-bottom: 1px solid #eaeaec;\n" +
                "      }\n" +
                "\n" +
                "      .purchase_heading p {\n" +
                "        margin: 0;\n" +
                "        color: #85878e;\n" +
                "        font-size: 12px;\n" +
                "      }\n" +
                "\n" +
                "      .purchase_footer {\n" +
                "        padding-top: 15px;\n" +
                "        border-top: 1px solid #eaeaec;\n" +
                "      }\n" +
                "\n" +
                "      .purchase_total {\n" +
                "        margin: 0;\n" +
                "        text-align: right;\n" +
                "        font-weight: bold;\n" +
                "        color: #333333;\n" +
                "      }\n" +
                "\n" +
                "      .purchase_total--label {\n" +
                "        padding: 0 15px 0 0;\n" +
                "      }\n" +
                "\n" +
                "      body {\n" +
                "        background-color: #f2f4f6;\n" +
                "        color: #51545e;\n" +
                "      }\n" +
                "\n" +
                "      p {\n" +
                "        color: #51545e;\n" +
                "      }\n" +
                "\n" +
                "      .email-wrapper {\n" +
                "        width: 100%;\n" +
                "        margin: 0;\n" +
                "        padding: 0;\n" +
                "        -premailer-width: 100%;\n" +
                "        -premailer-cellpadding: 0;\n" +
                "        -premailer-cellspacing: 0;\n" +
                "        background-color: #f2f4f6;\n" +
                "      }\n" +
                "\n" +
                "      .email-content {\n" +
                "        width: 100%;\n" +
                "        margin: 0;\n" +
                "        padding: 0;\n" +
                "        -premailer-width: 100%;\n" +
                "        -premailer-cellpadding: 0;\n" +
                "        -premailer-cellspacing: 0;\n" +
                "      }\n" +
                "      /* Masthead ----------------------- */\n" +
                "\n" +
                "      .email-masthead {\n" +
                "        padding: 25px 0;\n" +
                "        text-align: center;\n" +
                "      }\n" +
                "\n" +
                "      .email-masthead_logo {\n" +
                "        width: 94px;\n" +
                "      }\n" +
                "\n" +
                "      .email-masthead_name {\n" +
                "        font-size: 16px;\n" +
                "        font-weight: bold;\n" +
                "        color: #a8aaaf;\n" +
                "        text-decoration: none;\n" +
                "        text-shadow: 0 1px 0 white;\n" +
                "      }\n" +
                "      /* Body ------------------------------ */\n" +
                "\n" +
                "      .email-body {\n" +
                "        width: 100%;\n" +
                "        margin: 0;\n" +
                "        padding: 0;\n" +
                "        -premailer-width: 100%;\n" +
                "        -premailer-cellpadding: 0;\n" +
                "        -premailer-cellspacing: 0;\n" +
                "      }\n" +
                "\n" +
                "      .email-body_inner {\n" +
                "        width: 570px;\n" +
                "        margin: 0 auto;\n" +
                "        padding: 0;\n" +
                "        -premailer-width: 570px;\n" +
                "        -premailer-cellpadding: 0;\n" +
                "        -premailer-cellspacing: 0;\n" +
                "        background-color: #ffffff;\n" +
                "      }\n" +
                "\n" +
                "      .email-footer {\n" +
                "        width: 570px;\n" +
                "        margin: 0 auto;\n" +
                "        padding: 0;\n" +
                "        -premailer-width: 570px;\n" +
                "        -premailer-cellpadding: 0;\n" +
                "        -premailer-cellspacing: 0;\n" +
                "        text-align: center;\n" +
                "      }\n" +
                "\n" +
                "      .email-footer p {\n" +
                "        color: #a8aaaf;\n" +
                "      }\n" +
                "\n" +
                "      .body-action {\n" +
                "        width: 100%;\n" +
                "        margin: 30px auto;\n" +
                "        padding: 0;\n" +
                "        -premailer-width: 100%;\n" +
                "        -premailer-cellpadding: 0;\n" +
                "        -premailer-cellspacing: 0;\n" +
                "        text-align: center;\n" +
                "      }\n" +
                "\n" +
                "      .body-sub {\n" +
                "        margin-top: 25px;\n" +
                "        padding-top: 25px;\n" +
                "        border-top: 1px solid #eaeaec;\n" +
                "      }\n" +
                "\n" +
                "      .content-cell {\n" +
                "        padding: 45px;\n" +
                "      }\n" +
                "      /*Media Queries ------------------------------ */\n" +
                "\n" +
                "      @media only screen and (max-width: 600px) {\n" +
                "        .email-body_inner,\n" +
                "        .email-footer {\n" +
                "          width: 100% !important;\n" +
                "        }\n" +
                "      }\n" +
                "\n" +
                "      @media (prefers-color-scheme: dark) {\n" +
                "        body,\n" +
                "        .email-body,\n" +
                "        .email-body_inner,\n" +
                "        .email-content,\n" +
                "        .email-wrapper,\n" +
                "        .email-masthead,\n" +
                "        .email-footer {\n" +
                "          background-color: #333333 !important;\n" +
                "          color: #fff !important;\n" +
                "        }\n" +
                "        p,\n" +
                "        ul,\n" +
                "        ol,\n" +
                "        blockquote,\n" +
                "        h1,\n" +
                "        h2,\n" +
                "        h3,\n" +
                "        span,\n" +
                "        .purchase_item {\n" +
                "          color: #fff !important;\n" +
                "        }\n" +
                "        .attributes_content,\n" +
                "        .discount {\n" +
                "          background-color: #222 !important;\n" +
                "        }\n" +
                "        .email-masthead_name {\n" +
                "          text-shadow: none !important;\n" +
                "        }\n" +
                "      }\n" +
                "\n" +
                "      :root {\n" +
                "        color-scheme: light dark;\n" +
                "        supported-color-schemes: light dark;\n" +
                "      }\n" +
                "    </style>\n" +
                "    <!--[if mso]>\n" +
                "      <style type=\"text/css\">\n" +
                "        .f-fallback {\n" +
                "          font-family: Arial, sans-serif;\n" +
                "        }\n" +
                "      </style>\n" +
                "    <![endif]-->\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <span class=\"preheader\"\n" +
                "      >We're thrilled to have you here! Get ready to dive into your new account.</span\n" +
                "    >\n" +
                "    <table\n" +
                "      class=\"email-wrapper\"\n" +
                "      width=\"100%\"\n" +
                "      cellpadding=\"0\"\n" +
                "      cellspacing=\"0\"\n" +
                "      role=\"presentation\"\n" +
                "    >\n" +
                "      <tr>\n" +
                "        <td align=\"center\">\n" +
                "          <table\n" +
                "            class=\"email-content\"\n" +
                "            width=\"100%\"\n" +
                "            cellpadding=\"0\"\n" +
                "            cellspacing=\"0\"\n" +
                "            role=\"presentation\"\n" +
                "          >\n" +
                "            <tr>\n" +
                "              <td class=\"email-masthead\">\n" +
                "                <a href=\"https://localhost:3000\"\n" +
                "                  ><img\n" +
                "                    src=\"https://www.linkpicture.com/q/AUTO_PARTS-removebg-preview.png\"\n" +
                "                    alt=\"\"\n" +
                "                    style=\"height: auto; max-width: 100%;\"\n" +
                "                    \n" +
                "                /></a>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "            <!-- Email Body -->\n" +
                "            <tr>\n" +
                "              <td\n" +
                "                class=\"email-body\"\n" +
                "                width=\"570\"\n" +
                "                cellpadding=\"0\"\n" +
                "                cellspacing=\"0\"\n" +
                "              >\n" +
                "                <table\n" +
                "                  class=\"email-body_inner\"\n" +
                "                  align=\"center\"\n" +
                "                  width=\"570\"\n" +
                "                  cellpadding=\"0\"\n" +
                "                  cellspacing=\"0\"\n" +
                "                  role=\"presentation\"\n" +
                "                >\n" +
                "                  <!-- Body content -->\n" +
                "                  <tr>\n" +
                "                    <td class=\"content-cell\">\n" +
                "                      <div class=\"f-fallback\">\n" +
                "                        <h1>Welcome "+ firstName +"!</h1>\n" +
                "                        <p>\n" +
                "                          We're excited to have you get started. First, you need to\n" +
                "                  confirm your account. Just press the button below.\n" +
                "                        </p>\n" +
                "                        <!-- Action -->\n" +
                "                        <table\n" +
                "                          class=\"body-action\"\n" +
                "                          align=\"center\"\n" +
                "                          width=\"100%\"\n" +
                "                          cellpadding=\"0\"\n" +
                "                          cellspacing=\"0\"\n" +
                "                          role=\"presentation\"\n" +
                "                        >\n" +
                "                          <tr>\n" +
                "                            <td align=\"center\">\n" +
                "                              <!-- Border based button\n" +
                "           https://litmus.com/blog/a-guide-to-bulletproof-buttons-in-email-design -->\n" +
                "                              <table\n" +
                "                                width=\"100%\"\n" +
                "                                border=\"0\"\n" +
                "                                cellspacing=\"0\"\n" +
                "                                cellpadding=\"0\"\n" +
                "                                role=\"presentation\"\n" +
                "                              >\n" +
                "                                <tr>\n" +
                "                                  <td align=\"center\">\n" +
                "                                    <a\n" +
                "                                      href="+link+"\n" +
                "                                      class=\"f-fallback button button--green\"\n" +
                "                                      target=\"_blank\"\n" +
                "                                      >Confirm Account</a\n" +
                "                                    >\n" +
                "                                  </td>\n" +
                "                                </tr>\n" +
                "                              </table>\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </table>\n" +
                "                        <p style=\"margin: 0\">\n" +
                "                          If that doesn't work, copy and paste the following link in\n" +
                "                          your browser:\n" +
                "                        </p>\n" +
                "                            <p style=\"margin: 0\">\n" +
                "                              <a href="+link+"\" target=\"_blank\" style=\"color: #e63946\"\n" +
                "                                ></a\n" +
                "                              >\n" +
                "                            </p>\n" +
                "                        <!-- Sub copy -->\n" +
                "                        <table class=\"body-sub\" role=\"presentation\">\n" +
                "                          <tr>\n" +
                "                            <td>\n" +
                "                              <p style=\"margin: 0\">\n" +
                "                                If you have any questions, just reply to this\n" +
                "                                email&mdash;we're always happy to help out.\n" +
                "                              </p>\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </table>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </table>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "              <td>\n" +
                "                <table\n" +
                "                  class=\"email-footer\"\n" +
                "                  align=\"center\"\n" +
                "                  width=\"570\"\n" +
                "                  cellpadding=\"0\"\n" +
                "                  cellspacing=\"0\"\n" +
                "                  role=\"presentation\"\n" +
                "                >\n" +
                "                  <tr>\n" +
                "                    <td class=\"content-cell\" align=\"center\">\n" +
                "                      <p class=\"f-fallback sub align-center\">\n" +
                "                        [Auto Parts, GmbH]\n" +
                "                        <br />DE 40474 Düsseldorf<br />Felix-Klein-Straße 1, Germany\n" +
                "                      </p>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </table>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "          </table>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </table>\n" +
                "  </body>\n" +
                "</html>\n";
    }
}
