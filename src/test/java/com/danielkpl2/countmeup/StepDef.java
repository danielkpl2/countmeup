package com.danielkpl2.countmeup;

import com.danielkpl2.countmeup.Domain.Candidate;
import com.danielkpl2.countmeup.Domain.CandidateRepository;
import com.danielkpl2.countmeup.Domain.VoterRepository;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.deps.com.google.gson.Gson;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.Map;
import static junit.framework.TestCase.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CountMeUpApplication.class)
@SpringBootTest(classes = CountMeUpApplication.class)
public class StepDef {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;

    private ResultActions resultActions;

    @Autowired
    private VoterRepository voterRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    private Vote vote;

    private int votesBefore;
    private long startTime;
    private long endTime;

    private int getVotesBefore(String candidateName){
        if(candidateRepository.exists(candidateName)){
            return candidateRepository.findOne(candidateName).getVotes();
        }else return 0;
    }

    @Given("^I am count me up$")
    public void i_am_count_me_up() throws Throwable {
        vote = null;
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        candidateRepository.deleteAll();
        voterRepository.deleteAll();
        votesBefore = 0;
        resultActions = null;
    }

    @When("^I receive a vote for candidate A from voter A$")
    public void i_receive_a_vote_for_candidate_A_from_voter_A() throws Throwable {
        vote = new Vote("A", "A", 1);
        votesBefore = getVotesBefore("A");

        Gson gson = new Gson();
        String json = gson.toJson(vote);

        resultActions = mockMvc.perform(post("/vote").content(json).contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @When("^voter A has not voted before$")
    public void voter_A_has_not_voted_before() throws Throwable {
        Assert.assertFalse(voterRepository.exists("A"));
    }

    @Then("^I register that vote and return a (\\d+) response$")
    public void i_register_that_vote_and_return_a_response(int statusCode) throws Throwable {
        //the candidate's vote count should be that of his vote count before the request and that from the vote object

        Candidate candidate = candidateRepository.findOne(vote.getCandidateName());
        Assert.assertEquals(votesBefore + vote.getVotes(), candidate.getVotes());

        resultActions.andExpect(status().is(statusCode));
    }

    @Given("^I have received (\\d+) votes for candidate A from voter B$")
    public void i_have_received_votes_for_candidate_A_from_voter_B(int votes) throws Throwable {
        vote = new Vote("B", "A", votes);
        votesBefore = getVotesBefore("A");

        Gson gson = new Gson();
        String json = gson.toJson(vote);

        resultActions = mockMvc.perform(post("/vote").content(json).contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @When("^I receive a vote for candidate A from voter B$")
    public void i_receive_a_vote_for_candidate_A_from_voter_B() throws Throwable {
        vote = new Vote("B", "A", 1);
        votesBefore = getVotesBefore("A");

        Gson gson = new Gson();
        String json = gson.toJson(vote);

        resultActions = mockMvc.perform(post("/vote").content(json).contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Then("^I do not register that vote$")
    public void i_do_not_register_that_vote() throws Throwable {
        //The candidate's vote count before the request should be the same as after the request as it shouldn't have been saved
        Candidate candidate = candidateRepository.findOne(vote.getCandidateName());
        Assert.assertEquals(votesBefore, candidate.getVotes());
    }

    @Then("^I return a (\\d+) response$")
    public void i_return_a_response(int statusCode) throws Throwable {
        resultActions.andExpect(status().is(statusCode));
    }

    @Given("^I have received (\\d+) vote for candidate D from voter B$")
    public void i_have_received_vote_for_candidate_D_from_voter_B(int votes) throws Throwable {
        vote = new Vote("B", "D", votes);
        votesBefore = getVotesBefore("D");
        Gson gson = new Gson();
        String json = gson.toJson(vote);

        resultActions = mockMvc.perform(post("/vote").content(json).contentType(MediaType.APPLICATION_JSON_VALUE));

    }

    @When("^I receive a vote for candidate D from voter B$")
    public void i_receive_a_vote_for_candidate_D_from_voter_B() throws Throwable {
        vote = new Vote("B", "D", 1);
        votesBefore = getVotesBefore("D");

        Gson gson = new Gson();
        String json = gson.toJson(vote);

        resultActions = mockMvc.perform(post("/vote").content(json).contentType(MediaType.APPLICATION_JSON_VALUE));

    }

    @Given("^I have received 20000000 votes for 4 candidates and the votes are split:$")
    public void i_have_received_20000000_votes_for_4_candidates_and_the_votes_are_split(Map<String, Integer>  votesTable) throws Throwable {
        Candidate candidateA = new Candidate("A", votesTable.get("A"));
        Candidate candidateB = new Candidate("B", votesTable.get("B"));
        Candidate candidateC = new Candidate("C", votesTable.get("C"));
        Candidate candidateD = new Candidate("D", votesTable.get("D"));
        candidateRepository.save(candidateA);
        candidateRepository.save(candidateB);
        candidateRepository.save(candidateC);
        candidateRepository.save(candidateD);
    }



    @When("^I receive a request for the overall result$")
    public void i_receive_a_request_for_the_overall_result() throws Throwable {
        startTime = System.currentTimeMillis();
        resultActions = mockMvc.perform(get("/winner"));
        endTime = System.currentTimeMillis();

    }

    @Then("^I return the correct result$")
    public void i_return_the_correct_result() throws Throwable {
        resultActions.andExpect(content().string("{\"name\":\"A\",\"votes\":8000000}"));
    }

    @Then("^the response time is under (\\d+) second$")
    public void the_response_time_is_under_second(int second) throws Throwable {
        double timeElapsed = (double)(endTime - startTime)/1000;
        System.out.println("Total execution time: " + timeElapsed);
        assertTrue(timeElapsed < second);
    }

}
