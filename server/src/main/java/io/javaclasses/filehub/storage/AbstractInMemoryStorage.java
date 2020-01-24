package io.javaclasses.filehub.storage;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An abstract storage implementation, which uses JVM memory to store the records.
 *
 * @param <I>
 *         a type of record ID
 * @param <R>
 *         a type of record
 */
public abstract class AbstractInMemoryStorage<I extends RecordId, R extends Record<I>>
        implements Storage<I, R> {

    private final Map<I, R> records = new HashMap<>();

    @Override
    public Iterable<R> readAll() {
        return Collections.unmodifiableCollection(records.values());
    }

    @Override
    public Optional<R> read(I identifier) {
        checkNotNull(identifier);
        return Optional.ofNullable(records.get(identifier));
    }

    /**
     * {@inheritDoc}
     *
     * <p>If the record with such ID exists, it will be overwritten.
     */
    @Override
    public void write(R record) {
        checkNotNull(record);
        records.put(record.id(), record);
    }

    @Override
    public void clear() {
        records.clear();
    }

    /**
     * Obtains all records from the storage.
     */
    protected Collection<R> records() {
        return Collections.unmodifiableCollection(records.values());
    }

    @Override
    public void remove(I identifier) {
        records.remove(identifier);
    }
}
