package br.com.estudos.doc_processor;

import br.com.estudos.doc_processor.domain.DocumentoView;
import br.com.estudos.doc_processor.domain.event.DocCategoriaChangeEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
public class DocCategoriaProcessor extends BaseProcessor<DocCategoriaChangeEvent, DocCategoriaChangeEvent.DocCategoria, DocumentoView> {

    public DocCategoriaProcessor() {
        super("doc_categoria", DocCategoriaChangeEvent.class, after -> after.docId);
    }

    @Incoming("debezium-doc-categoria-in")
    @Outgoing("docview-out")
    @Transactional
    public DocumentoView process(String message) throws Exception {
        DocCategoriaChangeEvent event = parseMessage(message);
        if (event == null) {
            return null;
        }
        return processChangeEvent(event);
    }

    @Override
    protected DocumentoView processChangeEvent(DocCategoriaChangeEvent event) {
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

    @Override
    protected DocumentoView handleCreateOrUpdateOperation(DocCategoriaChangeEvent event) {
        try {
            Long docId = docIdExtractor.apply(event.after);
            DocumentoView docView = DocumentoView.findById(docId);
            if (docView != null) {
                return docView;
            } else {
                LOG.warnf("DocumentoView não encontrado para docId=%d durante o processamento da tabela '%s'.", docId, tableName);
                return null;
            }
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao processar evento para a tabela '%s': %s", tableName, e.getMessage());
            return null;
        }
    }
}
