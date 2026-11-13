const { onCall, HttpsError } = require("firebase-functions/v2/https");
const { defineSecret } = require("firebase-functions/params");
const { initializeApp } = require("firebase-admin/app");
const { getAuth } = require("firebase-admin/auth");
const { getFirestore } = require("firebase-admin/firestore");

initializeApp();
const db = getFirestore();
const webApiKey = defineSecret("FIREBASE_WEB_API_KEY");

/**
 * Resolves a username privately and verifies its password through Firebase
 * Identity Toolkit. The client receives a short-lived Firebase custom token,
 * never the account email address.
 */
exports.loginWithUsername = onCall({ secrets: [webApiKey], maxInstances: 10 }, async (request) => {
  const username = String(request.data?.username || "").trim().toLowerCase();
  const password = String(request.data?.password || "");
  if (!/^[a-z0-9_]{3,18}$/.test(username) || password.length < 6) {
    throw new HttpsError("invalid-argument", "Enter a valid username and password.");
  }

  const mapping = await db.collection("usernames").doc(username).get();
  if (!mapping.exists || !mapping.get("uid")) {
    throw new HttpsError("unauthenticated", "Incorrect username or password.");
  }

  try {
    const account = await getAuth().getUser(mapping.get("uid"));
    if (!account.email) throw new Error("No email credential");
    const response = await fetch(
      `https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=${webApiKey.value()}`,
      {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email: account.email, password, returnSecureToken: true })
      }
    );
    if (!response.ok) throw new Error("Invalid credentials");
    const identity = await response.json();
    const token = await getAuth().createCustomToken(identity.localId);
    return { customToken: token };
  } catch (error) {
    throw new HttpsError("unauthenticated", "Incorrect username or password.");
  }
});
