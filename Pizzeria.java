import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Pizzeria
{
	
    public static String url = "jdbc:mysql://localhost:3306/pizzeria";
    public static String utilisateur = "root";
    public static String motDePasse = "#voidAdmin25";

    public static Connection cx;

    public static Statement etat;
    
	private static void setPizzasAchetees(int id_compte) throws Exception {
		
		int calcul = getNbPizzaAchetees(id_compte) + 1;
        PreparedStatement preparedStatement1 = cx.prepareStatement("Update Compte set NbPizzaAchete = ? + 1 WHERE Id_Compte = ?");
        preparedStatement1.setInt(1, calcul);
        preparedStatement1.setInt(2, id_compte);
        preparedStatement1.executeUpdate();
	}
    
	private static int getNbPizzaAchetees(int id_compte) throws Exception {
		
        PreparedStatement preparedStatement1 = cx.prepareStatement("SELECT NbPizzaAchete FROM Compte WHERE Id_Compte = ?");
        preparedStatement1.setInt(1, id_compte);
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        resultSet1.next();
        int nbPizzas = resultSet1.getInt("NbPizzaAchete");
        return nbPizzas;
	}
    
    private static void voirLesInfosGenerales(int id_compte) throws Exception {
		
        PreparedStatement preparedStatement1 = cx.prepareStatement("SELECT * FROM Client as C INNER JOIN Compte as Co on C.Id_Compte = Co.Id_Compte WHERE C.Id_Compte = ?");
        preparedStatement1.setInt(1, id_compte);
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        resultSet1.next();
        System.out.println("");
        System.out.println("");
        System.out.println("Id client : " + resultSet1.getInt("C.Id_Client"));
        System.out.println("Nom client : " + resultSet1.getString("C.Nom"));
        System.out.println("Id compte : " + resultSet1.getInt("Co.Id_Compte"));
        System.out.println("Solde compte : " + resultSet1.getInt("Co.Solde"));
        System.out.println("Nombre de pizzas achetées : " + resultSet1.getInt("Co.NbPizzaAchete"));
	}
    
    private static float getSolde(int id_compte) throws Exception
    {
        PreparedStatement preparedStatement1 = cx.prepareStatement("SELECT Solde FROM Compte WHERE Id_Compte = ?");
        preparedStatement1.setInt(1, id_compte);
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        resultSet1.next();
        float solde = resultSet1.getFloat("Solde");
        return solde;
    }
    
    private static void setSolde(int id_compte, float credit) throws Exception
    {
    	float calcul = getSolde(id_compte) - credit;
        PreparedStatement preparedStatement1 = cx.prepareStatement("Update Compte set Solde = ? WHERE Id_Compte = ?");
        preparedStatement1.setFloat(1, calcul);
        preparedStatement1.setInt(2, id_compte);
        preparedStatement1.executeUpdate();
    }

	private static void rechargerSolde(int id_compte) throws Exception {
		
		System.out.println("De combien voulez vous augmenter votre solde");
    	Scanner scanner = new Scanner(System.in);
        int debit = scanner.nextInt();
        float calcul = getSolde(id_compte) + debit;
        PreparedStatement preparedStatement1 = cx.prepareStatement("Update Compte set Solde = ? WHERE Id_Compte = ?");
        preparedStatement1.setFloat(1, calcul);
        preparedStatement1.setInt(2, id_compte);
        preparedStatement1.executeUpdate();
	}
	
	private static void effectuerCommande(int tempsDeLivraison, float prix_total, int id_format, int id_client,
			int id_livreur, int id_vehicule, int id_pizza, Date dateCommande, int id_compte) throws Exception {
		
		if(getSolde(id_compte) < prix_total)
		{
			System.out.println("Votre solde est insuffisant !!!");
			return;
		}
		
		if(getNbPizzaAchetees(id_compte) != 0 && getNbPizzaAchetees(id_compte)%10 == 0) {
			prix_total = 0;
		}
		
        PreparedStatement preparedStatement = cx.prepareStatement( "INSERT INTO Commande (TempsLivraison, Prix_Total, Id_Format, Id_Client, Id_Livreur, Id_Vehicule, Id_Pizza, dateCommande ) " 
        		+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)" );
        preparedStatement.setInt( 1, tempsDeLivraison);
        preparedStatement.setFloat( 2, prix_total);
        preparedStatement.setInt( 3, id_format);
        preparedStatement.setInt( 4, id_client);
        preparedStatement.setInt( 5, id_livreur);
        preparedStatement.setInt( 6, id_vehicule);
        preparedStatement.setInt( 7, id_pizza);
        preparedStatement.setDate( 8, dateCommande);
        preparedStatement.executeUpdate();
        
        System.out.println("");
        System.out.println("");
        System.out.println("Voici le résumé de votre commande : ");
        
        PreparedStatement preparedStatement1 = cx.prepareStatement("SELECT NomFormat FROM Format WHERE Id_Format = ?");
        preparedStatement1.setInt(1, id_format);
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        resultSet1.next();
        String nomFormat = resultSet1.getString("NomFormat");
        
        preparedStatement1 = cx.prepareStatement("SELECT Nom FROM Client WHERE Id_Client = ?");
        preparedStatement1.setInt(1, id_client);
        resultSet1 = preparedStatement1.executeQuery();
        resultSet1.next();
        String nomClient = resultSet1.getString("Nom");
        
        preparedStatement1 = cx.prepareStatement("SELECT Nom FROM Livreur WHERE Id_Livreur = ?");
        preparedStatement1.setInt(1, id_livreur);
        resultSet1 = preparedStatement1.executeQuery();
        resultSet1.next();
        String nomLivreur = resultSet1.getString("Nom");
        
        preparedStatement1 = cx.prepareStatement("SELECT Nom FROM Pizza WHERE Id_Pizza = ?");
        preparedStatement1.setInt(1, id_pizza);
        resultSet1 = preparedStatement1.executeQuery();
        resultSet1.next();
        String nomPizza = resultSet1.getString("Nom");
        
        preparedStatement1 = cx.prepareStatement("SELECT Nom FROM Vehicule WHERE Id_Vehicule = ?");
        preparedStatement1.setInt(1, id_vehicule);
        resultSet1 = preparedStatement1.executeQuery();
        resultSet1.next();
        String nomVehicule = resultSet1.getString("Nom");
        
        System.out.println("Nom du client : " + nomClient);
        System.out.println("Nom du livreur : " + nomLivreur);
        System.out.println("Nom de la pizza : " + nomPizza);
        System.out.println("Format de la pizza : " + nomFormat);
        System.out.println("Prix : " + prix_total);
        System.out.println("Vehicule utilisé : " + nomVehicule);
        System.out.println("Temps de livraison : " + tempsDeLivraison);
        System.out.println("Date de la commande : " + dateCommande);
        
        setPizzasAchetees(id_compte);
        setSolde(id_compte, prix_total);
	}

	private static int vehiculeAleatoire() throws Exception {
		
		PreparedStatement preparedStatement = cx.prepareStatement( "SELECT count(*) FROM Vehicule");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int nombreDeVehicules = resultSet.getInt(1);
        int id_vehicule = 1 + (int)(Math.random() * ((nombreDeVehicules - 1) + 1));
        return id_vehicule;
	}
	
	private static int livreurAleatoire() throws Exception {
		
		PreparedStatement preparedStatement = cx.prepareStatement( "SELECT count(*) FROM Livreur");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int nombreDeLivreur = resultSet.getInt(1);
        int id_livreur = 1 + (int)(Math.random() * ((nombreDeLivreur - 1) + 1));
        return id_livreur;
	}
	
	private static int idClientDeIdCompte(int id_Compte) throws Exception {
		
    	PreparedStatement preparedStatement = cx.prepareStatement( "SELECT Id_Client FROM Client WHERE Id_Compte = ?");
        preparedStatement.setInt( 1, id_Compte);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int id_client = resultSet.getInt(1);
        return id_client;
	}
	
	private static float calculPrixPizzaFormat(int id_pizza, int taille) throws Exception {
		
    	PreparedStatement preparedStatement = cx.prepareStatement( "SELECT prix FROM Pizza WHERE Id_Pizza = ?");
        preparedStatement.setInt( 1, id_pizza);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int prix = resultSet.getInt(1);
        
    	PreparedStatement preparedStatement2 = cx.prepareStatement( "SELECT Multiple FROM Format WHERE Id_Format = ?");
        preparedStatement2.setInt( 1, taille);
        ResultSet resultSet2 = preparedStatement2.executeQuery();
        resultSet2.next();
        float multiple = resultSet2.getFloat(1);
        
        return prix * multiple;
	}
	
    private static int getIdPizza(String choix) throws Exception {
		
    	PreparedStatement preparedStatement = cx.prepareStatement( "SELECT Id_Pizza FROM Pizza WHERE Nom = ?");
        preparedStatement.setString( 1, choix);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        try
        {
        	resultSet.getInt("Id_Pizza");
        }
        catch (Exception e)
        {
        	return -1;
        }
        
        return resultSet.getInt("Id_Pizza");
	}
    
	private static Date getDateActuelle() throws Exception {
		
		PreparedStatement preparedStatement = cx.prepareStatement( "SELECT CURDATE()");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getDate(1);
	}

	private static void commanderUnePizza(int Id_Compte) throws Exception {
		
		System.out.println("Souhaitez vous afficher le menu pour faire votre choix de pizza ? (oui ou non)");
    	Scanner scanner = new Scanner(System.in);
        String choix = scanner.nextLine();
        if(choix.equals("oui"))
        {
        	affichageMenu();
        }
        else if (choix.equals("non"))
        {
        	System.out.println("Quelle pizza souhaitez vous commander : ");
        	String pizza = scanner.nextLine();
        	int id_pizza = getIdPizza(pizza);
        	if(id_pizza == -1) System.out.println("CHOIX DE PIZZA INCORRECT");
        	else
        	{
        		int tempsDeLivraison = 5 + (int)(Math.random() * ((45 - 5) + 1));
        		System.out.println("");
        		System.out.println("");
            	System.out.println("Voici les formats de pizza disponible : ");
            	System.out.println("1 - Naine");
            	System.out.println("2 - Humaine");
            	System.out.println("3 - Ogresse");
            	System.out.println("Choisissez un format : ");
            	int id_format = scanner.nextInt();
            	if(id_format < 1 || id_format > 3) System.out.println("CHOIX DE FORMAT INCORRECT");
            	else
            	{
            		float prix_total = calculPrixPizzaFormat(id_pizza, id_format);
            		int id_client = idClientDeIdCompte(Id_Compte);
            		int id_livreur = livreurAleatoire();
            		int id_vehicule = vehiculeAleatoire();
            		Date dateCommande = getDateActuelle();
            		effectuerCommande(tempsDeLivraison, prix_total, id_format, id_client, id_livreur, id_vehicule, id_pizza, dateCommande, Id_Compte);
            	}
        	}
        }
	}	

	private static int creerCompte() throws Exception {
    	
    	System.out.println("");
    	System.out.println("");
    	Scanner scanner = new Scanner(System.in);
        System.out.print("Veuillez renseigner votre nom : ");
        String nom = scanner.nextLine();
        System.out.print("Veuillez renseigner le solde que vous souhaitez mettre sur votre compte : ");
        int solde = scanner.nextInt();
        
        PreparedStatement preparedStatement = cx.prepareStatement( "INSERT INTO Compte (Solde, nbPizzaAchete) VALUES (?, 0)" );
        preparedStatement.setInt( 1, solde);
        preparedStatement.executeUpdate();
        
        PreparedStatement preparedStatement1 = cx.prepareStatement( "SELECT Id_Compte FROM Compte", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        resultSet1.last();
        int idCompte = resultSet1.getInt("Id_Compte");
        
        PreparedStatement preparedStatement2 = cx.prepareStatement( "INSERT INTO Client (Nom, Id_Compte) VALUES (?, ?)" );
        preparedStatement2.setString( 1, nom);
        preparedStatement2.setInt( 2, idCompte);
        preparedStatement2.executeUpdate();
        
        return idCompte;
    }
    
	private static void modeClient(String s) throws NumberFormatException, Exception {
		
		boolean continuer = true;
		while (continuer)
		{
			System.out.println("");
			System.out.println("");
			System.out.println("Bienvenue dans le mode client !");
			System.out.println("Choisissez les actions possibles : ");
			System.out.println("1 - Commander une pizza");
			System.out.println("2 - Recharger le solde");
			System.out.println("3 - Voir les informations générales");
			System.out.println("4 - Quitter le mode client");
			System.out.println("Choisissez une action à réaliser : ");
	    	Scanner scanner = new Scanner(System.in);
	        String choix = scanner.nextLine();
			if (choix.equals("1"))
			{
				commanderUnePizza(Integer.parseInt(s));
			}
			else if (choix.equals("2"))
			{
				rechargerSolde(Integer.parseInt(s));
			}
			else if (choix.equals("3"))
			{
				voirLesInfosGenerales(Integer.parseInt(s));
			}
			else if (choix.equals("4"))
			{
				continuer = false;
			}
		}
		
	}

	private static boolean compteExiste(String s) throws Exception {
    	
        PreparedStatement preparedStatement = cx.prepareStatement( "SELECT * FROM Compte WHERE Id_Compte = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        preparedStatement.setInt( 1, Integer.parseInt(s));
        ResultSet resultSet1 = preparedStatement.executeQuery();
        resultSet1.last();
        if(resultSet1.getRow() <= 0) {
        	return false;
        }
        else
        {
        	return true;
        }
    }
	
	private static void passageEnModeClient() throws Exception {
		
		boolean quitter = false;
		while (!quitter)
		{
	        System.out.println("Bienvenue cher client, si vous possédez un compte à la pizzeria choisissez 'se connecter'");
	        System.out.println("Sinon choisissez s'enregistrer pour créer un compte :");
	        System.out.println("1 - Se connecter");
	        System.out.println("2 - Crée un compte");
	        System.out.println("3 - Quitter le mode client");
	        Scanner scanner = new Scanner(System.in);
	        String s = scanner.nextLine();
	        if (s.equals("1")) 
	        {
	        	System.out.print("Veuillez entrer 'l'id de votre compte : ");
	        	s = scanner.nextLine();
	        	if (compteExiste(s))
	        	{
	        		modeClient(s);
	        		System.out.println("");
	        		System.out.println("");
	        	}
	        	else
	        	{
	        		System.out.println("L'id du compte que vous venez de taper est incorrect");
	        		System.out.println("");
	        		System.out.println("");
	        	}
	        }
	        else if (s.equals("2"))
	        {
	        	int idCompte = creerCompte();
	        	System.out.println("Voici l'id de votre compte fraichement crée : " + idCompte);
	        	System.out.println("");
	        	System.out.println("");
	        	System.out.println("");
	        }
	        else if (s.equals("3"))
	        {
	        	quitter = true;
	        }
		}
	
	}

	private static void affichageStatistiques() throws Exception {
		
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

        System.out.println("");
        System.out.println("");
        System.out.println("");
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
        	
        	System.out.println ("Problème au chargement"+ex.toString()); 
        }
        try {
        	
            cx = DriverManager.getConnection(url, utilisateur, motDePasse);

            etat = cx.createStatement ();
            
            /* EXEMPLE REQUETE
            PreparedStatement preparedStatement = cx.prepareStatement( "SELECT Id_Ingredient, Nom FROM Ingredient WHERE Nom = ?;" );
            while(true) {
            	
            	System.out.println("Quel ingrï¿½dient cherchez vous ?");
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
        {  System.err.println("Erreur : "+ex); }
    }

}
