package gitconflict.git;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Executes local Git commands in a specified repository.
 */
public class LocalGitExecutor {
    private final String repoPath;

    /**
     * Constructs a new LocalGitExecutor for the specified repository path.
     *
     * @param repoPath the path to the local Git repository
     */
    public LocalGitExecutor(String repoPath) {
        this.repoPath = repoPath;
    }

    /**
     * Finds the merge base between two branches.
     *
     * @param branchA the name of the first branch
     * @param branchB the name of the second branch
     * @return the merge base commit hash
     * @throws GitOperationException if the Git operation fails
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the process is interrupted
     */
    public String getMergeBase(String branchA, String branchB) throws GitOperationException, IOException, InterruptedException {
        if (branchA == null || branchA.isEmpty() || branchB == null || branchB.isEmpty()) {
            throw new IllegalArgumentException("Branch names must not be null or empty");
        }

        ProcessBuilder processBuilder = new ProcessBuilder("git", "merge-base", branchB, "origin/" + branchA);
        processBuilder.directory(new File(repoPath));
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new GitOperationException("Failed to find merge base between " + branchB + " and origin/" + branchA + " with exit code " + exitCode);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String mergeBase = reader.readLine();
            if (mergeBase == null) {
                throw new GitOperationException("No merge base found between " + branchB + " and origin/" + branchA);
            }
            return mergeBase.trim();
        }
    }

    /**
     * Gets the list of files changed since the specified merge base.
     *
     * @param mergeBase the merge base commit hash
     * @param branchB the name of the branch to compare
     * @return a list of changed files
     * @throws GitOperationException if the Git operation fails
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the process is interrupted
     */
    public List<String> getChangedFilesSinceMergeBase(String mergeBase, String branchB) throws GitOperationException, IOException, InterruptedException {
        if (mergeBase == null || mergeBase.isEmpty() || branchB == null || branchB.isEmpty()) {
            throw new IllegalArgumentException("Merge base and branch names must not be null or empty");
        }

        ProcessBuilder processBuilder = new ProcessBuilder("git", "diff", "--name-only", mergeBase, branchB);
        processBuilder.directory(new File(repoPath));
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new GitOperationException("Failed to get changed files from " + mergeBase + " to " + branchB + " with exit code " + exitCode);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            return reader.lines().collect(Collectors.toList());
        }
    }
}
