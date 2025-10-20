#!/bin/bash
set -e

# Configure Git
git config user.name "github-actions[bot]"
git config user.email "github-actions[bot]@users.noreply.github.com"

# Add the changed version files
git add build.gradle.kts

# Commit with a message
git commit -m "Increment version to $(cat new_version.build)"

# Push to the branch that triggered the workflow
git push origin HEAD
