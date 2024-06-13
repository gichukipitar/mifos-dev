package com.fineract.mifos.mifos_core.infrastructure.core.domain;

import org.springframework.core.NamedThreadLocal;
import org.springframework.transaction.TransactionStatus;

import java.util.Map;
import java.util.Optional;

public class BatchRequestContextHolder {
    private BatchRequestContextHolder() {}

    private static final ThreadLocal<Map<String, Object>> batchAttributes = new NamedThreadLocal<>("batchAttributes");

    private static final ThreadLocal<Optional<TransactionStatus>> batchTransaction = new NamedThreadLocal<>("batchTransaction") {

        @Override
        protected Optional<TransactionStatus> initialValue() {
            return Optional.empty();
        }
    };
    private static final ThreadLocal<Boolean> isEnclosingTransaction = new NamedThreadLocal<>("isEnclosingTransaction");

    /**
     * True if the batch attributes are set
     *
     * @return true if the batch attributes are set
     */
    public static boolean isBatchRequest() {
        return batchAttributes.get() != null;
    }

    /**
     * Returns the batch attributes for the current thread.
     *
     * @return the batch attributes for the current thread, cna be null
     */
    public static Map<String, Object> getRequestAttributes() {
        return batchAttributes.get();
    }

    /**
     * Set the batch attributes for the current thread.
     *
     * @param requestAttributes
     *            the new batch attributes
     */
    public static void setRequestAttributes(Map<String, Object> requestAttributes) {
        batchAttributes.set(requestAttributes);
    }

    /**
     * Reset the batch attributes for the current thread.
     */
    public static void resetRequestAttributes() {
        batchAttributes.remove();
    }

    /**
     * True if the batch attributes are set and the enclosing transaction is set to true
     *
     * @return
     */
    public static boolean isEnclosingTransaction() {
        return Boolean.TRUE.equals(isEnclosingTransaction.get());
    }

    /**
     * Set the isEnclosingTransaction flag for the current thread.
     *
     * @param isEnclosingTransaction
     */
    public static void setIsEnclosingTransaction(boolean isEnclosingTransaction) {
        BatchRequestContextHolder.isEnclosingTransaction.set(isEnclosingTransaction);
    }

    public static void resetIsEnclosingTransaction() {
        isEnclosingTransaction.remove();
    }

    /**
     * Return the transaction
     *
     * @return
     */
    public static Optional<TransactionStatus> getTransaction() {
        return batchTransaction.get();
    }

    /**
     * Return the enclosing transaction
     *
     * @return
     */
    public static Optional<TransactionStatus> getEnclosingTransaction() {
        return isEnclosingTransaction() ? getTransaction() : Optional.empty();
    }

    /**
     * Set the transaction for the current thread.
     *
     * @param enclosingTransaction
     */
    public static void setTransaction(TransactionStatus enclosingTransaction) {
        batchTransaction.set(Optional.ofNullable(enclosingTransaction));
    }

    /**
     * Set the enclosing transaction for the current thread.
     *
     * @param enclosingTransaction
     */
    public static void setEnclosingTransaction(TransactionStatus enclosingTransaction) {
        if (isEnclosingTransaction()) {
            setTransaction(enclosingTransaction);
        }
    }

    public static void resetTransaction() {
        batchTransaction.set(Optional.empty());
    }
}
