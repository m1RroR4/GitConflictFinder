package gitconflict.git;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class LocalGitExecutor {
    private final String repoPath;

    public LocalGitExecutor(String repoPath) {
        this.repoPath = repoPath;
    }

    // git merge-base <branchB> origin/<branchA>
    public String getMergeBase(String branchA, String branchB) throws GitOperationException, IOException, InterruptedException {
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

    // git diff --name-only <merge-base> <branchB>
    public List<String> getChangedFilesSinceMergeBase(String mergeBase, String branchB) throws GitOperationException, IOException, InterruptedException {
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
