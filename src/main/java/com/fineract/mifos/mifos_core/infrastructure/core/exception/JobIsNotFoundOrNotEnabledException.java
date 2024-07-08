package com.fineract.mifos.mifos_core.infrastructure.core.exception;

public class JobIsNotFoundOrNotEnabledException extends AbstractPlatformException{

    public JobIsNotFoundOrNotEnabledException(Exception e, String jobName) {
        super("error.msg.job.disabled", "Job " + jobName + " is not found or it is disabled", e);
    }

    public JobIsNotFoundOrNotEnabledException(String jobName) {
        super("error.msg.job.disabled", "Job " + jobName + " is not found or it is disabled");
    }

}
