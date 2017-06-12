Feature: counting votes
  As a BBC television presenter
  I want to see the counts for candidates within a given time frame
  So that I can announce the winner of the competition

  Scenario: Count Me Up accepts a vote
    Given I am count me up
    When voter A has not voted before
    And I receive a vote for candidate A from voter A
    Then I register that vote and return a 201 response

  Scenario: Count Me Up only accepts 3 votes per user
    Given I am count me up
    And I have received 3 votes for candidate A from voter B
    And I register that vote and return a 201 response
    When I receive a vote for candidate A from voter B
    Then I do not register that vote
    And I return a 403 response

  Scenario: Count Me Up only accepts 3 votes per user regardless of candidate
    Given I am count me up
    And I have received 2 votes for candidate A from voter B
    Then I register that vote and return a 201 response
    And I have received 1 vote for candidate D from voter B
    Then I register that vote and return a 201 response
    When I receive a vote for candidate D from voter B
    Then I do not register that vote
    And I return a 403 response

  Scenario: Count Me Up returns the voting results
    Given I am count me up
    And I have received 20000000 votes for 4 candidates and the votes are split:
      |    A        |  8000000  |
      |    B        |  2000000  |
      |    C        |  6000000  |
      |    D        |  4000000  |
    When I receive a request for the overall result
    Then I return the correct result
    And the response time is under 1 second