package br.com.estudos.doc_processor;

import br.com.estudos.doc_processor.domain.DocumentoView;
import br.com.estudos.doc_processor.domain.event.DocumentoChangeEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
public class DocumentoProcessor extends BaseProcessor<DocumentoChangeEvent, DocumentoChangeEvent.Documento> {

    public DocumentoProcessor() {
        super("documento", DocumentoChangeEvent.class, after -> after.id);
    }

    @Incoming("debezium-documento-in")
    @Outgoing("docview-out")
    @Transactional
    public DocumentoView process(String message) throws Exception {
        DocumentoChangeEvent event = parseMessage(message);
        if (event == null) {
            return null;
        }
        return processChangeEvent(event);
    }

    @Override
    protected DocumentoView processChangeEvent(DocumentoChangeEvent event) {
        if ("d".equals(event.op)) {
            LOG.info("Pulando operação de exclusão para documento.");
            return null;
        }
        return handleCreateOrUpdateOperation(event);
    }
}
