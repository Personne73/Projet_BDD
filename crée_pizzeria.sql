CREATE TABLE Pizza(
   Id_Pizza int auto_increment,
   NOM VARCHAR(50),
   PRIX DECIMAL(15,2),
   PRIMARY KEY(Id_Pizza)
);

CREATE TABLE INGREDIENT(
   Id_Ingredient int auto_increment,
   Nom VARCHAR(50),
   PRIMARY KEY(Id_Ingredient)
);

CREATE TABLE Livreur(
   Id_Livreur int auto_increment,
   Nom VARCHAR(50),
   PRIMARY KEY(Id_Livreur)
);

CREATE TABLE Vehicule(
   Id_Vehicule int auto_increment,
   Nom VARCHAR(50),
   PRIMARY KEY(Id_Vehicule)
);

CREATE TABLE Compte(
   Id_Compte int auto_increment,
   Solde DECIMAL(15,2),
   NbPizzaAchete INT,
   PRIMARY KEY(Id_Compte)
);

CREATE TABLE Format(
   Id_Format int auto_increment,
   NomFormat VARCHAR(50),
   Multiple DECIMAL(15,2),
   PRIMARY KEY(Id_Format)
);

CREATE TABLE Client(
   Id_Client int auto_increment,
   Nom VARCHAR(50),
   Id_Compte INT NOT NULL,
   PRIMARY KEY(Id_Client),
   UNIQUE(Id_Compte),
   FOREIGN KEY(Id_Compte) REFERENCES Compte(Id_Compte)
);

CREATE TABLE Commande(
   Id_Commande int auto_increment,
   TempsLivraison INT,
   Prix_total DECIMAL(15,2),
   Id_Format INT NOT NULL,
   Id_Client INT NOT NULL,
   Id_Livreur INT NOT NULL,
   Id_Vehicule INT NOT NULL,
   Id_Pizza INT NOT NULL,
   PRIMARY KEY(Id_Commande),
   FOREIGN KEY(Id_Format) REFERENCES Format(Id_Format),
   FOREIGN KEY(Id_Client) REFERENCES Client(Id_Client),
   FOREIGN KEY(Id_Livreur) REFERENCES Livreur(Id_Livreur),
   FOREIGN KEY(Id_Vehicule) REFERENCES Vehicule(Id_Vehicule),
   FOREIGN KEY(Id_Pizza) REFERENCES Pizza(Id_Pizza)
);

CREATE TABLE Contenir(
   Id_Pizza INT,
   Id_Ingredient INT,
   PRIMARY KEY(Id_Pizza, Id_Ingredient),
   FOREIGN KEY(Id_Pizza) REFERENCES Pizza(Id_Pizza),
   FOREIGN KEY(Id_Ingredient) REFERENCES INGREDIENT(Id_Ingredient)
);