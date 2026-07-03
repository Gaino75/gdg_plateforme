package com.gdg.service_stock.exception;

// ─── Exception métier ─────────────────────────────────────────────────────────

/**
 * Lancée quand la quantité demandée dépasse le stock disponible.
 * Correspond à la règle RG-05 du cahier des charges.
 */
public class StockInsuffisantException extends RuntimeException {
    public StockInsuffisantException(String message) {
        super(message);
    }
}
