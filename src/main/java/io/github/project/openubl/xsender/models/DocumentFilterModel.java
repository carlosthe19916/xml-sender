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
package io.github.project.openubl.xsender.models;

import java.util.List;

public class DocumentFilterModel {

    private List<String> ruc;
    private List<String> documentType;

    public List<String> getRuc() {
        return ruc;
    }

    public void setRuc(List<String> ruc) {
        this.ruc = ruc;
    }

    public List<String> getDocumentType() {
        return documentType;
    }

    public void setDocumentType(List<String> documentType) {
        this.documentType = documentType;
    }

    public static final class DocumentFilterModelBuilder {
        private List<String> ruc;
        private List<String> documentType;

        private DocumentFilterModelBuilder() {
        }

        public static DocumentFilterModelBuilder aDocumentFilterModel() {
            return new DocumentFilterModelBuilder();
        }

        public DocumentFilterModelBuilder withRuc(List<String> ruc) {
            this.ruc = ruc;
            return this;
        }

        public DocumentFilterModelBuilder withDocumentType(List<String> documentType) {
            this.documentType = documentType;
            return this;
        }

        public DocumentFilterModel build() {
            DocumentFilterModel documentFilterModel = new DocumentFilterModel();
            documentFilterModel.setRuc(ruc);
            documentFilterModel.setDocumentType(documentType);
            return documentFilterModel;
        }
    }
}
