package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;

import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/home")
public class HomeController {
    private UserService userService;
    private FileService fileService;
    private NoteService notesService;
    private CredentialService credentialService;
    private EncryptionService encryptionService;

    public HomeController(UserService userService, EncryptionService encryptionService, FileService fileService, NoteService notesService, CredentialService credentialService) {
        this.userService = userService;
        this.fileService = fileService;
        this.notesService = notesService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping
    public String goHomePage(Authentication authentication, Model model) {
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            User user = userService.getUser(username);
            if (Objects.nonNull(user)) {
                List<File> files = fileService.findAllByUserId(user.getUserId());
                List<Note> notes = notesService.findAllByUserId(user.getUserId());
                List<Credential> credentials = credentialService.findAllByUserId(user.getUserId());
                model.addAttribute("listFile", files);
                model.addAttribute("listNote", notes);
                model.addAttribute("listCredential", credentials);
            }
        }else {
            return "redirect:/login";
        }
        model.addAttribute("encryptionService",encryptionService);
        return "home";
    }
}
