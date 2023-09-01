package ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions;

public class NotEnoughMoneyException extends Exception {
    public NotEnoughMoneyException(long userMoney, long requiredMoney) {
        super("Required " + requiredMoney + " money, when user have only " + userMoney);
    }
}
