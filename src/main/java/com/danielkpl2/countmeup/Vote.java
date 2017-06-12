package com.danielkpl2.countmeup;

public class Vote {
    private String voterName;
    private String candidateName;
    private int votes;

    public Vote(){}
    public Vote(String voterName, String candidateName, int votes){
        this.voterName = voterName;
        this.candidateName = candidateName;
        this.votes = votes;
    }

    public boolean isVoteValid() {
        if(votes > 0 && votes <= 3){
            return true;
        }else{
            return false;
        }
    }

    public String getVoterName() {
        return voterName;
    }

    public void setVoterName(String voterName) {
        this.voterName = voterName;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "voterName='" + voterName + '\'' +
                "candidateName='" + candidateName + '\'' +
                ", votes=" + votes +
                '}';
    }
}
