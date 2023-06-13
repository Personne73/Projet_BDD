# ordre de création : 
# 1) Compte / Ingrédient / Véhicule / Format / Livreur
# 2) Client / Pizza
# 3) Contenir
# 4) Commande

INSERT INTO
	COMPTE (Solde, NbPizzaAchete)
VALUES
	(50.6, 16),
	(28, 2),
    (38, 54);

INSERT INTO
	INGREDIENT (Nom)
VALUES
	('Sauce tomate'),        #0
	('Mozza'),               #1
    ('Olive'),               #2
    ('Anchoi'),              #3
    ('Saumon'),              #4
    ('Champignon'),          #5
    ('Mélange de fromages'); #6
    
INSERT INTO
	VEHICULE (Nom)
VALUES
	('RENAULT CAMPUS'),
	('T MAX'),
    ('Byciclette');
    
INSERT INTO
	FORMAT (NomFormat, Multiple)
VALUES
	('Naine', 0.77777),
	('Humaine', 1),
    ('ogresse', 1.3333);
    
INSERT INTO
	LIVREUR (Nom)
VALUES
	('Bob'),
	('Henri'),
    ('Joël');