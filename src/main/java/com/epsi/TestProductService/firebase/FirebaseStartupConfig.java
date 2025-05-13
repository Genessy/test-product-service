package com.epsi.TestProductService.firebase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class FirebaseStartupConfig {

    @Autowired
    private FirebaseInitialisation firebaseInitialisation;

    @PostConstruct
    public void init() {
        firebaseInitialisation.initialization();
    }
}