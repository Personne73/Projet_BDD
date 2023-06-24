# ordre de création : 
# 1) Compte / Ingrédient / Véhicule / Format / Livreur
# 2) Client / Pizza
# 3) Contenir
# 4) Commande

INSERT INTO
	Compte (Solde, NbPizzaAchete)
VALUES
	(50.6, 16),
	(28, 2),
    (38, 54);

INSERT INTO
	Ingredient (Nom)
VALUES
	('Sauce tomate'),        #1
	('Mozza'),               #2
    ('Olive'),               #3
    ('Anchoi'),              #4
    ('Saumon'),              #5
    ('Champignon'),          #6
    ('Mélange de fromages'); #7

INSERT INTO
	TypeVehicule (Nom)
VALUES
	('Moto'),
	('Scooter'),
    ('Voiture'),
    ('Byciclette');

INSERT INTO
	Vehicule (Id_TypeVehicule, Nom)
VALUES
	(3, 'RENAULT CAMPUS'),
	(1, 'T MAX'),
    (4, 'Bicyclette');

INSERT INTO
	Format (NomFormat, Multiple)
VALUES
	('Naine', 0.77777),
	('Humaine', 1),
    ('Ogresse', 1.3333);

INSERT INTO
	Livreur (Nom)
VALUES
	('Bob'),
	('Henri'),
    ('Joël');