-- =====================================================================
        -- AgriConnect : dump des tables de paramétrage (MySQL 8, utf8mb4)
--
        -- Principe i18n : la base ne stocke que des CODES stables (fr, tomato,
-- gratuit, basicStock...) qui sont aussi les clés utilisées par Angular.
        -- Les libellés traduits (fr / en / wo) restent dans les fichiers de
-- traduction Angular. Les colonnes `nom` ne servent que de libellé par
-- défaut en français (back-office, e-mails, PDF).
        --
        -- À exécuter une seule fois, sur des tables vides. Noms de tables et de
-- colonnes : convention Spring Boot (camelCase -> snake_case).
        -- =====================================================================

SET NAMES utf8mb4;

-- ---------------------------------------------------------------------
        -- 0. Nouvelles tables (inutile si ddl-auto=update : Hibernate les crée)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS langues_supportees (
        code        VARCHAR(5)  NOT NULL PRIMARY KEY,   -- fr, en, wo : même identifiant qu'Angular
nom         VARCHAR(50) NOT NULL,               -- libellé en français (back-office)
nom_natif   VARCHAR(50) NOT NULL,               -- affiché tel quel dans le sélecteur de langue
actif       TINYINT(1)  NOT NULL,
par_defaut  TINYINT(1)  NOT NULL,
ordre       INT         NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS parametres_profil (
        profil         VARCHAR(15) NOT NULL PRIMARY KEY,  -- AGRICULTEUR, CLIENT, INVESTISSEUR, PARTENAIRE, ETAT
langue_defaut  VARCHAR(5)  NOT NULL,
CONSTRAINT fk_parametres_profil_langue
FOREIGN KEY (langue_defaut) REFERENCES langues_supportees (code)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

START TRANSACTION;

-- ---------------------------------------------------------------------
        -- 1. Langues supportées (doivent correspondre aux fichiers de traduction Angular)
-- ---------------------------------------------------------------------
INSERT INTO langues_supportees (code, nom, nom_natif, actif, par_defaut, ordre) VALUES
    ('fr', 'Français', 'Français', 1, 1, 1),
            ('en', 'Anglais',  'English',  1, 0, 2),
            ('wo', 'Wolof',    'Wolof',    1, 0, 3);

            -- ---------------------------------------------------------------------
            -- 2. Langue par défaut de chaque acteur (modifiable sans toucher au code)
--    Tous à 'fr' pour l'instant : à ajuster par profil si besoin.
        -- ---------------------------------------------------------------------
INSERT INTO parametres_profil (profil, langue_defaut) VALUES
    ('AGRICULTEUR',  'fr'),
            ('CLIENT',       'fr'),
            ('INVESTISSEUR', 'fr'),
            ('PARTENAIRE',   'fr'),
            ('ETAT',         'fr');

            -- ---------------------------------------------------------------------
            -- 3. Régions (14) : `code` = identifiant utilisé par le frontend
-- ---------------------------------------------------------------------
INSERT INTO regions (code, nom, date_creation, date_modification) VALUES
    ('dakar',       'Dakar',       NOW(), NOW()),
        ('diourbel',    'Diourbel',    NOW(), NOW()),
        ('fatick',      'Fatick',      NOW(), NOW()),
        ('kaffrine',    'Kaffrine',    NOW(), NOW()),
        ('kaolack',     'Kaolack',     NOW(), NOW()),
        ('kedougou',    'Kédougou',    NOW(), NOW()),
        ('kolda',       'Kolda',       NOW(), NOW()),
        ('louga',       'Louga',       NOW(), NOW()),
        ('matam',       'Matam',       NOW(), NOW()),
        ('saint-louis', 'Saint-Louis', NOW(), NOW()),
        ('sedhiou',     'Sédhiou',     NOW(), NOW()),
        ('tambacounda', 'Tambacounda', NOW(), NOW()),
        ('thies',       'Thiès',       NOW(), NOW()),
        ('ziguinchor',  'Ziguinchor',  NOW(), NOW());

        -- ---------------------------------------------------------------------
        -- 4. Départements (46)
-- ---------------------------------------------------------------------
INSERT INTO departements (nom, region_id, date_creation, date_modification)
SELECT d.nom, r.id, NOW(), NOW()
FROM (
        SELECT 'Dakar' AS nom, 'dakar' AS region_code
    UNION ALL SELECT 'Guédiawaye',          'dakar'
        UNION ALL SELECT 'Keur Massar',         'dakar'
        UNION ALL SELECT 'Pikine',              'dakar'
        UNION ALL SELECT 'Rufisque',            'dakar'
        UNION ALL SELECT 'Bambey',              'diourbel'
        UNION ALL SELECT 'Diourbel',            'diourbel'
        UNION ALL SELECT 'Mbacké',              'diourbel'
        UNION ALL SELECT 'Fatick',              'fatick'
        UNION ALL SELECT 'Foundiougne',         'fatick'
        UNION ALL SELECT 'Gossas',              'fatick'
        UNION ALL SELECT 'Birkelane',           'kaffrine'
        UNION ALL SELECT 'Kaffrine',            'kaffrine'
        UNION ALL SELECT 'Koungheul',           'kaffrine'
        UNION ALL SELECT 'Malem Hodar',         'kaffrine'
        UNION ALL SELECT 'Guinguinéo',          'kaolack'
        UNION ALL SELECT 'Kaolack',             'kaolack'
        UNION ALL SELECT 'Nioro du Rip',        'kaolack'
        UNION ALL SELECT 'Kédougou',            'kedougou'
        UNION ALL SELECT 'Salémata',            'kedougou'
        UNION ALL SELECT 'Saraya',              'kedougou'
        UNION ALL SELECT 'Kolda',               'kolda'
        UNION ALL SELECT 'Médina Yoro Foulah',  'kolda'
        UNION ALL SELECT 'Vélingara',           'kolda'
        UNION ALL SELECT 'Kébémer',             'louga'
        UNION ALL SELECT 'Linguère',            'louga'
        UNION ALL SELECT 'Louga',               'louga'
        UNION ALL SELECT 'Kanel',               'matam'
        UNION ALL SELECT 'Matam',               'matam'
        UNION ALL SELECT 'Ranérou Ferlo',       'matam'
        UNION ALL SELECT 'Dagana',              'saint-louis'
        UNION ALL SELECT 'Podor',               'saint-louis'
        UNION ALL SELECT 'Saint-Louis',         'saint-louis'
        UNION ALL SELECT 'Bounkiling',          'sedhiou'
        UNION ALL SELECT 'Goudomp',             'sedhiou'
        UNION ALL SELECT 'Sédhiou',             'sedhiou'
        UNION ALL SELECT 'Bakel',               'tambacounda'
        UNION ALL SELECT 'Goudiry',             'tambacounda'
        UNION ALL SELECT 'Koumpentoum',         'tambacounda'
        UNION ALL SELECT 'Tambacounda',         'tambacounda'
        UNION ALL SELECT 'Mbour',               'thies'
        UNION ALL SELECT 'Thiès',               'thies'
        UNION ALL SELECT 'Tivaouane',           'thies'
        UNION ALL SELECT 'Bignona',             'ziguinchor'
        UNION ALL SELECT 'Oussouye',            'ziguinchor'
        UNION ALL SELECT 'Ziguinchor',          'ziguinchor'
) d
JOIN regions r ON r.code = d.region_code;

-- ---------------------------------------------------------------------
        -- 5. Communes : NON fournies (environ 558 collectivités à charger depuis un
        --    référentiel officiel). Modèle de chargement à partir d'un fichier CSV :
        -- ---------------------------------------------------------------------
        -- CREATE TABLE tmp_communes (
--     departement VARCHAR(80), commune VARCHAR(100), latitude DOUBLE NULL, longitude DOUBLE NULL);
        -- LOAD DATA LOCAL INFILE 'communes.csv' INTO TABLE tmp_communes
--     FIELDS TERMINATED BY ';' ENCLOSED BY '"' IGNORE 1 LINES
--     (departement, commune, latitude, longitude);
        -- INSERT INTO communes (nom, departement_id, latitude, longitude, date_creation, date_modification)
-- SELECT t.commune, d.id, t.latitude, t.longitude, NOW(), NOW()
        -- FROM tmp_communes t JOIN departements d ON d.nom = t.departement;
-- DROP TABLE tmp_communes;

-- ---------------------------------------------------------------------
        -- 6. Produits de référence : `code` = clé Angular farmer.presales.products.<code>
-- ---------------------------------------------------------------------
INSERT INTO produits_reference (code, nom, categorie, date_creation, date_modification) VALUES
    ('tomato',  'Tomate',         'LEGUMES', NOW(), NOW()),
        ('onion',   'Oignon',         'LEGUMES', NOW(), NOW()),
        ('potato',  'Pomme de terre', 'LEGUMES', NOW(), NOW()),
        ('carrot',  'Carotte',        'LEGUMES', NOW(), NOW()),
        ('lettuce', 'Salade',         'LEGUMES', NOW(), NOW()),
        ('cabbage', 'Chou',          'LEGUMES', NOW(), NOW()),
        ('other',   'Autre',          'AUTRES',  NOW(), NOW());

        -- ---------------------------------------------------------------------
        -- 7. Plans d'abonnement : `code` = clé Angular subscription.plans.<code>.*
        --    Prix repris de votre SubscriptionComponent (5 000 / 48 000 au lieu de 60 000 FCFA).
        -- ---------------------------------------------------------------------
INSERT INTO plans_abonnement
        (code, prix, prix_original, periode, populaire, actif, ordre_affichage, date_creation, date_modification) VALUES
    ('gratuit', 0.00,     NULL,     'AUCUNE',  0, 1, 1, NOW(), NOW()),
        ('mensuel', 5000.00,  NULL,     'MENSUEL', 1, 1, 2, NOW(), NOW()),
        ('annuel',  48000.00, 60000.00, 'ANNUEL',  0, 1, 3, NOW(), NOW());

        -- Fonctionnalités incluses : clés Angular subscription.features.<code>
INSERT INTO plan_fonctionnalites (plan_id, code)
SELECT p.id, f.code
FROM plans_abonnement p
CROSS JOIN (
        SELECT 'basicStock' AS code
                UNION ALL SELECT 'publishProducts'
                UNION ALL SELECT 'salesHistory'
                UNION ALL SELECT 'textMessaging'
                UNION ALL SELECT 'emailSupport'
) f
WHERE p.code = 'gratuit';

INSERT INTO plan_fonctionnalites (plan_id, code)
SELECT p.id, f.code
FROM plans_abonnement p
CROSS JOIN (
        SELECT 'allFree' AS code
                UNION ALL SELECT 'smartPresale'
                UNION ALL SELECT 'aiAssistant'
                UNION ALL SELECT 'boost'
                UNION ALL SELECT 'whatsapp'
                UNION ALL SELECT 'realtimeStats'
                UNION ALL SELECT 'audioMessaging'
                UNION ALL SELECT 'prioritySupport'
                UNION ALL SELECT 'advancedDashboard'
                UNION ALL SELECT 'dataExport'
) f
WHERE p.code = 'mensuel';

INSERT INTO plan_fonctionnalites (plan_id, code)
SELECT p.id, f.code
FROM plans_abonnement p
CROSS JOIN (
        SELECT 'allPremium' AS code
                UNION ALL SELECT 'twoMonthsFree'
                UNION ALL SELECT 'training'
                UNION ALL SELECT 'monthlyConsultation'
                UNION ALL SELECT 'earlyAccess'
                UNION ALL SELECT 'dedicatedManager'
                UNION ALL SELECT 'customReports'
                UNION ALL SELECT 'advancedApi'
) f
WHERE p.code = 'annuel';

        -- Limitations (plan gratuit) : clés Angular subscription.limitations.<code>
INSERT INTO plan_limitations (plan_id, code)
SELECT p.id, l.code
FROM plans_abonnement p
CROSS JOIN (
        SELECT 'noPresaleCalc' AS code
                UNION ALL SELECT 'noAi'
                UNION ALL SELECT 'noBoost'
                UNION ALL SELECT 'noWhatsapp'
                UNION ALL SELECT 'noAdvancedStats'
) l
WHERE p.code = 'gratuit';

COMMIT;

-- ---------------------------------------------------------------------
        -- Vérifications attendues
-- ---------------------------------------------------------------------
        -- SELECT COUNT(*) FROM langues_supportees;     -- 3
        -- SELECT COUNT(*) FROM regions;                -- 14
        -- SELECT COUNT(*) FROM departements;           -- 46
        -- SELECT COUNT(*) FROM produits_reference;     -- 7
        -- SELECT COUNT(*) FROM plans_abonnement;       -- 3
        -- SELECT COUNT(*) FROM plan_fonctionnalites;   -- 23  (5 + 10 + 8)
        -- SELECT COUNT(*) FROM plan_limitations;       -- 5

        -- ---------------------------------------------------------------------
        -- Contraintes d'unicité recommandées (à ajouter une fois, évitent les doublons)
        -- ---------------------------------------------------------------------
        -- ALTER TABLE departements ADD CONSTRAINT uk_departement UNIQUE (region_id, nom);
-- ALTER TABLE communes     ADD CONSTRAINT uk_commune     UNIQUE (departement_id, nom);

-- ---------------------------------------------------------------------
        -- Si la table utilisateurs existe déjà avec l'ancienne colonne `langue` (enum 'FR', 'EN', 'WO') :
        -- ---------------------------------------------------------------------
        -- UPDATE utilisateurs SET langue = LOWER(langue);
-- ALTER TABLE utilisateurs
--     ADD CONSTRAINT fk_utilisateurs_langue FOREIGN KEY (langue) REFERENCES langues_supportees (code);

        -- =====================================================================
        -- DONNÉES DE DÉMONSTRATION : barèmes de l'État (NE PAS EXÉCUTER EN PRODUCTION)
        -- Ce sont les barèmes fictifs du composant React (fourchette min/max). Le prix de
-- référence est le milieu de la fourchette : valeur d'exemple, à remplacer par les
        -- vrais barèmes saisis par l'État depuis l'écran d'administration.
        -- =====================================================================
        -- INSERT INTO baremes_prix
--     (produit_reference_id, unite, prix_reference, prix_min, prix_max, date_creation, date_modification)
        -- SELECT pr.id, 'KG', b.ref, b.mn, b.mx, NOW(), NOW()
        -- FROM produits_reference pr
-- JOIN (
--     SELECT 'tomato' AS code, 325.00 AS ref, 250.00 AS mn, 400.00 AS mx
--     UNION ALL SELECT 'onion',   275.00, 200.00, 350.00
        --     UNION ALL SELECT 'potato',  375.00, 300.00, 450.00
        --     UNION ALL SELECT 'carrot',  350.00, 280.00, 420.00
        --     UNION ALL SELECT 'lettuce', 200.00, 150.00, 250.00
        --     UNION ALL SELECT 'cabbage', 240.00, 180.00, 300.00
        --     UNION ALL SELECT 'other',   350.00, 200.00, 500.00
        -- ) b ON b.code = pr.code;