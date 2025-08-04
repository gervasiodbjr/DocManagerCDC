package br.com.estudos.doc_processor;

import br.com.estudos.doc_processor.domain.DocumentoView;
import br.com.estudos.doc_processor.domain.event.ChangeEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.function.Function;

/**
 * Classe base abstrata para processadores de eventos de mudança (ChangeLog).
 * Fornece funcionalidade comum de parsing de mensagens, injeção de ObjectMapper
 * e tratamento de operações de criação/atualização.
 *
 * @param <T> O tipo específico de ChangeLog que este processador manipula.
 * @param <A> O tipo do objeto 'after' contido no ChangeLog.
 */
public abstract class BaseProcessor<T extends ChangeEvent<?>, A> {

    @Inject
    protected ObjectMapper objectMapper;

    protected final String tableName;
    protected final Logger LOG;
    protected final Class<T> eventType;
    protected final Function<A, Long> docIdExtractor;

    public BaseProcessor(String tableName, Class<T> eventType, Function<A, Long> docIdExtractor) {
        this.tableName = tableName;
        this.eventType = eventType;
        this.docIdExtractor = docIdExtractor;
        this.LOG = Logger.getLogger(getClass());
    }

    protected T parseMessage(String message) throws Exception {
        T changeEvent = message != null ? objectMapper.readValue(message, eventType) : null;
        if (changeEvent == null) {
            return null;
        }
        if (!tableName.equals(changeEvent.source.table)) {
            LOG.warnf("Mensagem recebida para tabela inesperada. Esperado '%s', recebido '%s'.", tableName, changeEvent.source.table);
            return null;
        }
        return changeEvent;
    }

    protected DocumentoView handleCreateOrUpdateOperation(T event) {
        try {
            Long docId = docIdExtractor.apply((A) event.after);
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

    protected abstract DocumentoView processChangeEvent(T event);
}
