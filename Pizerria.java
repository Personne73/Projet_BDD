import java.sql.*;
import java.util.Scanner;

public class Pizerria
{
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
        	
            String url = "jdbc:mysql://localhost:3306/cours";
            String utilisateur = "root";
            String motDePasse = "root";

            Connection cx = DriverManager.getConnection(url, utilisateur, motDePasse);

            Statement etat = cx.createStatement ();
            
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
            
            
            PreparedStatement pizzaEtPrix = cx.prepareStatement( "SELECT Nom, Prix FROM Pizza;" );
            PreparedStatement ingredientsPizza = cx.prepareStatement( "select Ingredient.Nom from"
            		+ " pizza inner join contenir on pizza.Id_Pizza = contenir.Id_Pizza"
            		+ " inner join ingredient on contenir.Id_Ingredient = ingredient.Id_Ingredient"
            		+ " where pizza.Nom = ?;" );
            
            
            
            ResultSet result = pizzaEtPrix.executeQuery();
            String nom = new String();
            String prix = new String();
            System.out.println("||       Nom Pizza        ||        Prix        ||                Ingredients                 ||");
            System.out.println("||                        ||                    ||                                            ||");
            while(result.next()) {
                
                nom = result.getString("Nom");
                prix = result.getString("Prix");
                System.out.print("||      " + nom + " " + prix + " ");
                
                ingredientsPizza.setString( 1, nom);
                ResultSet result2 = ingredientsPizza.executeQuery();
                while(result2.next()) {
                    System.out.print(result2.getString("Nom") + " ");
                }
                
                System.out.println("");
            }
                
                
                
            /*
            ingredientsPizza.setString( 1, result.getString("Nom"));
            ResultSet result2 = ingredientsPizza.executeQuery();
            while(result.next()) {
                System.out.print(result.getString("Nom") + " ");
            }
            
            System.out.println("fin");
            */
   
        }
        catch(SQLException ex)
        {  System.err.println("Erreur : "+ex); }
    }
}
