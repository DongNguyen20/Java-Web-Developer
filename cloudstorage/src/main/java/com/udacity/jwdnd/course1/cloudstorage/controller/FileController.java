package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/files")
public class FileController {
    private UserService userService;
    private FileService fileService;

    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@RequestParam(name = "fileUpload") MultipartFile fileUpload, Model model) throws IOException {
        if(fileUpload.isEmpty() || isExistFileName(fileUpload.getOriginalFilename())) {
            model.addAttribute("result", "error");
            model.addAttribute("message", "Invalid add new File");
            return "result";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUser(username);
        File file = new File();
        file.setFileName(fileUpload.getOriginalFilename());
        file.setContentType(fileUpload.getContentType());
        file.setFileSize(String.valueOf(fileUpload.getSize()));
        file.setFileData(fileUpload.getBytes());
        file.setUserId(user.getUserId());
        fileService.createFile(file);
        model.addAttribute("result", "success");

        return "result";
    }

    @GetMapping(path = "/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        File file = fileService.findOneByFileName(fileName);
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + file.getFileName();
        response.setHeader(headerKey, headerValue);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(file.getFileData());
        outputStream.close();
    }

    @GetMapping("/delete/{fileName}")
    public String delete(@PathVariable String fileName, Model model) {
        model.addAttribute("result", "success");
        try{
            fileService.deleteByFileName(fileName);
        }catch (Exception e) {
            model.addAttribute("result", "error");
            model.addAttribute("message", "Cannot delete file!");
        }
        return "result";
    }

    boolean isExistFileName(String fileName){
        return Objects.nonNull(fileService.findOneByFileName(fileName));
    }
}