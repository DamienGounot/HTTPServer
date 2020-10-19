import java.net.*;
import java.io.*;

class Client {
    public static void main(String argv[]) throws Exception
    {
        // On compose le numero de telephone de la personne a contacte 
        Socket socket = new Socket("localhost", 1111);

         // On ouvre un tube pour lire ce que nous envoie le serveur
        InputStreamReader inputStream = new InputStreamReader( socket.getInputStream() );
        BufferedReader socketInput = new BufferedReader(inputStream ); 

        //On ouvre un tube pour envoyer un message
        PrintStream socketOutput = new PrintStream( socket.getOutputStream() );

        //On ouvre un tube pour les I/O user
        BufferedReader inputUser = new BufferedReader( new InputStreamReader(System.in));
	    PrintStream outputUser = new PrintStream(System.out);
        String data= null;
        String requete = null;
        do
        {
            requete = inputUser.readLine(); // lecture clavier


            if (requete.equals("quit")) 
            {
                socket.close();
                System.exit(0);
            }
        }while(!requete.isEmpty());

        socketOutput.println(requete); // envoi de la requete
        data = socketInput.readLine(); // reponse de la requete
        outputUser.println("Server: " +  data); // affichage reponse
    }
}
