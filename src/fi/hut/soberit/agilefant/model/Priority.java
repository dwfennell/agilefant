package fi.hut.soberit.agilefant.model;

/**
 * A priority enumeration, which represents the priority of a todo and a story.
 * 
 * @see fi.hut.soberit.agilefant.model.Story
 * @see fi.hut.soberit.agilefant.model.Todo
 */
public enum Priority {
    TRIVIAL, MINOR, MAJOR, CRITICAL, BLOCKER, UNDEFINED;

    public int getOrdinal() {
        return this.ordinal();
    }

    public String getName() {
        return this.name();
    }
}