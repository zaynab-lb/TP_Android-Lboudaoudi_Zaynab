// JsonToFirebase.js

const admin = require('firebase-admin');
const serviceAccount = require('./serviceAccountKey.json');
const products = require('./products.json'); // fichier JSON avec tes produits

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

const db = admin.firestore();

async function uploadProducts() {
  for (const product of products) {
    if (product.productID && product.productID.toString().trim() !== "") {
      await db.collection('products').doc(product.productID.toString()).set(product);
      console.log('✅ Produit ajouté :', product.productID);
    } else {
      console.warn('❌ Produit ignoré (ID invalide) :', product);
    }
  }
  console.log('✅ Upload terminé.');
}

uploadProducts().catch(console.error);
