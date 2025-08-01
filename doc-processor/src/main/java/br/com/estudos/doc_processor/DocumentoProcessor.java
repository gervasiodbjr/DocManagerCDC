package br.com.estudos.doc_processor;

import br.com.estudos.doc_processor.domain.DocumentoView;
import br.com.estudos.doc_processor.domain.event.DocumentoChangeEvent;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class DocumentoProcessor {

    @Inject
    ObjectMapper objectMapper;

    private static final Logger LOG = Logger.getLogger(DocumentoProcessor.class);

    @Incoming("debezium-in")
    @Outgoing("docview-out")
    @Transactional
    public DocumentoView process(String message) throws Exception {
        LOG.infof("Received message: %s", message);
        DocumentoChangeEvent documentoChangeEvent = objectMapper.readValue(message, DocumentoChangeEvent.class);
        if (documentoChangeEvent == null || !"documento".equals(documentoChangeEvent.source.table)) {
            LOG.warn("Skipping message for table: " + (documentoChangeEvent != null ? documentoChangeEvent.source.table : "unknown"));
            return null;
        }
        if ("d".equals(documentoChangeEvent.op)) {
            LOG.info("Skipping delete operation");
            return null;
        }
        Long documentId = documentoChangeEvent.after.id;
        DocumentoView docView = DocumentoView.findById(documentId);
        if (docView == null) {
            LOG.warnf("Document with id %d not found in view", documentId);
            return null;
        }
        LOG.infof("Processed document: %s", docView.toString());
        return docView;
    }
}
