package com.fineract.mifos.mifos_core.infrastructure.core.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.ws.rs.core.UriInfo;
import lombok.Data;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.File;
import java.io.InputStream;

@Data
public class UploadRequest {

    @Schema(type = "string", format = "binary")
    @FormDataParam("file")
    private InputStream uploadedInputStream;

    @Schema(implementation = File.class, hidden = true)
    @FormDataParam("file")
    private File uploadedFile;

    @Schema(implementation = FormDataContentDisposition.class, hidden = true)
    @FormDataParam("file")
    private FormDataContentDisposition fileDetail;

    @Schema(implementation = UriInfo.class, hidden = true)
    @FormDataParam("file")
    private UriInfo uriInfo;

    @Schema(implementation = UriInfo.class, hidden = true)
    @FormDataParam("file")
    private FormDataBodyPart bodyPart;


    @Schema(name = "locale", type = "string", accessMode = Schema.AccessMode.READ_WRITE)
    @FormDataParam("locale")
    private String locale;


    @Schema(name = "dateFormat", type = "string", accessMode = Schema.AccessMode.READ_WRITE)
    @FormDataParam("dateFormat")
    private String dateFormat;

}
