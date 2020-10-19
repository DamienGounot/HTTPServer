import java.net.*;
import java.nio.file.Files;
import java.util.StringTokenizer;
import java.io.*;

class Server {
    public static void main(String argv[]) throws Exception {

        // On installe le combine sur le numero de telephone
        ServerSocket serversocket = new ServerSocket(1111);
        // On attend les appels entrants
        Socket socket = serversocket.accept();

        // On ouvre un tube pour envoyer un message
        PrintStream socketOuput = new PrintStream(socket.getOutputStream());
        

        // On ouvre un tube pour lire ce que nous envoie le client
        InputStreamReader inputStream = new InputStreamReader(socket.getInputStream());
        BufferedReader socketInput = new BufferedReader(inputStream);

        String data = null;
        String clientRequest = "";
        String[] methode = new String[3];
        String host = "";
        String serverAnswer = "";
        long fileSize;
        String filetype = "";
        int code = 0;
        boolean erreur = false;
        byte[] data1 = null;
        FileInputStream fileIn = null;

        do { // construction de la requete client
            data = socketInput.readLine();
             System.out.println(" Data <"+data+">");

            clientRequest += data + "\r\n";

        } while (!data.isEmpty());

        System.out.println(" Requete <" + clientRequest + ">");
        StringTokenizer st = new StringTokenizer(clientRequest, " "); // Parsing de la requete
        String token = "";

        try {
            
            for (int i = 0; i < 3; i++) { // On recupère la methode (on sait que c'est en premier)
            token = st.nextToken();
            methode[i] = token;
            System.out.println("Methode" + i + ": " + "<"+token+">");
            }
        } catch (Exception e) {
            System.out.println(methode[2] +" 400 Bad Request");
            serverAnswer += methode[2] + " 400 Bad Request";
            erreur = true;
        }

        if (!(methode[2].contains("HTTP/1.0")) && !(methode[2].contains("HTTP/1.1")) ) { // si version non supportée
            System.out.println(methode[2] +" 400 Bad Request");
            serverAnswer += methode[2] + " 400 Bad Request";
            erreur = true;                
        }        

        if(methode[2].contains("HTTP/1.1")){ // si version 1.1 de HTTP on verifie que le champ Host existe
            try {
                host = st.nextToken();
                System.out.println("Host: "+"<"+host+">");
                erreur = false;        
            } catch (Exception e) {
                System.out.println(methode[2] +" 400 Bad Request");
                serverAnswer += methode[2] + " 400 Bad Request";                
                erreur = true; // si on trouve pas de champs apres, la requete est forcement bad
            }    
        }

            if(!erreur) // si requete valide, on la traite
            {
    
                File file = new File("." + methode[1]);
                if (!Files.exists(file.toPath())) {
                    System.out.println(methode[2] +" 404 Not Found");
                    serverAnswer += methode[2] + " 404 Not Found";
                    code = 404;
                } else {
                    code = 200;
                    fileSize = Files.size(file.toPath());
                    System.out.println("Taille du fichier: " + fileSize);
                    filetype = Files.probeContentType(file.toPath());
                    System.out.println("Type du fichier: " + filetype);

                    // envoit de la ressource 
    
                    int length = 0;
                    fileIn = new FileInputStream("."+methode[1]);
                    byte[] bytes = new byte[1024];
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
        
                    while ((length = fileIn.read(bytes)) != -1 ){
                        bos.write(bytes, 0, length);
                    }
                    bos.flush();
                    bos.close();
                    data1 = bos.toByteArray();
    
                    // generation de l'en tete http
                    
                     serverAnswer = methode[2] +" "+ code +" OK\r\n" + "content-type:"+filetype+";charset=utf-8\r\n" + "content-length:"
                    + data1.length + "\r\n\r\n";
    
                }
                
            }
            socketOuput.print(serverAnswer); // envoit de la reponse

            if(code == 200) // si Ok on envoit la ressource
            {
                socketOuput.write(data1, 0, data1.length);
                socketOuput.flush();
                fileIn.close();
            }
            socketOuput.close();
            
    }
}
