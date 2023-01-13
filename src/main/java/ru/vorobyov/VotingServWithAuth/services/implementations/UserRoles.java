package ru.vorobyov.VotingServWithAuth.services.implementations;

public enum UserRoles {
    ROLE_ADMIN{
        @Override
        public String toString() {
            return "ROLE_ADMIN";
        }
    }, ROLE_USER{
        @Override
        public String toString() {
            return "ROLE_USER";
        }
    }, ROLE_VOTER{
        @Override
        public String toString() {
            return "ROLE_VOTER";
        }
    }


}
