package org.webp.misc.testutils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;

/**
 * raw TCP soketleri kullanarak düşük seviyede HTTP iletişimi gerçekleştiren sınıf
 * Kullanmak zorunda olduğunuz bir şey değildir ancak HTTP iletişimini anlamak için gereklidir.
 */
public class HttpUtils {

    /**
     * Verilen host:port'a doğrudan TCP soketleri ile bağlanıp, girdi olarka string gönderip sonuç olarak (varsa)
     * string alan metot
     */
    public static String executeHttpCommand(String host, int port, String request) throws Exception {
        return executeHttpCommand(host, port, request, "UTF-8");
    }

    /**
     * Verilen host:port'a doğrudan TCP soketleri ile bağlanıp, girdi olarka string gönderip sonuç olarak (varsa)
     * string alan metot
     */
    public static String executeHttpCommand(String host, int port, String request, String charset) throws Exception {
        Objects.requireNonNull(host);
        Objects.requireNonNull(request);

        try (Socket socket = new Socket(host, port)) {
            socket.getOutputStream().write(request.getBytes(charset));
            socket.shutdownOutput();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), charset));

            StringBuilder response = new StringBuilder();
            String line = in.readLine();

            while (line != null) {
                response.append(line).append("\n");
                line = in.readLine();
            }

            return response.toString();
        }
    }

    /**
     * HTTP mesajındaki header'ları al
     *
     * @param message
     * @return
     */
    public static String getHeaderBlock(String message) {
        Objects.requireNonNull(message);

        String[] lines = message.split("\n");
        StringBuilder headers = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].isEmpty()) {
                //header sonundaki boş satır
                break;
            }
            headers.append(lines[i]).append("\n");
        }
        return headers.toString();
    }

    /**
     * HTTP mesajının body'sini (varsa) al
     * Body, header'ın ardından gelir.
     *
     * @param message
     * @return
     */
    public static String getBodyBlock(String message) {
        Objects.requireNonNull(message);

        String[] lines = message.split("\n");
        StringBuilder body = new StringBuilder();

        boolean isHeader = true;

        for (int i = 0; i < lines.length; i++) {
            if (isHeader && lines[i].isEmpty()) {
                isHeader = false;
                continue;
            }
            if (!isHeader) {
                body.append(lines[i]).append("\n");
            }
        }
        return body.toString();
    }

    /**
     * @param message
     * @param name    header adı, ör "Content-Type"
     * @return {@code null} eğer header yok ise olur yoksa string ifadedir
     */
    public static String getHeaderValue(String message, String name) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(message);

        String[] lines = message.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String h = lines[i];
            if (h.isEmpty()) {
                break;
            }
            if (h.toLowerCase().startsWith(name.toLowerCase())) {
                int splitPoint = h.indexOf(':');
                return h.substring(splitPoint + 1, h.length()).trim();
            }
        }
        return null;
    }


    public static String getCookie(String message, String cookieName) {

        String cookies = getHeaderValue(message, "Set-Cookie");

        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies.split(";"))
                .filter(s -> s.trim().startsWith(cookieName + "="))
                .findAny()
                .orElse(null);

    }

    public static String getSessionCookie(String message) {
        return getCookie(message, "JSESSIONID");
    }
}