package com.danielkpl2.countmeup.Controller;

import com.danielkpl2.countmeup.Domain.Candidate;
import com.danielkpl2.countmeup.Service.CountMeUpService;
import com.danielkpl2.countmeup.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountMeUpController {

    @Autowired
    private CountMeUpService countMeUpService;

    @RequestMapping(value="/vote", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> votes(@RequestBody Vote vote) {
        if(vote.isVoteValid()){
            if(!countMeUpService.voterExists(vote.getVoterName())){
                countMeUpService.addNewVoter(vote);
                countMeUpService.saveVote(vote);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }else{
                if(countMeUpService.canVoterVote(vote)){
                    countMeUpService.addVoterCount(vote);
                    countMeUpService.saveVote(vote);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                }else{
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            }

        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    //returns the winning candidate
    @RequestMapping("/winner")
    public ResponseEntity<Candidate> winner(){
        return new ResponseEntity<>(countMeUpService.getWinner(), HttpStatus.OK);
    }

    //returns the candidate vote counts
    @RequestMapping("/results")
    public ResponseEntity<Iterable<Candidate>> results(){
        return new ResponseEntity<>(countMeUpService.getResults(), HttpStatus.OK);
    }
}
