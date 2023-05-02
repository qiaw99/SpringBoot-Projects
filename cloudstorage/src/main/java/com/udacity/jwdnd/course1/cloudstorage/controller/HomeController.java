package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class HomeController {

    private NoteService noteService;
    private UserService userService;
    private CredentialService credentialService;
    private EncryptionService encryptionService;

    private FileService fileService;

    public HomeController(FileService fileService, NoteService noteService, UserService userService, CredentialService credentialService, EncryptionService encryptionService){
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
        this.fileService = fileService;
    }

    @GetMapping("/home")
    public String getHome(Model model, @ModelAttribute("note") Note note, @ModelAttribute("credentials") Credential credential){
        model.addAttribute("notes", noteService.getAllNotes());
        model.addAttribute("allcredentials", this.credentialService.getAllCredentials());
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("files", this.fileService.findAllFiles());
        return "home";
    }

    @PostMapping("/home")
    public String addNote(Authentication authentication, @ModelAttribute("note") Note note,@ModelAttribute("credentials") Credential credential, Model model){
        User user = this.userService.getUser(authentication.getName());

        note.setUserid(user.getUserId());
        this.noteService.addNote(note);
        model.addAttribute("notes", noteService.getAllNotes());
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("allcredentials", this.credentialService.getAllCredentials());
        return "redirect:/home";
    }

    @GetMapping("/home/{noteid}")
    public String deleteNote(@PathVariable String noteid, @ModelAttribute("note") Note note, @ModelAttribute("credentials") Credential credential, Model model){
        this.noteService.deleteNote(Integer.parseInt(noteid));
        model.addAttribute("notes", noteService.getAllNotes());
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("allcredentials", this.credentialService.getAllCredentials());
        return "redirect:/home";
    }

    @PostMapping("/credential")
    public String addCredential(Authentication authentication, Model model, @ModelAttribute("note") Note note, @ModelAttribute("credentials") Credential credential){
        User user = this.userService.getUser(authentication.getName());
        credential.setUserid(user.getUserId());

        model.addAttribute("encryptionService", encryptionService);
        this.credentialService.addCredential(credential);

        model.addAttribute("allcredentials", this.credentialService.getAllCredentials());
        model.addAttribute("notes", noteService.getAllNotes());


        return "redirect:/home";
    }

    @GetMapping("/credential/{credentialid}")
    public String deleteCredential(@PathVariable String credentialid, Model model, @ModelAttribute("note") Note note, @ModelAttribute("credentials") Credential credential){
        this.credentialService.deleteCredential(Integer.parseInt(credentialid));

        model.addAttribute("notes", noteService.getAllNotes());
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("allcredentials", this.credentialService.getAllCredentials());

        return "redirect:/home";
    }

    @PostMapping("/file-upload")
    public String handleFile(RedirectAttributes redirectAttributes, Authentication authentication, @RequestParam("fileUpload")MultipartFile fileUpload, Model model, @ModelAttribute("note") Note note, @ModelAttribute("credentials") Credential credential) throws IOException {
        User user = this.userService.getUser(authentication.getName());
        File file = new File();
        file.setUserid(user.getUserId());
        file.setContenttype(fileUpload.getContentType());
        file.setFilename(fileUpload.getOriginalFilename());
        file.setFilesize(String.valueOf(fileUpload.getSize()));
        file.setFiledata(fileUpload.getBytes());

        int addedRow = this.fileService.insert(file);
        String failwords;
        if(addedRow == -1) {
            failwords = "The file is already existed!";
            redirectAttributes.addFlashAttribute("failwords", failwords);
        } else {
            redirectAttributes.addFlashAttribute("sucessword", "Successfully uploaded the file!");
        }

        model.addAttribute("files", this.fileService.findAllFiles());

        return "redirect:/home";
    }

    @GetMapping("/file/{fileid}")
    public String deleteFile(@PathVariable String fileid, Model model, @ModelAttribute("note") Note note, @ModelAttribute("credentials") Credential credential){
        this.fileService.deleteFile(Integer.parseInt(fileid));

        model.addAttribute("notes", noteService.getAllNotes());
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("allcredentials", this.credentialService.getAllCredentials());
        model.addAttribute("files", this.fileService.findAllFiles());

        return "redirect:/home";
    }

    @GetMapping("download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId){
        File file = this.fileService.findFilebyID(Integer.parseInt(fileId));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                .body(file.getFiledata());
    }
}
