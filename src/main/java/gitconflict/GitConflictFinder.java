package gitconflict;

import java.util.List;

/**
 * Interface for finding conflicting files in a Git repository.
 */
public interface GitConflictFinder {
    /**
     * Finds the list of files that have conflicts.
     *
     * @return a list of filenames of the conflicting files
     * @throws Exception if an error occurs while finding conflicting files
     */
    List<String> findConflictingFiles() throws Exception;
}
