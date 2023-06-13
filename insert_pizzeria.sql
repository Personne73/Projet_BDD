# ordre de création : 
# 1) Compte / Ingrédient / Véhicule / Format / Livreur
# 2) Client / Pizza
# 3) Contenir
# 4) Commande

insert into 
	Compte (Solde, NbPizzaAchete) 
values 
	(50.6, 16),
	(28, 2),
    (38, 54);

insert into 
	ingredient (Nom) 
values 
	('Sauce tomate'),        #0
	('Mozza'),               #1
    ('Olive'),               #2
    ('Anchoi'),              #3
    ('Saumon'),              #4
    ('Champignon'),          #5
    ('Mélange de fromages'); #6
    
insert into 
	vehicule (Nom) 
values 
	('RENAULT CAMPUS'),
	('T MAX'),
    ('Bicyclette');
    
insert into 
	format (NomFormat, Multiple) 
values 
	('Naine', 0.77777),
	('Humaine', 1),
    ('ogresse', 1.3333);
    
insert into 
	Livreur (Nom) 
values 
	('Bob'),
	('Henri'),
    ('Joël');