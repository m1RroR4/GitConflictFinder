package gitconflict.git;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocalGitExecutorTest {
    private LocalGitExecutor localGitExecutor;

    @BeforeEach
    void setUp() {
        localGitExecutor = new LocalGitExecutor("/path/to/repo");
    }

    @Test
    void getMergeBase_shouldThrowIllegalArgumentException_whenBranchAIsNull() {
        assertThrows(IllegalArgumentException.class, () -> localGitExecutor.getMergeBase(null, "branchB"));
    }

    @Test
    void getMergeBase_shouldThrowIllegalArgumentException_whenBranchBIsNull() {
        assertThrows(IllegalArgumentException.class, () -> localGitExecutor.getMergeBase("branchA", null));
    }

    @Test
    void getChangedFilesSinceMergeBase_shouldThrowIllegalArgumentException_whenMergeBaseIsNull() {
        assertThrows(IllegalArgumentException.class, () -> localGitExecutor.getChangedFilesSinceMergeBase(null, "branchB"));
    }

    @Test
    void getChangedFilesSinceMergeBase_shouldThrowIllegalArgumentException_whenBranchBIsNull() {
        assertThrows(IllegalArgumentException.class, () -> localGitExecutor.getChangedFilesSinceMergeBase("mergeBase", null));
    }
}
