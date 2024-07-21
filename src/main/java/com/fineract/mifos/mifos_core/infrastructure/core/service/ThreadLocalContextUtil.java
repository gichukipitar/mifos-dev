package com.fineract.mifos.mifos_core.infrastructure.core.service;

import com.fineract.mifos.mifos_core.infrastructure.businessdate.dto.BusinessDateType;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.ActionContext;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.FineractContext;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.FineractPlatformTenant;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.HashMap;

public final class ThreadLocalContextUtil {

    public static final String CONTEXT_TENANTS = "tenants";
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();
    private static final ThreadLocal<FineractPlatformTenant> tenantContext = new ThreadLocal<>();
    private static final ThreadLocal<String> authTokenContext = new ThreadLocal<>();
    private static final ThreadLocal<HashMap<BusinessDateType, LocalDate>> businessDateContext = new ThreadLocal<>();
    private static final ThreadLocal<ActionContext> actionContext = new ThreadLocal<>();

    private ThreadLocalContextUtil() {}

    public static FineractPlatformTenant getTenant() {
        return tenantContext.get();
    }

    public static void setTenant(final FineractPlatformTenant tenant) {
        tenantContext.set(tenant);
    }

    public static void clearTenant() {
        tenantContext.remove();
    }

    public static String getDataSourceContext() {
        return contextHolder.get();
    }

    public static void setDataSourceContext(final String dataSourceContext) {
        contextHolder.set(dataSourceContext);
    }

    public static void clearDataSourceContext() {
        contextHolder.remove();
    }

    public static String getAuthToken() {
        return authTokenContext.get();
    }

    public static void setAuthToken(final String authToken) {
        authTokenContext.set(authToken);
    }

    // Map is not serializable, but Hashmap is
    public static HashMap<BusinessDateType, LocalDate> getBusinessDates() {
        Assert.notNull(businessDateContext.get(), "Business dates cannot be null!");
        return businessDateContext.get();
    }

    public static void setBusinessDates(HashMap<BusinessDateType, LocalDate> dates) {
        Assert.notNull(dates, "Business dates cannot be null!");
        businessDateContext.set(dates);
    }

    public static LocalDate getBusinessDateByType(BusinessDateType businessDateType) {
        Assert.notNull(businessDateType, "Business date type cannot be null!");
        LocalDate localDate = getBusinessDates().get(businessDateType);
        Assert.notNull(localDate, String.format("Business date with type `%s` is not initialised!", businessDateType));
        return localDate;
    }

    public static LocalDate getBusinessDate() {
        BusinessDateType businessDateType = getActionContext().getBusinessDateType();
        return getBusinessDateByType(businessDateType);
    }

    public static ActionContext getActionContext() {
        return actionContext.get() == null ? ActionContext.DEFAULT : actionContext.get();
    }

    public static void setActionContext(ActionContext context) {
        Assert.notNull(context, "context cannot be null");
        actionContext.set(context);
    }

    public static FineractContext getContext() {
        return new FineractContext(getDataSourceContext(), getTenant(), getAuthToken(), getBusinessDates(), getActionContext());
    }

    public static void init(final FineractContext fineractContext) {
        Assert.notNull(fineractContext, "FineractContext cannot be null during synchronisation!");
        setDataSourceContext(fineractContext.getContextHolder());
        setTenant(fineractContext.getTenantContext());
        setAuthToken(fineractContext.getAuthTokenContext());
        setBusinessDates(fineractContext.getBusinessDateContext());
        setActionContext(fineractContext.getActionContext());
    }

    public static void reset() {
        contextHolder.remove();
        tenantContext.remove();
        authTokenContext.remove();
        businessDateContext.remove();
        actionContext.remove();
    }
}
