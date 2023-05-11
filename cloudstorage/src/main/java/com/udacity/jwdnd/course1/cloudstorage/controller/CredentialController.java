package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping(path = "/credentials")
public class CredentialController {
    private CredentialService credentialService;
    private UserService userService;
    private EncryptionService encryptionService;

    public CredentialController(CredentialService credentialService, UserService userService, EncryptionService encryptionService) {
        this.credentialService = credentialService;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    @PostMapping
    public String credential(Authentication authentication, Credential credential, Model model) {
        if (!userService.isUsernameAvailable(credential.getUserName())) {
            return "redirect:/home";
        }
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        if (userName == null) {
            return "redirect:/home";
        }
        int rs;
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);
        credential.setUserId(user.getUserId());

        if (credential.getCredentialId() == null) {
            rs = credentialService.create(credential);
            if (rs > 0) {
                model.addAttribute("result", "success");
            } else {
                model.addAttribute("message", "Cannot create Credential!");
            }
        } else {
            rs = credentialService.update(credential.getCredentialId(), credential);
            if (rs > 0) {
                model.addAttribute("result", "success");
            } else {
                model.addAttribute("message", "Cannot update Credential!");
            }
        }

        return "result";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        try {
            credentialService.deleteCredential(id);
            model.addAttribute("result", "success");
        }catch (Exception e){
            model.addAttribute("result", "error");
            model.addAttribute("message", "Cannot delete Credential!");
        }
        return "result";
    }
}
