#!/bin/bash

# Input the sdk version
read -p "Enter the sdk version: " SDK_VERSION

# Create a new branch
git checkout -b "release/$SDK_VERSION"

# Replace the version in the gradle/libs.versions.toml
sed -i '' "s/latest.version/$SDK_VERSION/" gradle/libs.versions.toml

# Replace the version in the README.md
sed -i '' "s/latest.version/$SDK_VERSION/" README.md 

# Commit the changes
git add .
git commit -m "Release $SDK_VERSION"

# Push the changes
git push origin "release/$SDK_VERSION"