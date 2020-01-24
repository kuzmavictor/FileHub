package io.javaclasses.filehub.storage;

import java.util.Optional;

/**
 * An abstract storage containing records.
 *
 * @param <I>
 *         a type of record identifiers
 * @param <R>
 *         a type of data record
 */
public interface Storage<I extends RecordId, R extends Record<I>> {

    /**
     * Reads all records from storage.
     */
    Iterable<R> readAll();

    /**
     * Reads record by record identifier.
     *
     * @param identifier
     *         a record identifier to search records
     * @return the record read or the {@code Optional.empty()} if such record does not exist
     */
    Optional<R> read(I identifier);

    /**
     * Writes a record to storage.
     *
     * @param record
     *         to be written
     * @implNote The descendants define the behavior of records with the same ID.
     */
    void write(R record);

    /**
     * Clears all records.
     */
    void clear();

    /**
     * Removes the record from the storage by the identifier.
     *
     * @param identifier
     *         the identifier of the removable record
     */
    void remove(I identifier);
}
