package expresstalk.dev.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.username}")
    private String username;
    private final JavaMailSender mailSender;

    public void sendEmailWithCode(String emailTo, String code) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(new InternetAddress(username));
        message.setRecipients(MimeMessage.RecipientType.TO, emailTo);
        message.setSubject("ExpressTalk Authentication");

        String htmlContent = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "    <title></title>\n" +
                "    <style>\n" +
                "      @import url(https://fonts.googleapis.com/css?family=Roboto:regular,700);\n" +
                "\n" +
                "      body {\n" +
                "        margin: 0;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <table\n" +
                "      width=\"100%\"\n" +
                "      style=\"background-color: #353f75; min-width: 600px; font-family: Roboto\">\n" +
                "      <tbody>\n" +
                "        <tr>\n" +
                "          <td align=\"center\" valign=\"top\" width=\"100%\" style=\"min-width: 600px\">\n" +
                "            <center>\n" +
                "              <table>\n" +
                "                <tbody>\n" +
                "                  <tr>\n" +
                "                    <td align=\"center\">\n" +
                "                      <table\n" +
                "                        width=\"100%\"\n" +
                "                        style=\"min-width: 500px\"\n" +
                "                        border=\"0\"\n" +
                "                        cellpadding=\"0\"\n" +
                "                        cellspacing=\"0\">\n" +
                "                        <tbody>\n" +
                "                          <tr height=\"50\">\n" +
                "                            <td\n" +
                "                              width=\"100%\"\n" +
                "                              height=\"50\"\n" +
                "                              style=\"line-height: 1px; font-size: 1px\">\n" +
                "                              &nbsp;\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                          <tr>\n" +
                "                            <td align=\"center\">\n" +
                "                              <table\n" +
                "                                border=\"0\"\n" +
                "                                cellpadding=\"0\"\n" +
                "                                cellspacing=\"0\"\n" +
                "                                style=\"min-width: 500px\">\n" +
                "                                <tbody>\n" +
                "                                  <tr>\n" +
                "                                    <td valign=\"middle\" align=\"center\">\n" +
                "                                      <div style=\"max-height: 40px\">\n" +
                "                                        <div>\n" +
                "                                          <a href=\"#\" target=\"_blank\"\n" +
                "                                            ><img\n" +
                "                                              align=\"none\"\n" +
                "                                              border=\"0\"\n" +
                "                                              hspace=\"0\"\n" +
                "                                              src=\"https://www.aionlinecourse.com/uploads/ai_software/image/2023/10/logocom.png\"\n" +
                "                                              style=\"\n" +
                "                                                max-width: 70px;\n" +
                "                                                height: auto;\n" +
                "                                                display: block;\n" +
                "                                                margin: 0px;\n" +
                "                                              \"\n" +
                "                                              vspace=\"0\"\n" +
                "                                              width=\"90px\"\n" +
                "                                              data-bit=\"iit\" />\n" +
                "                                          </a>\n" +
                "                                        </div>\n" +
                "                                      </div>\n" +
                "                                    </td>\n" +
                "                                  </tr>\n" +
                "                                </tbody>\n" +
                "                              </table>\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </tbody>\n" +
                "                      </table>\n" +
                "                      <table>\n" +
                "                        <tbody>\n" +
                "                          <tr>\n" +
                "                            <td align=\"center\" style=\"font-size: 26px; line-height: 32px\">\n" +
                "                              &nbsp;\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                          <tr>\n" +
                "                            <td align=\"center\">&nbsp;</td>\n" +
                "                          </tr>\n" +
                "                        </tbody>\n" +
                "                      </table>\n" +
                "                      <table\n" +
                "                        width=\"100%\"\n" +
                "                        style=\"min-width: 500px\"\n" +
                "                        border=\"0\"\n" +
                "                        cellpadding=\"0\"\n" +
                "                        cellspacing=\"0\">\n" +
                "                        <tbody>\n" +
                "                          <tr>\n" +
                "                            <td align=\"center\">\n" +
                "                              <table\n" +
                "                                width=\"500\"\n" +
                "                                border=\"0\"\n" +
                "                                cellpadding=\"0\"\n" +
                "                                cellspacing=\"0\"\n" +
                "                                style=\"min-width: 500px\">\n" +
                "                                <tbody>\n" +
                "                                  <tr>\n" +
                "                                    <td\n" +
                "                                      width=\"100%\"\n" +
                "                                      height=\"50\"\n" +
                "                                      style=\"line-height: 1px; font-size: 1px\">\n" +
                "                                      <div\n" +
                "                                        style=\"\n" +
                "                                          color: #fff;\n" +
                "                                          text-align: center;\n" +
                "                                          font-size: 26px;\n" +
                "                                          line-height: 32px;\n" +
                "                                          line-height: 100%;\n" +
                "                                          letter-spacing: 2px;\n" +
                "                                        \">\n" +
                "                                        Your account code:\n" +
                "                                      </div>\n" +
                "                                    </td>\n" +
                "                                  </tr>\n" +
                "                                  <tr>\n" +
                "                                    <td\n" +
                "                                      width=\"100%\"\n" +
                "                                      height=\"50\"\n" +
                "                                      style=\"line-height: 1px; font-size: 1px\">\n" +
                "                                      <div\n" +
                "                                        style=\"\n" +
                "                                          color: #fff;\n" +
                "                                          text-align: center;\n" +
                "                                          font-size: 26px;\n" +
                "                                          line-height: 32px;\n" +
                "                                          line-height: 100%;\n" +
                "                                          letter-spacing: 2px;\n" +
                "                                        \">\n" +
                                                        code  +
                "                                      </div>\n" +
                "                                    </td>\n" +
                "                                  </tr>\n" +
                "                                </tbody>\n" +
                "                              </table>\n" +
                "                              <table\n" +
                "                                width=\"100%\"\n" +
                "                                style=\"min-width: 500px\"\n" +
                "                                border=\"0\"\n" +
                "                                cellpadding=\"0\"\n" +
                "                                cellspacing=\"0\">\n" +
                "                                <tbody>\n" +
                "                                  <tr height=\"40\">\n" +
                "                                    <td align=\"center\">&nbsp;</td>\n" +
                "                                  </tr>\n" +
                "                                </tbody>\n" +
                "                              </table>\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </tbody>\n" +
                "                      </table>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody>\n" +
                "              </table>\n" +
                "\n" +
                "              <table\n" +
                "                width=\"500\"\n" +
                "                border=\"0\"\n" +
                "                cellpadding=\"0\"\n" +
                "                cellspacing=\"0\"\n" +
                "                style=\"\n" +
                "                  background-color: #202957;\n" +
                "                  color: #fff;\n" +
                "                  margin: 0 auto;\n" +
                "                  border-radius: 24px;\n" +
                "                \">\n" +
                "                <tbody>\n" +
                "                  <tr height=\"50\">\n" +
                "                    <td width=\"50\">&nbsp;</td>\n" +
                "                    <td height=\"50\" style=\"line-height: 1px; font-size: 1px\">&nbsp;</td>\n" +
                "                    <td width=\"50\">&nbsp;</td>\n" +
                "                  </tr>\n" +
                "                  <tr>\n" +
                "                    <td width=\"50\">&nbsp;</td>\n" +
                "                    <td\n" +
                "                      align=\"left\"\n" +
                "                      style=\"\n" +
                "                        font-size: 14px;\n" +
                "                        line-height: 19px;\n" +
                "                        line-height: 134%;\n" +
                "                        letter-spacing: 0.5px;\n" +
                "                      \">\n" +
                "                      Hello there!!! \uD83D\uDC4B\n" +
                "\n" +
                "                      <br /><br />\n" +
                "                    </td>\n" +
                "                    <td width=\"50\">&nbsp;</td>\n" +
                "                  </tr>\n" +
                "                  <tr>\n" +
                "                    <td width=\"50\">&nbsp;</td>\n" +
                "                    <td\n" +
                "                      align=\"left\"\n" +
                "                      style=\"\n" +
                "                        font-size: 14px;\n" +
                "                        line-height: 19px;\n" +
                "                        line-height: 134%;\n" +
                "                        letter-spacing: 0.5px;\n" +
                "                      \">\n" +
                "                      You recently tried to sign up your account. Use this code to\n" +
                "                      complete the operation.\n" +
                "                      <br /><br />\n" +
                "                    </td>\n" +
                "                    <td width=\"50\">&nbsp;</td>\n" +
                "                  </tr>\n" +
                "                  <tr>\n" +
                "                    <td width=\"50\">&nbsp;</td>\n" +
                "                    <td\n" +
                "                      align=\"left\"\n" +
                "                      style=\"font-size: 14px; line-height: 19px; line-height: 134%\">\n" +
                "                      <div\n" +
                "                        style=\"font-size: 14px; line-height: 19px; letter-spacing: 0.5px\">\n" +
                "                        With thanks,\n" +
                "                        <br />ExpressTalk Team\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td width=\"50\">&nbsp;</td>\n" +
                "                  </tr>\n" +
                "                  <tr height=\"50\">\n" +
                "                    <td width=\"50\">&nbsp;</td>\n" +
                "                    <td height=\"50\" style=\"line-height: 1px; font-size: 1px\">&nbsp;</td>\n" +
                "                    <td width=\"50\">&nbsp;</td>\n" +
                "                  </tr>\n" +
                "                </tbody>\n" +
                "              </table>\n" +
                "            </center>\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "        <tr height=\"50\">\n" +
                "          <td width=\"100%\" height=\"20\" style=\"line-height: 1px; font-size: 1px\">\n" +
                "            &nbsp;\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "      </tbody>\n" +
                "    </table>\n" +
                "  </body>\n" +
                "</html>\n";

        message.setContent(htmlContent, "text/html; charset=utf-8");

        mailSender.send(message);
    }

}
