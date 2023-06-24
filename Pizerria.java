import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Pizerria
{

    public static String url = "jdbc:mysql://localhost:3306/pizzeria";
    public static String utilisateur = "root";
    public static String motDePasse = "#voidAdmin25";

    public static Connection cx;

    public static Statement etat;

    private static void passageEnModeClient() {

    }

    private static void affichageStatistiques() throws Exception{
        // Quels sont les véhicules n’ayant jamais servi ?
        String sql1 = "SELECT * FROM VEHICULE WHERE Id_Vehicule NOT IN (SELECT DISTINCT Id_Vehicule FROM COMMANDE)";
        Statement statement1 = cx.createStatement();
        ResultSet resultSet1 = statement1.executeQuery(sql1);
        while (resultSet1.next()) {
            System.out.println("Véhicule n'ayant jamais servi : " + resultSet1.getString("Nom"));
        }

        // Calcul du nombre de commandes par client
        String sql2 = "SELECT Id_Client, COUNT(*) as nb_commandes FROM COMMANDE GROUP BY Id_Client";
        Statement statement2 = cx.createStatement();
        ResultSet resultSet2 = statement2.executeQuery(sql2);
        while (resultSet2.next()) {
            System.out.println("Client ID: " + resultSet2.getInt("Id_Client") + ", nombre de commandes: " + resultSet2.getInt("nb_commandes"));
        }

        // Calcul de la moyenne des commandes
        String sql3 = "SELECT AVG(Prix_total) as moyenne FROM COMMANDE";
        Statement statement3 = cx.createStatement();
        ResultSet resultSet3 = statement3.executeQuery(sql3);
        if (resultSet3.next()) {
            System.out.println("La moyenne des commandes est : " + resultSet3.getDouble("moyenne"));
        }

        // Extraction des clients ayant commandé plus que la moyenne
        String sql4 = "SELECT Id_Client FROM COMMANDE GROUP BY Id_Client HAVING AVG(Prix_total) > (SELECT AVG(Prix_total) FROM COMMANDE)";
        Statement statement4 = cx.createStatement();
        ResultSet resultSet4 = statement4.executeQuery(sql4);
        while (resultSet4.next()) {
            System.out.println("Client ID ayant commandé plus que la moyenne : " + resultSet4.getInt("Id_Client"));
        }
    }


    private static void affichageFicheLivraison() throws Exception {

        PreparedStatement preparedStatement = cx.prepareStatement( "SELECT TempsLivraison FROM Commande;" );
        ArrayList<Integer> listTempsLivraison = new ArrayList<Integer>();
        ResultSet result = preparedStatement.executeQuery();
        while(result.next()) {

            listTempsLivraison.add(result.getInt("TempsLivraison"));
        }

        PreparedStatement pizzaEtPrix = cx.prepareStatement( "SELECT L.Nom, TV.Nom, C.Nom, dateCommande, P.Nom, P.Prix FROM Commande as Co"
                + " inner join Livreur as L on Co.Id_Livreur = L.Id_Livreur"
                + " inner join Client as C on Co.Id_Client = C.Id_Client"
                + " inner join Pizza as P on Co.Id_Pizza = P.Id_Pizza"
                + " inner join Vehicule as V on Co.Id_Vehicule = V.Id_Vehicule"
                + " inner join TypeVehicule as TV on V.Id_TypeVehicule = TV.Id_TypeVehicule;");

        result = pizzaEtPrix.executeQuery();
        int i = 0;
        String retard = "A l'heure";
        while(result.next()) {

            if(listTempsLivraison.get(i) > 30)
            {
                retard = "En retard";
            }

            System.out.println(
                    "Nom du livreur : "
                            + result.getString("L.Nom")
                            + "\nType de véhicule : "
                            + result.getString("TV.Nom")
                            + "\nNom du client : "
                            +  result.getString("C.Nom")
                            + "\nDate de la commande : "
                            + result.getString("dateCommande")
                            + "\nPizza : "
                            + result.getString("P.Nom")
                            + "\nPrix de base de la Pizza : "
                            + result.getString("P.Prix") + " €"
                            + "\nArrivée à destination : "
                            + retard
                            + "\n"
            );

            retard = "A l'heure";
        }
        System.out.println("");
        System.out.println("");
        System.out.println("");
    }

    private static void affichageMenu() throws Exception {

        PreparedStatement pizzaEtPrix = cx.prepareStatement( "SELECT Nom, Prix FROM Pizza;" );
        PreparedStatement ingredientsPizza = cx.prepareStatement( "select Ingredient.Nom from"
                + " pizza inner join contenir on pizza.Id_Pizza = contenir.Id_Pizza"
                + " inner join ingredient on contenir.Id_Ingredient = ingredient.Id_Ingredient"
                + " where pizza.Nom = ?;" );



        ResultSet result = pizzaEtPrix.executeQuery();
        String nom = new String();
        String prix = new String();
        System.out.printf("------------------------------------------------------------------------------------------------------------------------------------------------\n");
        System.out.printf("||%60s%20s%60s||%n", "", "", "");
        System.out.printf("||%60s%20s%60s||%n", "", "", "");
        System.out.printf("||%60s%s%60s||%n", "", "CARTE DE LA PIZZERIA", "");
        System.out.printf("||%60s%20s%60s||%n", "", "", "");
        System.out.printf("||%60s%20s%60s||%n", "", "", "");
        System.out.println("||-----------------------------------||-----------------------------------||------------------------------------------------------------------||");
        System.out.printf("||%20s%15s||%20s%15s||%30s%36s||%n", "" , "" , "" , "", "", "");
        System.out.printf("||%20s%15s||%20s%15s||%30s%36s||%n", "Nom Pizza", "", "Prix", "", "Ingredients", "");
        //System.out.println("|| \t \t \t || \t \t || ");
        System.out.printf("||%20s%15s||%20s%15s||%30s%36s||%n", "" , "" , "" , "", "", "");
        System.out.println("||-----------------------------------||-----------------------------------||--------------------------------------------------------------------");
        System.out.printf("||%20s%15s||%20s%15s||%30s%30s%n", "" , "" , "" , "", "", "");
        System.out.printf("||%20s%15s||%20s%15s||%30s%30s%n", "" , "" , "" , "", "", "");
        while(result.next()) {

            nom = result.getString("Nom");
            prix = result.getString("Prix");
            System.out.printf("||%20s%15s||%20s%15s||", nom , "" , prix , "");

            ingredientsPizza.setString( 1, nom);
            System.out.printf("%10s", "");
            ResultSet result2 = ingredientsPizza.executeQuery();
            while(result2.next()) {
                if(result2.isLast()) {
                    System.out.print(result2.getString("Nom"));
                } else {
                    System.out.print(result2.getString("Nom") + " , ");
                }

            }

            System.out.println("");
        }

        System.out.printf("||%20s%15s||%20s%15s||%30s%36s%n", "" , "" , "" , "", "", "");
        System.out.println("||-----------------------------------||-----------------------------------||--------------------------------------------------------------------");
        System.out.println("");
        System.out.println("");
        System.out.println("");
    }

    public static void main(String[] args) throws SQLException, Exception
    {
        try {

            System.out.println("Initialisation de la connexion");
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

        }
        catch (ClassNotFoundException ex){

            System.out.println ("Problème au chargement " + ex.toString());
        }
        try {

            cx = DriverManager.getConnection(url, utilisateur, motDePasse);

            etat = cx.createStatement ();

            /* EXEMPLE REQUETE
            PreparedStatement preparedStatement = cx.prepareStatement( "SELECT Id_Ingredient, Nom FROM Ingredient WHERE Nom = ?;" );
            while(true) {

            	System.out.println("Quel ingrédient cherchez vous ?");
                Scanner scanner = new Scanner(System.in);
                String s = scanner.nextLine();
                preparedStatement.setString( 1, s );
                ResultSet result = preparedStatement.executeQuery();
                while(result.next()) {
                	System.out.println(result.getString("Nom"));
                }
            }
            */

            while (true)
            {

                System.out.println("Bienvenue sur l'application de gestion de notre magnifique pizzeria !");
                System.out.println("Voici les actions possible : ");
                System.out.println("1 - Afficher le menu");
                System.out.println("2 - Afficher la fiche de livraison");
                System.out.println("3 - Afficher des statistiques");
                System.out.println("4 - Passer en mode client");
                System.out.print("Choisissez une action à réaliser : ");
                Scanner scanner = new Scanner(System.in);
                String s = scanner.nextLine();
                System.out.println("");
                System.out.println("");
                System.out.println("");
                System.out.println("");
                System.out.println("");

                if (s.equals("1"))
                {
                    affichageMenu();
                }
                else if (s.equals("2"))
                {
                    affichageFicheLivraison();
                }
                else if (s.equals("3"))
                {
                    affichageStatistiques();
                }
                else if (s.equals("4"))
                {
                    passageEnModeClient();
                }
                else
                {
                    System.out.println("CHOIX D'ACTION INCORRECT RESSAYER !");
                    System.out.println();
                }
            }

        }
        catch(SQLException ex)
        {  System.err.println("Erreur : " + ex); }
    }

}
