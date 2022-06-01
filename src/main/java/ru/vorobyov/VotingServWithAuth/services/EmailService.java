package ru.vorobyov.VotingServWithAuth.services;

public interface EmailService {
    public void sendSimpleEmail(String toAddress, String subject, String message);
    public void sendEmailWithAttachment(String toAddress, String subject, String message, String attachment);
}
