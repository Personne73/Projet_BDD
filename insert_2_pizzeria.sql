INSERT INTO
    Client (Nom, Id_Compte)
VALUES
	('Pierre', 1),
	('Yanis', 2),
    ('Maxime', 3);

INSERT INTO
	Pizza (Nom, prix)
VALUES
	('4 fromages', 9),
	('Margueritta', 10),
    ('Classique', 7);

INSERT INTO
	Contenir (Id_Pizza, Id_Ingredient)
VALUES
	(1, 1),
    (1, 3),
    (1, 7),
	(2, 1),
    (2, 2),
    (2, 3),
    (3, 1),
    (3, 2),
    (3, 4),
    (3, 6);