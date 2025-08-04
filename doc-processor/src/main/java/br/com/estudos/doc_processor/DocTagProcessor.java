package br.com.estudos.doc_processor;

import br.com.estudos.doc_processor.domain.DocumentoView;
import br.com.estudos.doc_processor.domain.event.DocTagChangeEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
public class DocTagProcessor extends BaseProcessor<DocTagChangeEvent, DocTagChangeEvent.DocTag> {

    public DocTagProcessor() {
        super("doc_tag", DocTagChangeEvent.class, after -> after.docId);
    }

    @Incoming("debezium-doc-tag-in")
    @Outgoing("docview-out")
    @Transactional
    public DocumentoView process(String message) throws Exception {
        DocTagChangeEvent event = parseMessage(message);
        if (event == null) {
            return null;
        }
        return processChangeEvent(event);
    }

    @Override
    protected DocumentoView processChangeEvent(DocTagChangeEvent event) {
        if ("d".equals(event.op)) {
            LOG.infof("Pulando operação de exclusão para %s.", tableName);
            return null;
        } else if ("c".equals(event.op) || "u".equals(event.op)) {
            return handleCreateOrUpdateOperation(event);
        } else {
            LOG.warnf("Operação não suportada para %s: %s", tableName, event.op);
        }
        return null;
    }
}
