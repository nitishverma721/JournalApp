package com.springboot.journalApp.controller;

import com.springboot.journalApp.entity.JournalEntry;
import com.springboot.journalApp.entity.User;
import com.springboot.journalApp.service.JournalEntryService;
import com.springboot.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("{userName}")
    public ResponseEntity<?> getAll(@PathVariable String userName){
        User user = userService.findByUserName(userName);
        List<JournalEntry> all = user.getJournalEntries();
        if(all!=null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("{userName}")
    public ResponseEntity<JournalEntry> postAll(@RequestBody JournalEntry myEntry, @PathVariable String userName){
        try{
            journalEntryService.saveEntry(myEntry, userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(myEntry, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getJournalByIdAll(@PathVariable ObjectId myId){
        Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
        if(journalEntry.isPresent()){
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{myId}")
    public ResponseEntity<?> deleteJournal(@PathVariable ObjectId myId){
        journalEntryService.deleteById(myId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @PutMapping("/id/{myId}")
//    public ResponseEntity<?> updateJournal(@PathVariable ObjectId myId, @RequestBody JournalEntry newEntry){
//        JournalEntry oldEntry = journalEntryService.findById(myId).orElse(null);
//        if(oldEntry != null){
//            oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : oldEntry.getTitle());
//            oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : oldEntry.getContent());
//            journalEntryService.saveEntry(oldEntry);
//            return new ResponseEntity<>(oldEntry, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
}
