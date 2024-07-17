package com.fineract.mifos.mifos_core.infrastructure.bulkimport.service;

import com.fineract.mifos.mifos_core.infrastructure.bulkimport.dto.GlobalEntityType;
import com.fineract.mifos.mifos_core.infrastructure.bulkimport.dto.ImportData;
import com.fineract.mifos.mifos_core.infrastructure.documentmanagement.DocumentData;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.Collection;

public interface BulkImportWorkbookService {
    Long importWorkbook(String entityType, InputStream inputStream, FormDataContentDisposition fileDetail, String locale,
                        String dateFormat);

    Collection<ImportData> getImports(GlobalEntityType type);

    DocumentData getOutputTemplateLocation(String importDocumentId);

    Response getOutputTemplate(String importDocumentId);

}
