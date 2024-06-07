package com.fineract.mifos.core.infrastructure.core.exception;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Exception with multiple root causes.
 *
 * Intended to be used in places where N operations are performed in a loop over something, each of which could fail,
 * but where we don't want to fail immediately but continue, and then fail at end.
 *
 * <p>
 * The failures should typically also each be logged within the loop, as they occur; this exception is only thrown to
 * propagate the failure, and the caller may or may not log this with the details.
 *
 * <p>
 * Originally inspired by org.junit.runners.model.MultipleFailureException
 * </p>
 * .
 *
 * @author Michael Vorburger.ch <mike@vorburger.ch>
 */
@Slf4j
public class MultiException extends Exception {

    private final List<Throwable> throwables;

    @SuppressFBWarnings("CT_CONSTRUCTOR_THROW")
    public MultiException(List<Throwable> problems) {
        super("MultiException with " + problems.size() + " contained causes (details available)");
        if (problems.isEmpty()) {
            throw new IllegalArgumentException("List of Throwables must not be empty");
        }
        this.throwables = new ArrayList<>(problems);
    }

    public List<Throwable> getCauses() {
        return Collections.unmodifiableList(throwables);
    }

    @Override
    @SuppressWarnings("RegexpSinglelineJava")
    public String getMessage() {
        int i = 0;
        StringBuilder sb = new StringBuilder(super.getMessage());
        for (Throwable e : throwables) {
            sb.append("\n    ");
            sb.append(++i);
            sb.append(". ");
            Writer w = CharStreams.asWriter(sb);
            e.printStackTrace(new PrintWriter(w, true));
        }
        sb.append("\n  which was itself thrown..");
        return sb.toString();
    }

    @Override
    @SuppressWarnings("RegexpSinglelineJava")
    @SuppressFBWarnings("SLF4J_SIGN_ONLY_FORMAT")
    public void printStackTrace() {
        log.error("{}", super.getMessage());
        int i = 0;
        for (Throwable e : throwables) {
            log.error("{}.", ++i);
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("RegexpSinglelineJava")
    public void printStackTrace(PrintStream s) {
        s.println(super.getMessage());
        int i = 0;
        for (Throwable e : throwables) {
            s.print(++i + ".");
            e.printStackTrace(s);
        }
    }

    @Override
    @SuppressWarnings("RegexpSinglelineJava")
    public void printStackTrace(PrintWriter s) {
        s.println(super.getMessage());
        int i = 0;
        for (Throwable e : throwables) {
            s.print(++i + ".");
            e.printStackTrace(s);
        }
    }
}

