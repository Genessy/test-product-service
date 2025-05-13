package com.epsi.TestProductService.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;
import java.io.FileInputStream;

@Service
public class FirebaseInitialisation {
    public void initialization() {
        try {
            String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
            FileInputStream serviceAccount = new FileInputStream(credentialsPath);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            throw new RuntimeException("Échec de l'initialisation Firebase : " + e.getMessage(), e);
        }
    }
}

