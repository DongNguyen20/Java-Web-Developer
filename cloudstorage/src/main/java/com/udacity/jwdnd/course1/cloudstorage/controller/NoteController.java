package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/notes")
public class NoteController {
    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String note(Authentication authentication, Note note, Model model) {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        if (userName == null) {
            return "redirect:/home";
        }
        note.setUserId(user.getUserId());
        int rs;
        if (note.getNoteId() == null) {
            rs = noteService.create(note);
            if (rs > 0) {
                model.addAttribute("result", "success");
            } else {
                model.addAttribute("message", "Cannot create Note!");
            }
        } else {
            rs = noteService.update(note.getNoteId(), note);
            if (rs > 0) {
                model.addAttribute("result", "success");
            } else {
                model.addAttribute("message", "Cannot create Note!");
            }
        }
        return "result";
    }

    @GetMapping(path = "/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        try {
            int rs = noteService.delete(id);
            model.addAttribute("result", "success");
        }catch (Exception e){
            model.addAttribute("result", "error");
            model.addAttribute("message", "Cannot delete Note!");
        }
        return "result";
    }
}
