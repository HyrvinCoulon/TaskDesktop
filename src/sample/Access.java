package sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.hc.client5.http.fluent.Form;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.HttpResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

final class Access {

    private static ObjectInputStream ois;
    private static ObjectOutputStream oos;


    public static String ActionId(String[] s){
        if(s.length >= 2)
           return s[1];
        else
            return null;
    }

    public static boolean isInt(String chaine){
        boolean valeur = true;
        char[] tab = chaine.toCharArray();

        for(char carac : tab){
            if(!Character.isDigit(carac) && valeur){ valeur = false; }
        }

        return valeur;
    }

    public static void save(ArrayList<ArrayList<String>> t){
        File f = new File("Save.txt");

        try{
            FileOutputStream fou = new FileOutputStream(f);
            oos = new ObjectOutputStream(fou);

            oos.writeObject(t);
            oos.flush();
            oos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ArrayList<String>> retrieve(){
        File f = new File("Save.txt");
        ArrayList<ArrayList<String>> t;
        try{
            FileInputStream fin = new FileInputStream(f);
            ois = new ObjectInputStream(fin);

            t = (ArrayList<ArrayList<String>>) ois.readObject();
            ois.close();

        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
        return t;
    }

    public static void saveString(ArrayList<String> t){
        File f = new File("SaveS.txt");

        try{
            FileOutputStream fou = new FileOutputStream(f);
            oos = new ObjectOutputStream(fou);

            oos.writeObject(t);
            oos.flush();
            oos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> retrieveString(){
        File f = new File("SaveS.txt");
        ArrayList<String> t;
        try{
            FileInputStream fin = new FileInputStream(f);
            ois = new ObjectInputStream(fin);

            t = (ArrayList<String>) ois.readObject();
            ois.close();

        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
        return t;
    }

    public static HashMap<String, ArrayList<Tasks>> retrieveH() throws FileNotFoundException {
        File f = new File("SaveH.txt");

        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(f))) {
            return (HashMap<String, ArrayList<Tasks>>) is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveString(HashMap<String, ArrayList<Tasks>> t) throws IOException {
        File f = new File("SaveH.txt");

        try{
            FileOutputStream fou = new FileOutputStream(f);
            oos = new ObjectOutputStream(fou);

            oos.writeObject(t);

            oos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        /*try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(f))){
            os.writeObject(t);
        }*/
    }

    static HashMap<String, ArrayList<String>> loadMap() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("Save.json")) {
            assert in != null;
            return mapper.readValue(in, HashMap.class);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void saveJ(HashMap<String, ArrayList<String>> map) {
        try (PrintWriter out = new PrintWriter("Save.json")) {
            out.println(toString(map));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toString(Object obj) {
        try (StringWriter w = new StringWriter();) {
            new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true).writeValue(w, obj);
            return w.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*public static void Feedback(String a, String m){

         // Recipient's email ID needs to be mentioned.
         String to = "bangolamoukogho@gmail.com";

         // Sender's email ID needs to be mentioned
         //String from = "bomyron891@gmail.com";

         // Assuming you are sending email from localhost
         String host = "smtp.example.com";


         // Get system properties
         Properties properties = System.getProperties();

         //String host = "smtp.gmail.com";
         Session session = Session.getInstance(properties, null);

         // Setup mail server
         properties.put("mail.smtp.host", host);
         //properties.put("mail.smtp.port", "465");

         try {
             // Create a default MimeMessage object.
             MimeMessage message = new MimeMessage(session);

             // Set From: header field of the header.
             message.setFrom(new InternetAddress(a));

             // Set To: header field of the header.
             message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

             // Set Subject: header field
             message.setSubject("Feedback : Desktop App");

             // Now set the actual message
             message.setText(m);

             // Send message
             Transport.send(message);
             System.out.println("Sent message successfully....");
         } catch (MessagingException mex) {
             mex.printStackTrace();
         }

    }*/

    static void Feedback(String name, String email, String message) throws IOException {
        URL url = new URL("https://hyrvin.pythonanywhere.com/gateway_user/feedback/create/");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        //con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String jsonInputString = "{\"username\": \""+ name +"\", \"email\": \""+ email +"\", \"message\" : \""+ message + "\", \"type_feedback\" : \"DesktopTask\" }";

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }

       /* HttpResponse response = Request.post("https://hyrvin.pythonanywhere.com/gateway_user/feedback/create/").bodyForm(
                Form.form().add("username", name).add("email", email)
                        .add("message", message).add("typpe_feedback", "DesktopTask")
                        .build())
                .execute().returnResponse();*/
    }


    public static String word(HashMap<String, ArrayList<Tasks>> lt){
        List<String> keyList = new ArrayList<>(lt.keySet());

        int size = keyList.size();
        int randIdx = new Random().nextInt(size);
        String randomKey = keyList.get(randIdx);
        Tasks t;
        do {
            int s = lt.get(randomKey).size();
            randIdx = new Random().nextInt(s);
            t = lt.get(randomKey).get(randIdx);
            System.out.println(t.isDone());
        }while(t.isDone());

        return t.getTitle();
    }
}
