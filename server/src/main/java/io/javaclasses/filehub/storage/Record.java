package io.javaclasses.filehub.storage;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An abstract base for classes, describing the data contained in {@link Storage}.
 *
 * @param <I>
 *         a type of data identifier, which is used to find specific data
 */
public abstract class Record<I extends RecordId> {

    private final I recordId;

    /**
     * Creates a record instance.
     *
     * @param identifier
     *         a record identifier
     */
    protected Record(I identifier) {
        this.recordId = checkNotNull(identifier);
    }

    /**
     * Returns a record identifier.
     */
    public I id() {
        return recordId;
    }
}
