package org.jmb.bootstrap;

/**
 * This class represents a database. It is used to configure and set up different Database properties upon
 * its creation.
 */
public class Database {

    //First version only has this option, file storage support to be added later.
    private final boolean inMemory;

    private Database(boolean inMemory) {
        this.inMemory = inMemory;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isInMemory() {
        return inMemory;
    }

    public static class Builder {
        private boolean inMemory = true;

        public Builder inMemory(boolean inMemory) {
            this.inMemory = inMemory;
            return this;
        }

        public Database build() {
            return new Database(inMemory);
        }
    }
}
