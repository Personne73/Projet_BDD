CREATE PROCEDURE CommanderPizza(IN Id_Client INT, IN Id_Pizza INT, IN Id_Livreur INT, IN Id_Vehicule INT, IN Id_Format INT)
BEGIN
    DECLARE PrixPizza DECIMAL(15,2);
    DECLARE SoldeClient DECIMAL(15,2);
    SELECT PRIX INTO PrixPizza FROM PIZZA WHERE Id_Pizza = Id_Pizza;
    SELECT Solde INTO SoldeClient FROM COMPTE WHERE Id_Compte = (SELECT Id_Compte FROM CLIENT WHERE Id_Client = Id_Client);
    IF SoldeClient >= PrixPizza THEN
        INSERT INTO COMMANDE(Id_Client, Id_Pizza, Id_Livreur, Id_Vehicule, Id_Format, Prix_total)
        VALUES (Id_Client, Id_Pizza, Id_Livreur, Id_Vehicule, Id_Format, PrixPizza);
        UPDATE COMPTE SET Solde = Solde - PrixPizza WHERE Id_Compte = (SELECT Id_Compte FROM CLIENT WHERE Id_Client = Id_Client);
    ELSE
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Solde insuffisant pour cette commande';
    END IF;
END;

CREATE TRIGGER AjoutCommande
    AFTER INSERT ON COMMANDE
    FOR EACH ROW
BEGIN
    UPDATE COMPTE SET NbPizzaAchete = NbPizzaAchete + 1 WHERE Id_Compte = (SELECT Id_Compte FROM CLIENT WHERE Id_Client = NEW.Id_Client);
END;

CREATE TRIGGER SupprCommande
    AFTER DELETE ON COMMANDE
    FOR EACH ROW
BEGIN
    UPDATE COMPTE SET NbPizzaAchete = NbPizzaAchete - 1 WHERE Id_Compte = (SELECT Id_Compte FROM CLIENT WHERE Id_Client = OLD.Id_Client);
END;

CREATE PROCEDURE AjouterArgent(IN Id_Client INT, IN Montant DECIMAL(15,2))
BEGIN
    UPDATE COMPTE SET Solde = Solde + Montant WHERE Id_Compte = (SELECT Id_Compte FROM CLIENT WHERE Id_Client = Id_Client);
END;


