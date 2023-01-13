package ru.vorobyov.VotingServWithAuth.services.interfaces;

public interface EmailService {
    void sendSimpleEmail(String toAddress, String subject, String message);
}
