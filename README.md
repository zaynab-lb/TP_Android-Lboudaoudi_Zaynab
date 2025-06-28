# TP Android :
## Nom et Prenom : Lboudaoudi Zaynab
## Num d'inscription : DCC0005/24

### Objectif du projet :  
Développer une application mobile permettant aux clients de parcourir, acheter et gérer des produits (bijoux), tout en offrant une interface d'administration pour la gestion des utilisateurs, produits et commandes 

### Description du projet : 
C’est une application Android (Jetpack Compose) de e-commerce spécialisée dans la vente de bijoux, avec un système de rôles utilisateurs :

•	Visiteurs peuvent s’inscrire ou se connecter via email/mot de passe (Firebase Authentication).

•	Clients peuvent consulter les produits, filtrer par catégorie (colliers, bracelets, bagues, etc.), voir les détails, ajouter des articles à leur panier personnel, passer des commandes (avec saisie d’adresse et téléphone), suivre l’état de leurs commandes, et modifier leur profil (email/mot de passe).

•	Admins peuvent gérer les utilisateurs, les produits (ajout, modification, suppression avec stock, prix, description et catégorie), et consulter toutes les commandes passées dans une interface dédiée.

### Technologies utilisées :

•	Frontend : Jetpack Compose (UI), MVI

•	Backend : Firebase Firestore (base de données), Firebase Authentication (Par méthode émail + mot de passe)

•	Injection de dépendances : Hilt

•	Langage : Kotlin

### Captures d'écran :
### 1.	Pour visiteur :
Page de connexion :

![image](https://github.com/user-attachments/assets/8dd2ac91-040a-4478-8e28-97d723f31feb)

Page d’inscription :

![image](https://github.com/user-attachments/assets/2bac19c0-6bf5-4236-b331-7e9baa79d771)

### 2.	Pour Membre (Client) :
La page d’accuiel (catalogue) : 

![image](https://github.com/user-attachments/assets/30f9908e-1889-4072-848f-e9e6ffa0a2ef)

Le filtrage des produits selon la catégorie : 

![image](https://github.com/user-attachments/assets/2cad8f74-b02f-4bff-ac2a-9d84d51dfd52)

Détail du produit :

![image](https://github.com/user-attachments/assets/515c48a3-59dd-424a-a7c3-926b410d0856)

![image](https://github.com/user-attachments/assets/93ea6e53-65d3-4b66-a928-715aa783686a)

Le panier vide :

![image](https://github.com/user-attachments/assets/025ca2d3-571b-445e-89df-7b02c9a55032)

Le panier contient des produits :

![image](https://github.com/user-attachments/assets/633fa8d3-15df-4cc2-b855-12296b84ac3a)

La validation du commande : 

![image](https://github.com/user-attachments/assets/7d162c2a-bc12-411b-afc0-655441a1721b)

Liste du commandes vide : 

![image](https://github.com/user-attachments/assets/0730eec2-6802-45ac-8ffd-1fabdf805504)

Liste du commandes contient des commandes :

![image](https://github.com/user-attachments/assets/72a82876-a6a6-4259-940c-91fd05b4058c)

### 3.	Pour admin :
La page d’accueil :

![image](https://github.com/user-attachments/assets/92275e5a-6080-4acc-8362-ea7a88e584b0)

#### Gestion des utilisateurs :
La liste des utilisateurs :

![image](https://github.com/user-attachments/assets/b5115443-ed9a-49c7-8f8e-c38975901f12)

Modification du rôle d’utilisateur :

![image](https://github.com/user-attachments/assets/96131be6-f11e-475e-bc83-b65fa09f61f5)

![image](https://github.com/user-attachments/assets/d3e9a9db-f0e5-419b-beb4-053007cb553a)

#### Gestion des produits :
La liste des produits :

![image](https://github.com/user-attachments/assets/4525919d-b64a-4ce4-b10d-990e0da091db)

La modification d’un produit:

![image](https://github.com/user-attachments/assets/0dad6b8b-02d4-480d-8688-703a491aa0ae)

La suppression d’un produit :

![image](https://github.com/user-attachments/assets/f4630339-cf3e-4904-92fd-acca1d97a40b)

#### La gestion des commandes :
La liste des commandes :

![image](https://github.com/user-attachments/assets/576833f1-362c-44de-ae67-af14702135af)

Change la statut d'une commande :

![image](https://github.com/user-attachments/assets/94dd3877-89c6-43ba-843c-57d440d07440)

![image](https://github.com/user-attachments/assets/cee2e419-4211-480b-97da-e11d7f6524d2)

![image](https://github.com/user-attachments/assets/36e611fe-3d58-422c-b6a8-32772c09b460)

### 4.  Pour admin + Client :
La page profil :

![image](https://github.com/user-attachments/assets/02fc155c-f5dd-4b52-94d4-cb238f467610)

La modification du profil :

![image](https://github.com/user-attachments/assets/638102e8-36cb-4cce-ba62-47b23096c4b3)







