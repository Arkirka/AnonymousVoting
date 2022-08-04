# AnonymousVoting
Java Spring Boot server for private anonymous voting.  
Link to the test sample: http://141.8.198.190:5001    

# Accounts
| Login | Password | Role |
|---------|---------|---------|
| Admin | Admin123! | Admin |
| User1 | User123! | User |
| User2 | User223! | Voter |

# Description of roles
* Admin - can create votes, invite participants to vote, receive data on the voting process 
and receive voting reports in the form of a general summary and anonymous bulletins of those who voted,
configure all user data except password.    
* User - may be invited to vote.
* Voter - can vote in the voting.
