# GitConflictFinder

GitConflictFinder is a Java library that identifies files modified independently in two Git branches—branchA (remote) and branchB (local)—since their last common commit (merge base). It’s designed to detect potential merge conflicts without fetching the remote branch locally, making it ideal for pre-merge analysis in collaborative development workflows.

**This project was completed as part of the application process for a JetBrains internship.**

## Features

- Detects files changed in both a remote branchA (via GitHub API) and a local branchB since their merge base.
- Uses the GitHub REST API for remote repository interactions.
- Executes Git commands locally via command-line execution for repository analysis.
- Includes unit tests and error handling.
- Provides detailed Javadoc documentation.

## Prerequisites
- Java: Developed and tested with Java 21. Compatibility with earlier versions (e.g., Java 11 or 17) is not guaranteed and may require additional testing or adjustments.
- Git: Installed locally for command-line execution.
- GitHub Access Token: Required for API authentication.
- Gradle: For building and managing dependencies (version 8.0 or higher recommended).

## Setup
1. Clone the Repository
```bash
git clone https://github.com/m1RroR4/GitConflictFinder.git
cd GitConflictFinder
```
2. Build the Project
```bash
gradle build
```
3. Run Tests
```bash
gradle test
```
4. Add to Your Project
   - Include the compiled JAR or add as a local dependency in your build.gradle.

## Usage

Here is an example of how to use the GitConflictFinder library:

```java
import gitconflict.GitConflictFinder;
import gitconflict.GitConflictFinderImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String owner = "your-github-username";
        String repo = "your-repo-name";
        String accessToken = "your-github-access-token";
        String localRepoPath = "/path/to/local/repo";
        String branchA = "main";  // Remote branch
        String branchB = "feature";  // Local branch

        GitConflictFinder conflictFinder = new GitConflictFinderImpl(
                owner, repo, accessToken, localRepoPath, branchA, branchB
        );

        try {
            List<String> conflictingFiles = conflictFinder.findConflictingFiles();
            if (conflictingFiles.isEmpty()) {
                System.out.println("No potential conflicts found.");
            } else {
                System.out.println("Potential conflicting files:");
                conflictingFiles.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.err.println("Error finding conflicts: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

## Documentation

The Javadoc documentation for GitConflictFinder is available [here](https://m1rror4.github.io/GitConflictFinder/javadoc/).

## Project Structure

```
GitConflictFinder/
├── docs/
│   └── javadoc/
│       └── ... (generated Javadoc files)
├── src/
│   ├── main/
│   │   └── java/
│   │       └── ... (source code files)
│   └── test/
│       └── java/
│           └── ... (test code files)
├── build.gradle
└── README.md
```

## Contributing
Contributions are welcome! Feel free to fork this repository, submit issues, or create pull requests.