/*
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Eclipse Public License - v 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.project.openubl.xsender.models.utils;

import io.github.project.openubl.xsender.idm.*;
import io.github.project.openubl.xsender.models.PageModel;
import io.github.project.openubl.xsender.models.jpa.entities.CompanyEntity;
import io.github.project.openubl.xsender.models.jpa.entities.NamespaceEntity;
import io.github.project.openubl.xsender.models.jpa.entities.UBLDocumentEntity;
import org.apache.http.client.utils.URIBuilder;

import javax.ws.rs.core.UriInfo;
import java.net.URISyntaxException;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EntityToRepresentation {
    private EntityToRepresentation() {
        // Just static methods
    }

    public static NamespaceRepresentation toRepresentation(NamespaceEntity entity) {
        NamespaceRepresentation rep = new NamespaceRepresentation();

        rep.setId(entity.getId());
        rep.setName(entity.getName());
        rep.setDescription(entity.getDescription());

        return rep;
    }

    public static CompanyRepresentation toRepresentation(CompanyEntity entity) {
        CompanyRepresentation rep = new CompanyRepresentation();

        rep.setId(entity.getId());
        rep.setRuc(entity.getRuc());
        rep.setName(entity.getName());
        rep.setDescription(entity.getDescription());

        if (entity.getSunatUrls() != null) {
            SunatUrlsRepresentation sunatUrlsRep = new SunatUrlsRepresentation();
            rep.setWebServices(sunatUrlsRep);

            sunatUrlsRep.setFactura(entity.getSunatUrls().getSunatUrlFactura());
            sunatUrlsRep.setGuia(entity.getSunatUrls().getSunatUrlGuiaRemision());
            sunatUrlsRep.setRetenciones(entity.getSunatUrls().getSunatUrlPercepcionRetencion());
        }

        if (entity.getSunatCredentials() != null) {
            SunatCredentialsRepresentation credentialsRep = new SunatCredentialsRepresentation();
            rep.setCredentials(credentialsRep);

            credentialsRep.setUsername(entity.getSunatCredentials().getSunatUsername());
        }

        return rep;
    }

    public static DocumentRepresentation toRepresentation(UBLDocumentEntity entity) {
        DocumentRepresentation rep = new DocumentRepresentation();

        rep.setId(entity.getId());
        rep.setInProgress(entity.isInProgress());

        rep.setCreatedOn(entity.getCreatedOn().getTime());
        rep.setError(entity.getError());

        // File

        rep.setFileContentValid(entity.getFileValid());
        rep.setFileContentValidationError(entity.getFileValidationError());

        rep.setFileContent(new DocumentContentRepresentation());
        rep.getFileContent().setRuc(entity.getRuc());
        rep.getFileContent().setDocumentID(entity.getDocumentID());
        rep.getFileContent().setDocumentType(entity.getDocumentType());

        // Sunat

        rep.setSunat(new DocumentSunatStatusRepresentation());

        rep.getSunat().setCode(entity.getSunatCode());
        rep.getSunat().setTicket(entity.getSunatTicket());
        rep.getSunat().setStatus(entity.getSunatStatus());
        rep.getSunat().setDescription(entity.getSunatDescription());

        // Events

//        List<DocumentSunatEventRepresentation> eventsRepresentation = entity.getSunatEvents().stream().map(f -> {
//            DocumentSunatEventRepresentation e = new DocumentSunatEventRepresentation();
//            e.setDescription(f.getDescription());
//            e.setStatus(f.getStatus().toString());
//            e.setCreatedOn(f.getCreatedOn().getTime());
//            return e;
//        }).collect(Collectors.toList());
//
//        rep.setSunatEvents(eventsRepresentation);

        return rep;
    }

    public static <T, R> PageRepresentation<R> toRepresentation(PageModel<T> model, Function<T, R> mapper) {
        PageRepresentation<R> rep = new PageRepresentation<>();

        // Meta
        PageRepresentation.Meta repMeta = new PageRepresentation.Meta();
        rep.setMeta(repMeta);

        repMeta.setCount(model.getTotalElements());

        // Data
        rep.setData(model.getPageElements().stream()
                .map(mapper)
                .collect(Collectors.toList())
        );

        return rep;
    }

    private static URIBuilder getURIBuilder(UriInfo uriInfo) throws URISyntaxException {
        return new URIBuilder(uriInfo.getPath());
    }
}
