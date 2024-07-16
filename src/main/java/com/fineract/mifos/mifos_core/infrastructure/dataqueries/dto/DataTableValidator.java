package com.fineract.mifos.mifos_core.infrastructure.dataqueries.dto;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.*;

@Component
public class DataTableValidator {

    private final FromJsonHelper fromApiJsonHelper;
    private static final Set<String> SUPPORTED_PARAMETERS = new HashSet<>(
            Arrays.asList(DataTableApiConstant.categoryParamName, DataTableApiConstant.localParamName));

    @Autowired
    public DataTableValidator(final FromJsonHelper fromApiJsonHelper) {
        this.fromApiJsonHelper = fromApiJsonHelper;
    }

    public void validateDataTableRegistration(final String json) {

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, SUPPORTED_PARAMETERS);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors)
                .resource(DataTableApiConstant.DATATABLE_RESOURCE_NAME);
        final JsonElement element = this.fromApiJsonHelper.parse(json);

        if (this.fromApiJsonHelper.parameterExists(DataTableApiConstant.categoryParamName, element)) {

            final Integer category = this.fromApiJsonHelper.extractIntegerWithLocaleNamed(DataTableApiConstant.categoryParamName, element);
            Object[] objectArray = new Integer[] { DataTableApiConstant.CATEGORY_PPI, DataTableApiConstant.CATEGORY_DEFAULT };
            baseDataValidator.reset().parameter(DataTableApiConstant.categoryParamName).value(category).isOneOfTheseValues(objectArray);
        }

        baseDataValidator.throwValidationErrors();
    }

    public void validateTableSearch(@NotNull AdvancedQueryRequest queryRequest) {
        final List<ApiParameterError> errors = new ArrayList<>();
        final DataValidatorBuilder validator = new DataValidatorBuilder(errors).resource(DataTableApiConstant.DATATABLE_RESOURCE_NAME);
        AdvancedQueryData baseQuery = queryRequest.getBaseQuery();
        if (baseQuery != null) {
            validateQueryData(baseQuery, validator);
        }
        List<TableQueryData> datatableQueries = queryRequest.getDatatableQueries();
        if (datatableQueries != null) {
            for (TableQueryData datatableQuery : datatableQueries) {
                validator.reset().parameter(API_PARAM_TABLE).value(datatableQuery.getTable()).notBlank();
                AdvancedQueryData queryData = datatableQuery.getQuery();
                validator.reset().parameter(API_PARAM_QUERY).value(queryData).notBlank();
                if (queryData != null) {
                    validateQueryData(queryData, validator);
                }
            }
        }
        validator.throwValidationErrors();
    }

    public void validateTableSearch(@NotNull AdvancedQueryData queryData) {
        final DataValidatorBuilder validator = new DataValidatorBuilder(new ArrayList<>())
                .resource(DataTableApiConstant.DATATABLE_RESOURCE_NAME);
        validateQueryData(queryData, validator);
        validator.throwValidationErrors();
    }

    private void validateQueryData(@NotNull AdvancedQueryData queryData, @NotNull DataValidatorBuilder validator) {
        List<ColumnFilterData> columnFilters = queryData.getColumnFilters();
        if (columnFilters != null) {
            for (ColumnFilterData columnFilter : columnFilters) {
                validator.reset().parameter(API_PARAM_COLUMN).value(columnFilter.getColumn()).notNull();
                List<FilterData> filters = columnFilter.getFilters();
                validator.reset().parameter(API_PARAM_FILTERS).value(filters == null ? null : filters.toArray()).notNull().arrayNotEmpty();
                if (filters != null) {
                    for (FilterData filter : filters) {
                        validator.reset().parameter(API_PARAM_OPERATOR).value(filter.getOperator()).notNull();
                    }
                }
            }
            List<String> resultColumns = queryData.getResultColumns();
            if (resultColumns != null) {
                for (String resultColumn : resultColumns) {
                    validator.reset().parameter(API_PARAM_RESULTCOLUMNS).value(resultColumn).notBlank();
                }
            }
        }
    }
}