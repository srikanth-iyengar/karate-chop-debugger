#!/bin/bash

last_version=$(git tag --sort=-creatordate | head -n 1)

version=${last_version#v}

IFS='.' read -r major minor patch <<< "$version"

patch=$((patch + 1))

new_version="$major.$minor.$patch"

echo "Latest version: $last_version"
echo "New version: $new_version"

echo $version > old_version.build
echo $new_version > new_version.build