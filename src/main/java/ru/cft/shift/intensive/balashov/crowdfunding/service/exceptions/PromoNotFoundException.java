package ru.cft.shift.intensive.balashov.crowdfunding.service.exceptions;

public class PromoNotFoundException extends Exception {
    public PromoNotFoundException(String promo) {
        super("Promo '" + promo + "' does not exists");
    }
}
