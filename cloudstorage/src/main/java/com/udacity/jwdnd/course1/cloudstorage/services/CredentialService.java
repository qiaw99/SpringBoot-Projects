package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private EncryptionService encryptionService;
    private CredentialMapper credentialMapper;

    public CredentialService(EncryptionService encryptionService, CredentialMapper credentialMapper) {
        this.encryptionService = encryptionService;
        this.credentialMapper = credentialMapper;
    }

    public Credential getCredentialByID(Integer credentialID){
        return this.credentialMapper.getCredentialByID(credentialID);
    }

    public List<Credential> getAllCredentials(){
        ArrayList<Credential> allCredentials = (ArrayList<Credential>) this.credentialMapper.getAllCredentials();
//        String encodedKey, decryptedPassword;
//
//        for (Credential credential: allCredentials){
//            encodedKey = credential.get_key();
//            decryptedPassword = encryptionService.decryptValue(credential.getPassword(), encodedKey);
//            credential.setPassword(decryptedPassword);
//        }

        return allCredentials;
    }

    public int addCredential(Credential credential){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);

        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setPassword(encryptedPassword);
        credential.set_key(encodedKey);

        if (credential.getCredentialid() != null) {
            return this.credentialMapper.updateCredential(credential);
        } else {
            return this.credentialMapper.insert(credential);
        }
    }

    public void deleteCredential(Integer credentialid){
        this.credentialMapper.deleteCredential(credentialid);
    }
}
