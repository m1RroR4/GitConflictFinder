package gitconflict;

import java.util.List;

public interface GitConflictFinder {
    List<String> findConflictingFiles() throws Exception;
}
