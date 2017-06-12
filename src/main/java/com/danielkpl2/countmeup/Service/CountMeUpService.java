package com.danielkpl2.countmeup.Service;

import com.danielkpl2.countmeup.Domain.Candidate;
import com.danielkpl2.countmeup.Domain.CandidateRepository;
import com.danielkpl2.countmeup.Domain.Voter;
import com.danielkpl2.countmeup.Domain.VoterRepository;
import com.danielkpl2.countmeup.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountMeUpService {
    @Autowired
    private VoterRepository voterRepository;

    @Autowired
    private CandidateRepository candidateRepository;


    public CountMeUpService() {}

    public void addVoterCount(Vote vote){
        Voter repositoryVoter = voterRepository.findOne(vote.getVoterName());
        repositoryVoter.setVotes(repositoryVoter.getVotes() + vote.getVotes());
        voterRepository.save(repositoryVoter);
    }

    public void addNewVoter(Vote vote){
        Voter voter = new Voter(vote.getVoterName(), vote.getVotes());
        voterRepository.save(voter);
    }

    public boolean canVoterVote(Vote vote) {
        Voter repositoryVoter = voterRepository.findOne(vote.getVoterName());
        if(repositoryVoter.getVotes() < 3 && (repositoryVoter.getVotes() + vote.getVotes()) <= 3){
            return true;
        }else{
            return false;
        }

    }

    public boolean voterExists(String name){
        return voterRepository.exists(name);
    }

    public void saveVote(Vote vote){
            if(candidateRepository.exists(vote.getCandidateName())){
                Candidate c = candidateRepository.findOne(vote.getCandidateName());
                c.setVotes(c.getVotes() + vote.getVotes());
                candidateRepository.save(c);
            }else{
                Candidate c = new Candidate(vote.getCandidateName(), vote.getVotes());
                candidateRepository.save(c);
            }
    }

    public Iterable<Candidate> getResults(){
        return candidateRepository.findAll();
    }

    public Candidate getWinner(){
        Iterable<Candidate> candidates = candidateRepository.findAll();

        int max = 0;
        Candidate winner = null;
        for (Candidate candidate: candidates){
            if(candidate.getVotes() > max){
                max = candidate.getVotes();
                winner = candidate;
            }
        }
        return winner;
    }
}
