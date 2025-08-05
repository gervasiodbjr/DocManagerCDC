package br.com.estudos.doc_processor;

import br.com.estudos.doc_processor.domain.event.ChangeEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.function.Function;


/**
 * Abstract base class for processing change events related to a specific database table.
 * This class provides common logic for parsing change event messages, handling creation
 * or update operations, and validates that the events correspond to the expected table.
 *
 * @param <T> The type of ChangeEvent being processed, extends {@code ChangeEvent<?>}.
 * @param <A> The type of the "after" object within the ChangeEvent (Input Event Entity).
 * @param <B> The type of the entity associated with the ChangeEvent, extends (Output Event Entity) {@code PanacheEntityBase}.
 */
public abstract class BaseProcessor<T extends ChangeEvent<?>, A, B> {

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

    /**
     * Parses a ChangeEventt message into an instance of the specified type.
     * Validates that the parsed event corresponds to the expected table name.
     * If the message is null, or if the event's table does not match
     * the expected table, the method returns null.
     *
     * @param message The JSON-formatted string representing the ChangeEvent message,
     *                or null if no message is available.
     * @return The parsed ChangeEvent of type {@code T}, or null if the message is invalid,
     *         the parsed object cannot be created, or the table name does not match the expected value.
     * @throws Exception If there is an error during message parsing.
     */
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

    protected abstract B processChangeEvent(T event);

    protected abstract B handleCreateOrUpdateOperation(T event);
}
